package org.techtown.projectmain;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//하단바 내정보 탭
public class ProjectHomeFragment2 extends Fragment implements onBackPressedListener{

    private static final int RESULT_OK = -1;
    public static ProjectHomeFragment2 mContext;
    public static StringBuffer buffer = new StringBuffer();
    private TextView profile_name, profile_id, profile_pw, profile_pw_ch;
    public ImageView profile_pic;
    private Button profile_bt;
    public static String img, projectName, myImgPath;
    String imgPath, responseImg;

    //프래그먼트 종료 시켜주는 메소드
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(ProjectHomeFragment2.this).commit();
        fragmentManager.popBackStack();
    }

    //뒤로가기 버튼 눌렀을 때 홈화면 전환하고 전 프래그먼트 종료
    @Override
    public void onBackPressed() {
        ((ProjectHome)getActivity()).replaceFragment(ProjectHomeRecyclerView.newInstance());
        goToMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup profileDP = (ViewGroup)inflater.inflate(R.layout.update_profile, container, false);

        profile_name = profileDP.findViewById(R.id.profile_name);
        profile_id = profileDP.findViewById(R.id.profile_id);
        profile_pw = profileDP.findViewById(R.id.profile_pw);
        profile_pw_ch =profileDP.findViewById(R.id.profile_pw_ch);
        profile_bt = profileDP.findViewById(R.id.profile_bt);
        profile_pic = profileDP.findViewById(R.id.profile_pic);


        mContext=this;

        SearchProject searchProject = new SearchProject();
        searchProject.execute();
        GetPrivateProfile getPrivateProfile = new GetPrivateProfile();
        getPrivateProfile.execute();
        Loaddb loadDB = new Loaddb();
        loadDB.execute();


        //확인버튼
        profile_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myNameCh = profile_name.getText().toString();
                String password1 = profile_pw.getText().toString();
                String password2 = profile_pw_ch.getText().toString();

                if (spaceCheck(myNameCh) || spaceCheck(password1)==true || spaceCheck(password2)){
                    Toast.makeText(getActivity(), "이름과 비밀번호에 공백이 존재하면 안됩니다. ",Toast.LENGTH_LONG).show();
                }
                else if (profile_pw.getText().toString().equals(profile_pw_ch.getText().toString())){
                    SetProfile setProfile = new SetProfile();
                    setProfile.execute();


                    pic_upload();

                    //현재 내 프래그먼트 닫기
                    //removeFragment(ProjectHomeFragment2.this);

                    ProjectHome.profileImgDB profileImgdb = new ProjectHome.profileImgDB();
                    profileImgdb.execute();


                    Toast.makeText(getContext(), "정보 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                }

                else{
                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다. ",Toast.LENGTH_LONG).show();
                }
//                ProjectHome.profileImgDB profileImgdb = new ProjectHome.profileImgDB();
//                profileImgdb.execute();

            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 or 사진 앱 실행하여 사진을 선택하도록..
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });

        return profileDP;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            //profile_pic.setImageResource(0);
            Toast.makeText(getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 사진의 경로(Uri)객체 얻어오기
                    Uri uri = data.getData();

                    //사진첨부 했을 때
                    if(uri!=null){
                        profile_pic.setImageURI(uri);
                        imgPath= getRealPathFromUri(uri);
                        break;
                    }
                    else{
                        Toast.makeText(getContext(), "이미지를 선택하지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
        }

    }//onActivityResult() END

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private String getRealPathFromUri (Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) //사용자가 허가 했다면
                {
                    Toast.makeText(getContext(), "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();

                } else {//거부했다면
                    Toast.makeText(getContext(), "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


    //프래그먼트 닫는 메소드
    private void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            final FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
        }
    }


    //공백이 있는지 없는지 검출해주는 메소드(공백이 있으면 true 없으면 false)
    public boolean spaceCheck(String spaceCheck)
    {
        for(int i = 0 ; i < spaceCheck.length() ; i++)
        {
            if(spaceCheck.charAt(i) == ' ')
                return true;
        }
        return false;
    }




    //자신의 정보 가져오는 스레드
    public class GetPrivateProfile extends AsyncTask<Void, Integer, Void> {
        String data = "";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String param = "u_id=" + pId + "";

            //Check param
            Log.e("ProjectHomeFragment2 : ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getProfile.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("profile data: ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String profileData = "";
            profileData = data;

            int sequence1 = profileData.indexOf("/");
            String myName = profileData.substring(0,sequence1);

            profile_name.setText(myName);

            int sequence2= profileData.indexOf("!");
            String myId = profileData.substring(sequence1+1,sequence2);

            profile_id.setText(myId);

            int sequence3 = profileData.indexOf("?");
            String myPw1 = profileData.substring(sequence2+1,sequence3);

            profile_pw.setText(myPw1);

            String myPw2 = profileData.substring(sequence3+1);

            profile_pw_ch.setText(myPw2);


        }
    }

    //정보 수정 업데이트 스레드
    public class SetProfile extends AsyncTask<Void, Integer, Void>{
        String data ="";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String gName = profile_name.getText().toString();
            String gPw1 = profile_pw.getText().toString();
            String gPw2 = profile_pw_ch.getText().toString();

            String param = "u_id="+pId+"&u_name=" + gName +"&u_pw1="+ gPw1 +"&u_pw2="+ gPw2+"";

            //Check param
            Log.e("setProfile : ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/setProfile.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("profile data: ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @SuppressLint("LongLogTag")
    public void pic_upload(){

        //서버로 보낼 데이터
        String id = MainActivity.getsId();
        //프로젝트 이름(문자열)이 들어있는 변수
        String project = projectName;


        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl = "http://jwab.dothome.co.kr/Android/profileImg.php";

        //Volley plus Library를 이용해서 파일전송
        //파일 전송 요청 객체 생성[결과를 String으로 받음]a
        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    new AlertDialog.Builder(ProjectHomeFragment2.this).setMessage("응답:" + response).create().show();
                        Log.e("php응답: ",response);
                        responseImg = "http://jwab.dothome.co.kr/Android/" +response.trim();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        //param값 php로 전송
        smpr.addStringParam("id",id);
        smpr.addStringParam("projectName",project);
        //이미지 파일 추가
        if(imgPath == null){
            smpr.addStringParam("imgPath", "null");
        }else{
            smpr.addFile("imgPath", imgPath);
        }


        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(smpr);

    }

    //내정보 탭을 눌렀을 때 프로필사진 바로 띄워줌
    public class Loaddb extends AsyncTask<Void, Integer, Void> {


        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            String id = MainActivity.getsId();
            String param = "id=" + id + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/profileImgLoad.php";

            try {
                URL url = new URL(serverUri);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                //connection.setDoOutput(true);// 이 예제는 필요 없다.
                connection.setUseCaches(false);

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = connection.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String Contents = buffer.toString();

          //  Log.e("이미지경로로로로로", Contents);

            img = "http://jwab.dothome.co.kr/Android/" + Contents.trim();
            Glide.with(ProjectHomeFragment2.this).load(img).error(R.drawable.basic_people2).into(profile_pic);

        }
    }


    //로그인된 사용자의 프로젝트 이름 모두 가져옴(프로젝트 테이블의 사진 업데이트 할 때 필요)
    public class SearchProject extends AsyncTask<Void, Integer, Void> {


        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            String id = MainActivity.getsId();
            String param = "id=" + id + "";
            String serverUri = "http://rtemd.suwon.ac.kr/guest/getProjectName.php";

            try {
                URL url = new URL(serverUri);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                //connection.setDoOutput(true);// 이 예제는 필요 없다.
                connection.setUseCaches(false);

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = connection.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //!프로젝트이름_0!프로젝트이름_1 ... 문자열로 저장돼있음
            projectName = buffer.toString();

        }
    }

    }


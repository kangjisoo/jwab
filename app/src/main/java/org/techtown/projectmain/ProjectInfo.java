package org.techtown.projectmain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

//프로젝트정보
public class ProjectInfo extends AppCompatActivity {
    ImageView projectImg;
    TextView name, pw, date, expireDate;
    Button imgBt;
    String imgPath, responseImg;
    StringBuffer buffer, buffer2, buffer3;
    Uri uri;
    public static String pname, pkey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_info);

        projectImg = findViewById(R.id.info_projectImg);
        name = findViewById(R.id.info_set_name);
        pw = findViewById(R.id.info_set_pw);
        date = findViewById(R.id.info_set_date);
        expireDate = findViewById(R.id.info_set_exprie_date);
        imgBt = findViewById(R.id.info_img_bt);

        pname = ProjectHomeListAdapter.getProjectNameImsi();
        pkey = ProjectHomeListAdapter.getSee();
        name.setText(pname);


        //프로젝트 이미지 띄워주기
        InfoImgLoad infoImgLoad = new InfoImgLoad();
        infoImgLoad.execute();
        //프로젝트 비밀번호 띄워주기
        InfoPwLoad infoPwLoad = new InfoPwLoad();
        infoPwLoad.execute();
        //프로젝트 생성 날짜와 기간을 로드
        InfoDateLoad infoDateLoad = new InfoDateLoad();
        infoDateLoad.execute();

        imgBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 or 사진 앱 실행하여 사진을 선택하도록..

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 사진의 경로(Uri)객체 얻어오기
                    uri = data.getData();

                    //사진첨부 했을 때
                    if(uri!=null){
                        imgPath= getRealPathFromUri(uri);
                        projectImg.setImageURI(uri);

                        //프로젝트 사진 로드
                        pic_upload();
                        break;
                    }
                    else{
                        Toast.makeText(this, "이미지를 선택하지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
        }

    }

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private String getRealPathFromUri (Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    //메모리 접근 권한
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) //사용자가 허가 했다면
                {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();

                } else {//거부했다면
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    //프로젝트 사진 로드
    @SuppressLint("LongLogTag")
    public void pic_upload(){

        //프로젝트 이름(문자열)이 들어있는 변수
        pname = ProjectHomeListAdapter.getProjectNameImsi();
        pkey = ProjectHomeListAdapter.getSee();
        String projectName = pname+"_"+pkey;



        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl = "http://jwab.dothome.co.kr/Android/projectInfoImg.php";

        //Volley plus Library를 이용해서 파일전송
        //파일 전송 요청 객체 생성[결과를 String으로 받음]a
        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        responseImg = "http://jwab.dothome.co.kr/Android/" +response.trim();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        //param값 php로 전송
        smpr.addStringParam("projectName",projectName);
        //이미지 파일 추가
        if(imgPath == null){
            smpr.addStringParam("imgPath", "null");
        }else{
            smpr.addFile("imgPath", imgPath);
        }


        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smpr);

    }

    //프로젝트정보 탭을 눌렀을 때 프로필사진 바로 띄워줌
    public class InfoImgLoad extends AsyncTask<Void, Integer, Void> {
        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();
            String projectName = pname+"_"+pkey;
            String param = "projectName=" + projectName + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/projectInfoLoad.php";

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

          String img = "http://jwab.dothome.co.kr/Android/" + Contents.trim();
            Glide.with(ProjectInfo.this).load(img).error(R.drawable.basic_people2).into(projectImg);

        }
    }

    public class InfoPwLoad extends AsyncTask<Void, Integer, Void> {
        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();
            String param = "pname=" + pname + "&pkey=" + pkey + "";
            String serverUri = "http://rtemd.suwon.ac.kr/guest/getPassword.php";

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

                buffer3 = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer3.append(line + "\n");
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

            String setPassword = buffer3.toString();
            pw.setText(setPassword.trim());

        }
    }
    //프로젝트 생성 날짜와 기간을 로드
    public class InfoDateLoad extends AsyncTask<Void, Integer, Void> {
        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();
            String projectName = pname+"_"+pkey;
            String param = "pname=" + projectName + "";
            String serverUri = "http://rtemd.suwon.ac.kr/guest/getProjectDate.php";

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

                buffer2 = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer2.append(line + "\n");
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
            String[] rows = buffer2.toString().split("_");

            date.setText(rows[0].trim());
            if(rows[1].equals("")){
                expireDate.setText("기간이 설정되지 않았습니다.");
            }else{
                expireDate.setText(rows[1].trim());
            }

        }
    }

}
package org.techtown.projectmain;

import org.techtown.board.BoardAddTest;
import org.techtown.board.BoardMainRecycler;
import org.techtown.loginactivity.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ProjectAdd extends Fragment implements onBackPressedListener{
    private static final Object TAG = "MAIN";

    private ArrayList<ProjectPerson> mArrayList;
    private ProjectPersonAdapter mAdapter;

    //리스트에 추가할 번호 변수
    public static int count =0;
    private String value;
    private EditText projectName;
    private String nameValue;
    private static String  pname, pkey;
    private String projectKey;

    //insert_text창에서 String형으로 받아올 변수
    String stridPhone=null;
    String dbDataCheck="-2";
    boolean alreadyExistIdCheck = true;
    TextView membercount;
    EditText insertText;
    boolean allCheckBoxYesOrNo;
    Button makeButton;
    private String myId;

    //프래그먼트 종료 시켜주는 메소드
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(ProjectAdd.this).commit();
        fragmentManager.popBackStack();
    }

    //뒤로가기 버튼 눌렀을 때 홈화면 전환하고 전 프래그먼트 종료
    @Override
    public void onBackPressed() {
        ((ProjectHome)getActivity()).replaceFragment(ProjectHomeRecyclerView.newInstance());
        goToMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup createProject = (ViewGroup) inflater.inflate(R.layout.project_add, container, false);

        //총 조원의 수를 나타내기 위해 textview를 받은 변수 (전역 변수로 설정한 이유는 삭제 버튼을 클릭했을 때도 실행, 추가 버튼을 눌렀을 때도 실행되야하기 때문)
        membercount = (TextView) createProject.findViewById(R.id.project_add_membercountview);

        //프로젝트 이름
        projectName = (EditText)createProject.findViewById(R.id.project_add_name);

        //로그인된 자신의 아이디
        myId = MainActivity.getsId();


        //리싸이클러뷰를 xml파일에 리싸이클러뷰에 연동
        final RecyclerView recyclerView =(RecyclerView) createProject.findViewById(R.id.project_add_recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        //arraylist를 할당
        mArrayList = new ArrayList<>();

        //person어뎁터를 배열로 만들기
        mAdapter = new ProjectPersonAdapter(mArrayList);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //추가버튼을 누르면 실행되는 코드
        Button button = (Button)createProject.findViewById(R.id.project_add_addbutton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                insertText = (EditText)createProject.findViewById(R.id.project_add_insert_id);
                stridPhone = (String)insertText.getText().toString();

                //비어 있을 경우 메세지창 띄우기
                if (stridPhone.equals("")){
                    Toast.makeText(getContext(),"추가할 조원의 아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                }

                else if (stridPhone.equals(myId)){
                    Toast.makeText(getContext(),"본인 아이디는 자동으로 추가됩니다.",Toast.LENGTH_LONG).show();
                }
                //비어있지 않으면 실행
                else {
                    FindMyMemberDB findMyMemberDB = new FindMyMemberDB();
                    findMyMemberDB.execute();

                    //초기화
                    dbDataCheck = "-2";
                    alreadyExistIdCheck = false;
                }
            }
        });


        //삭제 버튼을 클릭시 선택된 리스트 삭제
        Button deleteButton = (Button)createProject.findViewById(R.id.project_add_deletebutton);
        deleteButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

                //arraylist 사이즈를 저장
                int listSizeCheck = mArrayList.size();
                int deleteListSize = mArrayList.size();

                //리스트에 있는 만큼 for문 돌리기
                for (int j = 0; j<=listSizeCheck;j++) {

                    //만약 arraylist의 사이즈가 0이면 더이상 확인할 리스트가 없는 것이기 때문에 for문을 탈출하는 if문 작성
                    if (mArrayList.size() == 0) {
                        deleteListSize = mArrayList.size();
                        break;
                    }
                    //arraylist가 1이상일 경우 검사할 리스트가 있는 것이므로 하위 for문을 돌려줌
                    //이중 포문을 써준이유는 arraylist가 삭제되면 사이즈가 줄어들어 모든 리스트를 검사하지 않기 떄문
                    else {
                        for (int i = 0; i < mArrayList.size(); i++) {

                            ProjectPerson checked = mArrayList.get(i);

                            //체크박스가 true라면 선택이 된 상태
                            if (checked.isChecked()) {

                                //arraylist에서 지워줌
                                mArrayList.remove(i);

                                //리스트 사이즈를 비교하기 위해
                                deleteListSize = mArrayList.size();

                                //count 변수를 감소시키고 총조원 수를 다시 나타냄
                                count--;
                                membercount.setText(String.valueOf(mArrayList.size()));

                            }
                        }
                    }

                    //바뀐 상태를 변경해줌
                    mAdapter.notifyDataSetChanged();
                }

                if (listSizeCheck==deleteListSize){
                    Toast.makeText(getContext(), "삭제된 조원이 없습니다.", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getContext(), "삭제 완료", Toast.LENGTH_LONG).show();

            }
        });


        //전체선택 체크박스를 체크할 때 발생하는 리스너
        final CheckBox allCheckBox = (CheckBox)createProject.findViewById(R.id.project_add_allcheckbox);
        allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //전체선택 체크박스가 체크되어 있을 때 전체 리스트의 체크박스를 true로 설정
                if (allCheckBox.isChecked() == true) {
                    for (int i = 0; i < mArrayList.size(); i++) {
                        ProjectPerson allCheck = mArrayList.get(i);
                        allCheck.setChecked(true);

                        //새로 리스트에 조원이 추가될 때 전체선택 체크 박스가 체크되어 있으면 true상태의 멤버가 추가되야 하기 때문에 확인용 변수
                        allCheckBoxYesOrNo=true;
                    }

                }

                //전체선택 체크박스가 해제되었을 때 전체 리스트의 체크박스를 false로 설정
                else{
                    for (int i =0; i<mArrayList.size();i++){
                        ProjectPerson allcheck = mArrayList.get(i);
                        allcheck.setChecked(false);
                        allCheckBoxYesOrNo=false;
                    }
                }

                //변경값 적용
                mAdapter.notifyDataSetChanged();
            }
        });

        //만들기 버튼 클릭시 수행되는 리스너
        makeButton=(Button)createProject.findViewById(R.id.project_add_makebutton);
        makeButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(final View view) {

                //프로젝트 이름이 비어 있는지 확인하기 위해
                nameValue=(String)projectName.getText().toString();
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

                //dialog에 textview를 넣어주기 위해
                final EditText projectPassword = new EditText(getContext());
                String message = "※공백이나 특수문자는 사용할 수 없습니다.※";

                alertBuilder
                        .setTitle("프로젝트의 비밀번호를 생성해주세요")
                        .setMessage("※공백이나 특수문자는 사용할 수 없습니다.※")
                        .setCancelable(true)
                        .setView(projectPassword)

                        //확인 버튼 클릭시
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //프로젝트이름이 비어있는지 확인
                                if (nameValue.length()==0){
                                    Toast.makeText(getContext(),"프로젝트 이름을 입력해주세요",Toast.LENGTH_LONG).show();
                                    // dialog.dismiss();
                                }

                                //프로젝트 이름의 공백과 특수문자 유무확인
                                else if (spaceCheck(nameValue)||specialCharacters(nameValue)){
                                    Toast.makeText(getContext(),"프로젝트 이름에 공백이나 특수문자가 존재합니다 ",Toast.LENGTH_LONG).show();
                                }

                                //비어있지 않으면서 공백과 특수문자가 없다면 실행
                                else{
                                    value = projectPassword.getText().toString();
                                    if (value.length()==0) {
                                        Toast.makeText(getContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                                    }
                                    else if (spaceCheck(value)) {
                                        Toast.makeText(getContext(), "비밀번호에 공백이 존재합니다 다시 입력해주세요", Toast.LENGTH_LONG).show();
                                    }
                                    else if (specialCharacters(value)){
                                        Toast.makeText(getContext(), "비밀번호에 특수문자가 존재합니다 다시 입력해주세요", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Log.v("TAG", "확인 버튼 클릭");
                                        Log.v("ProjectPw : ", value);


                                        MakeProjectDB makeProjectDB = new MakeProjectDB();
                                        makeProjectDB.execute();

                                        Toast.makeText(getContext(), "프로젝트가 생성되었습니다.", Toast.LENGTH_LONG).show();

                                        getActivity().finish();

                                        Intent intent = new Intent(getContext(), ProjectHome.class);
                                        startActivity(intent);



                                    }
                                }
                            }
                        });

                //취소 버튼 클릭시
                alertBuilder
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Log.v("TAG : ","취소버튼");
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        });



        return createProject;

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

    //특수문자가 있는지 확인해주는 메소드(있으면 false 없으면 true)
    public boolean specialCharacters(String str) {
        boolean result;

        result = str.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
        if (result==true)
            return false;
        else
            return true;
    }
    //추가 버튼을 눌렀을때 아이디가 존재하는 아이디인지 아닌지 확인하기 위해 php와 연결하여 db를 조사
    public class FindMyMemberDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {
            String param = "u_id=" + stridPhone + "";
            Log.e("POST", param);
            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/projectmemberadd.php");
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
                Log.e("RECV DATA", data);

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

            /* 서버에서 응답 */
            Log.e("RECV DATA",data);


            //php에서 오는 data를 받아 비교
            // 1이면 같은 아이디 없음
            if (data.equals("0")){

                Log.e("RESULT", "추가 가능한 조원");


                //ProjectPerson tmp를 null값으로 초기화 시키고 tmp에 추가할 데이터 값인 newMember를 대입
                ProjectPerson tmp = null;
                //tmp = newMember;

                //arraylist조회하여 리스트에 새로추가할 newMember가 리스트안에 존재하는 아이디인지 확인하기 위한 코드
                for (int i=0; i<mArrayList.size();i++){

                    //mArrayList를 get으로 순서대로 받아오기
                    tmp = mArrayList.get(i);

                    //insert_text에 값이 들어가 있는 stridPhone과 tmp안에 SearchId를 받아서 비교 같으면 반복문 나오기
                    if (stridPhone.equals(tmp.getSearchId())){
                        Log.e("RESULT","이미 추가된 조원");

                        Toast.makeText(getContext(), "이미 추가된 조원 입니다.", Toast.LENGTH_LONG).show();
                        alreadyExistIdCheck=true;
                        break;

                    }
                }

                if (alreadyExistIdCheck==false) {

                    //membercount.setText(count);라고 쓰면 오류남 count가 string형이 아니기 때문에
                    membercount.setText(String.valueOf(mArrayList.size()+1));



                    //전체선택 체크박스 체크되어 있으면 true상태로 추가
                    if (allCheckBoxYesOrNo==false) {

                        //카운트를 추가 시키고 member로 아래 목록을 리싸이클러뷰에 띄우기
                        ProjectPerson newMember = new ProjectPerson("조원", stridPhone, false);
                        mArrayList.add(newMember);
                    }
                    else
                    {
                        ProjectPerson newMember = new ProjectPerson("조원", stridPhone, true);
                        mArrayList.add(newMember);
                    }

                    //어느 위치에 삽입할지를 정해줌 count 위치에 삽입함으로써 리스트 밑에 삽입
                    //0을 넣으면 위에 삽입
                    mAdapter.notifyItemInserted(count);
                    count++;


                    //추가 되고 난 후 insert창을 비워줌줌
                    insertText.getText().clear();

                }

            }

            //db에서 없는 아이디이면 data 1을 출력
            else if (data.equals("1")) {
                Log.e("RESULT", "찾을 수 없는 아이디");
                Toast.makeText(getContext(), "찾을 수 없는 아이디 입니다.", Toast.LENGTH_LONG).show();
            }

            //입력창이 비어 있으면 data -1을 출력
            else if (data.equals("-1")){
                Log.e("RESULT", "입력창이 빈칸");
                Toast.makeText(getContext(), "입력창이 비어 있음", Toast.LENGTH_LONG).show();
            }

            //다른 값이 들어가면 출력되는 문
            else
            {
                Log.e("RESULT", "알 수 없는 에러 ERRCODE = " + data);
                Toast.makeText(getContext(), "시스템 오류 입니다. 잠시후 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }


    //DB에 프로젝트 만들기 위한 스레드
    public class MakeProjectDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

            //현재날짜 저장 변수
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
            String time = mFormat.format(date);

            //param값이 하나만 넘어가는 것 같아 param하나의 조원과 프로젝트 이름, 총조원수, 프로젝트비밀번호를 한꺼번에 넘겨줌
            String partner[] = new String[count];
            String param = "u_member=";

            for (int i = 0; i<count; i++) {

                //조원의 아이디를 받아와 배열에 저장
                partner[i] = mArrayList.get(i).getSearchId();

                //u_member=조원1아이디,조원2아이디,조원3아이디....
                param = param.concat(partner[i])+",";


            }
            param = param.concat(myId)+",";

            //u_member=조원1아이디,조원2아이디,조원3아이디....&u_projectTitle=프로젝트이름&u_howManyMembers=총조원수&u_projectPw=비밀번호
            param= param.concat("&u_projectTitle=" + nameValue + "&u_howManyMembers=" + (count+1)+ "&u_projectPw=" + value + "&u_projectBirth="+time+"");

            Log.e("POST", param);
            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/createProject.php");
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

                //프로젝트키를 projectKey변수에 저장
                //clickAdd()메소드에서 사용됨
                projectKey = data;

                //프로젝트 만들기 눌렀을 시 실행되는 메소드
                clickAdd();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    //프로젝트 만들기 눌렀을 시 게시판DB 생성되는 스레드
    @SuppressLint("LongLogTag")
    public void clickAdd() {

        //서버로 전송시킬 데이터
        String prjName = nameValue;

        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://jwab.dothome.co.kr/Android/boardTableCreate.php";

        //Volley plus Library를 이용해서
        //파일 전송하도록..
        //Volley+는 AndroidStudio에서 검색이 안됨 [google 검색 이용]

        //파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getContext(), "프로젝트가 생성되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //param값 php로 전송
        smpr.addStringParam("pname", prjName);
        smpr.addStringParam("pkey", projectKey);

        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(smpr);

    }

}
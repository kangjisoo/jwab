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
import androidx.fragment.app.FragmentActivity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MemberAdd extends AppCompatActivity {
    private static final Object TAG = "MAIN";

    private ArrayList<ProjectPerson> mArrayList;
    private ProjectPersonAdapter mAdapter;

    //리스트에 추가할 번호 변수
    public static int countMember =0;
    private String value;
    private String nameValue;
    private static String  pname, pkey;
    private String projectKey;

    //insert_text창에서 String형으로 받아올 변수
    String stridPhone=null;
    String dbDataCheck="-2";
    boolean alreadyExistIdCheck = true;
    EditText insertText;
    boolean allCheckBoxYesOrNo;
    Button makeButton;
    private String myId;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);

        //추가 카운트 초기화
        countMember=0;

        //로그인된 자신의 아이디
        myId = MainActivity.getsId();

        //리싸이클러뷰를 xml파일에 리싸이클러뷰에 연동
        final RecyclerView recyclerView =(RecyclerView) findViewById(R.id.member_add_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

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
        Button button = (Button)findViewById(R.id.member_add_addbutton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                insertText = (EditText)findViewById(R.id.member_add_insert_id);
                stridPhone = (String)insertText.getText().toString();

                //비어 있을 경우 메세지창 띄우기
                if (stridPhone.equals("")){
                    Toast.makeText(MemberAdd.this,"추가할 조원의 아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                }

                else if (stridPhone.equals(myId)){
                    Toast.makeText(MemberAdd.this,"본인 아이디는 자동으로 추가됩니다.",Toast.LENGTH_LONG).show();
                }

                //비어있지 않으면 실행
                else {

                    //조원 검색해주는 스레드
                    ProjectMemberDB projectMemberDB = new ProjectMemberDB();
                    projectMemberDB.execute();


                    //초기화
                    dbDataCheck = "-2";
                    alreadyExistIdCheck = false;
                }
            }
        });


        //삭제 버튼을 클릭시 선택된 리스트 삭제
        Button deleteButton = (Button)findViewById(R.id.member_add_deletebutton);
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
                                countMember--;
                                //membercount.setText(String.valueOf(mArrayList.size()));

                            }
                        }
                    }

                    //바뀐 상태를 변경해줌
                    mAdapter.notifyDataSetChanged();
                }

                if (listSizeCheck==deleteListSize){
                    Toast.makeText(MemberAdd.this, "삭제된 조원이 없습니다.", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MemberAdd.this, "삭제 완료", Toast.LENGTH_LONG).show();

            }
        });


        //전체선택 체크박스를 체크할 때 발생하는 리스너
        final CheckBox allCheckBox = (CheckBox)findViewById(R.id.member_add_allcheckbox);
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

        //초대하기 버튼 클릭시 수행되는 리스너
        makeButton=(Button)findViewById(R.id.member_add_makebutton);
        makeButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(final View view) {

                memberAdd memberadd = new memberAdd();
                memberadd.execute();

                Toast.makeText(MemberAdd.this, "멤버 초대가 완료되었습니다..", Toast.LENGTH_LONG).show();
                //           getActivity().finish();
//                Intent intent = new Intent(MemberAdd.this, InnerMainRecycler.class);
//                startActivity(intent);


                finish();
            }
        });

    }

    //추가 버튼을 눌렀을때 프로젝트에 아이디가 존재하는 아이디인지 아닌지 확인하기 위해 php와 연결하여 db를 조사
    public class ProjectMemberDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        String pname = ProjectHomeListAdapter.getProjectNameImsi();
        String pkey = ProjectHomeListAdapter.getSee();
        String projectName = pname+"_"+pkey;
        @Override
        protected Void doInBackground(Void... unused) {
            String param = "u_id=" + stridPhone + "&pname=" + projectName + "";
            Log.e("POST", param);
            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/findProjectMember.php");
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
                Log.e("MemberAdd : ", data);

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

                        Toast.makeText(MemberAdd.this, "이미 추가된 조원 입니다.", Toast.LENGTH_LONG).show();
                        alreadyExistIdCheck=true;
                        break;

                    }
                }

                if (alreadyExistIdCheck==false) {

                    //membercount.setText(count);라고 쓰면 오류남 count가 string형이 아니기 때문에
                    //membercount.setText(String.valueOf(mArrayList.size()+1));



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
                    mAdapter.notifyItemInserted(countMember);
                    countMember++;


                    //추가 되고 난 후 insert창을 비워줌줌
                    insertText.getText().clear();

                }

            }

            //db에서 없는 아이디이면 data 1을 출력
            else if (data.equals("1")) {
                Log.e("RESULT", "찾을 수 없는 아이디");
                Toast.makeText(MemberAdd.this, "찾을 수 없는 아이디 입니다.", Toast.LENGTH_LONG).show();
            }

            //입력창이 비어 있으면 data -1을 출력
            else if (data.equals("-1")){
                Log.e("RESULT", "입력창이 빈칸");
                Toast.makeText(MemberAdd.this, "입력창이 비어 있습니다.", Toast.LENGTH_LONG).show();
            }

            //이미 프로젝트에 존재하는 멤버이면 2를 출력
            else if (data.equals("2")){
                Log.e("RESULT", "이미 프로젝트에 존재하는 멤버");
                Toast.makeText(MemberAdd.this, "이미 프로젝트 멤버입니다.", Toast.LENGTH_LONG).show();
            }

            //다른 값이 들어가면 출력되는 문
            else
            {
                Log.e("RESULT", "알 수 없는 에러 ERRCODE = " + data);
                Toast.makeText(MemberAdd.this, "시스템 오류 입니다. 잠시후 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                MemberAdd.this.finish();
            }
        }
    }
    //DB에 프로젝트 만들기 위한 스레드
    public class memberAdd extends AsyncTask<Void, Integer, Void> {
        String data = "";
        String pname = ProjectHomeListAdapter.getProjectNameImsi();
        String pkey = ProjectHomeListAdapter.getSee();
        String projectName = pname+"_"+pkey;
        @Override
        protected Void doInBackground(Void... unused) {

            //param값이 하나만 넘어가는 것 같아 param하나의 조원과 프로젝트 이름, 총조원수, 프로젝트비밀번호를 한꺼번에 넘겨줌
            String partner[] = new String[countMember];
            String param = "u_member=";

            Log.e("확인", countMember+"");

            for (int i = 0; i<countMember; i++) {

                //조원의 아이디를 받아와 배열에 저장
                partner[i] = mArrayList.get(i).getSearchId();

                //u_member=조원1아이디,조원2아이디,조원3아이디....
                param = param.concat(partner[i])+",";
                
            }
           // param = param.concat(myId)+",";

            //u_member=조원1아이디,조원2아이디,조원3아이디....&pname=프로젝트이름
            param= param.concat("&pname=" + pname +"&pkey="+ pkey + "&howManyMember=" + (countMember) + "");

            Log.e("POST", param);
            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/memberAdd.php");
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
//                projectKey = data;

                Log.e("memberAdd : ", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

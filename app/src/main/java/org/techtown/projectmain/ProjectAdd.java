package org.techtown.projectmain;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;


public class ProjectAdd extends AppCompatActivity {
    final Context context = this;
    private ArrayList<ProjectPerson> mArrayList;
    private ProjectPersonAdapter mAdapter;

    //리스트에 추가할 번호 변수
    private int count =0;

    //insert_text창에서 String형으로 받아올 변수
    String stridPhone=null;
    String dbDataCheck="-2";
    boolean alreadyExistIdCheck = true;
    TextView membercount;
    EditText insertText;
    CheckBox allCheckBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_add);

        //총 조원의 수를 나타내기 위해 textview를 받은 변수 (전역 변수로 설정한 이유는 삭제 버튼을 클릭했을 때도 실행, 추가 버튼을 눌렀을 때도 실행되야하기 때문)
        membercount = (TextView) findViewById(R.id.project_add_membercountview);
        //allCheckBox = (CheckBox)findViewById(R.id.project_add_allcheckbox);
        //allCheckBox.setChecked(false);



        //리싸이클러뷰를 xml파일에 리싸이클러뷰에 연동
        final RecyclerView recyclerView =(RecyclerView) findViewById(R.id.project_add_recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        //arraylist를 할당
        mArrayList = new ArrayList<>();

        //person어뎁터를 배열로 만들기
        //mAdapter = new ProjectPersonAdapter(mArrayList);
        mAdapter = new ProjectPersonAdapter(mArrayList);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //추가버튼을 누르면 실행되는 코드
        Button button = (Button)findViewById(R.id.project_add_addbutton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                insertText = (EditText)findViewById(R.id.project_add_insert_id);
                stridPhone = (String)insertText.getText().toString();

                 //비어 있을 경우 메세지창 띄우기
                if (stridPhone.equals("")){
                    Toast.makeText(ProjectAdd.this,"추가할 조원의 아이디를 입력해주세요",Toast.LENGTH_LONG).show();
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
        Button deleteButton = (Button)findViewById(R.id.project_add_deletebutton);
        deleteButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

                recyclerView.setItemViewCacheSize(mArrayList.size());


                for (int i=0; i<mArrayList.size();i++){
                    ProjectPerson checked = mArrayList.get(i);

                    if (checked.isChecked()){
                    //

                        mArrayList.remove(i);
                        //mArrayList.remove(i);

                    Toast.makeText(ProjectAdd.this, "삭제 완료", Toast.LENGTH_LONG).show();
                        //count 변수를 감소시키고 총조원 수를 다시 나타냄
                        //count--;
                        //membercount.setText(String.valueOf(count+1));
                        //mAdapter.notifyItemRemoved(i);
                         mAdapter.notifyDataSetChanged();

                    }

                }

            }
        });
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

                        Toast.makeText(ProjectAdd.this, "이미 추가된 조원 입니다.", Toast.LENGTH_LONG).show();
                        alreadyExistIdCheck=true;
                        break;

                    }
                }

                if (alreadyExistIdCheck==false) {

                    //Toast.makeText(ProjectAdd.this, "추가", Toast.LENGTH_LONG).show();


                    //membercount.setText(count);라고 쓰면 오류남 count가 string형이 아니기 때문에
                    membercount.setText(String.valueOf(count+1));


                    //카운트를 추가 시키고 member로 아래 목록을 리싸이클러뷰에 띄우기
                    ProjectPerson newMember = new ProjectPerson("조원 #" + (count+1), stridPhone, false );
                    //newMember.setChecked(newMember.isChecked());
                    //배열에 newMember값을 추가
                    mArrayList.add(newMember);


                    Toast.makeText(ProjectAdd.this, " " + count , Toast.LENGTH_LONG).show();


                    //어느 위치에 삽입할지를 정해줌 count 위치에 삽입함으로써 리스트 밑에 삽입
                    //0을 넣으면 위에 삽입
                    mAdapter.notifyItemInserted(count);
                    count++;

                    // mAdapter.notifyDataSetChanged();

                    //추가 되고 난 후 insert창을 비워줌줌
                    insertText.getText().clear();

                }

            }

            //db에서 없는 아이디이면 data 1을 출력
            else if (data.equals("1")) {
                Log.e("RESULT", "찾을 수 없는 아이디");
                Toast.makeText(ProjectAdd.this, "찾을 수 없는 아이디 입니다.", Toast.LENGTH_LONG).show();
            }

            //입력창이 비어 있으면 data -1을 출력
            else if (data.equals("-1")){
                Log.e("RESULT", "입력창이 빈칸");
                 Toast.makeText(ProjectAdd.this, "입력창이 비어 있음", Toast.LENGTH_LONG).show();
            }

            //다른 값이 들어가면 출력되는 문
            else
            {
                Log.e("RESULT", "알 수 없는 에러 ERRCODE = " + data);
                 Toast.makeText(ProjectAdd.this, "시스템 오류 입니다. 잠시후 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                finish();
            }
        }


        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Log.e("onItemClick", "클릭 -> "+position);
            //mAdapter.checkedConfirm(position);
            mAdapter.notifyDataSetChanged();
        }

    }

}
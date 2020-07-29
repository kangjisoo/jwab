package org.techtown.projectmain;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Iterator;


public class ProjectAdd extends AppCompatActivity {
    final Context context = this;
    private ArrayList<ProjectPerson> mArrayList;
    private ProjectPersonAdapter mAdapter;

    //리스트에 추가할 번호 변수
    private int count =-1;

    //insert_text창에서 String형으로 받아올 변수
    String stridPhone=null;

    ProjectPerson unid;

    static String oneMoreCheckId = null;
    String dbDataCheck="-2";
    boolean alreadyExistIdCheck = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_add);

        //리싸이클러뷰를 xml파일에 리싸이클러뷰에 연동
        final RecyclerView recyclerView =(RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        //배열을 할당
        mArrayList = new ArrayList<>();

        //person어뎁터를 배열로 만들기
        mAdapter = new ProjectPersonAdapter(mArrayList);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //추가버튼을 누르면 실행되는 코드
        Button button = (Button)findViewById(R.id.addbutton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                EditText insertText = (EditText)findViewById(R.id.insert_text);
                stridPhone = (String)insertText.getText().toString();



                if (stridPhone.equals("")){
                    Toast.makeText(ProjectAdd.this,"추가할 조원의 아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                }
                else {
                    FindMyMemberDB findMyMemberDB = new FindMyMemberDB();
                    findMyMemberDB.execute();

                    //비어 있을 경우 메세지창 띄우기
                    if (dbDataCheck.equals("-1")) {
                        Toast.makeText(ProjectAdd.this, "추가할 조원의 아이디를 입력해주세요2", Toast.LENGTH_LONG).show();

                    }

                    //비어있지 않으면 실행
                    else if (dbDataCheck.equals("1")) {
                        Toast.makeText(ProjectAdd.this, "찾을 수 없는 아이디 입니다.", Toast.LENGTH_LONG).show();

                    } else if (dbDataCheck.equals("0")) {

                        ProjectPerson tmp = null;
                        oneMoreCheckId = stridPhone;

                        count++;
                        ProjectPerson data = new ProjectPerson("조원 #" + (count), stridPhone);
                        tmp = data;


                        //arraylist조회하여 리스트에 존재하는 아이디인지 확인하기 위한 코드
                        for (int i=0; i<mArrayList.size();i++){
                            tmp = mArrayList.get(i);

                            if (stridPhone.equals(tmp.getSearchId())){
                                Toast.makeText(ProjectAdd.this, "이미 추가된 조원 입니다.", Toast.LENGTH_LONG).show();
                                alreadyExistIdCheck=true;
                                break;
                            }
                        }



                        if (alreadyExistIdCheck==false) {
                            Toast.makeText(ProjectAdd.this, "추가"+count, Toast.LENGTH_LONG).show();

                            //카운트를 추가 시키고 member로 아래 목록을 리싸이클러뷰에 띄우기

                            //배열에 data값을 추가
                            mArrayList.add(data);

                            //어느 위치에 삽입할지를 정해줌 count 위치에 삽입함으로써 리스트 밑에 삽입
                            //0을 넣으면 위에 삽입
                            mAdapter.notifyItemInserted(count);

                            //초기화
                            dbDataCheck = "-2";
                            alreadyExistIdCheck = false;
                            oneMoreCheckId = null;

                            // mAdapter.notifyDataSetChanged();
                        }
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
                        "http://" + MainActivity.CONNECTION_IPADDRESS + "/jwabPHP/ProjectMemberAdd.php");
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

                //db에서 존재하는 아이디를 찾았을때 data 0을 출력
                if (data.equals("0")){
                    Log.e("RESULT", "추가 가능한 조원");

                }

                //db에서 없는 아이디이면 data 1을 출력
                else if (data.equals("1")) {
                    Log.e("RESULT", "찾을 수 없는 아이디");
                }

                //입력창이 비어 있으면 data -1을 출력
                else if (data.equals("-1")){
                    Log.e("RESULT", "입력창이 빈칸");
                }

                //다른 값이 들어가면 출력되는 문
                else
                {
                    Log.e("RESULT", "알 수 없는 에러 ERRCODE = " + data);
                }

                //data값을 변수에 넘겨줌
                dbDataCheck=data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
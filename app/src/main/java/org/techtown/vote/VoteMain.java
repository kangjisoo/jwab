package org.techtown.vote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VoteMain extends AppCompatActivity {

    //액티비티를 두번째 화면에서 종료 시키기 위해서 선언된 변수
    public static Activity VoteMainAc;

    public static final int REQUEST_CODE_VOTE = 101;
    private TextView voteTextView,listCountText,listCountTextView;
    private EditText voteTitleEditView, voteAddList;
    private Button voteAddBt,voteDeleteBt,voteMakeBt;
    private CheckBox voteAllCheckBox;
    private RecyclerView voteRecyclerView;

    private ArrayList<VoteData> vArrayList;
    private VoteAdapter vAdapter;

    private int listNum=0;

    public static String voteNameAndKey="";

    public static String GetVoteNameAndKey(){
        return voteNameAndKey;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_main_dp);

        VoteMainAc = VoteMain.this;
        voteTextView = findViewById(R.id.voteTextView);
        listCountText = findViewById(R.id.listCountText);
        listCountTextView = findViewById(R.id.listCountTextView);
        voteTitleEditView=findViewById(R.id.voteTitleEditView);
        voteAddList = findViewById(R.id.voteAddList);
        voteAddBt =findViewById(R.id.voteAddBt);
        voteDeleteBt =findViewById(R.id.voteDeleteBt);
        voteMakeBt = findViewById(R.id.voteMakeBt);
        voteAllCheckBox = findViewById(R.id.voteAllCheckBox);
        voteRecyclerView = findViewById(R.id.voteRecyclerView);


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        voteRecyclerView.setLayoutManager(layoutManager);
        vArrayList= new ArrayList<>();

        //list갯수 = 0
        listCountTextView.setText(Integer.toString(listNum));

        vAdapter = new VoteAdapter(vArrayList);
        voteRecyclerView.setAdapter(vAdapter);

            voteAllCheckBox.setChecked(false);

            //전체선택 클릭 리스너
        voteAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (voteAllCheckBox.isChecked()==true){
                    for (int i =0; i< vArrayList.size(); i++){
                        VoteData allCheckList = vArrayList.get(i);
                        allCheckList.setCheckBoxCh(true);
                    }
                }
                else
                {
                    for (int i =0; i<vArrayList.size(); i++){
                        VoteData allCheckList = vArrayList.get(i);
                        allCheckList.setCheckBoxCh(false);
                    }
                }

                //변경사항 적용
                vAdapter.notifyDataSetChanged();
            }
        });

        //추가버튼 클릭
        voteAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getList;
                getList = (String)voteAddList.getText().toString();

                //추가할 항목이 공백이거나 비워져 있을 때
                if (getList.equals("") || getList.trim().equals("")){
                    Toast.makeText(VoteMain.this,"투표 항목을 입력해주세요",Toast.LENGTH_LONG).show();

                }

                else{

                    //전체선택 선택되어 있을 시
                    if (voteAllCheckBox.isChecked()==true){
                        //리스트 추가
                        VoteData newVoteData = new VoteData(true, getList);
                        vArrayList.add(newVoteData);
                        vAdapter.notifyItemInserted(vAdapter.getItemCount());

                        listNum++;
                        listCountTextView.setText(Integer.toString(listNum));
                    }

                    //전체선택 해제되어 있을 시
                   else {
                        //리스트 추가
                        VoteData newVoteData = new VoteData(false, getList);
                        vArrayList.add(newVoteData);
                        vAdapter.notifyItemInserted(vAdapter.getItemCount());

                        listNum++;
                        listCountTextView.setText(Integer.toString(listNum));

                    }

                    //입력창 비워주기
                    voteAddList.getText().clear();
                }
            }
        });

        //삭제버튼 클릭
        voteDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //arraylist 사이즈를 저장
                int listSizeCheck = vArrayList.size();
                int deleteListSize = vArrayList.size();

                for(int j=0; j<=listSizeCheck; j++){
                    if (vArrayList.size()==0){
                        deleteListSize = vArrayList.size();
                        break;
                    }

                    else{
                        for (int i=0; i<vArrayList.size(); i++){

                            VoteData voteCheck = vArrayList.get(i);

                            if (voteCheck.isCheckBoxCh()){

                                vArrayList.remove(i);

                                deleteListSize = vArrayList.size();

                                listNum--;
                                listCountTextView.setText(Integer.toString(listNum));

                            }
                        }

                    }
                    //바뀐 상태를 변경해줌
                    vAdapter.notifyDataSetChanged();
                }

                if (listSizeCheck==deleteListSize){
                    Toast.makeText(VoteMain.this, "선택된 항목이 없습니다.", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(VoteMain.this, "삭제 완료", Toast.LENGTH_LONG).show();

            }
        });

        //확인버튼 클릭시
        voteMakeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteTitleEditView.getText().toString();

                //제목이 공백이거나 빈칸일 때
                if (voteTitleEditView.equals("")|| voteTitleEditView.getText().toString().trim().equals("")){
                    Toast.makeText(VoteMain.this, "투표 주제를 입력해주세요",Toast.LENGTH_LONG).show();
                }
                //투표 목록이 1개 미만일 때
                else if (vArrayList.size()<1){
                    Toast.makeText(VoteMain.this, "투표 목록이 존재하지 않습니다.",Toast.LENGTH_LONG).show();
                }
                //모든 조건 만족 시시
               else{
                    //db올리는 코드
                    CreateVoteDB createVoteDB = new CreateVoteDB();
                    createVoteDB.execute();

                    //알림DB에 올리는 코드
                    SetNoticeDB setNoticeDB =new SetNoticeDB();
                    setNoticeDB.execute();

                    Intent intent = new Intent(getApplicationContext(), VoteFinish.class);
                    startActivityForResult(intent,REQUEST_CODE_VOTE);

                }
            }
        });

    }


    //db에 투표기능 만드는 스레드
    public class CreateVoteDB extends AsyncTask<Void, Integer, Void>
    {
        String data = "";
        String pname = InnerMainRecycler.getPname();
        String pkey = InnerMainRecycler.getPkey();

        @Override
        protected Void doInBackground(Void... unused) {

            String voteTitle = voteTitleEditView.getText().toString();
            String voteLists[] = new String[listNum];
            String voteAllList="";

            //현재날짜 저장 변수
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String time = mFormat.format(date);

            //현재날짜에서 3일 뒤에 날짜 저장
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE,3);
            String deathDate = mFormat.format(cal.getTime());


            //투표 목록을 합쳐서 변수에 저장
            for (int i =0; i<listNum; i++){

                voteLists[i] = vArrayList.get(i).getVoteListValue();
                voteAllList = voteAllList.concat(voteLists[i])+",";

            }

            String param = "u_voteProjectName=" + pname + "&u_voteProjectPrk="+ pkey +"&u_voteName="+voteTitle+"&u_voteList="+voteAllList+"&u_voteListCount="+(listNum+1)+"&u_voteDate="+deathDate+"";

            //Check param
            Log.e("voteMain.param", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/createVote.php");
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
                Log.e("voteMain : ", data);
                voteNameAndKey = data;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //알림DB에 데이터를 넣어줄 스레드
    public class SetNoticeDB extends AsyncTask<Void, Integer, Void>{
        String data = "";
        String pname = InnerMainRecycler.getPname();
        String pkey = InnerMainRecycler.getPkey();
        String pId = MainActivity.getsId();

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {

            String voteTitle = voteTitleEditView.getText().toString();

            //현재날짜 저장 변수
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
            String time = mFormat.format(date);

            String projectInfo = pname+"_"+pkey;

            String param = "u_nId="+pId+"&u_nContents="+voteTitle+"&u_nDate="+time+"&u_nProjectInfo="+projectInfo+"&u_nKind="+"투표"+"";
            //Check param
            Log.e("VoteMain.setNotice.param", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/setNotice.php");
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
                Log.e("voteMain.Set  : ", data);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

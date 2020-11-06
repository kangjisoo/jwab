package org.techtown.vote;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Calendar;
import java.util.Date;

public class VoteFinish extends AppCompatActivity {

    private TextView voteFinishTitle, voteFinishMember;
    private RecyclerView voteFinishRecyclerView;
    private Button voteFinishBt;

    private ArrayList<VoteFinishData> fArrayList;
    private VoteFinishAdapter fAdapter;
    private String deliverTitle="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_finish_dp);

        voteFinishTitle = findViewById(R.id.voteFinishTitle);
        voteFinishMember = findViewById(R.id.voteFinishMember);
        voteFinishRecyclerView = findViewById(R.id.voteFinishRecyclerView);
        voteFinishBt = findViewById(R.id.voteFinishBt);


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        voteFinishRecyclerView.setLayoutManager(layoutManager);
        fArrayList= new ArrayList<>();


        fAdapter = new VoteFinishAdapter(fArrayList);
        voteFinishRecyclerView.setAdapter(fAdapter);

        voteFinishMember.setText("0");

        GetVoteDB getVoteDB = new GetVoteDB();
        getVoteDB.execute();

        voteFinishBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VoteAttend voteAttend = new VoteAttend();
                voteAttend.execute();

                VoteMain voteActivity = (VoteMain)VoteMain.VoteMainAc;
                VoteDP voteDPActivity = (VoteDP)VoteDP.VoteDPAc;

                //VoteMain, VoteDP 액티비티 종료 후 VoteFinish화면 종료
                voteDPActivity.finish();
                voteActivity.finish();
                finish();

            }
        });


    }


    //투표 자신의 항목 업데이트 하는 스레드
    public class GetVoteDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        String myId = MainActivity.getsId();


        @Override
        protected Void doInBackground(Void... unused) {
            String findVoteName = VoteMain.GetVoteNameAndKey();
            String[] nameKey = findVoteName.split("_");

            deliverTitle = nameKey[0];


            String param = "u_voteName=" + nameKey[0] + "&u_voteKey=" + nameKey[1] + "&u+myId=" + myId + "";

            //Check param
            Log.e("POST.param", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getVoteInfo.php");
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
                Log.e("vote content: ", data);
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

            String[] divided = data.split(",");

            //db에서 받아온 데이터 분활해서 리스트 값으로 넘겨주기
            for (int i = 0; i < divided.length; i++) {
                VoteFinishData newVoteFinishData = new VoteFinishData(false, divided[i]);
                fArrayList.add(newVoteFinishData);
                fAdapter.notifyItemInserted(fAdapter.getItemCount());
            }

            //제목 셋팅
            voteFinishTitle.setText(deliverTitle);
        }
    }

    //투표 참여하는 스레드
    public class VoteAttend extends AsyncTask<Void, Integer, Void>{

        String data = "";
        String myId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {
            String findVoteName = VoteMain.GetVoteNameAndKey();

            if(findVoteName==""){
                findVoteName = VoteDP.getSendVoteInfoFromDP();
            }

            String[] nameKey = findVoteName.split("_");
            String sNum = "";

            //선택항목 true시 값 db에 넘겨주기
            for (int i=0; i<fArrayList.size(); i++) {
                if (fArrayList.get(i).isVoteRadioButton()==true){
                   sNum = fArrayList.get(i).getVoteFinishItems();
                }
            }

            String param = "u_id="+myId+"&u_voteName="+nameKey[0]+"&u_votePrkey="+nameKey[1]+"&u_selectNum="+sNum+"";

            //Check param
            Log.e("POST.param", param);


            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/voteAttend.php");
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
                Log.e("vote content: ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}



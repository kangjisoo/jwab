package org.techtown.vote;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;
import org.techtown.randomgame.RandomGameSecond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class VoteDP extends AppCompatActivity {

    public static Activity VoteDPAc;
    private TextView voteProjectView, voteNothing;
    private RadioGroup voteRadioGroup;
    private RadioButton voteRadioAll;
    private RadioButton voteRadioAt;
    private RadioButton voteRadioNat;
    private RecyclerView voteProjectListRecyclerVIew;
    private Button voteMakeButton;


    private ArrayList<VoteDPData> dArrayList;
    private VoteDPAdapter dAdapter;
    String pname = InnerMainRecycler.getPname();
    String pkey = InnerMainRecycler.getPkey();
    String myId = MainActivity.getsId();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_make_or_attend);

        VoteDPAc = VoteDP.this;
        voteProjectView = findViewById(R.id.voteProjectView);
        voteRadioGroup = findViewById(R.id.voteRadioGroup);
        voteRadioAll = findViewById(R.id.voteRadioAll);
        voteRadioAt =findViewById(R.id.voteRadioAt);
        voteRadioNat = findViewById(R.id.voteRadioNat);
        voteProjectListRecyclerVIew = findViewById(R.id.voteProjectListRecyclerVIew);
        voteMakeButton = findViewById(R.id.voteMakeButton);
        voteNothing = findViewById(R.id.voteNothing);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        voteProjectListRecyclerVIew.setLayoutManager(layoutManager);

        dArrayList = new ArrayList<>();
        dAdapter = new VoteDPAdapter(dArrayList);

        voteProjectListRecyclerVIew.setAdapter(dAdapter);

        GetAllVoteList getAllVoteList = new GetAllVoteList();
        getAllVoteList.execute();

        dAdapter.notifyDataSetChanged();

        //맨 위 textView에 나올 프로젝트 이름
        voteProjectView.setText(pname+" 투표 목록");

        if (dAdapter.getItemCount()==0){
            voteNothing.setVisibility(View.VISIBLE);
        }

        else{
            voteNothing.setVisibility(View.INVISIBLE);
        }

        voteMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), // 현재 화면의 제어권자
                        VoteMain.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });


    }

    public class GetAllVoteList extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {


            String param = "u_projectName="+pname+"&u_projectKey="+pkey+"&u_myId="+myId+"";

            //Check param
            Log.e("POST.param", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getAllVoteList.php");
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
                Log.e("vote list: ", data);
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

            String voteListPrint="";
            voteListPrint=data;

            String[] div = voteListPrint.split("@");

            Log.e("체크", Arrays.toString(div));

            int sequence, sequenceS;
            String[] getVoteName = new String[div.length];
            String[] getVoteInfo = new String[div.length];
            String[] getVoteKey = new String[div.length];
            String[] getSelect = new String[div.length];

            for (int i = 1; i<div.length; i++){


                sequence = div[i].indexOf("/");

                getVoteName[i] = div[i].substring(0,sequence);
                getVoteInfo[i] = div[i].substring(sequence+1);

                sequenceS = getVoteInfo[i].indexOf("!");

                getVoteKey[i] = getVoteInfo[i].substring(0,sequenceS);
                getSelect[i] = getVoteInfo[i].substring(sequenceS+1);

                Log.e("투표이름 :",getVoteName[i]);
                Log.e("중간 확인:",getVoteInfo[i]);
                Log.e("투표키 :",getVoteKey[i]);
                Log.e("투표선택 :",getSelect[i]);

                if(Integer.parseInt(getSelect[i])>0){
                    VoteDPData newVoteDpData = new VoteDPData(getVoteName[i],"참여",Integer.parseInt(getVoteKey[i]));
                    dArrayList.add(newVoteDpData);
                    dAdapter.notifyItemInserted(dAdapter.getItemCount());
                }
                else{
                    VoteDPData newVoteDpData = new VoteDPData(getVoteName[i],"불참",Integer.parseInt(getVoteKey[i]));

                    dArrayList.add(newVoteDpData);
                    dAdapter.notifyItemInserted(dAdapter.getItemCount());
                }

            }


        }
    }
}

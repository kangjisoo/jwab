package org.techtown.laddergame;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class LadderGameMain extends AppCompatActivity {

    private TextView ladderGameTitle, ladderGameTextView1, ladderGameMemberTitle, ladderGameMember;
    private EditText ladderGameEditText;
    private ImageButton ladderGameMinusButton ,ladderGamePlusButton;
    private Button ladderGameCheckBt;

    private String ladderTitleName;
    private static int memberCount=0;
    private int maxMemberCount;

    public static int GetMemberCount() {
        return memberCount;
    }
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ladder_game_main);

        ladderGameTitle = findViewById(R.id.ladderGameTitle);
        ladderGameTextView1 = findViewById(R.id.ladderGameTextView1);
        ladderGameMemberTitle = findViewById(R.id.ladderGameMemberTitle);
        ladderGameMember = findViewById(R.id.ladderGameMember);

        ladderGameEditText = findViewById(R.id.ladderGameEditText);

        ladderGameMinusButton = findViewById(R.id.ladderGameMinusButton);
        ladderGamePlusButton = findViewById(R.id.ladderGamePlusButton);
        ladderGameCheckBt = findViewById(R.id.ladderGameCheckBt);

        ladderGameMember.setText(Integer.toString(memberCount));

        GetMemberCount getMemberCount = new GetMemberCount();
        getMemberCount.execute();

        ladderGameMinusButton.setEnabled(false);

        ladderGameMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinusBt();
            }
        });

        ladderGamePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusBt();
            }
        });

        //확인버튼 클릭
        ladderGameCheckBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ladderTitleName = ladderGameEditText.getText().toString();

                //제목이 공백이거나 입력되지 않았을때
                if(ladderTitleName.equals("") || ladderTitleName.trim().equals("")){
                    Toast.makeText(LadderGameMain.this,"제목을 입력해주세요!",Toast.LENGTH_LONG).show();
                }

                else{
                    LadderGameThis ladderGameThis =new LadderGameThis(4,4);
                    ladderGameThis.drawLine(0,0);
                    ladderGameThis.drawLine(3,0);


                }
            }
        });
    }

    // 마이너스 버튼클릭
    public void MinusBt(){
        if (memberCount==0){
            ladderGameMinusButton.setEnabled(false);
            ladderGamePlusButton.setEnabled(true);
        }
        else if(memberCount==1){
            memberCount--;
            ladderGameMember.setText(Integer.toString(memberCount));
            ladderGameMinusButton.setEnabled(false);
            ladderGamePlusButton.setEnabled(true);
        }
        else if(memberCount>=1 && memberCount<maxMemberCount){
            memberCount--;
            ladderGameMember.setText(Integer.toString(memberCount));
            ladderGameMinusButton.setEnabled(true);
            ladderGamePlusButton.setEnabled(true);
        }
        else if(memberCount==maxMemberCount){
            memberCount--;
            ladderGameMember.setText(Integer.toString(memberCount));
            ladderGameMinusButton.setEnabled(true);
            ladderGamePlusButton.setEnabled(true);
        }
    }

    //플러스 버튼 클릭
    public void PlusBt(){
        if (memberCount>=maxMemberCount){
            ladderGamePlusButton.setEnabled(false);
            ladderGameMinusButton.setEnabled(true);
        }
        else if(memberCount==maxMemberCount-1){
            memberCount++;
            ladderGameMember.setText(Integer.toString(memberCount));
            ladderGamePlusButton.setEnabled(false);
            ladderGameMinusButton.setEnabled(true);
        }
        else if (memberCount<maxMemberCount && memberCount>=1){
            memberCount++;
            ladderGameMember.setText(Integer.toString(memberCount));
            ladderGameMinusButton.setEnabled(true);
            ladderGamePlusButton.setEnabled(true);
        }
        else if (memberCount==0){
            memberCount++;
            ladderGameMember.setText(Integer.toString(memberCount));
            ladderGameMinusButton.setEnabled(true);
            ladderGamePlusButton.setEnabled(true);
        }
    }

    //DB에 총조원 조회
    public class GetMemberCount extends AsyncTask<Void, Integer, Void> {
        String data = "";

//        String pTitle = InnerMainRecycler.getPname();
//        String pTitleKey = InnerMainRecycler.getPkey();

        String pTitle = "test";
        String pTitleKey = "0";
        @Override
        protected Void doInBackground(Void... unused) {

            String param = "&u_projectTitle="+pTitle+"&u_prjKey"+pTitleKey;

            Log.e("POST", param);
            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/ladderGetMemberCount.php");
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

                maxMemberCount = Integer.parseInt(data);
                if (maxMemberCount!=0){
                    Log.i("ladderGame","DB정상 작동");
                }
                else{
                    Log.e("LadderGame!","DB 총 멤버 수 오류");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

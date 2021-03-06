package org.techtown.loginactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.techtown.projectmain.ProjectHome;



public class MainActivity extends Activity {
    public static final int REQUEST_CODE_MENU = 101;
    public static final String CONNECTION_IPADDRESS = "rtemd.suwon.ac.kr";    //20.07.29 현재 RTEMD SERVER address
    StringBuffer buffer = new StringBuffer();
    final Context context = this;
    EditText et_id, et_pw;
    CheckBox chk_auto;
    Button btn_login;
    private ImageView profile_pic;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String img;
    static String sId, sPw;


    public static String getsId() { return sId; }


    //액티비티 옮겼을때 메세지
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU) {


            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //로딩화면
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        et_id = (EditText) findViewById(R.id.editTextTextEmailAddress);
        et_pw = (EditText) findViewById(R.id.editTextTextPassword);
        chk_auto = (CheckBox) findViewById(R.id.project_add_allcheckbox);  //자동로그인 체크박스
        btn_login = (Button) findViewById(R.id.button4); //로그인버튼
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        profile_pic =  findViewById(R.id.profile_pic);


        Button button = findViewById(R.id.SignButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignActivty.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        setting = getSharedPreferences("setting", 0);   //자동로그인할때 필요함
        editor = setting.edit();
        if (setting.getBoolean("chk_auto", false)) {
            et_id.setText(setting.getString("ID", ""));
            et_pw.setText(setting.getString("PW", ""));
            chk_auto.setChecked(true);
        }

        //로그인 버튼 눌렀을때 동작
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();

                } catch (NullPointerException e) {
                    Log.e("err", e.getMessage());
                }

                loginDB lDB = new loginDB();
                lDB.execute();

            }
        });
    }
    public class loginDB extends AsyncTask<Void, Integer, Void> {
    String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";
            Log.e("MainActivity.param:",param);
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/login.php");
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
                Log.e("mainActivity:", data);

                if (data.equals("0")) {
                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                }

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
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            //id == pw일 경우
            if(data.equals("1"))
            {
                if (chk_auto.isChecked()) { //자동로그인 체크시
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();
                    editor.putString("ID", sId);
                    editor.putString("PW", sPw);
                    editor.putBoolean("chk_auto", true);
                    editor.commit();
                } else {    //자동로그인 체크 해제시
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();
                    editor.clear();
                    editor.commit();
                }
                Log.e("RESULT","성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("로그인 되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(MainActivity.this, ProjectHome.class);
                                startActivity(intent);

                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                 dialog.show();
            }

            //id존재, pw오류일경우
            else if(data.equals("0"))
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }

            //id오류 OR id, pw모두 오류일 경우
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록되지 않은 계정입니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }



    }


}
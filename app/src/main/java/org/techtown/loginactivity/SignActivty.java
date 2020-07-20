package org.techtown.loginactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;



public class SignActivty extends AppCompatActivity {
    final Context context = this;

    //중복확인 시 확인가능하면 true, 기본적으로는 false로 설정
    boolean idDoubleCheck=false;

    //중복확인 시 아이디를 수정했을때 확인할 수 있게 도와주는 변수
    static String doubleChekId=null;

    //url한번만 수정할 수 있도록 수정
    final String ourUrl ="http://10.210.11.84/";

    EditText NameText, PhoneText, IdText, PasswordText1, PasswordText2;
    String sname, sphone, sid, spw, spwck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_activty);

        NameText = (EditText) findViewById(R.id.NameText);
        PhoneText = (EditText) findViewById(R.id.PhoneText);
        IdText = (EditText) findViewById(R.id.IdText);
        PasswordText1 = (EditText) findViewById(R.id.PasswordText1);
        PasswordText2 = (EditText) findViewById(R.id.PasswordText2);


        Button button = findViewById(R.id.BackButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", "mike");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //◀ 버튼 누르면 전 액티비티로 돌아가는 코드


    }

    //가입하기버튼을 클릭할 떄 실행되는 버튼 메소드
    public void bt_Join(View view) {

        sname = NameText.getText().toString();
        sphone = PhoneText.getText().toString();
        sid = IdText.getText().toString();
        spw = PasswordText1.getText().toString();
        spwck = PasswordText2.getText().toString();


        //빈칸이 하나라도 있을 경우 메세지 띄움
        if(sname.length()==0||sphone.length()==0||sid.length()==0||spw.length()==0||spwck.length()==0){

            Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_LONG).show();
        }

        //공백확인해 주는 조건문
        else if(spaceCheck(sname)==true||spaceCheck(sphone)==true||spaceCheck(sid)==true||spaceCheck(spw)==true||spaceCheck(spwck)==true){

            Toast.makeText(this,"문자열에 공백이 있으면 안됩니다.",Toast.LENGTH_LONG).show();
        }

        //idDoubleCheck가 완료되지 않으면 기본적으로 false값이 들어가있고 가입하기 눌렀을 시 db에 들어가지 않고 메세지가 뜸
        else if(idDoubleCheck==false) {
            Toast.makeText(this, "중복확인이 되지 않았습니다.", Toast.LENGTH_LONG).show();
        }

        //중복확인 누른 후 아이디를 저장, 아이디 수정했을 때 중복확인을 다시 하게 하기 위해 만든 else if문
        else if(doubleChekId.equals(sid)) {
            if(idDoubleCheck==true) {
                if (spw.equals(spwck)) {

                    Toast.makeText(this, "가입완료", Toast.LENGTH_LONG).show();
                    registDB rdb = new registDB();
                    rdb.execute();
                    finish();
                }
                //중복확인을 되었으나 비밀번호가 맞지 않을경우 메시지
                else if (spw != spwck) {
                    //비밀번호가 서로 맞지 않으면 메세지 박스로 알려줌
                    Toast.makeText(this, "비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show();

                }
                idDoubleCheck=false;
            }
        }
        else
        {
            Toast.makeText(this,"중복확인이 되지 않았습니다.",Toast.LENGTH_LONG).show();
        }

        //중복확인도 되고 비밀번호와 비밀번호확인이 맞을 경우 가입완료

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


    //중복체크 버튼을 누르면 실행되는 버튼 메소드
    public void bt_idcheck(View view) {
        try {
            //IdText에 입력된 문자열을 sid에 대입
            sid = IdText.getText().toString();
        } catch (NullPointerException e) {

            //실패시 로그에 메세지 전달
            Log.e("err", e.getMessage());
        }


        doublecheckDB lDB = new doublecheckDB();
        lDB.execute();

    }

    //회원가입시 아이디 중복 확인 체크하는 클래스
    public class doublecheckDB extends AsyncTask<Void, Integer, Void> {
        //data를 onPostExecute에서 사용할 수 있도록 전역변수로 설정
        String data="";

        @Override
        protected Void doInBackground(Void... unused) {


            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sid + "";
            Log.e("POST",param);
            try {
                /* 서버연결 */
                URL url = new URL(ourUrl+"doublecheck.php");
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
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();


                /* 서버에서 응답 */
                Log.e("RECV DATA",data);

                if(data.equals("0"))
                {
                    Log.e("RESULT","성공적으로 처리되었습니다!");

                }
                else
                {
                    Log.e("RESULT","에러 발생! ERRCODE = " + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //중복확인 눌렀을때 아이디가 중복이 있는지 없는지 확인해주는 메소드(doInBackground에서 할 수 없는 후처리 기능)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /* 서버에서 응답 */
            Log.e("RECV DATA",data);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            //php에서 오는 data를 받아 비교
            // 1이면 같은 아이디 없음
            if(data.equals("1"))
            {
                doubleChekId = sid;
                Log.e("RESULT","성공적으로 처리되었습니다!");
                //idDoubleCheck boolean을 true로 설정해 가입하기를 눌렀을 때 실행 가능할 수 있게 변수를 설정하는 부분
                idDoubleCheck = true;
                alertBuilder
                        .setTitle("알림")
                        .setMessage("사용가능한 아이디 입니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();

                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }


            //그외 다른 값이 들어오면 사용중인 아이디가 있음
            else if (data.equals("0"))
            {
                Log.e("RESULT"," ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("이미 사용중인 아이디 입니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  finish();

                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }

            //아이디칸을 빈칸으로 두고 중복확인을 눌렀을때 php에서 2를 data로 받음
            else if (data.equals("2"))
            {
                Log.e("RESULT","아이디칸이 비워있습니다.");
                //idDoubleCheck boolean을 true로 설정해 가입하기를 눌렀을 때 실행 가능할 수 있게 변수를 설정하는 부분
                alertBuilder
                        .setTitle("알림")
                        .setMessage("아이디를 입력해주세요")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();

                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else{
                Log.e("RESULT","다른 문제가 생겼습니다."+ data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("시스템 오류 잠시후 시도해 주세요")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }


    //앱에 회원가입 데이터를 입력받아 데이터베이스에 새로운 필드를 생성하는 registDB 클래스
    public class registDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {
            /* 인풋 파라메터값 생성 */

            String param = "u_name=" + sname + "&u_phone=" + sphone + "&u_id=" + sid + "&u_pw=" + spw + "&u_pwck=" + spwck + "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        ourUrl+"snclib_join.php");
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
                String data = "";


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                Log.e("RECV DATA", data);

                if (data.equals("0")) {
                    if(idDoubleCheck==false){
                        Log.e("RESULT","아이디 중복체크 안됨");
                    }
                    else {
                        Log.e("RESULT", "완료");
                    }

                }
                else if(data.equals("1")){
                    Log.e("RESUL","빈칸이 있음");

                }

                else {

                    Log.e("RESULT", "error=" + data);
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
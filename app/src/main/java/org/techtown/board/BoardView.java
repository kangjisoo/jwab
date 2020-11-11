package org.techtown.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectHomeListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//게시물 전체 내용을 보여주는 클래스
public class BoardView extends AppCompatActivity {
    private static String pname, pkey;
    public static final int REQUEST_CODE_MENU = 101;


    StringBuffer buffer = new StringBuffer();
    TextView title, writer, date, contents;
    EditText comment;
    ImageView Img1, Img2, Img3, Img4, Img5;
    BoardViewAdapter boardViewAdapter;
    ArrayList<BoardViewList> boardViewLists = new ArrayList<>();
    ArrayList<BoardCommentList> boardCommentList;

    String Date ,Title;
    static String  img1, img2, img3, img4, img5;
    public static String getsImg1() { return img1; }
    public static String getsImg2() { return img2; }
    public static String getsImg3() { return img3; }
    public static String getsImg4() { return img4; }
    public static String getsImg5() { return img5; }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_post);

        title = findViewById(R.id.post_title);
        writer = findViewById(R.id.post_writer);
        date = findViewById(R.id.post_date);
        contents = findViewById(R.id.post_text);
        comment = (EditText)findViewById(R.id.comment);

        Img1 = findViewById(R.id.post_img1);
        Img2 = findViewById(R.id.post_img2);
        Img3 = findViewById(R.id.post_img3);
        Img4 = findViewById(R.id.post_img4);
        Img5 = findViewById(R.id.post_img5);

        Title = BoardMainRecycler.getsTitle();
        String Writer = BoardMainRecycler.getsWriter();
        Date = BoardMainRecycler.getsDate();

        title.setText(Title);
        writer.setText(Writer);
        date.setText(Date);

        boardViewAdapter = new BoardViewAdapter(getLayoutInflater(), boardViewLists);

        LoadDB loadDb = new LoadDB();
        loadDb.execute();
    }

    public class LoadDB extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();

            String param = "pname=" + pname + "&pkey=" + pkey + "&date=" + Date;
            String serverUri = "http://jwab.dothome.co.kr/Android/boardViewLoad.php";

            try {
                URL url = new URL(serverUri);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                //connection.setDoOutput(true);// 이 예제는 필요 없다.
                connection.setUseCaches(false);

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = connection.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
            String[] rows = buffer.toString().split(";");


            //대량의 데이터 초기화
            boardViewLists.clear();

            for (String row : rows) {
                //한줄 데이터에서 한 칸씩 분리
                String[] datas = row.split("&");
                if (datas.length != 6) continue;

                String Contents = datas[0];

                img1 = "http://jwab.dothome.co.kr/Android/" + datas[1];
                img2 = "http://jwab.dothome.co.kr/Android/" + datas[2];
                img3 = "http://jwab.dothome.co.kr/Android/" + datas[3];
                img4 = "http://jwab.dothome.co.kr/Android/" + datas[4];
                img5 = "http://jwab.dothome.co.kr/Android/" + datas[5];
                //데이터 ArrayList에 추가
                boardViewLists.add(new BoardViewList(Contents, img1, img2, img3, img4, img5));

                //게시물 내용과 사진 set해주기
                contents.setText(Contents);
                Glide.with(BoardView.this).load(img1).into(Img1);
                Glide.with(BoardView.this).load(img2).into(Img2);
                Glide.with(BoardView.this).load(img3).into(Img3);
                Glide.with(BoardView.this).load(img4).into(Img4);
                Glide.with(BoardView.this).load(img5).into(Img5);

                Img1.setOnClickListener(new MyListener());
                Img2.setOnClickListener(new MyListener());
                Img3.setOnClickListener(new MyListener());
                Img4.setOnClickListener(new MyListener());
                Img5.setOnClickListener(new MyListener());

            }
        }
    }
    //이미지 클릭했을 시 호출되는 클래스(이미지 크게보기)
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent(BoardView.this, BoardImgClick.class);
            if(v.getId()==R.id.post_img1){
                intent.putExtra("sign",1);
            }
            if(v.getId()==R.id.post_img2){
                intent.putExtra("sign",2);
            }
            if(v.getId()==R.id.post_img3){
                intent.putExtra("sign",3);
            }
            if(v.getId()==R.id.post_img4){
                intent.putExtra("sign",4);
            }
            if(v.getId()==R.id.post_img5){
                intent.putExtra("sign",5);
            }
            startActivityForResult(intent, REQUEST_CODE_MENU);
        }
    }

    @SuppressLint("LongLogTag")
    public void commentload(){

        //서버로 보낼 데이터
        String id = MainActivity.getsId();
        String contents = comment.getText().toString();
        String boardDate = Date;
        pname = ProjectHomeListAdapter.getProjectNameImsi();
        pkey = ProjectHomeListAdapter.getSee();
        String name = Title;


        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl = "http://jwab.dothome.co.kr/Android/boardComment.php";

        //Volley plus Library를 이용해서 파일전송
        //파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                            new AlertDialog.Builder(BoardAddTest.this).setMessage("응답:" + response).create().show();

//                        Intent intent = new Intent(BoardAddTest.this, BoardMainRecycler.class);
//                        startActivity(intent);
//                        Toast.makeText(BoardAddTest.this, "업로드 되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BoardView.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });


        //param값 php로 전송
        smpr.addStringParam("id",id);
        smpr.addStringParam("pname", pname);
        smpr.addStringParam("pkey", pkey);
        smpr.addStringParam("name", name);
        smpr.addStringParam("contents", contents);
        smpr.addStringParam("boardDate",boardDate);


        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smpr);

    }













    public class CommentDB extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();

            String param = "pname=" + pname + "&pkey=" + pkey + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/boardComment.php";


            try {
                URL url = new URL(serverUri);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                //connection.setDoOutput(true);// 이 예제는 필요 없다.
                connection.setUseCaches(false);

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = connection.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            final BoardCommentAdapter adapter = new BoardCommentAdapter(getLayoutInflater(),boardCommentList);
            RecyclerView recyclerView = BoardView.this.findViewById(R.id.comment_recycler);


            //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
            String[] rows = buffer.toString().split(";");


            //대량의 데이터 초기화
            boardCommentList.clear();

            for (String row : rows) {
                //한줄 데이터에서 한 칸씩 분리
                String[] datas = row.split("&");
                if (datas.length != 3) continue;

                String comment = datas[0];
                String id = datas[1];
                String date = datas[2];

                adapter.addItem(new BoardCommentList(comment, id, date));

            }

            recyclerView.setAdapter(adapter);

        }
    }

}





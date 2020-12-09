package org.techtown.board;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.techtown.loginactivity.SignActivty;
import org.techtown.projectinner.InnerMainRecycler;
import org.techtown.projectmain.ProjectHomeFragment2;
import org.techtown.projectmain.ProjectHomeListAdapter;

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
import java.util.Date;

//게시물 전체 내용을 보여주는 클래스
public class BoardView extends AppCompatActivity {
    private static String pname, pkey;
    public static final int REQUEST_CODE_MENU = 101;
    public static BoardView mContext;

    StringBuffer buffer = new StringBuffer();
    TextView title, writer, date, contents;
    EditText comment;
    ImageView Img1, Img2, Img3, Img4, Img5;

    BoardViewAdapter boardViewAdapter;
    BoardCommentAdapter boardCommentAdapter;

    ArrayList<BoardViewList> boardViewLists = new ArrayList<>();
    ArrayList<BoardCommentList> boardCommentList = new ArrayList<>();

    String Date ,Title, Writer;
    static String  img1, img2, img3, img4, img5;
    public static String getsImg1() { return img1; }
    public static String getsImg2() { return img2; }
    public static String getsImg3() { return img3; }
    public static String getsImg4() { return img4; }
    public static String getsImg5() { return img5; }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_post);

        mContext=this;

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
        Writer = BoardMainRecycler.getsWriter();
        Date = BoardMainRecycler.getsDate();





        boardViewAdapter = new BoardViewAdapter(getLayoutInflater(), boardViewLists);
        boardCommentAdapter = new BoardCommentAdapter(getLayoutInflater(), boardCommentList);

        ImageButton button = findViewById(R.id.comment_bt);

        //댓글 전송 버튼 누를 시
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contents = comment.getText().toString();
                if (contents.length() == 0) {
                    Toast.makeText(BoardView.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    commentload();



                    Toast.makeText(BoardView.this, "commentLoad 실행완료", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                CommentDB commentDB = new CommentDB();
                commentDB.execute();
                //게시물 작성자 프로필사진 set
                profileImgDB profileImgdb = new profileImgDB();
                profileImgdb.execute();

                Img1.setOnClickListener(new MyListener());
                Img2.setOnClickListener(new MyListener());
                Img3.setOnClickListener(new MyListener());
                Img4.setOnClickListener(new MyListener());
                Img5.setOnClickListener(new MyListener());

                //댓글 리사이클러뷰



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


    //댓글 전송버튼 누를 시, DB로 댓글,프로젝트명, id, 날짜 넘겨주기
    @SuppressLint("LongLogTag")
    public void commentload(){
        final BoardCommentAdapter adapter = new BoardCommentAdapter(getLayoutInflater(),boardCommentList);
        RecyclerView recyclerView = BoardView.this.findViewById(R.id.comment_recycler);

        //서버로 보낼 데이터
        String id = MainActivity.getsId();                      //댓글 남기는 사용자의 id
        String contents = comment.getText().toString();         //댓글내용
        String boardDate = Date;                                //댓글을 남기는 해당 게시물의 날짜
        pname = ProjectHomeListAdapter.getProjectNameImsi() +
                      "_" + ProjectHomeListAdapter.getSee();    //프로젝트명_프로젝트키
        String name = Title;                                    //게시물의 제목



        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl = "http://jwab.dothome.co.kr/Android/boardComment.php";

        //Volley plus Library를 이용해서 파일전송
        //파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            //new AlertDialog.Builder(BoardView.this).setMessage("응답:" + response).create().show();

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
        smpr.addStringParam("name", name);
        smpr.addStringParam("contents", contents);
        smpr.addStringParam("boardDate",boardDate);

//현재날짜 저장 변수
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
        String time = mFormat.format(date);


        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smpr);

        adapter.addItem(new BoardCommentList(contents, id, time));
        adapter.notifyItemInserted(adapter.getItemCount());
        adapter.notifyDataSetChanged();

        //댓글을 달았을 시 알림DB에 데이터 전송되는 스레드
        SetNoticeDB setNoticeDB = new SetNoticeDB();
        setNoticeDB.execute();





        //버튼 누르면 댓글창 초기화
        comment.setText(null);



    }


    //댓글 로드하는 스레드
    public class CommentDB extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi() + "_" + ProjectHomeListAdapter.getSee();
            String boardDate = Date;
            String boardTitle = Title;


            String param = "pname=" + pname + "&boardDate=" + boardDate + "&boardTitle=" + boardTitle +"";
            String serverUri = "http://jwab.dothome.co.kr/Android/boardCommentLoad.php";


            try {
                URL url = new URL(serverUri);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                //connection.setDoOutput(true);
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

    //알림DB에 데이터를 넣어줄 스레드
    public class SetNoticeDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        String pname = InnerMainRecycler.getPname();
        String pkey = InnerMainRecycler.getPkey();
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String commentTitle = comment.getText().toString();

            //현재날짜 저장 변수
            long now = System.currentTimeMillis();
            java.util.Date date = new Date(now);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
            String time = mFormat.format(date);

            Log.e("notice Time check", time);

            String projectInfo = pname+"_"+pkey;

            String param = "u_nId="+pId+"&u_nContents="+commentTitle+"&u_nDate="+time+"&u_nProjectInfo="+projectInfo+"&u_nKind="+"댓글"+"";
            //Check param
            Log.e("VoteMain.param", param);

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
                Log.e("notice  : ", data);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //게시글 작성자의 프로필사진, 이름, 날짜, 제목 set해주는 스레드
    public class profileImgDB extends com.android.volley.misc.AsyncTask<Void, Integer, Void> {
        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            String id = MainActivity.getsId();
            String param = "id=" + id + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/profileImgLoad.php";

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

            String Path = buffer.toString();

            ImageView board_img = (ImageView) findViewById(R.id.post_writer_pic);

            String img = "http://jwab.dothome.co.kr/Android/" + Path.trim();

            Glide.with(BoardView.this).load(img).error(R.drawable.ic_menu_camera).into(board_img);
            title.setText(Title);
            writer.setText(Writer);
            date.setText(Date);

        }
    }
}





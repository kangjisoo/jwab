package org.techtown.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class BoardMainRecycler extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton imageButton;
    BoardList item;
    ArrayList<BoardList> boardList = new ArrayList<>();
    static String title, writer, date,Contents;
    public static String getsTitle() { return title; }
    public static String getsWriter(){return writer;}
    public static String getsDate(){return date;}

    private static String pname, pkey;



    StringBuffer buffer = new StringBuffer();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inner_menu, menu);
        return true;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        툴바의 옵션메뉴 동작
//        setHasOptionsMenu(true);
        setContentView(R.layout.board_main);
        BoardAdapter adapter = new BoardAdapter(getLayoutInflater(),boardList);
        recyclerView = (RecyclerView) findViewById(R.id.board_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //플러스 버튼 누르면 프로젝트생성 액티비티로 전환
        imageButton = (ImageButton) findViewById(R.id.post_add);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardMainRecycler.this, BoardAddTest.class);
                startActivity(intent);
            }
        });


            loadDB loaddb = new loadDB();
            loaddb.execute();

    }

    //게시판DB에서 제목,글쓴이,작성날짜를 가져와서 리사이클러뷰에 띄워주는 클래스
    public class loadDB extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();

            String param = "pname=" + pname + "&pkey=" + pkey + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/loadDB.php";


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
            final BoardAdapter adapter = new BoardAdapter(getLayoutInflater(),boardList);
            RecyclerView recyclerView = BoardMainRecycler.this.findViewById(R.id.board_recyclerView);


            //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
            String[] rows = buffer.toString().split(";");


            //대량의 데이터 초기화
            boardList.clear();

            for (String row : rows) {
                //한줄 데이터에서 한 칸씩 분리
                String[] datas = row.split("&");
                if (datas.length != 3) continue;

                String title = datas[0];
                String id = datas[1];
                String date = datas[2];

                adapter.addItem(new BoardList(title, id, date));

            }

            recyclerView.setAdapter(adapter);

            //게시물 아이템을 클릭했을 때 실행
            adapter.setOnItemClicklistener(new BoardItemClickListener() {
                @Override
                public void onItemClick(BoardAdapter.ViewHolder holder, View view, int position) {
                    item = adapter.getItem(position);
                    title = item.getTitle();
                    writer = item.getWriter();
                    date = item.getDate();

                    Intent intent = new Intent(BoardMainRecycler.this, BoardView.class);
                    startActivity(intent);
                }
            });

        }
    }

}

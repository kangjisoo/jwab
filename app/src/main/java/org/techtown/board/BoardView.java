package org.techtown.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

//게시물 전체 내용을 보여주는 클래스
public class BoardView extends AppCompatActivity {
    private static String pname, pkey;
    StringBuffer buffer = new StringBuffer();
    TextView title, writer, date, contents;
    ImageView Img1, Img2, Img3, Img4, Img5;
    BoardViewAdapter boardViewAdapter;
    BoardViewList item;
    ArrayList<BoardViewList> boardViewLists = new ArrayList<>();

    String Date;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_post_pic);

        title = findViewById(R.id.post_title);
        writer = findViewById(R.id.post_writer);
        date = findViewById(R.id.post_date);
        contents = findViewById(R.id.post_text);

        Img1 = findViewById(R.id.post_img1);
        Img2 = findViewById(R.id.post_img2);
        Img3 = findViewById(R.id.post_img3);
        Img4 = findViewById(R.id.post_img4);
        Img5 = findViewById(R.id.post_img5);

        String Title = BoardMainRecycler.getsTitle();
        String Writer = BoardMainRecycler.getsWriter();
        Date = BoardMainRecycler.getsDate();

        title.setText(Title);
        writer.setText(Writer);
        date.setText(Date);

        LoadDB loadDb = new LoadDB();
        loadDb.execute();
        boardViewAdapter = new BoardViewAdapter(getLayoutInflater(),boardViewLists);



    }

    public class LoadDB extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();

            String param = "pname=" + pname + "&pkey=" + pkey + "&date=" + Date;
            String serverUri = "http://jwab.dothome.co.kr/Android/boardViewLoad.php";
            Log.e("Date값 ㅇ오롱롱롱ㄹ", Date);

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
            Log.e("bufferrrrrrrrrrrr", String.valueOf(buffer));
            //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
            String[] rows = buffer.toString().split(";");


            //대량의 데이터 초기화
            boardViewLists.clear();
            String img1, img2, img3, img4, img5;
            for (String row : rows) {
                //한줄 데이터에서 한 칸씩 분리
                String[] datas = row.split("&");
                if (datas.length != 6) continue;

                String Contents = datas[0];
                Log.e("contentsssssssss: ", Contents);
                //이미지는 상대경로라 앞에 서버 주소를 써줘야 됨
//                if(datas[1]!=null){
//                img1 = "http://jwab.dothome.co.kr/Android/"+datas[1];
//                }else{
//                    img1 = "null";
//                }
//                if(datas[2]!=null){
//                    img2 = "http://jwab.dothome.co.kr/Android/"+datas[2];
//                }else{
//                    img2 = "null";
//                }
//                if(datas[3]!=null){
//                    img3 = "http://jwab.dothome.co.kr/Android/"+datas[3];
//                }else{
//                    img3 = "null";
//                }
//                if(datas[4]!=null){
//                    img4 = "http://jwab.dothome.co.kr/Android/"+datas[4];
//                }else{
//                    img4 = "null";
//                }
//                if(datas[5]!=null){
//                    img5 = "http://jwab.dothome.co.kr/Android/"+datas[5];
//                }else{
//                    img5 = "null";
//                }
                img1 = "http://jwab.dothome.co.kr/Android/"+datas[1];
                img2 = "http://jwab.dothome.co.kr/Android/"+datas[2];
                img3 = "http://jwab.dothome.co.kr/Android/"+datas[3];
                img4 = "http://jwab.dothome.co.kr/Android/"+datas[4];
                img5 = "http://jwab.dothome.co.kr/Android/"+datas[5];
                //데이터 ArrayList에 추가
                boardViewLists.add(new BoardViewList(Contents, img1, img2, img3, img4, img5));
                contents.setText(Contents);


               // item = boardViewAdapter.getItem(position);




            }


        }
    }








}

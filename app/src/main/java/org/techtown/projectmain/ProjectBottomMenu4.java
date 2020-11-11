package org.techtown.projectmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.misc.AsyncTask;

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

public class ProjectBottomMenu4 extends Fragment {

    private RecyclerView notice_RecyclerView;
    private ArrayList<NoticeData> nArraylist;
    private NoticeAdapter nAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup noticeView = (ViewGroup)inflater.inflate(R.layout.project_bottom_menu4, container, false);

        nArraylist = new ArrayList<>();

        notice_RecyclerView = (RecyclerView)noticeView.findViewById(R.id.notice_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        notice_RecyclerView.setLayoutManager(layoutManager);
        nAdapter = new NoticeAdapter(nArraylist);

        notice_RecyclerView.setAdapter(nAdapter);

        GetEveryNotice getEveryNotice = new GetEveryNotice();
        getEveryNotice.execute();

        return noticeView;
    }

    public class GetEveryNotice extends AsyncTask<Void, Integer, Void>{
        String data = "";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String param = "u_id="+pId+"";

            //Check param
            Log.e("POST.param", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getNotice.php");
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
    }
}
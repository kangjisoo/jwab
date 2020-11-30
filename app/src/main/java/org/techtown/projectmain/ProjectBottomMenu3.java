package org.techtown.projectmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

//슬라이드 내 활동 탭
public class ProjectBottomMenu3 extends Fragment {

    private RecyclerView myActivities_RecyclerView;
    private ArrayList<MyActivitiesData> mArrayList;
    private MyActivitiesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup myActivitiesView = (ViewGroup)inflater.inflate(R.layout.project_bottom_menu3, container, false);

        mArrayList = new ArrayList<>();

        myActivities_RecyclerView = myActivitiesView.findViewById(R.id.myActivities_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myActivities_RecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyActivitiesAdapter(mArrayList);
        myActivities_RecyclerView.setAdapter(mAdapter);

        GetMyAvtions getMyAvtions = new GetMyAvtions();
        getMyAvtions.execute();

        return myActivitiesView;
    }

    //내 활동만 가져오는 스레드
    public class GetMyAvtions extends AsyncTask<Void, Integer, Void> {
        String data="";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String param = "u_id="+pId+"";

            //Check param
            Log.e("ProjectBottomMenu3: ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getMyActivities.php");
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
                Log.e("my Activities : ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
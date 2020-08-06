package org.techtown.projectmain;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Arrays;

public class ProjectHomeFragment0 extends Fragment {
    ImageButton imageButton;
    ItemTouchHelper helper;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.project_home_list, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.project_home_list_recyclerView);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ProjectHomeListAdapter adapter = new ProjectHomeListAdapter();
        recyclerView.setAdapter(adapter);

        imageButton = (ImageButton) rootView.findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectAdd.class);
                startActivity(intent);
            }
        });
        MyProjectDB m = new MyProjectDB();
        m.execute();

        return rootView;

    }

    //로그인 된 ID에 맞는 프로젝트 찾기
    public class MyProjectDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {
            String param = "param1=" + MainActivity.getsId() + "";
            //String param = "param1=z100444";
            Log.e("POST", param);
            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getProjectInfo.php");
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

                //db에서 존재하는 아이디를 찾았을때 data 0을 출력


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
            ProjectHomeListAdapter adapter = new ProjectHomeListAdapter();
            RecyclerView recyclerView = getView().findViewById(R.id.project_home_list_recyclerView);



            /* 서버에서 응답 */
            Log.e("RECV DATA", data);

            // AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            String projectName = data;
            String[]splited = projectName.split("@");

            Log.e("projectNameTest= ", Arrays.toString(splited));
            //ItemTouchHelper 생성
            helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
            //RecyclerView에 ItemTouchHelper 붙이기
            helper.attachToRecyclerView(recyclerView);

            for(int i = 1; i < splited.length; i++){
                adapter.addItem(new ProjectHomeList(splited[i], "kangjisoo"));
            }
            recyclerView.setAdapter(adapter);

        }

    }
    private void setUpRecyclerView(){
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
                helper.onDraw(c,parent, state);
            }
        });
    }

}

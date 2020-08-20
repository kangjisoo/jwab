package org.techtown.projectmain;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
//리사이클러뷰의 Main

public class ProjectHomeRecyclerView extends Fragment {
    ImageButton imageButton;    //플러스버튼
    ItemTouchHelper helper;
    RecyclerView recyclerView;
    //Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.project_home_recycler, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.project_home_list_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ProjectHomeListAdapter adapter = new ProjectHomeListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        //플러스 버튼 누르면 프로젝트생성 액티비티로 전환
        imageButton = (ImageButton) rootView.findViewById(R.id.plusButton);
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


    //로그인 된 ID에 맞는 프로젝트 가져오기
    public class MyProjectDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {
            String param = "param1=" + MainActivity.getsId() + "";

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
                Log.e("MyProjectDB DATA", data);

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
            ProjectHomeListAdapter adapter = new ProjectHomeListAdapter(getActivity());
            RecyclerView recyclerView = getView().findViewById(R.id.project_home_list_recyclerView);

            /* 서버에서 응답 */
            Log.e("ProjectNameString", data);
            String projectNameString = data;

            //String으로 받아온 프로젝트 이름을 "@"로 구분
            String[]splited = projectNameString.split("@");
            //projectName만을 추출
            String projectName[] = new String[splited.length+1];

            Log.e("projectNameTest= ", Arrays.toString(splited));

            //ItemTouchHelper 생성
            helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
            //RecyclerView에 ItemTouchHelper 붙이기
            helper.attachToRecyclerView(recyclerView);

            for(int i = 1; i < splited.length; i++){
                //각각 나눌 번호를 저장할 index번호
                int index, index2, index3;

                String inn[] = new String[splited.length];
                int countMember[] = new int[splited.length];
                String imsi[]=new String[splited.length];

                //prjket를 구분하는"_", countMember를 구분하는 "/"
                index = splited[i].indexOf("_");
                index2 = splited[i].indexOf("/");

                //substring(0,index) -> 처음부터 index까지만 출력
                //ex) splited[0]=프로젝트1_0/3 --> 프로젝트 이름: 프로젝트1, projectKey: 0, 총조원 수 : 3명
                //projectName[0] = 1
                projectName[i] = splited[i].substring(0,index);

                //substring(index+1) -> 찾은 문자부터 끝까지 출력
                //imsi[0] = 0/3
                imsi[i] = splited[i].substring(index+1);

                //imsi에서 "/"찾기
                index3 = imsi[i].indexOf("/");
                Log.e("imsiTest ", imsi[i]);

                //inn[0] = 0
                inn[i]=imsi[i].substring(0,index3);
                Log.e("projectNameTest2= ", inn[i]);

                //조원의 총 수를 String 형으로 받아와 int형으로 변환 후 -1 (누구님 외 몇명)
                countMember[i] =  Integer.parseInt(splited[i].substring(index2+1))-1;

                //카드뷰에 프로젝트 이름과 인원수 입력
                adapter.addItem(new ProjectHomeList(projectName[i], MainActivity.getsId() +"님 외 " + countMember[i] +"명"));
                adapter.items.get(i-1).setKey(inn[i]);


            }

            recyclerView.setAdapter(adapter);

        }

    }

}

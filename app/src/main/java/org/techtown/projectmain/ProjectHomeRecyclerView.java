package org.techtown.projectmain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.techtown.board.BoardCommentList;
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
    ProjectAdd projectAdd;

    //프로젝트 정보(이름, 상태메시지)를 가져올 변수
    String projectData = "";
    String projectNameString="";
    public static StringBuffer buffer = new StringBuffer();

    public static ProjectHomeRecyclerView newInstance() {
        return new ProjectHomeRecyclerView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.project_home_recycler, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.project_home_list_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ProjectHomeListAdapter adapter = new ProjectHomeListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        projectAdd = new ProjectAdd();

        //플러스 버튼 누르면 프로젝트생성 액티비티로 전환
        imageButton = (ImageButton) rootView.findViewById(R.id.ladderGamePlusButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProjectHome) getActivity()).replaceFragment(projectAdd);

            }
        });
        ProjectHome.profileImgDB profileImgdb = new ProjectHome.profileImgDB();
        profileImgdb.execute();

        MyProjectDB m = new MyProjectDB();
        m.execute();




        return rootView;

    }


    //로그인 된 ID에 맞는 프로젝트 가져오기
    public class MyProjectDB extends AsyncTask<Void, Integer, Void> {


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
                projectData = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("MyProjectDB DATA", projectData);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /* 서버에서 응답 */
            Log.e("ProjectNameString", projectData);

            String[] splited = projectData.split("@");
            String projectName2[] = new String[splited.length + 1];

            for (int i = 1; i < splited.length; i++) {
                //각각 나눌 번호를 저장할 index번호
                int index;

                //indexOf 몇번째에 있는지 알려주는 변수
                index = splited[i].indexOf("/");


                //substring(0,index) -> 처음부터 index까지만 출력
                //ex) splited[0]=프로젝트1_0/3 --> 프로젝트 이름: 프로젝트1, projectKey: 0, 총조원 수 : 3명
                //projectName[0] = 1
                projectName2[i] = splited[i].substring(0, index);
                Log.e("split한 프로젝트 이름", projectName2[i]);

                //projectLoad에서 사용할 로그인된 사용자의 프로젝트 이름 (프로젝트이름_0@프로젝트이름_1@...)
                projectNameString = projectNameString + "@" + projectName2[i];




            }
            Log.e("프로젝트이름 어떻게 넘어가나", projectNameString);
            ProjectLoad projectLoad = new ProjectLoad();
            projectLoad.execute();
        }
    }
        //리사이클러뷰에 프로젝트 사진과 이름, 조원수 표시해줌
        public class ProjectLoad extends AsyncTask<Void, Integer, Void> {
            @SuppressLint("LongLogTag")
            @Override
            protected Void doInBackground(Void... unused) {

                String param = "pData=" + projectNameString + "";
                Log.e("tqtqtqtqtqkfergjawoiergh", projectNameString);
                String serverUri = "http://jwab.dothome.co.kr/Android/projectHomeLoad.php";

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
                ProjectHomeListAdapter adapter = new ProjectHomeListAdapter(getActivity());
                RecyclerView recyclerView = getView().findViewById(R.id.project_home_list_recyclerView);

                //프로젝트 이미지 파일 경로
                String imgString = String.valueOf(buffer);
                //프로젝트 이름, 조원수
                String projectNameString = projectData;

                Log.e("imgString", imgString);
                //문자열로 받아온 이미지 경로를 "@"로 나눠줌
                String[] imgPath = imgString.split("@");



                //문자열로 받아온 프로젝트 이름을 "@"로 구분
                String[] splited = projectNameString.split("@");
                //projectName만을 추출
                String projectName[] = new String[splited.length+1];

                Log.e("projectNameTest= ", Arrays.toString(splited));

                //ItemTouchHelper 생성
                helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
                //RecyclerView에 ItemTouchHelper 붙이기
                helper.attachToRecyclerView(recyclerView);

                for (int i = 1; i < splited.length; i++) {
                    //각각 나눌 번호를 저장할 index번호
                    int index, index2, index3;

                    String inn[] = new String[splited.length];
                    int countMember[] = new int[splited.length];
                    String imsi[] = new String[splited.length];

                    //prjket를 구분하는"_", countMember를 구분하는 "/"
                    //indexOf 몇번째에 있는지 알려주는 메소드
                    index = splited[i].indexOf("_");
                    index2 = splited[i].indexOf("/");

                    //substring(0,index) -> 처음부터 index까지만 출력
                    //ex) splited[0]=프로젝트1_0/3 --> 프로젝트 이름: 프로젝트1, projectKey: 0, 총조원 수 : 3명
                    //projectName[0] = 1
                    projectName[i] = splited[i].substring(0, index);


                    //substring(index+1) -> 찾은 문자부터 끝까지 출력
                    //imsi[0] = 0/3
                    imsi[i] = splited[i].substring(index + 1);

                    //imsi에서 "/"찾기
                    index3 = imsi[i].indexOf("/");


                    //inn[0] = 0
                    inn[i] = imsi[i].substring(0, index3);


                    //조원의 총 수를 String 형으로 받아와 int형으로 변환 후 -1 (누구님 외 몇명)
                    countMember[i] = Integer.parseInt(splited[i].substring(index2 + 1)) - 1;
                    Log.e("projectName[",i+ "]= " + projectName[i]);
                    Log.e("countMember[",i+ "]= " + String.valueOf(countMember[i]));
                    Log.e("imgPath[",i+ "]= " + imgPath[i].trim());

                    //카드뷰에 프로젝트 이름과 인원수 입력
                    adapter.addItem(new ProjectHomeList(projectName[i], MainActivity.getsId() + "님 외 " + countMember[i] + "명",imgPath[i].trim()));
                    adapter.items.get(i - 1).setKey(inn[i]);
                    adapter.notifyItemInserted(adapter.getItemCount());
                    adapter.notifyDataSetChanged();


                }
                recyclerView.setAdapter(adapter);



            }
        }

    }

package org.techtown.projectmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import java.util.Arrays;

//슬라이드 내 활동 탭
public class ProjectBottomMenu3 extends Fragment implements onBackPressedListener{

    private RecyclerView myActivities_RecyclerView;
    private ArrayList<MyActivitiesData> mArrayList;
    private MyActivitiesAdapter mAdapter;

    //프래그먼트 종료 시켜주는 메소드
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(ProjectBottomMenu3.this).commit();
        fragmentManager.popBackStack();
    }

    //뒤로가기 버튼 눌렀을 때 홈화면 전환하고 전 프래그먼트 종료
    @Override
    public void onBackPressed() {
        ((ProjectHome)getActivity()).replaceFragment(ProjectHomeRecyclerView.newInstance());
        goToMain();
    }

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

        //내 아이디와 알림DB에 있는 내아이디를 조회하여 정보들을 출력하여 list에 추가
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String myActions = "";
            myActions = data;

            String[] firstDiv = myActions.split("@");
            int actionCount = 0;
            actionCount = firstDiv.length;

            Log.e("values check", Arrays.toString(firstDiv));

            int sequence, sequence2, sequence3, sequence4, sequence5;
            String[] getWriteId = new String[actionCount];
            String[] NoticeInfo = new String[actionCount];
            String[] getContents = new String[actionCount];
            String[] remainInfo = new String[actionCount];
            String[] getDate = new String[actionCount];
            String[] remainProjectKind = new String[actionCount];
            String[] getProjectName = new String[actionCount];
            String[] getKind = new String[actionCount];

            for (int i=1; i<actionCount; i++){

                sequence = firstDiv[i].indexOf("/");

                getWriteId[i] = firstDiv[i].substring(0,sequence);
                NoticeInfo[i] = firstDiv[i].substring(sequence+1);

                sequence2 = NoticeInfo[i].indexOf("!");

                getContents[i] = NoticeInfo[i].substring(0,sequence2);
                remainInfo[i] = NoticeInfo[i].substring(sequence2+1);

                sequence3 = remainInfo[i].indexOf("?");

                getDate[i] = remainInfo[i].substring(0,sequence3);
                remainProjectKind[i] = remainInfo[i].substring(sequence3+1);

                sequence4 = remainProjectKind[i].indexOf("_");

                getProjectName[i] = remainProjectKind[i].substring(0,sequence4);

                sequence5 = remainProjectKind[i].indexOf("#");

                getKind[i] = remainProjectKind[i].substring(sequence5+1);

                Log.e("값 제대로 들어가는지 확인", getWriteId[i]+"/"+getContents[i]+"/"+getDate[i]+"/"+getProjectName[i]+"/"+getKind[i]+"");

                MyActivitiesData myActivitiesData = new MyActivitiesData(getWriteId[i],getKind[i],getContents[i],getDate[i],getProjectName[i]);

                //첫번째 줄에 삽입
                mArrayList.add(0,myActivitiesData);
                mAdapter.notifyItemInserted(0);

            }

        }
    }
}
package org.techtown.projectmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.misc.AsyncTask;
import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.randomgame.RandomGameFirst;
import org.techtown.randomgame.RandomGameSecond;

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
import java.util.Objects;

//하단바 알림 탭
public class ProjectBottomMenu4 extends Fragment implements onBackPressedListener{

    private RecyclerView notice_RecyclerView;
    private ArrayList<NoticeData> nArraylist;
    private NoticeAdapter nAdapter;

    //프래그먼트 종료 시켜주는 메소드
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(ProjectBottomMenu4.this).commit();
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


    //모든 알림 가져오는 스레드
    public class GetEveryNotice extends AsyncTask<Void, Integer, Void>{
        String data = "";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String param = "u_id="+pId+"";

            //Check param
            Log.e("ProjectBottomMenu4: ", param);

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
                Log.e("notice data: ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //가져온 자료들을 분활하여 추가
        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String noticeBell = "";
            noticeBell = data;

            String[] firstDiv = noticeBell.split("@");
            int noticeCount = 0;
            noticeCount = firstDiv.length;

            Log.e("values check", Arrays.toString(firstDiv));

            int sequence, sequence2, sequence3, sequence4, sequence5;
            String[] getWriteId = new String[noticeCount];
            String[] NoticeInfo = new String[noticeCount];
            String[] getContents = new String[noticeCount];
            String[] remainInfo = new String[noticeCount];
            String[] getDate = new String[noticeCount];
            String[] remainProjectKind = new String[noticeCount];
            String[] getProjectName = new String[noticeCount];
            String[] getKind = new String[noticeCount];

            for (int i=1; i<noticeCount; i++){

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

                Log.e("projectBottomMenu4 value check:", getWriteId[i]+"/"+getContents[i]+"/"+getDate[i]+"/"+getProjectName[i]+"/"+getKind[i]+"");

                NoticeData addNoticeData = new NoticeData(getWriteId[i],getContents[i],getDate[i],getProjectName[i],getKind[i]);

                //첫번째 줄에 삽입
                nArraylist.add(0,addNoticeData);
                nAdapter.notifyItemInserted(0);

            }

        }
    }


}
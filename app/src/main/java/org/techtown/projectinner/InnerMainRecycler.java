package org.techtown.projectinner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectHome;
import org.techtown.projectmain.ProjectHomeList;
import org.techtown.projectmain.ProjectHomeListAdapter;
import org.techtown.projectmain.ProjectHomeRecyclerView;
import org.w3c.dom.Text;

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

import org.techtown.projectmain.ProjectHomeListAdapter;


public class InnerMainRecycler extends Fragment {
    private String personIdString;
    RecyclerView recyclerView;
    public String[] splited2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        InnerListAdapter adapter = new InnerListAdapter(getActivity());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.inner_project, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.inner_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        InnerDB innerDB = new InnerDB();
        innerDB.execute();

        return rootView;
    }

        //선택된 프로젝트의 회원 정보를 가져오는 DB
        public class InnerDB extends AsyncTask<Void, Integer, Void> {
            String data = "";


            @Override
        protected Void doInBackground(Void... unused) {
            //선택된 프로젝트 이름과 key값 받아옴
          String pname=  ProjectHomeListAdapter.getProjectNameImsi();
          String pkey = ProjectHomeListAdapter.getSee();

            String param = "param1=" + pname+ "_" + pkey+"";
            Log.e("프로젝트이름 확인 ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getPersonInfo.php");
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
                Log.e("InnerDB DATA", data);

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

            /* 서버에서 응답 */
            Log.e("PersonID ", data);
            personIdString = data;

            //GetNameDB에서 내 아이디와 팀원들의 아이디를 비교 연산하기 위해 splited2사용
            splited2 = personIdString.split("@");

            GetNameDB getNameDB= new GetNameDB();
            getNameDB.execute();


        }
    }



    //받아온 id를 userTable의 이름을 검색하여 가져오기
    public class GetNameDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {


            String param = "param1=" + personIdString+"";
            Log.e("GetNameDB param1 확인ㅋㅋㅋ ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getPersonName.php");
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
                Log.e("InnerDB DATA", data);

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
            InnerListAdapter adapter = new InnerListAdapter(getActivity());
            RecyclerView recyclerView = getView().findViewById(R.id.inner_recycler);

            /* 서버에서 응답 */
            Log.e("PersonName", data);
            String personNameString = data;

            //String으로 받아온 프로젝트 이름을 "@"로 구분
            String[] splited = personNameString.split("@");

            //personName만을 추출
            for(int i = 1; i < splited.length; i++){

                //로그인 된 아이디와 이름을 비교하여 나의 프로필이면 제일 상단의 카드뷰에 내 프로필 삽입
                /*네비게이션뷰의 이름과 splited[i]를 비교, 네비게이션뷰의 아이디와 프로젝트에 삽입된 아이디를 and 연산하여
                true이면 맨 위 카드뷰인 my_name에 내 닉네임 삽입*/
                String userName = ProjectHome.getsName();
                if((userName.equals(splited[i])) && (MainActivity.getsId().equals(splited2[i]))){
                    TextView myName = getView().findViewById(R.id.my_name);
                    myName.setText(splited[i]);
                    continue;
                }
                //나의 프로필과 맞지 않으면 리사이클러뷰의 아이템에 삽입
                adapter.addItem(new InnerList(splited[i],"ㅎㅇ"));
            }


            recyclerView.setAdapter(adapter);

        }
    }


}

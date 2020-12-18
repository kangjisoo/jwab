package org.techtown.projectinner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.board.BoardMainRecycler;
import org.techtown.calendar.innercalendar;
import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectmain.ItemTouchHelperCallback;
import org.techtown.projectmain.MemberAdd;
import org.techtown.projectmain.ProjectAdd;
import org.techtown.projectmain.ProjectHome;
import org.techtown.projectmain.ProjectHomeList;
import org.techtown.projectmain.ProjectHomeListAdapter;
import org.techtown.projectmain.ProjectHomeRecyclerView;
import org.techtown.randomgame.RandomGameFirst;
import org.techtown.randomgame.RandomGameSecond;
import org.techtown.vote.VoteDP;
import org.techtown.vote.VoteMain;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

import org.techtown.projectmain.ProjectHomeListAdapter;

//프로젝트에 들어갔을 때 보이는 사용자들의 프로필
public class InnerMainRecycler extends Fragment {
    Context context;
    BoardMainRecycler boardMainRecycler;
    public static String personIdString;
    public static String Contents, myContents;

    RecyclerView recyclerView;
    private String[] splited2;
    private String message;
    private static String pname;
    private static String pkey;
    private StringBuffer buffer;
    private static String[] onlyName;
    private static String[] splitedMessage;
    private ImageButton inner_Project_memberBt;


    public static String getPname() {
        return pname;
    }

    public static String getPkey() {
        return pkey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //툴바의 옵션메뉴 동작
        setHasOptionsMenu(true);

        InnerListAdapter adapter = new InnerListAdapter(getActivity());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.inner_project, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.inner_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        inner_Project_memberBt = rootView.findViewById(R.id.inner_project_memberBt);


        InnerDB innerDB = new InnerDB();
        innerDB.execute();

        myProfileImgDB myProfileImgdb = new myProfileImgDB();
        myProfileImgdb.execute();

        profileImgDB profileImgdb = new profileImgDB();
        profileImgdb.execute();

        //플러스 버튼 클릭시
        inner_Project_memberBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MemberAdd.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    //툴바 오른쪽 메뉴 설정(캘린더, 게시판...)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inner_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings1:
                Toast.makeText(getContext(), "캘린더", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), innercalendar.class);
                startActivity(intent);
                return true;

            case R.id.action_settings2:
                Toast.makeText(getContext(), "게시판", Toast.LENGTH_LONG).show();
                boardMainRecycler = new BoardMainRecycler();
                Intent intent2 = new Intent(getContext(), BoardMainRecycler.class);
                startActivity(intent2);
                return true;
            case R.id.action_settings3:
                Toast.makeText(getContext(), "뽑기", Toast.LENGTH_LONG).show();

                Intent intent3 = new Intent(getContext(), RandomGameFirst.class);
                startActivity(intent3);
                return true;

            case R.id.action_settings4:
                Toast.makeText(getContext(), "투표", Toast.LENGTH_LONG).show();

                Intent intent4 = new Intent(getContext(), VoteDP.class);
                startActivity(intent4);
                return true;

            default:
                Toast.makeText(getContext(), "멤버초대", Toast.LENGTH_LONG).show();

                return true;
        }
    }


    //선택된 프로젝트에 속한 팀원들의 id를 가져오는 DB
    public class InnerDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {
            //선택된 프로젝트 이름과 key값 받아옴
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();

            String param = "param1=" + pname + "_" + pkey + "";


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

            //받아온 사용자들의 ID를 GetNameDB.class에서 이름으로 변환할 수 있도록 값을 넘겨줌
            personIdString = data;

            //GetNameDB에서 내 아이디와 팀원들의 아이디를 비교 연산하기 위해 splited2사용
            splited2 = personIdString.split("@");

            GetNameDB getNameDB = new GetNameDB();
            getNameDB.execute();


        }
    }


    //InnerDB class에서 받아온 id를 jwabProjectRelDB의 userTable의 이름과 비교하여 일치하는 것 가져옴
    //일치하는 이름과 이름에 해당하는 상태메시지를 가져옴
    public class GetNameDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            //사용자들의 ID, 프로젝트이름_key값을 param으로 넘겨줌
            String param = "param1=" + personIdString + "&p_name=" + pname + "&p_key=" + pkey + "";

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


            InnerListAdapter adapter = new InnerListAdapter(getActivity());
            RecyclerView recyclerView = getActivity().findViewById(R.id.inner_recycler);


            //사용자들의 이름을 personNameString에 저장(@이름_상메@이름_상메@이름_상메@...)
            String personNameString = data;

            int messageIndex, nameIndex;

            //String으로 받아온 "@이름_상메@..."를 "@"로 구분
            String[] splited = personNameString.split("@");
            onlyName = new String[splited.length];
            splitedMessage = new String[splited.length];


            //personName만을 추출
            for (int i = 1; i < splited.length; i++) {
                //"splited = 이름_상메" 형태로 되어있음. "_"로 구분해야하므로 이름과 상메 인덱스에 위치 저장
                messageIndex = splited[i].indexOf("_");
                nameIndex = splited[i].indexOf("_");

                //onlyName[0] = "이름"
                          onlyName[i] = splited[i].substring(0,nameIndex);

                //로그인 된 아이디와 이름을 비교하여 나의 프로필이면 제일 상단의 카드뷰에 내 프로필 삽입
                /*네비게이션뷰의 이름과 splited[i]를 비교, 네비게이션뷰의 아이디와 프로젝트에 삽입된 아이디를 and 연산하여
                true이면 맨 위 카드뷰인 my_name에 내 닉네임 삽입*/
                String userName = ProjectHome.getsName();

                if ((userName.equals(onlyName[i])) && (MainActivity.getsId().equals(splited2[i]))) {
                    TextView myName = getView().findViewById(R.id.my_name);
                    myName.setText(onlyName[i]);
                    continue;
                }
                //splitedMessage[0] = "상메"
                splitedMessage[i] = splited[i].substring(messageIndex+1);

                //나의 프로필과 맞지 않으면 리사이클러뷰의 아이템에 삽입
              adapter.addItem(new InnerList(onlyName[i], splitedMessage[i]));
            }
            //내 프로필에 해당하는 카드뷰 선언
            CardView cardView = getView().findViewById(R.id.my_profile_card);
            final EditText stageMessage = new EditText(getContext());
            TextView textView = getView().findViewById(R.id.my_message);
            textView.setText(message);

            //카드뷰 클릭 시 상태메시지를 변경할 수 있도록 dialog 띄워줌
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    //상태메시지 변경후 다시 눌렀을때 나는 오류 해결을 위한 코드
                    if (stageMessage.getParent() != null)
                        ((ViewGroup) stageMessage.getParent()).removeView(stageMessage);

                    builder
                            .setTitle("상태메시지")
                            .setCancelable(true)
                            .setView(stageMessage)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    message = stageMessage.getText().toString();

                                    Toast.makeText(getContext(), "상태메시지가 변경되었습니다.", Toast.LENGTH_LONG).show();
                                    message = stageMessage.getText().toString();
                                    //상태메시지를 변경했을 시 호출되는 DB
                                    stateMessageDB messageDB = new stateMessageDB();
                                    messageDB.execute();

                                }
                            });


                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });

            recyclerView.setAdapter(adapter);

            //상태메시지를 변경하지 않았을 시 호출되는 DB
            myMessageDB myMessageDB = new myMessageDB();
            myMessageDB.execute();
        }

    }

    //상태메시지를 변경하였을 때 호출되어 DB의 stateMessage를 업데이트 시켜줌
    public class stateMessageDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {
            //상메, 프로젝트이름, key값, 사용자ID 넘겨줌
            String param = "stateMessage=" + message + "&p_name=" + pname + "&p_key=" + pkey + "&p_id=" + MainActivity.getsId() + "";

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/stateMessage.php");
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
            //DB에서 변경된 상태메시지를 띄워줌
            TextView myMessage = getView().findViewById(R.id.my_message);
            myMessage.setText(data);
        }

    }

    //나의 상태메시지를 띄워주는 클래스
    public class myMessageDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();
            String param = "p_name=" + pname + "&p_key=" + pkey + "&p_id=" + MainActivity.getsId() + "";

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/myMessage.php");
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

            //DB에서 가져온 상태메시지를 띄워줌
            TextView myMessage = getView().findViewById(R.id.my_message);
            myMessage.setText(data);
        }
    }
    public class myProfileImgDB extends com.android.volley.misc.AsyncTask<Void, Integer, Void> {


        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            String id = MainActivity.getsId();
            String param = "id=" + id + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/profileImgLoad.php";

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

            myContents = buffer.toString();

            ImageView inner_img = (ImageView) getView().findViewById(R.id.my_image);

            String img = "http://jwab.dothome.co.kr/Android/" + myContents.trim();

            Glide.with(InnerMainRecycler.this).load(img).error(R.drawable.basic_people2).into(inner_img);

        }
    }

    public class profileImgDB extends com.android.volley.misc.AsyncTask<Void, Integer, Void> {


        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... unused) {
            String id = MainActivity.getsId();
            String member = personIdString;
            String param = "id=" + id + "&member=" + member + "";
            String serverUri = "http://jwab.dothome.co.kr/Android/memberProfile.php";

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
            InnerListAdapter adapter = new InnerListAdapter(getActivity());
            Contents = buffer.toString();
            int pathIndex, nameIndex;

            //String으로 받아온 "@이름_상메@..."를 "@"로 구분
            String[] splited = Contents.split("@");
            String[] userName = new String[splited.length];
            String[] userImg = new String[splited.length];

            //personName만을 추출
            for (int i = 1; i < splited.length; i++) {
                //"splited = 이름_상메" 형태로 되어있음. "_"로 구분해야하므로 이름과 상메 인덱스에 위치 저장
                nameIndex = splited[i].indexOf("!");
                pathIndex = splited[i].indexOf("!");

                //onlyName[0] = "이름"
                userName[i] = splited[i].substring(0,nameIndex);
                userImg[i] = splited[i].substring(pathIndex+1);

                Log.e("userImg[i]",userImg[i]);

                //로그인 된 아이디와 이름을 비교하여 나의 프로필이면 제일 상단의 카드뷰에 내 프로필 삽입
                /*네비게이션뷰의 이름과 splited[i]를 비교, 네비게이션뷰의 아이디와 프로젝트에 삽입된 아이디를 and 연산하여
                true이면 맨 위 카드뷰인 my_name에 내 닉네임 삽입*/
                String userId = MainActivity.getsId();
                if (userId.equals(userName[i])){

                }else{
                    adapter.addItem(new InnerList(onlyName[i], splitedMessage[i], userImg[i]));
                }
            }
            recyclerView.setAdapter(adapter);




        }
    }

}
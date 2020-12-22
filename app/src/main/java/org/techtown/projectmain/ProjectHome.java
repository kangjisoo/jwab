package org.techtown.projectmain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.techtown.board.BoardView;
import org.techtown.board.BoardMainRecycler;
import org.techtown.loginactivity.FragmentCallback;
import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
//ProjectHome메인


public class ProjectHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    ProjectHomeRecyclerView fragment0; //로그인 후 첫화면
    ProjectHomeFragment1 fragment1;
    ProjectHomeFragment2 fragment2;
    ProjectHomeFragment3 fragment3;
    ProjectBottomMenu1 bottom_menu1;
    ProjectBottomMenu2 bottom_menu2;
    ProjectBottomMenu3 bottom_menu3;
    ProjectBottomMenu4 bottom_menu4;
    ProjectHelp projectHelp;

    public static StringBuffer buffer = new StringBuffer();
    public static String img;
    BoardMainRecycler boardMainRecycler;
    public static NavigationView navigationView;
    public static View headerView;
    DrawerLayout drawer;
    Toolbar toolbar;
    public static String t1;
    private ProjectHome Context;
    private long lastTimeBackPressed;

    public static String getsName(){return t1;}

    ImageView board_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_home_slide_menu);


        Context = this;

        //상단바
        toolbar = findViewById(R.id.toolbar);
        if(useToolbar()){
        setSupportActionBar(toolbar);
        }else{
            toolbar.setVisibility(View.GONE);
        }

        //프로필에 사용자 ID 띄우기
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        TextView navUserId = (TextView) headerView.findViewById(R.id.profile_email);
        navUserId.setText(MainActivity.getsId());

        //프로필에 사용자 이름 띄우는 DB
        getNameDB getnameDB = new getNameDB();
        getnameDB.execute();





        //슬라이드메뉴
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView2 = findViewById(R.id.nav_view);
        navigationView2.setNavigationItemSelectedListener(this);


        board_img = findViewById(R.id.post_writer_pic);

        fragment0 = new ProjectHomeRecyclerView();
        fragment1 = new ProjectHomeFragment1();

        //하단바 내 정보탭
        fragment2 = new ProjectHomeFragment2();
        fragment3 = new ProjectHomeFragment3();

        bottom_menu1 = new ProjectBottomMenu1();
        bottom_menu2 = new ProjectBottomMenu2();

        //슬라이드 도움말 탭
        projectHelp = new ProjectHelp();

        //슬라이드 내활동 탭
        bottom_menu3 = new ProjectBottomMenu3();
        //하단바 알림 탭
        bottom_menu4 = new ProjectBottomMenu4();
        boardMainRecycler = new BoardMainRecycler();

        //가장 처음에 나오는 액티비티(아무것도 누르지 않은 상태)
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment0).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment0).commit();

        //하단바
       BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
       bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.tab2:
                                Toast.makeText(getApplicationContext(), "홈 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(3, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment0).commit();
                                return true;

                            case R.id.tab1:
                                Toast.makeText(getApplicationContext(), "내 정보 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(5, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "알림 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(6, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu4).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );


    }   //onCreate() 끝

    //뒤로가기 버튼 클릭시 실행
    @Override
    public void onBackPressed() {

        //프래그먼트 onBackPressedListener사용
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof onBackPressedListener) {
                ((onBackPressedListener) fragment).onBackPressed();
                return;
            }
        }

        //두 번 클릭시 어플 종료
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();

    }

    //프래그먼트 전환
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    //툴바를 사용할지 말지 정함
    protected boolean useToolbar(){
        return true;
    }

//        @Override
//        public void onBackPressed () {
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//                super.onBackPressed();
//            }
//        }

        //슬라이드메뉴
        @Override
        public boolean onNavigationItemSelected (MenuItem item){

            int id = item.getItemId();
            if (id == R.id.menu1) {
                Toast.makeText(this, "내 프로젝트 선택", Toast.LENGTH_LONG).show();
                onFragmentSelected(0, null);

            } else if (id == R.id.menu2) {
                Toast.makeText(this, "내 활동 선택", Toast.LENGTH_LONG).show();
                onFragmentSelected(1, null);
            } else if (id == R.id.menu3) {
                Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show();
                //onFragmentSelected(2, null);
                logOut(ProjectHome.this);
            } else if(id == R.id.menu4){
                Toast.makeText(this, "도움말",Toast.LENGTH_LONG).show();
                onFragmentSelected(7, null);
            }


            drawer.closeDrawer(GravityCompat.START);

            return true;
        }

        //로그아웃 메소드(전에 있는 모든 엑티비티를 끄고 로그인 화면으로 이동)
    public static void logOut(Activity act) {

        //전있던 엑티비티 종료
        act.finish();
        Intent i = new Intent(act, MainActivity.class );
        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        i.putExtra( "KILL", true );
        act.startActivity(i);
    };

        //각 프래그먼트 상단 이름
        @Override
        public void onFragmentSelected (int position, Bundle bundle){
            Fragment curFragment = null;

            if (position == 0) {
                curFragment = fragment0;
                toolbar.setTitle("프로젝트 홈");
            } else if (position == 1) {
                //
                curFragment = bottom_menu3;
                toolbar.setTitle("내 활동");
            } else if (position == 2) {
                curFragment = fragment3;
                toolbar.setTitle("세 번째 화면");
            } else if (position == 3) {
                curFragment = fragment0;
                toolbar.setTitle("프로젝트 홈");
            } else if (position == 4) {
                curFragment = bottom_menu2;
                toolbar.setTitle("두 번째 탭");
            } else if (position == 5) {
                curFragment = fragment2;
                //
                toolbar.setTitle("내 정보");
            }else if (position == 6) {
                curFragment = bottom_menu4;
                toolbar.setTitle("알림");
            }else if(position==7){
                curFragment = projectHelp;
                toolbar.setTitle("도움말");
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
        }


    //프로필에 사용자 이름 띄우는 DB
    public class getNameDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String id = MainActivity.getsId();

            //param = id=id
            String param = "id=" + id + "";

            //Check param
            Log.e("POST.param", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getName.php");
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable(){
                @Override public void run() {

                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUserName = (TextView) headerView.findViewById(R.id.profile_nameView);
                    navUserName.setText(data);
                    t1 = navUserName.getText().toString();
                }
                });

            return null;
        }
    }
    public static class profileImgDB extends com.android.volley.misc.AsyncTask<Void, Integer, Void> {


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

            String Contents = buffer.toString();

            ImageView drawer_img = (ImageView) headerView.findViewById(R.id.profile_image);

            String img = "http://jwab.dothome.co.kr/Android/" + Contents.trim();
            Glide.with(headerView).load(img).error(R.drawable.basic_people2).into(drawer_img);

        }
    }

}
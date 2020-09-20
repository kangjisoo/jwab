package org.techtown.projectmain;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


    DrawerLayout drawer;
    Toolbar toolbar;
    public static String t1;
    public static String getsName(){return t1;}




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_home_slide_menu);
        //상단바
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //프로필에 사용자 ID 띄우기
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
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


        fragment0 = new ProjectHomeRecyclerView();
        fragment1 = new ProjectHomeFragment1();
        fragment2 = new ProjectHomeFragment2();
        fragment3 = new ProjectHomeFragment3();
        bottom_menu1 = new ProjectBottomMenu1();
        bottom_menu2 = new ProjectBottomMenu2();
        bottom_menu3 = new ProjectBottomMenu3();
        bottom_menu4 = new ProjectBottomMenu4();

        //가장 처음에 나오는 액티비티(아무것도 누르지 않은 상태)
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment0).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment0).commit();

        //하단바
       BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
       bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab1:
                                Toast.makeText(getApplicationContext(), "첫번째 탭 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(3, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment0).commit();
                                return true;

                            case R.id.tab2:
                                Toast.makeText(getApplicationContext(), "두번째 탭 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(5, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu3).commit();
                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "세번째 탭 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(6, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu4).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );
    }   //onCreate() 끝



        @Override
        public void onBackPressed () {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        //슬라이드메뉴
        @Override
        public boolean onNavigationItemSelected (MenuItem item){

            int id = item.getItemId();
            if (id == R.id.menu1) {
                Toast.makeText(this, "첫 번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
                onFragmentSelected(0, null);
            } else if (id == R.id.menu2) {
                Toast.makeText(this, "두 번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
                onFragmentSelected(1, null);
            } else if (id == R.id.menu3) {
                Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show();
                //onFragmentSelected(2, null);

                logOut(ProjectHome.this);
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
                curFragment = fragment1;
                toolbar.setTitle("첫 번째 화면");
            } else if (position == 1) {
                curFragment = fragment2;
                toolbar.setTitle("두 번째 화면");
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
                curFragment = bottom_menu3;
                toolbar.setTitle("게시판");
            }else if (position == 6) {
                curFragment = bottom_menu4;
                toolbar.setTitle("알림");
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
                Log.e("getName : ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable(){
                @Override public void run() {

                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUserName = (TextView) headerView.findViewById(R.id.profile_name);
                    navUserName.setText(data);
                    t1 = navUserName.getText().toString();
                }
                });

            return null;
        }
    }

}
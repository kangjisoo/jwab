package org.techtown.projectmain;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.FragmentCallback;
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

public class ProjectHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    ProjectHomeFragment0 fragment0; //로그인 후 첫화면
    ProjectHomeFragment1 fragment1;
    ProjectHomeFragment2 fragment2;
    ProjectHomeFragment3 fragment3;
    ProjectBottomMenu1 bottom_menu1;
    ProjectBottomMenu2 bottom_menu2;
    ProjectBottomMenu3 bottom_menu3;
    ProjectBottomMenu4 bottom_menu4;


    DrawerLayout drawer;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_home_slide_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //아이디 프로필에 띄우기
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserId = (TextView) headerView.findViewById(R.id.profile_email);
        navUserId.setText(MainActivity.getsId());

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView2 = findViewById(R.id.nav_view);
        navigationView2.setNavigationItemSelectedListener(this);

        fragment0 = new ProjectHomeFragment0();
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
                                onFragmentSelected(4, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu2).commit();
                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "세번째 탭 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(5, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu3).commit();
                                return true;

                            case R.id.tab4:
                                Toast.makeText(getApplicationContext(), "네번째 탭 선택", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "세 번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
                onFragmentSelected(2, null);
            }

            drawer.closeDrawer(GravityCompat.START);

            return true;
        }

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
                toolbar.setTitle("첫 번째 탭");
            } else if (position == 4) {
                curFragment = bottom_menu2;
                toolbar.setTitle("두 번째 탭");
            } else if (position == 5) {
                curFragment = bottom_menu3;
                toolbar.setTitle("세 번째 탭");
            }else if (position == 6) {
                curFragment = bottom_menu4;
                toolbar.setTitle("네 번째 탭");
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
        }

}

/* 기존에 있던 코드
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
*/
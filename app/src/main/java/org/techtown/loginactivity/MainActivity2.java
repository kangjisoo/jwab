package org.techtown.loginactivity;

import android.os.Bundle;
import android.view.MenuItem;
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

import org.techtown.loginactivity.ui.gallery.Bottom_menu1;
import org.techtown.loginactivity.ui.gallery.Bottom_menu2;
import org.techtown.loginactivity.ui.gallery.Bottom_menu3;
import org.techtown.loginactivity.ui.gallery.Fragment0;
import org.techtown.loginactivity.ui.gallery.Fragment1;
import org.techtown.loginactivity.ui.gallery.Fragment2;
import org.techtown.loginactivity.ui.gallery.Fragment3;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    Fragment0 fragment0;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Bottom_menu1 bottom_menu1;
    Bottom_menu2 bottom_menu2;
    Bottom_menu3 bottom_menu3;

    DrawerLayout drawer;
    Toolbar toolbar;

    //private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fragment0~3 : 하단바 탭을 눌렀을 때 나오는 액티비티
        //bottom_menu1~3 : 슬라이드메뉴 액티비티
        fragment0 = new Fragment0();
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        bottom_menu1 = new Bottom_menu1();
        bottom_menu2 = new Bottom_menu2();
        bottom_menu3 = new Bottom_menu3();

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
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu1).commit();
                                return true;


                            case R.id.tab2:
                                Toast.makeText(getApplicationContext(), "두번째 탭 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(4, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment0).commit();
                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "세번째 탭 선택", Toast.LENGTH_LONG).show();
                                onFragmentSelected(5, null);
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, bottom_menu3).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //슬라이드메뉴
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
    public void onFragmentSelected(int position, Bundle bundle) {
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
        }else if (position == 3) {
            curFragment = fragment3;
            toolbar.setTitle("첫 번째 탭");
        }else if (position == 4) {
            curFragment = fragment3;
            toolbar.setTitle("두 번째 탭");
        }else if (position == 5) {
            curFragment = fragment3;
            toolbar.setTitle("세 번째 탭");
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
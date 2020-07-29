package org.techtown.projectmain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.techtown.loginactivity.R;

public class ProjectShowNavigationView extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    // view 참조 변수 정의
    NavigationView menuNavigationView;
    Button expiredDateButton, plusButton, sendButton;
    TextView projectTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_show_navigation_view);

        // view 참조 변수의 구성
        projectTitleTextView = (TextView) findViewById(R.id.prj_show_projectTitle);
        projectTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RenameProjectTitle();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    // project에 따라 project_show.xml의 view:content 수정
    protected void InitProject() {

    }

    // ProjectTitle을 클릭시 RenameProjectTitle() 실행
    protected void RenameProjectTitle() {
        // Dialog 관련 객체 정의
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);;
        AlertDialog dialog;
        // Dialog에서 값을 받아오는데 사용할 EditText
        final EditText revisedTitle = new EditText(this);
        // dialog 준비
        revisedTitle.setText(projectTitleTextView.getText());
        alertDialogBuilder
                .setMessage("프로젝트 제목 수정")
                .setCancelable(true)
                .setView(revisedTitle)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog의 확인 버튼을 눌렀을 때 listener
                        projectTitleTextView.setText( revisedTitle.getText() );
                    }
                });

        //위 Builder대로 dialog 생성, 팝업 띄움
        dialog = alertDialogBuilder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_show_navigation_view, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
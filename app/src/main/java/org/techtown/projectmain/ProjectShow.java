package org.techtown.projectmain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.techtown.loginactivity.R;

public class ProjectShow extends AppCompatActivity {

    // view 참조 변수 정의
    Button menuButton, expiredDateButton, plusButton, sendButton;
    TextView projectTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_show);

        // view 참조 변수의 구성
        menuButton = (Button) findViewById(R.id.prj_show_menu);
        expiredDateButton = (Button) findViewById(R.id.prj_show_expiredDate);
        plusButton = (Button) findViewById(R.id.prj_show_plus);
        sendButton = (Button) findViewById(R.id.prj_show_send);
        projectTitleTextView = (TextView) findViewById(R.id.prj_show_projectTitle);
        projectTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RenameProjectTitle();
            }
        });
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

}
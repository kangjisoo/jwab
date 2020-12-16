package org.techtown.projectmain;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.loginactivity.R;

public class ProjectHelp extends Fragment {

    private TextView helpSubTitle,helpContentsView;
    private Button helpIntroBt, helpSignBt,helpProjectCreBt,helpBottomBt,helpLeftBt;
    private Drawable backColor;
    private Integer position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup helpView = (ViewGroup) inflater.inflate(R.layout.project_help, container, false);

        helpSubTitle = helpView.findViewById(R.id.helpSubTitle);
        helpIntroBt = helpView.findViewById(R.id.helpIntroBt);
        helpContentsView = helpView.findViewById(R.id.helpContentsView);
        helpSignBt=helpView.findViewById(R.id.helpSignBt);
        helpProjectCreBt = helpView.findViewById(R.id.helpProjectCreBt);
        helpBottomBt = helpView.findViewById(R.id.helpBottomBt);
        helpLeftBt= helpView.findViewById(R.id.helpLeftBt);

        helpSubTitle.setText("");
        helpContentsView.setText("");

        //기존의 버튼의 색을 저장하기 위해
        backColor = helpIntroBt.getBackground();

        //앱소개 버튼 클릭
        helpIntroBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=1;
                initBt();

                helpSubTitle.setText("앱 소개");
                helpContentsView.setText("-  조별과제나 프로젝트를 진행 시 도움을 줄 수 있는 앱입니다. \n" +
                        "게시판을 이용해서 정보 공유를 할 수 있고 달력으로 프로젝트 기간을 설정하여 효율적으로 프로젝트 진행을 도와줍니다.");

            }
        });

        //회원가입 버튼 클릭
        helpSignBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=2;
                initBt();

                helpSubTitle.setText("회원가입");
                helpContentsView.setText("-  초기화면에서 회원가입 버튼을 클릭하여 이름과 아이디 비밀번호를 입력하고 아이디 중복확인을 클릭해주세요. \n"+
                        "중복 확인이 완료되어야 아이디를 생성할 수 있습니다. \n 또한 비밀번호 확인시에 같아야지만 회원가입이 완료됩니다.");

            }
        });

        //프로젝트 생성 클릭
        helpProjectCreBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=3;
                initBt();

                helpSubTitle.setText("프로젝트 생성");
                helpContentsView.setText("-  로그인을 하면 처음 화면에 플러스 버튼을 통해 프로젝트를 생성할 수 있습니다.\n" +
                        "프로젝트의 이름을 입력하고 추가하고 싶은 조원의 아이디를 입력하여 추가 버튼을 누르면 조원이 추가 됩니다.\n" +
                        "조원을 잘못 추가 했다면 체크박스를 누르고 삭제를 누르면 조원 삭제가 가능합니다.  \n" +
                        "프로젝트의 이름을 입력하고 조원을 추가했다면 만들기 버튼을 눌러 프로젝트를 생성할 수 있습니다.\n");
            }
        });

        //하단버튼 종류 클릭
        helpBottomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=4;
                initBt();

                helpSubTitle.setText("하단버튼 종류");
                helpContentsView.setText("-  하단에 있는 3가지 버튼이 있습니다.\n\n" +
                        "1. 내정보 탭입니다.\n" +
                        "자신의 아이디의 대한 정보가 나옵니다. 사진을 클릭하면 프로필사진이 변경가능합니다.\n" +
                        "아이디는 변경할 수 없고 이름과 비밀번호는 변경 가능합니다.\n" +
                        "\n" +
                        "2. 홈버튼 입니다. 자신이 참여중인 프로젝트의 전체 목록을 볼 수 있고 클릭하면 해당 프로젝트 내부로 들어갈 수 있습니다.\n" +
                        "\n" +
                        "3. 알림 버튼으로 모든 프로젝트에서 팀원들의 활동 알림을 볼 수 있습니다.");

            }
        });

        //최상단 왼쪽 버튼 클릭
        helpLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=5;
                initBt();

                helpSubTitle.setText("최상단 왼쪽 버튼튼");
               helpContentsView.setText("-  왼쪽위 상단바에 있는 버튼을 클릭하면 슬라이드 화면이 나옵니다.\n" +
                       "자신의 프로필사진과 이름과 아이디를 볼 수 있고 내프로젝트, 내활동, 로그아웃, 도움말의 4가지 탭을 볼 수 있습니다.\n" +
                       "\n" +
                       "1. 내프로젝트 버튼은 홈버튼과 동일한 프로젝트 목록을 보여줍니다.\n" +
                       "\n" +
                       "2. 내활동 버튼은 프로젝트에서 자신이 올린 게시물과 댓글 투표를 볼 수 있는 활동 목록입니다.\n" +
                       "\n" +
                       "3. 로그아웃 버튼은 로그아웃화면으로 전환할 수 있습니다.\n" +
                       "\n" +
                       "4. 마지막으로 도움말 탭은 앱을 사용하는데 도움을 주는 설명을 볼 수 있습니다.");

            }
        });
        return helpView;

    }

    //버튼 초기화 시키는 메소드
    public void initBt(){

        //앱정보
        if (position==1){
            helpIntroBt.setBackgroundColor(Color.WHITE);
            helpIntroBt.setTextColor(Color.YELLOW);


            helpSignBt.setBackground(backColor);
            helpSignBt.setTextColor(Color.WHITE);
            helpProjectCreBt.setBackground(backColor);
            helpProjectCreBt.setTextColor(Color.WHITE);
            helpBottomBt.setBackground(backColor);
            helpBottomBt.setTextColor(Color.WHITE);
            helpLeftBt.setBackground(backColor);
            helpLeftBt.setTextColor(Color.WHITE);

            //회원가입
        }else if(position==2){

            helpSignBt.setBackgroundColor(Color.WHITE);
            helpSignBt.setTextColor(Color.YELLOW);

            helpIntroBt.setBackground(backColor);
            helpIntroBt.setTextColor(Color.WHITE);
            helpProjectCreBt.setBackground(backColor);
            helpProjectCreBt.setTextColor(Color.WHITE);
            helpBottomBt.setBackground(backColor);
            helpBottomBt.setTextColor(Color.WHITE);
            helpLeftBt.setBackground(backColor);
            helpLeftBt.setTextColor(Color.WHITE);

            //프로젝트 생성
        }else if (position==3){

            helpProjectCreBt.setBackgroundColor(Color.WHITE);
            helpProjectCreBt.setTextColor(Color.YELLOW);

            helpIntroBt.setBackground(backColor);
            helpIntroBt.setTextColor(Color.WHITE);
            helpSignBt.setBackground(backColor);
            helpSignBt.setTextColor(Color.WHITE);
            helpBottomBt.setBackground(backColor);
            helpBottomBt.setTextColor(Color.WHITE);
            helpLeftBt.setBackground(backColor);
            helpLeftBt.setTextColor(Color.WHITE);

            //하단 버튼
        }else if (position==4){

            helpBottomBt.setBackgroundColor(Color.WHITE);
            helpBottomBt.setTextColor(Color.YELLOW);

            helpIntroBt.setBackground(backColor);
            helpIntroBt.setTextColor(Color.WHITE);
            helpSignBt.setBackground(backColor);
            helpSignBt.setTextColor(Color.WHITE);
            helpProjectCreBt.setBackground(backColor);
            helpProjectCreBt.setTextColor(Color.WHITE);
            helpLeftBt.setBackground(backColor);
            helpLeftBt.setTextColor(Color.WHITE);

        }else if (position==5){
            helpLeftBt.setBackgroundColor(Color.WHITE);
            helpLeftBt.setTextColor(Color.YELLOW);

            helpIntroBt.setBackground(backColor);
            helpIntroBt.setTextColor(Color.WHITE);
            helpSignBt.setBackground(backColor);
            helpSignBt.setTextColor(Color.WHITE);
            helpProjectCreBt.setBackground(backColor);
            helpProjectCreBt.setTextColor(Color.WHITE);
            helpBottomBt.setBackground(backColor);
            helpBottomBt.setTextColor(Color.WHITE);
        }
    }
}

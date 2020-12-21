package org.techtown.projectmain;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.techtown.loginactivity.R;

//도움말
public class ProjectHelp extends Fragment implements onBackPressedListener{

    private TextView helpSubTitle,helpContentsView;
    private Button helpIntroBt, helpSignBt,helpProjectCreBt,help_memberAddBt,helpBottomBt,helpLeftBt,helpRightBt;
    private Drawable backColor;
    private Integer position;
    private ScrollView helpScrollView;

    //프래그먼트 종료 시켜주는 메소드
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(ProjectHelp.this).commit();
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
        final ViewGroup helpView = (ViewGroup) inflater.inflate(R.layout.project_help, container, false);

        helpSubTitle = helpView.findViewById(R.id.helpSubTitle);
        helpIntroBt = helpView.findViewById(R.id.helpIntroBt);
        helpContentsView = helpView.findViewById(R.id.helpContentsView);
        help_memberAddBt = helpView.findViewById(R.id.help_memberAddBt);
        helpSignBt=helpView.findViewById(R.id.helpSignBt);
        helpProjectCreBt = helpView.findViewById(R.id.helpProjectCreBt);
        helpBottomBt = helpView.findViewById(R.id.helpBottomBt);
        helpLeftBt= helpView.findViewById(R.id.helpLeftBt);
        helpRightBt=helpView.findViewById(R.id.helpRightBt);
        helpScrollView = helpView.findViewById(R.id.helpScrollView);

        helpSubTitle.setText("");
        helpContentsView.setText("");

        helpContentsView.setMovementMethod(new ScrollingMovementMethod());
        helpContentsView.setFocusableInTouchMode(true);
        helpContentsView.setFocusable(true);

        //스크롤뷰 지정
        helpContentsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //스크롤뷰가 텍스트뷰의 터치이벤트를 가져가지 못하게 함
                helpScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //기존의 버튼의 색을 저장하기 위해
        backColor = helpIntroBt.getBackground();

        //앱소개 버튼 클릭
        helpIntroBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=1;
                init2();

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
                init2();

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
                init2();

                helpSubTitle.setText("프로젝트 생성");
                helpContentsView.setText("-  로그인을 하면 처음 화면에 플러스 버튼을 통해 프로젝트를 생성할 수 있습니다.\n" +
                        "프로젝트의 이름을 입력하고 추가하고 싶은 조원의 아이디를 입력하여 추가 버튼을 누르면 조원이 추가 됩니다.\n" +
                        "조원을 잘못 추가 했다면 체크박스를 누르고 삭제를 누르면 조원 삭제가 가능합니다.  \n" +
                        "프로젝트의 이름을 입력하고 조원을 추가했다면 만들기 버튼을 눌러 프로젝트를 생성할 수 있습니다.\n");
            }
        });

        help_memberAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=4;
                init2();

                helpSubTitle.setText("멤버 초대");
                helpContentsView.setText("-  만들어진 프로젝트를 클릭하여 들어가면 플러스 버튼이 보입니다.\n" +
                        "\n" +
                        "플러스 버튼을 클릭하면 프로젝트에 멤버를 초대할 수 있습니다. \n" +
                        "\n" +
                        "현재 참여중인 조원을 제외한 다른 조원을 추가하고 싶다면 \n" +
                        "추가하고 싶은 조원의 아이디를 입력하여 추가를 누르고 초대하기를 누르면 멤버를 초대할 수 있습니다.");

            }
        });
        //하단버튼 종류 클릭
        helpBottomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=5;
                init2();

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
                position=6;
                init2();

                helpSubTitle.setText("최상단 왼쪽 버튼");
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

        //상단 오른쪽 버튼 클릭
        helpRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=7;
                init2();

                helpSubTitle.setText("상단 오른쪽 버튼");
                helpContentsView.setText("-  만들어진 프로젝트를 클릭하여 들어가게되면 자신과 프로젝트 조원 목록이 보입니다.\n" +
                        "맨 위에 자신의 탭을 클릭하여 상태메세지를 변경 할 수 있습니다.\n" +
                        "\n" +
                        "프로젝트를 클릭해서 들어가면 오른쪽 맨위 점 3개가 찍혀있는 버튼이 있습니다. \n" +
                        "클릭하면 캘린더, 게시판, 뽑기, 투표 기능이 있습니다.\n" +
                        "\n" +
                        "1. 캘린더를 클릭하면 달력이 나오고 날짜 선택과 메모, 기간버튼이 보이게 됩니다. \n" +
                        "\n" +
                        "- 메모 버튼을 클릭 후 원하는 날짜를 클릭하면 내용을 입력할 수 있습니다.\n" +
                        "저장을 누르면 메모가 저장되며 달력에 빨간점으로 표시됩니다. \n" +
                        "메모를 저장 후 수정 버튼이나 삭제 버튼을 사용해 메모를 수정하거나 삭제할 수 있습니다.\n" +
                        "\n" +
                        "- 기간 버튼을 선택하면 시작 날짜를 선택하고 프로젝트 종료날짜를 다음에 선택하면 기간이 화면에 표시됩니다. \n" +
                        "기간 저장을 누르면 프로젝트 기간이 저장되며 다른 조원의 달력에도 표시됩니다.\n" +
                        "기간초기화 버튼을 클릭해서 기간을 초기화 시킬 수 있습니다.\n" +
                        "\n" +
                        "2. 게시판을 클릭 시 플러스버튼 클릭을 통해 해당 프로젝트 게시판에 글을 생성할 수 있고 게시판 제목을 입력하고 내용을 작성할 수 있습니다.\n" +
                        "사진 버튼을 클릭하여 원하는 사진을 5장까지 게시, 공유 가능하고 업로드 버튼을 클릭하면 프로젝트 게시판에 작성한 글이 올라가는 것을 볼 수 있습니다.\n" +
                        "\n" +
                        "3. 뽑기버튼을 클릭하면 랜덤뽑기 화면이 나타납니다. \n" +
                        "제목을 입력하고 뽑을 사람의 수를 추가하면 목록에 뽑을 목록창이 뜬게됩니다.\n" +
                        "당첨이나, 꽝 같은 원하는 뽑을 목록을 입력하고 확인을 누르면\n" +
                        "인원수에 따라 1~n까지의 숫자가 왼쪽에 보이고 각자 몇번을 선택할지 정하고 결과보기를 누르면 랜덤으로 뽑기 목록에서 배정되어 화면에 나타납니다.\n" +
                        "다시섞기를 누르면 다시 랜덤으로 배정됩니다.\n" +
                        "\n" +
                        "4. 투표 버튼을 클릭하면 진행되고 있는 투표 목록이 나옵니다.\n" +
                        "전체를 선택하면 전체 투표 목록이, 참여한 투표를 클릭하면 본인이 참여한 투표 목록이 보이고\n" +
                        "불참한 투표를 선택하면 참여하지않은 투표 목록이 보이게 됩니다.\n" +
                        "각 항목들을 클릭하여 투표를 참여할 수 있고 선택을 변경 할 수 있습니다.\n" +
                        "\n" +
                        "- 투표를 만들고 싶다면 투표 만들기 버튼을 클릭하여\n" +
                        "투표주제를 입력하고 투표할 목록을 추가하고 확인을 눌러주면 투표 화면이 나오고 투표를 확인을 누르면 투표가 만들어지고 투표 목록화면에 뜨게 됩니다.\n\n" +

                        "5. 프로젝트 정보 탭을 클릭하면 해당 프로젝트에 대한 정보를 볼 수 있습니다.\n" +
                        "\n" +
                        "- 프로젝트의 프로필을 클릭하여 사진을 변경 할 수 있습니다.\n" +
                        "프로젝트의 이름, 생성날짜, 프로젝트 설정 기간, 프로젝트 비밀번호를 알 수 있습니다.\n" +
                        "프로젝트의 비밀번호를 잃어버렸다면 이곳에서 볼 수 있습니다.");

            }
        });


        return helpView;

    }

    //버튼 초기화 시키는 메소드
    public void init2(){

        //모든 버튼들 초기화
        helpIntroBt.setBackground(backColor);
        helpIntroBt.setTextColor(Color.WHITE);
        helpSignBt.setBackground(backColor);
        helpSignBt.setTextColor(Color.WHITE);
        helpProjectCreBt.setBackground(backColor);
        helpProjectCreBt.setTextColor(Color.WHITE);
        help_memberAddBt.setBackground(backColor);
        help_memberAddBt.setTextColor(Color.WHITE);
        helpBottomBt.setBackground(backColor);
        helpBottomBt.setTextColor(Color.WHITE);
        helpLeftBt.setBackground(backColor);
        helpLeftBt.setTextColor(Color.WHITE);
        helpRightBt.setBackground(backColor);
        helpRightBt.setTextColor(Color.WHITE);

        //앱 기능
        if (position==1){

            helpIntroBt.setBackgroundColor(Color.WHITE);
            helpIntroBt.setTextColor(Color.YELLOW);

            //회원가입
        }else if (position==2){

            helpSignBt.setBackgroundColor(Color.WHITE);
            helpSignBt.setTextColor(Color.YELLOW);

            //프로젝트 생성
        }else if(position==3) {

            helpProjectCreBt.setBackgroundColor(Color.WHITE);
            helpProjectCreBt.setTextColor(Color.YELLOW);

            //하단 버튼
        }else if (position==4){

            help_memberAddBt.setBackgroundColor(Color.WHITE);
            help_memberAddBt.setTextColor(Color.YELLOW);

        }else if (position==5){

            helpBottomBt.setBackgroundColor(Color.WHITE);
            helpBottomBt.setTextColor(Color.YELLOW);

            //최상단 왼쪽 버튼
        }else if (position==6){

            helpLeftBt.setBackgroundColor(Color.WHITE);
            helpLeftBt.setTextColor(Color.YELLOW);

            //상단 오른쪽 버튼
       }else if (position==7){

            helpRightBt.setBackgroundColor(Color.WHITE);
            helpRightBt.setTextColor(Color.YELLOW);

        }

    }
}

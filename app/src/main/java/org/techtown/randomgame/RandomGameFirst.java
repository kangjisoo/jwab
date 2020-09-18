package org.techtown.randomgame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class RandomGameFirst extends AppCompatActivity {

    private EditText randomGameTitleView;
    private ImageButton memberMinus, memberPlus;
    private TextView memberCountView;
    private RecyclerView gameListRecyclerView;
    private Button ch_Bt, listAddBt;
    private int numOfMember=0;
    private int maxOfMember=20;
    private ArrayList<RandomGameList> rArrayList;
    private RandomGameAdapter rAdapter;
    private int listCount=0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_game_first);

        randomGameTitleView = findViewById(R.id.randomGameTitleView);
        memberMinus = findViewById(R.id.memberMinusBt);
        memberPlus = findViewById(R.id.memberPlusBt);
        memberCountView = findViewById(R.id.memberCountView);
        gameListRecyclerView = findViewById(R.id.gameListRecyclerView);
        listAddBt = findViewById(R.id.listAddBt);
        ch_Bt = findViewById(R.id.ch_Bt);

        memberCountView.setText(Integer.toString(numOfMember));
        memberMinus.setEnabled(false);


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        gameListRecyclerView.setLayoutManager(layoutManager);

        rArrayList = new ArrayList<>();
        rAdapter = new RandomGameAdapter(rArrayList);
        gameListRecyclerView.setAdapter(rAdapter);

        //리싸이클러뷰에 색 넣기?
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(gameListRecyclerView.getContext(),
                layoutManager.getOrientation());
        gameListRecyclerView.addItemDecoration(dividerItemDecoration);

        //목록 추가 버튼 클릭
        listAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RandomGameList newList = new RandomGameList("");
                rArrayList.add(newList);
                listCount++;
                rAdapter.notifyDataSetChanged();

            }
        });

        memberMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinusBt();
            }
        });

        memberPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusBt();
            }
        });

        //확인버튼 클릭
        ch_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleName = randomGameTitleView.getText().toString();

                //제목이 빈칸이거나 공백만 있을 경우
                if (titleName.equals("")|| titleName.trim().equals("")){
                    Toast.makeText(RandomGameFirst.this, "제목을 입력해주세요!",Toast.LENGTH_LONG).show();
                } else if (numOfMember>listCount || numOfMember<listCount){
                    Toast.makeText(RandomGameFirst.this, "사람 수 :"+ numOfMember +"  뽑을 목록 : "+listCount +"\n"+"수가 일치 해야 합니다.",Toast.LENGTH_LONG).show();
                }else {
                    //뽑기 목록값에 빈칸이 있을 경우 메세지 출력
                    for (int i = 0; i < listCount; i++) {

                        if (rArrayList.get(i).getRandomGameListItem().equals("")) {
                            Toast.makeText(RandomGameFirst.this, i + "번째 뽑기 목록이 비어있습니다. 적어주세요", Toast.LENGTH_LONG).show();
                        } else {
                            //모든 조건 완료, 화면 전환
                        }

                    }
                }

            }
        });
    }

    // 마이너스 버튼클릭
    public void MinusBt(){
        if (numOfMember==0){
            memberMinus.setEnabled(false);
            memberPlus.setEnabled(true);
        }
        else if(numOfMember==1){
            numOfMember--;
            memberCountView.setText(Integer.toString(numOfMember));
            memberMinus.setEnabled(false);
            memberPlus.setEnabled(true);
        }
        else if(numOfMember>1 && numOfMember<=maxOfMember){
            numOfMember--;
            memberCountView.setText(Integer.toString(numOfMember));
            memberMinus.setEnabled(true);
            memberPlus.setEnabled(true);
        }
    }

    //플러스 버튼 클릭
    public void PlusBt(){
        if (numOfMember>=0){
            numOfMember++;
            memberCountView.setText(Integer.toString(numOfMember));
            memberPlus.setEnabled(true);
            memberMinus.setEnabled(true);
        }
        else if (numOfMember==maxOfMember-1){
            numOfMember++;
            memberCountView.setText(Integer.toString(numOfMember));
            memberPlus.setEnabled(false);
            memberMinus.setEnabled(true);
        }

    }

}

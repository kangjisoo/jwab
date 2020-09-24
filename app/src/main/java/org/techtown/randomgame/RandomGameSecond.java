package org.techtown.randomgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RandomGameSecond extends AppCompatActivity {
    private TextView randomGameSecondTitle;
    private RecyclerView resultRecyclerView;
    private Button resultBt, reshuffleBt;
    private ArrayList<RandomGameData> sArrayList;
    private RandomGameSecondAdapter sAdapter;
    private ArrayList<String> sList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_game_second);

        randomGameSecondTitle = findViewById(R.id.randomGameSecondTitle);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        resultBt = findViewById(R.id.resultBt);
        reshuffleBt = findViewById(R.id.reshuffleBt);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        sArrayList = new ArrayList<>();
        sAdapter = new RandomGameSecondAdapter(sArrayList);
        resultRecyclerView.setAdapter(sAdapter);

        resultRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(resultRecyclerView.getContext(),
                layoutManager.getOrientation());
        resultRecyclerView.addItemDecoration(dividerItemDecoration);


        //제목 받아오기
        randomGameSecondTitle.setText(RandomGameFirst.getWhatTitleName());

        //첫번째 화면에서 리사이클러뷰 안에 들어간 아이템 넘겨받기
        Bundle extras = getIntent().getExtras();
        sList = new ArrayList<>();
        sList = extras.getStringArrayList("resultValue");

        //아이템들이 랜더으로 나오도록 섞어주기
        Collections.shuffle(sList);

       for (int i =0; i<RandomGameFirst.getnumOfMember(); i++){

           //두번째 화면 리스트에 추가해주기
           RandomGameData randomGameData = new RandomGameData(sList.get(i));
           sArrayList.add(randomGameData);

           Log.e("receive to Firstlist", sList+"");

       }
        sAdapter.notifyDataSetChanged();


        //결과보기 버튼 클릭
        resultBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultBt.setVisibility(View.INVISIBLE);
            }
        });

        //다시섞기 버튼 클릭
        reshuffleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultBt.setVisibility(View.VISIBLE);

                //다시섞기
                Collections.shuffle(sArrayList);

                //(xml)countNum을 순서대로 넣기위해
                sArrayList.get(0).setInitNum(0);

                sAdapter.notifyDataSetChanged();

            }
        });
    }

    //뒤로가기 버튼 눌렀을때 두번째 화면 종료 첫번째뽑기 화면으로 돌아가기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RandomGameSecond.this.finish();
      Intent intent = new Intent(RandomGameSecond.this,RandomGameFirst.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);

    }

}

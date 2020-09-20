package org.techtown.randomgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class RandomGameSecond extends AppCompatActivity {
    private TextView randomGameSecondTitle;
    private RecyclerView resultRecyclerView;
    private Button resultBt, reshuffleBt;
    private ArrayList<RandomGameList> sArrayList;
    private RandomGameSecondAdapter sAdapter;
    private RandomGameList sList;

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

        RandomGameList randomGameList = new RandomGameList("");
        sArrayList.add(randomGameList);
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

                //리사이클러뷰 다시 섞기
            }
        });
    }

}

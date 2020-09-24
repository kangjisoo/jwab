package org.techtown.randomgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class RandomGameFirst extends AppCompatActivity {


    private EditText randomGameTitleView;
    private ImageButton memberMinus, memberPlus,listAddBt;
    private TextView memberCountView;
    private RecyclerView gameListRecyclerView;
    private Button ch_Bt;
    private static int numOfMember=0;
    private int maxOfMember=20;
    private ArrayList<RandomGameData> rArrayList;
    private RandomGameAdapter rAdapter;
    private static String whatTitleName;
    private ArrayList<String> storage;
    private int okay=0;


    public static String getWhatTitleName(){
        return whatTitleName;
    }

    public static int getnumOfMember(){
        return numOfMember;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_game_first);

        numOfMember=0;
        randomGameTitleView = findViewById(R.id.randomGameTitleView);
        memberMinus = findViewById(R.id.memberMinusBt);
        memberPlus = findViewById(R.id.memberPlusBt);
        memberCountView = findViewById(R.id.memberCountView);
        gameListRecyclerView = findViewById(R.id.gameListRecyclerView);
        ch_Bt = findViewById(R.id.ch_Bt);

        memberCountView.setText(Integer.toString(numOfMember));
        randomGameTitleView.setText("");
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


        memberMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinusBt();

                //rAdapter.notifyItemRemoved(rAdapter.getItemCount());
                Log.e("왜삭제안되냐",rAdapter.getItemCount()+"");
                rArrayList.remove(rAdapter.getItemCount()-1);
                rAdapter.notifyDataSetChanged();

                //rAdapter.notifyItemRangeChanged(position, randomLists.size());

            }
        });

        memberPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusBt();
                RandomGameData newList = new RandomGameData("");
                rArrayList.add(rAdapter.getItemCount(),newList);
                rAdapter.notifyItemInserted(rAdapter.getItemCount());
                Log.e("count of list", +rAdapter.getItemCount()+"");

                for (int i =0; i<rAdapter.getItemCount(); i++){
                    Log.e("추가하면 리스트에 들어있는 값", rArrayList.get(i).getRandomGameListItem()+"");
                }
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
                } else if (numOfMember>rAdapter.getItemCount() || numOfMember<rAdapter.getItemCount() ){
                    Toast.makeText(RandomGameFirst.this, "사람 수 :"+ numOfMember +"  뽑을 목록 : "+rAdapter.getItemCount()  +"\n"+"수가 일치 해야 합니다.",Toast.LENGTH_LONG).show();
                }else {

                    //뽑기 목록값에 빈칸이 있을 경우 메세지 출력
                    for (int i = 0; i < rAdapter.getItemCount() ; i++) {

                        if (rArrayList.get(i).getRandomGameListItem().equals("")) {
                            Toast.makeText(RandomGameFirst.this, i + "번째 뽑기 목록이 비어있습니다. 적어주세요", Toast.LENGTH_LONG).show();
                        } else {
                            okay=okay+1; }
                    }

                        if (okay == rAdapter.getItemCount() ){
                            //제목 받기
                            whatTitleName = randomGameTitleView.getText().toString();

                            //모든 조건 완료, 화면 전환
                            Intent intent = new Intent(getApplicationContext(), // 현재 화면의 제어권자
                                    RandomGameSecond.class); // 다음 넘어갈 클래스 지정

                            storage = new ArrayList<>();

                            //두번째 화면에 전달할 아이템 저장
                            for (int k =0; k<rAdapter.getItemCount() ; k++){
                                storage.add(k,rArrayList.get(k).getRandomGameListItem());

                                Log.e("item send to second ",rArrayList.get(k).getRandomGameListItem()+"");

                            }

                            //두번째 화면으로 아이템 보내기
                            intent.putStringArrayListExtra("resultValue",storage);
                            startActivity(intent); // 다음 화면으로 넘어간다
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

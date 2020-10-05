package org.techtown.vote;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectAdd;

import java.util.ArrayList;

public class VoteMain extends AppCompatActivity {

    private TextView voteTextView,listCountText,listCountTextView;
    private EditText voteTitleEditView, voteAddList;
    private Button voteAddBt,voteDeleteBt,voteMakeBt;
    private CheckBox voteAllCheckBox;
    private RecyclerView voteRecyclerView;

    private ArrayList<VoteData> vArrayList;
    private VoteAdapter vAdapter;

    private int listNum=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_main_dp);

        voteTextView = findViewById(R.id.voteTextView);
        listCountText = findViewById(R.id.listCountText);
        listCountTextView = findViewById(R.id.listCountTextView);
        voteTitleEditView=findViewById(R.id.voteTitleEditView);
        voteAddList = findViewById(R.id.voteAddList);
        voteAddBt =findViewById(R.id.voteAddBt);
        voteDeleteBt =findViewById(R.id.voteDeleteBt);
        voteMakeBt = findViewById(R.id.voteMakeBt);
        voteAllCheckBox = findViewById(R.id.voteAllCheckBox);
        voteRecyclerView = findViewById(R.id.voteRecyclerView);


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);

        voteRecyclerView.setLayoutManager(layoutManager);
        vArrayList= new ArrayList<>();

        //list갯수 = 0
        listCountTextView.setText(Integer.toString(listNum));

        vAdapter = new VoteAdapter(vArrayList);
        voteRecyclerView.setAdapter(vAdapter);

            voteAllCheckBox.setChecked(false);

        voteAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (voteAllCheckBox.isChecked()==true){
                    for (int i =0; i< vArrayList.size(); i++){
                        VoteData allCheckList = vArrayList.get(i);
                        allCheckList.setCheckBoxCh(true);
                    }
                }
                else
                {
                    for (int i =0; i<vArrayList.size(); i++){
                        VoteData allCheckList = vArrayList.get(i);
                        allCheckList.setCheckBoxCh(false);
                    }
                }
                vAdapter.notifyDataSetChanged();
            }
        });

        //추가버튼 클릭
        voteAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getList;
                getList = (String)voteAddList.getText().toString();

                //추가할 항목이 공백이거나 비워져 있을 때
                if (getList.equals("") || getList.trim().equals("")){
                    Toast.makeText(VoteMain.this,"투표 항목을 입력해주세요",Toast.LENGTH_LONG).show();

                }

                else{

                    if (voteAllCheckBox.isChecked()==true){
                        //리스트 추가
                        VoteData newVoteData = new VoteData(true, getList);
                        vArrayList.add(newVoteData);
                        vAdapter.notifyItemInserted(vAdapter.getItemCount());

                        listNum++;
                        listCountTextView.setText(Integer.toString(listNum));
                    }
                    else {
                        //리스트 추가
                        VoteData newVoteData = new VoteData(false, getList);
                        vArrayList.add(newVoteData);
                        vAdapter.notifyItemInserted(vAdapter.getItemCount());

                        listNum++;
                        listCountTextView.setText(Integer.toString(listNum));

                    }

                    //입력창 비워주기
                    voteAddList.getText().clear();
                }
            }
        });

        //삭제버튼 클릭
        voteDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //arraylist 사이즈를 저장
                int listSizeCheck = vArrayList.size();
                int deleteListSize = vArrayList.size();

                for(int j=0; j<=listSizeCheck; j++){
                    if (vArrayList.size()==0){
                        deleteListSize = vArrayList.size();
                        break;
                    }

                    else{
                        for (int i=0; i<vArrayList.size(); i++){

                            VoteData voteCheck = vArrayList.get(i);

                            if (voteCheck.isCheckBoxCh()){

                                vArrayList.remove(i);

                                deleteListSize = vArrayList.size();

                                listNum--;
                                listCountTextView.setText(Integer.toString(listNum));

                            }
                        }

                    }
                    //바뀐 상태를 변경해줌
                    vAdapter.notifyDataSetChanged();
                }

                if (listSizeCheck==deleteListSize){
                    Toast.makeText(VoteMain.this, "선택된 항목이 없습니다.", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(VoteMain.this, "삭제 완료", Toast.LENGTH_LONG).show();

            }
        });

    }


}

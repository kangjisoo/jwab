package org.techtown.board;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.techtown.loginactivity.FragmentCallback;
import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectAdd;
import org.techtown.projectmain.ProjectHome;


public class BoardMainRecycler extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton imageButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inner_menu, menu);
        return true;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        툴바의 옵션메뉴 동작
//        setHasOptionsMenu(true);
        setContentView(R.layout.board_main);
        BoardAdapter adapter = new BoardAdapter(this);

        recyclerView =(RecyclerView) findViewById(R.id.board_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.addItem(new BoardList("안녕하세용?","지수!!!","2020-09-07"));
        recyclerView.setAdapter(adapter);

        //플러스 버튼 누르면 프로젝트생성 액티비티로 전환
        imageButton = (ImageButton)findViewById(R.id.post_add);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardMainRecycler.this, BoardAdd.class);
                startActivity(intent);
            }
        });

    }






}

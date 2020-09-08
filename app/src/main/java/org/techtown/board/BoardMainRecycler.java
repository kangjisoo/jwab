package org.techtown.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.R;


public class BoardMainRecycler extends Fragment {
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //툴바의 옵션메뉴 동작
        setHasOptionsMenu(true);

        BoardAdapter adapter = new BoardAdapter(getActivity());
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.board_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.board_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.addItem(new BoardList("안녕하세용?","지수!!!","2020-09-07"));
        recyclerView.setAdapter(adapter);

        return rootView;
    }



}

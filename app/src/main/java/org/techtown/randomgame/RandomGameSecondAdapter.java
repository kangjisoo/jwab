package org.techtown.randomgame;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RandomGameSecondAdapter extends RecyclerView.Adapter<RandomGameSecondAdapter.ViewHolder> {
    private ArrayList<RandomGameData> sRandomLists;
    private int count;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView countNum;
        private TextView resultListView2;

        public ViewHolder(View itemView) {
            super(itemView);

            this.countNum = itemView.findViewById(R.id.countNum);
            this.resultListView2 = itemView.findViewById(R.id.resultlistView2);

        }
    }

    public RandomGameSecondAdapter(ArrayList<RandomGameData> resultListAdapter) {
        this.sRandomLists = resultListAdapter;
    }

    @Override
    public RandomGameSecondAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.random_game_second_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void onBindViewHolder(RandomGameSecondAdapter.ViewHolder viewHolder, int position) {

        RandomGameData randomGameData = sRandomLists.get(position);

        viewHolder.countNum.setGravity(Gravity.CENTER);
        viewHolder.resultListView2.setGravity(Gravity.CENTER);

        viewHolder.countNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        viewHolder.resultListView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            //순서를 줄 것
            count=sRandomLists.get(0).getInitNum()+1;
            sRandomLists.get(0).setInitNum(count);
            viewHolder.countNum.setText(Integer.toString(count));


        for (int i =0; i<=RandomGameFirst.getnumOfMember();i++){

            viewHolder.resultListView2.setText(randomGameData.getRandomGameListItem());
        }
    }


    @Override
    public int getItemCount()  {
        return (null != sRandomLists ? sRandomLists.size() : 0);
    }

    public int nnum(){
        return 0;
    }

}


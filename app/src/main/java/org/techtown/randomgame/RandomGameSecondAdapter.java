package org.techtown.randomgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class RandomGameSecondAdapter extends RecyclerView.Adapter<RandomGameSecondAdapter.ViewHolder> {
    private ArrayList<RandomGameList> sRandomLists;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView countNum;
        private TextView resultListView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.countNum = itemView.findViewById(R.id.countNum);
            this.resultListView = itemView.findViewById(R.id.resultListView);

        }
    }

    public RandomGameSecondAdapter(ArrayList<RandomGameList> resultListAdapter) {
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
        RandomGameList randomGameList = sRandomLists.get(position);

        for (int i =0; i<=getItemCount();i++){
            viewHolder.countNum.setText(Integer.toString(i));
            viewHolder.resultListView.setText(randomGameList.getRandomGameListItem());
        }

    }

    @Override
    public int getItemCount()  {
        return (null != sRandomLists ? sRandomLists.size() : 0);
    }
}


package org.techtown.randomgame;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectPersonAdapter;

import java.util.ArrayList;

public class RandomGameAdapter extends RecyclerView.Adapter<RandomGameAdapter.ViewHolder> {
    private ArrayList<RandomGameList> randomLists;

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected EditText randomListItem;
        protected Button listDeleteBt;

        public ViewHolder(View itemView) {
            super(itemView);

            this.randomListItem = itemView.findViewById(R.id.randomGameListItem);
            this.listDeleteBt = itemView.findViewById(R.id.randomGameDeleteBt);

        }
    }

    public RandomGameAdapter(ArrayList<RandomGameList> adapterlist) {
        this.randomLists = adapterlist;

    }

    @Override
    public RandomGameAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.random_game_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RandomGameAdapter.ViewHolder viewHolder, final int position) {
        final RandomGameList randomGameList = randomLists.get(position);

        viewHolder.randomListItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        viewHolder.randomListItem.setGravity(Gravity.CENTER);
        viewHolder.listDeleteBt.setGravity(Gravity.CENTER);

        viewHolder.randomListItem.setText(randomGameList.getRandomGameListItem());

    }

    @Override
    public int getItemCount()  {
        return (null != randomLists ? randomLists.size() : 0);
    }
    }


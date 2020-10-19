package org.techtown.vote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class VoteDPAdapter extends RecyclerView.Adapter<VoteDPAdapter.ViewHolder> {
    private ArrayList<VoteDPData> dlist;

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView voteMyTitle, voteConfirm;

        public ViewHolder(View itemView) {
            super(itemView);
            this.voteMyTitle = (TextView) itemView.findViewById(R.id.voteMyTitle);
            this.voteConfirm = (TextView) itemView.findViewById(R.id.voteConfirm);
        }
    }

    public VoteDPAdapter(ArrayList<VoteDPData> list) {
        this.dlist = list;
    }

    @Override
    public VoteDPAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vote_make_or_attend_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoteDPAdapter.ViewHolder viewHolder, final int position) {
        VoteDPData voteDPData = dlist.get(position);

    }


    @Override
    public int getItemCount() {
        return (null != dlist ? dlist.size() : 0);
    }

}
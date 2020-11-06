package org.techtown.vote;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class VoteDPAdapter extends RecyclerView.Adapter<VoteDPAdapter.ViewHolder> implements OnPersonItemClickListener {
    private ArrayList<VoteDPData> dlist;
    OnPersonItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView voteMyTitle, voteConfirm;

        public ViewHolder(View itemView) {
            super(itemView);
            this.voteMyTitle = (TextView) itemView.findViewById(R.id.voteMyTitle);
            this.voteConfirm = (TextView) itemView.findViewById(R.id.voteConfirm);

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() ;
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }
    }
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }

    public VoteDPData getItem(int position){ return dlist.get(position); }


    public void setOnItemClicklistener(OnPersonItemClickListener listener){ this.listener = listener; }

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

        viewHolder.voteConfirm.setGravity(Gravity.CENTER);
        viewHolder.voteMyTitle.setText(voteDPData.getVoteTitleShow());
        viewHolder.voteConfirm.setText(voteDPData.getVoteConfirmShow());

        //참여일 때 글자색 초록색
        if(viewHolder.voteConfirm.getText()=="참여"){

            viewHolder.voteConfirm.setTextColor(Color.parseColor("#00a564"));
        }
        //불참일 때 글자색 빨간색
        else
        {
            viewHolder.voteConfirm.setTextColor(Color.parseColor("#BD1010"));

        }

    }


    @Override
    public int getItemCount() {
        return (null != dlist ? dlist.size() : 0);
    }

}
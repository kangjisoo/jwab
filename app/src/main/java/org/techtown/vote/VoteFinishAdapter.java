package org.techtown.vote;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class VoteFinishAdapter  extends RecyclerView.Adapter<VoteFinishAdapter.ViewHolder> {
    private ArrayList<VoteFinishData> flist;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RadioButton voteFinishRadioBt;
        protected TextView voteFinishItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.voteFinishRadioBt = (RadioButton) itemView.findViewById(R.id.voteFinishRadioBt);
            this.voteFinishItem = (TextView) itemView.findViewById(R.id.voteFinishItem);

        }
    }

    public VoteFinishAdapter(ArrayList<VoteFinishData> list){
        this.flist = list;
    }

    @Override
    public VoteFinishAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vote_finish_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoteFinishAdapter.ViewHolder viewHolder, final int position) {

        final VoteFinishData voteFinishData = flist.get(position);
        viewHolder.voteFinishRadioBt.setGravity(Gravity.CENTER);

        //라디오버튼 클릭 이벤트
        viewHolder.voteFinishRadioBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //모든버튼 선택해제
                for (int i =0; i<=getItemCount(); i++){
                    flist.get(i).setVoteRadioButton(false);
                }

                //선택된 버튼만 체크
                voteFinishData.setVoteRadioButton(isChecked);

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != flist ? flist.size() : 0);
    }


}
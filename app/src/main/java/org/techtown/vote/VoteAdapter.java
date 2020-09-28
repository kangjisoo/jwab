package org.techtown.vote;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    private ArrayList<VoteData> vlist;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected CheckBox checkBoxSelect;
        protected TextView voteListViewSet;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBoxSelect = (CheckBox) itemView.findViewById(R.id.checkBoxSelect);
            this.voteListViewSet = (TextView) itemView.findViewById(R.id.voteListViewSet);
        }
    }

    public VoteAdapter(ArrayList<VoteData> list) {
        this.vlist = list;
    }

    @Override
    public VoteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vote_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoteAdapter.ViewHolder viewHolder, final int position) {

        final VoteData voteData = vlist.get(position);
        viewHolder.voteListViewSet.setGravity(Gravity.CENTER);
        viewHolder.voteListViewSet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        viewHolder.voteListViewSet.setText(voteData.getVoteListValue());


        //체크박스의 리스너를 초기화 하면서 리스트를 스크롤을 내렸을 때 체크박스가 선택해제되는것을 방지
        viewHolder.checkBoxSelect.setOnCheckedChangeListener(null);

        viewHolder.checkBoxSelect.setChecked(voteData.isCheckBoxCh());
        viewHolder.checkBoxSelect.setTag(viewHolder);


        //체크박스가 선택되었을때 실행되는 리스너, 선택된 상태를 설정해준다*
        //만약에 체크박스가 선택되어 있으면 true 선택되어 있지 않으면 false
        viewHolder.checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                voteData.setCheckBoxCh(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != vlist ? vlist.size() : 0);
    }


}

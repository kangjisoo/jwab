package org.techtown.projectmain;


import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class ProjectPersonAdapter extends RecyclerView.Adapter<ProjectPersonAdapter.ViewHolder> {
    private ArrayList<ProjectPerson> mlist;



    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView membernum;
        protected TextView findidorphone;
        protected CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.membernum = (TextView) itemView.findViewById(R.id.project_person_item_memberview);
            this.findidorphone = (TextView) itemView.findViewById(R.id.project_person_item_addmember_id);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.project_person_item_checkbox);

        }


    }

    public ProjectPersonAdapter(ArrayList<ProjectPerson> list) {
        this.mlist = list;

    }
    // public void setItem(Member item){
    //   membernum.setText(item.getMembers());
    //  findidorphone.setText(item.getSearchId());
    //}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.project_person_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final ProjectPerson projectPerson = mlist.get(position);
        String context = "<b>lalalla</b>";


        viewHolder.membernum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        viewHolder.findidorphone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewHolder.membernum.setGravity(Gravity.CENTER);
        viewHolder.findidorphone.setGravity(Gravity.CENTER);

        viewHolder.membernum.setText(projectPerson.getMembers());
        viewHolder.findidorphone.setText(projectPerson.getSearchId());


        //만약에 체크박스가 선택되어 있으면 true 선택되어 있지 않으면 false
        //viewHolder.checkBox.setClickable(true);
        //viewHolder.checkBox.setFocusable(false);


        //체크박스의 리스너를 초기화 하면서 리스트를 스크롤을 내렸을 때 체크박스가 선택해제되는것을 방지
         viewHolder.checkBox.setOnCheckedChangeListener(null);

        viewHolder.checkBox.setChecked(projectPerson.isChecked());
        viewHolder.checkBox.setTag(viewHolder);

        //체크박스가 선택되었을때 실행되는 리스너, 선택된 상태를 설정해준다*
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                projectPerson.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mlist ? mlist.size() : 0);
    }





//    public void checkedConfirm(int position) {
//        ProjectPerson projectPerson = mlist.get(position);
//        ViewHolder.checkBox.setChecked(projectPerson.isChecked());
//    }
}


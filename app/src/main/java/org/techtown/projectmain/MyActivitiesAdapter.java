package org.techtown.projectmain;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

//내 활동 어뎁터 ProjectBottomMenu3
public class MyActivitiesAdapter extends RecyclerView.Adapter<MyActivitiesAdapter.ViewHolder> {
    private ArrayList<MyActivitiesData> mlist;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView myActivities_title, myActivities_project, myActivities_date;
        public ViewHolder(View itemView) {
            super(itemView);

            myActivities_title = itemView.findViewById(R.id.myActivities_title);
            myActivities_project = itemView.findViewById(R.id.myActivities_project);
            myActivities_date = itemView.findViewById(R.id.myActivities_date);

        }
    }

    public MyActivitiesAdapter(ArrayList<MyActivitiesData> list){ this.mlist = list; }

    @Override
    public MyActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_activities_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyActivitiesAdapter.ViewHolder viewHolder, final int position) {
        final MyActivitiesData myActivitiesData = mlist.get(position);

        //content 변수에 메세지를 넣고
        String content = myActivitiesData.getMyAcId()+"님이 "+myActivitiesData.getMyAcKind()+"을(를) 올렸습니다: \""+myActivitiesData.getMyAcContents()+"\"";

        //content를 매개변수로 받는 spannableString 새로운 객체 생성
        SpannableString spannableString = new SpannableString(content);

        //kind항목에 게시판이나 투표, 댓글이 들어갈 경우 항목값만 진하고 크기를 키워서 출력
        if (myActivitiesData.getMyAcKind().equals("게시판")||myActivitiesData.getMyAcKind().equals("투표")||myActivitiesData.getMyAcKind().equals("댓글")){

            int start = content.indexOf(myActivitiesData.getMyAcKind());
            int end = start + myActivitiesData.getMyAcKind().length();
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.1f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        //spannableString을 setText해준다
        viewHolder.myActivities_title.setText(spannableString);
        viewHolder.myActivities_project.setText("프로젝트 이름 : " + myActivitiesData.getMyAcInfo());
        viewHolder.myActivities_date.setText(myActivitiesData.getMyAcDate()+"");
        viewHolder.myActivities_date.setTextColor(Color.parseColor("#c9cacd"));
    }

    @Override
    public int getItemCount() {
        return (null != mlist ? mlist.size() : 0);
    }
}

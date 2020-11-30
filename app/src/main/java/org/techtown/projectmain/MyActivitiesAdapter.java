package org.techtown.projectmain;

import android.graphics.Color;
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

        viewHolder.myActivities_title.setText(myActivitiesData.getMyAcId()+"님이 "+myActivitiesData.getMyAcKind()+" 올렸습니다: \""+myActivitiesData.getMyAcContents()+"\"");
        viewHolder.myActivities_project.setText("프로젝트 이름 : " + myActivitiesData.getMyAcInfo());
        viewHolder.myActivities_date.setText(myActivitiesData.getMyAcDate()+"");
        viewHolder.myActivities_date.setTextColor(Color.parseColor("#c9cacd"));
    }

    @Override
    public int getItemCount() {
        return (null != mlist ? mlist.size() : 0);
    }
}

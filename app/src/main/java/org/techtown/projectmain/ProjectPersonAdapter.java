package org.techtown.projectmain;


import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.R;
import java.lang.reflect.Member;
import java.util.ArrayList;

public class ProjectPersonAdapter extends RecyclerView.Adapter<ProjectPersonAdapter.ViewHolder> {
    private ArrayList<ProjectPerson> mlist;


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView membernum;
        protected TextView findidorphone;

        public ViewHolder(View itemView) {
            super(itemView);
            this.membernum = (TextView) itemView.findViewById(R.id.projectText);
            this.findidorphone = (TextView) itemView.findViewById(R.id.addmemberView);
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
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.membernum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            viewHolder.findidorphone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

            viewHolder.membernum.setGravity(Gravity.CENTER);
            viewHolder.findidorphone.setGravity(Gravity.CENTER);

            viewHolder.membernum.setText(mlist.get(position).getMembers());
            viewHolder.findidorphone.setText(mlist.get(position).getSearchId());
        }

        @Override
        public int getItemCount() {
            return (null != mlist ? mlist.size() : 0);
        }

    }



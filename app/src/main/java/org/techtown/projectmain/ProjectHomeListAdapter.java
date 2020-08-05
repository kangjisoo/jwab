package org.techtown.projectmain;

import android.app.Person;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProjectHomeListAdapter extends RecyclerView.Adapter<ProjectHomeListAdapter.ViewHolder> {
    ArrayList<ProjectHomeList> items = new ArrayList<ProjectHomeList>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView, textView2;

        public ViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.project_name);
            textView2 = itemView.findViewById(R.id.project_person);
        }
        public void setItem(ProjectHomeList item){
            textView.setText(item.getProjectName());
            textView2.setText(item.getPerson());
        }
    }

    @NonNull
    @Override
    //뷰객체 만들어줌
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.project_home_project_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    //뷰홀더가 재사용될 때 호출되므로 뷰객체는 기존 것을 그대로 사용하고 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ProjectHomeList item = items.get(position);
        viewHolder.setItem(item);

    }

    @Override
    //전체 아이템 갯수 확인 후 그 값 반환
    public int getItemCount() {
        return items.size();
    }
    public void addItem(ProjectHomeList item){
        items.add(item);
    }
    public void setItems(ArrayList<ProjectHomeList> items){
        this.items = items;
    }
    public ProjectHomeList getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, ProjectHomeList item){
        items.set(position, item);
    }
}

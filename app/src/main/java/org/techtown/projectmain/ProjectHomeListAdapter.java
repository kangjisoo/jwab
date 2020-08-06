package org.techtown.projectmain;

import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProjectHomeListAdapter extends RecyclerView.Adapter<ProjectHomeListAdapter.ViewHolder>
        implements ItemTouchHelperListener{
    ArrayList<ProjectHomeList> items = new ArrayList<ProjectHomeList>();
    Context context;
    public ProjectHomeListAdapter(Context context){
        this.context = context;
    }

    public ProjectHomeListAdapter() {

    }

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
    //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
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
    @Override public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
         ProjectHomeList projectHomeList = items.get(from_position);
         //이동할 객체 삭제
         items.remove(from_position);
         //이동하고 싶은 position에 추가
         items.add(to_position,projectHomeList);
         //Adapter에 데이터 이동알림
         notifyItemMoved(from_position,to_position);
         return true;
    }
    @Override public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    //오른쪽 버튼 누르면 아이템 삭제
     @Override
     public void onRightClick(final int position, RecyclerView.ViewHolder viewHolder) {

                         items.remove(position);
                         notifyItemRemoved(position);


     }

     }



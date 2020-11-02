package org.techtown.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.R;


import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>{
    LayoutInflater inflater;
    ArrayList<BoardList> items;
    Context context;

    public BoardAdapter(LayoutInflater inflater, ArrayList<BoardList> items) {
        this.inflater = inflater;
        this.items = items;
    }
   // public BoardAdapter(Context context) {
       // this.context = context;
    //}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.board_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BoardList item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BoardList item) {
        items.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, writer, date;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            writer = itemView.findViewById(R.id.writer);
            date = itemView.findViewById(R.id.date);
            //            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//
//                }
//
//            });
        }

        public void setItem(BoardList item) {
            title.setText(item.getTitle());
            writer.setText(item.getWriter());
            date.setText(item.getDate());
        }
    }
}

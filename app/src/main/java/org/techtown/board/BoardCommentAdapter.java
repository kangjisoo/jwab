package org.techtown.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class BoardCommentAdapter extends RecyclerView.Adapter<BoardCommentAdapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<BoardCommentList> items = new ArrayList<>();

    public BoardCommentAdapter(LayoutInflater inflater, ArrayList<BoardCommentList> items) {
        this.inflater = inflater;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.board_comment_item, viewGroup, false);
        return new BoardCommentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        BoardCommentList item = items.get(position);
        viewholder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BoardCommentList item) {
        items.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment, writer, date;
        //ImageView img1, img2, img3, img4, img5;
        public ViewHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            writer = itemView.findViewById(R.id.comment_writer);
            date = itemView.findViewById(R.id.comment_date);
        }

        public void setItem(BoardCommentList item) {

            comment.setText(item.getComment());
            writer.setText(item.getWriter());
            date.setText(item.getDate());
        }
    }

    public void setItem(ArrayList<BoardCommentList> items){
        this.items = items;
    }

    public BoardCommentList getItem(int position){
        return items.get(position);
    }
}

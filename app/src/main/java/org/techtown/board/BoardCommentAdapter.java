package org.techtown.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.loginactivity.R;

import java.util.ArrayList;
//BoardView의 어댑터(댓글)
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
        ImageView profileImg;

        public ViewHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            writer = itemView.findViewById(R.id.comment_writer);
            date = itemView.findViewById(R.id.comment_date);
            profileImg = itemView.findViewById(R.id.comment_picture);
        }

        public void setItem(BoardCommentList item) {

            comment.setText(item.getComment());
            writer.setText(item.getWriter());
            date.setText(item.getDate());
            String img = "http://jwab.dothome.co.kr/Android/" + item.getProfileImg();

            Glide.with(itemView.getContext()).load(img.trim()).error(R.drawable.basic_people2).into(profileImg);
        }
    }

    public void setItem(ArrayList<BoardCommentList> items){
        this.items = items;
    }

    public BoardCommentList getItem(int position){
        return items.get(position);
    }
}

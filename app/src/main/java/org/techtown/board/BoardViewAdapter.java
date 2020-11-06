package org.techtown.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.R;
import com.bumptech.glide.Glide;



import java.util.ArrayList;

public class BoardViewAdapter extends BaseAdapter{
    LayoutInflater inflater;
    ArrayList<BoardViewList> boardViewLists;

    public BoardViewAdapter(LayoutInflater inflater, ArrayList<BoardViewList> boardViewLists){
        this.inflater = inflater;
        this.boardViewLists = boardViewLists;
    }


    @Override
    public int getCount() {
        return boardViewLists.size();
    }

    @Override
    public Object getItem(int position) {
        return boardViewLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            view= inflater.inflate(R.layout.board_post_pic, viewGroup, false);
        }
        TextView contents = view.findViewById(R.id.post_text);
        ImageView img1 = view.findViewById(R.id.post_img1);
        ImageView img2 = view.findViewById(R.id.post_img2);
        ImageView img3 = view.findViewById(R.id.post_img3);
        ImageView img4 = view.findViewById(R.id.post_img4);
        ImageView img5 = view.findViewById(R.id.post_img5);

        BoardViewList boardViewList = boardViewLists.get(position);

        contents.setText(boardViewList.getContents());
        //네트워크에 있는 이미지 읽어오기
        Glide.with(view).load(boardViewList.getImg1()).into(img1);
        Glide.with(view).load(boardViewList.getImg2()).into(img2);
        Glide.with(view).load(boardViewList.getImg3()).into(img3);
        Glide.with(view).load(boardViewList.getImg4()).into(img4);
        Glide.with(view).load(boardViewList.getImg5()).into(img5);

        return view;
    }
}

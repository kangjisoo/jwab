package org.techtown.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.R;
import com.bumptech.glide.Glide;



import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>
                                implements BoardItemClickListener {
    LayoutInflater inflater;
    ArrayList<BoardList> items = new ArrayList<>();
    BoardItemClickListener listener;
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
        return new ViewHolder(itemView, (BoardItemClickListener)this);
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


    public void setOnItemClicklistener(BoardItemClickListener listener){
        this.listener = listener;
    }
    //@Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){ listener.onItemClick(holder,view,position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, writer, date, contents;
        //ImageView img1, img2, img3, img4, img5;
        public ViewHolder(View itemView, final BoardItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            writer = itemView.findViewById(R.id.writer);
            date = itemView.findViewById(R.id.date);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(BoardList item) {

            title.setText(item.getTitle());
            writer.setText(item.getWriter());
            date.setText(item.getDate());
        }
    }

    public void setItem(ArrayList<BoardList> items){
        this.items = items;
    }

    public BoardList getItem(int position){
        return items.get(position);
    }
    public void setItems(int position, BoardList item){
        items.set(position, item);
    }

}

package org.techtown.projectinner;

import android.content.Context;
import android.util.Log;
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

public class InnerListAdapter  extends RecyclerView.Adapter<InnerListAdapter.ViewHolder> {

    ArrayList<InnerList> items = new ArrayList<InnerList>();
    Context context;

    public InnerListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.inner_project_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        InnerList item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(InnerList item) {
        items.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2;
        ImageView inner_img;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.person_name);
            textView2 = itemView.findViewById(R.id.person_message);
            inner_img = itemView.findViewById(R.id.person_image);
        }

        public void setItem(InnerList item) {
            textView.setText(item.getPersonName());
            textView2.setText(item.getMessage());

            String img = "http://jwab.dothome.co.kr/Android/" + item.getImg();
            Log.e("img어댑터",img);

            Glide.with(itemView.getContext()).load(img.trim()).error(R.drawable.ic_menu_camera).into(inner_img);
        }
    }
}


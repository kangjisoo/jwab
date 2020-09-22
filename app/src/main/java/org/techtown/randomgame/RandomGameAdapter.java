package org.techtown.randomgame;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class RandomGameAdapter extends RecyclerView.Adapter<RandomGameAdapter.ViewHolder> {
    private ArrayList<RandomGameData> randomLists;
    private ArrayList<String> strings = new ArrayList();
    private int num =0;


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected EditText randomListItem;
        protected Button listDeleteBt;

        public ViewHolder(View itemView) {
            super(itemView);

            this.randomListItem = itemView.findViewById(R.id.randomGameListItem);
            this.listDeleteBt = itemView.findViewById(R.id.randomGameDeleteBt);

        }
    }

    public RandomGameAdapter(ArrayList<RandomGameData> adapterlist) {
        this.randomLists = adapterlist;

    }

    @Override
    public RandomGameAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.random_game_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RandomGameAdapter.ViewHolder viewHolder, final int position) {
        final RandomGameData randomGameData = randomLists.get(position);
        String itemValue="";
        viewHolder.randomListItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        viewHolder.randomListItem.setGravity(Gravity.CENTER);
        viewHolder.listDeleteBt.setGravity(Gravity.CENTER);

        viewHolder.randomListItem.setText(randomGameData.getRandomGameListItem());

        randomGameData.setRandomGameListItem(viewHolder.randomListItem.getText().toString());

        //TextWatcher를 이용하여 값이 변경된 후 randomGameList안에 수정된 값이 저장되도록 만듬듬
       viewHolder.randomListItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //입력하기 전에
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //입력되는 텍스트에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable s) {
                //입력이 끝났을 때
                randomGameData.setRandomGameListItem(viewHolder.randomListItem.getText().toString());
            }
        });


    }


    @Override
    public int getItemCount()  {
        return (null != randomLists ? randomLists.size() : 0);
    }
    }


package org.techtown.randomgame;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.techtown.loginactivity.R;

public class RandomGameViewHolderFirst extends RandomGameMyItemView {

    EditText randomGameListItem;
    Button randomGameDeleteBt;

    RandomGameData data;

    public RandomGameViewHolderFirst(@NonNull View itemView){
        super(itemView);

        randomGameListItem = itemView.findViewById(R.id.randomGameListItem);
        randomGameDeleteBt = itemView.findViewById(R.id.randomGameDeleteBt);
    }

    public void onBind(RandomGameMyItem data){
        this.data = (RandomGameData) data;
        randomGameListItem.setText(this.data.getRandomGameListItem());
    }

}

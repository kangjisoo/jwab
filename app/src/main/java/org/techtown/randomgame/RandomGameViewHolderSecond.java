package org.techtown.randomgame;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.techtown.loginactivity.R;

public class RandomGameViewHolderSecond extends RandomGameMyItemView {
    TextView countNum, resultlistView2;
    LinearLayout linearLayout;

    RandomGameData data;

    public RandomGameViewHolderSecond(@NonNull View itemView){
        super(itemView);

        countNum = itemView.findViewById(R.id.countNum);
        resultlistView2 = itemView.findViewById(R.id.resultlistView2);


    }


}

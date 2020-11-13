package org.techtown.board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.techtown.loginactivity.R;

public class BoardImgClick extends AppCompatActivity {
    ImageView imageView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_img_click);
        imageView= findViewById(R.id.board_img_click_view);

        Intent intent = getIntent() ;

        if(intent.getIntExtra("sign",1)==1){
            Glide.with(BoardImgClick.this).load(BoardView.getsImg1()).into(imageView);}
        else if(intent.getIntExtra("sign",2)==2){
            Glide.with(BoardImgClick.this).load(BoardView.getsImg2()).into(imageView);}

         else if(intent.getIntExtra("sign",3)==3){
        Glide.with(BoardImgClick.this).load(BoardView.getsImg3()).into(imageView);}

         else if(intent.getIntExtra("sign",4)==4){
         Glide.with(BoardImgClick.this).load(BoardView.getsImg4()).into(imageView);}

         else if(intent.getIntExtra("sign",5)==5){
         Glide.with(BoardImgClick.this).load(BoardView.getsImg5()).into(imageView);}


    Button button = findViewById(R.id.board_back_bt);
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    });

    }
 }



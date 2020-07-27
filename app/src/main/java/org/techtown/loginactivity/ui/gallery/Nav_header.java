package org.techtown.loginactivity.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;

public class Nav_header extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);
        TextView ID = (TextView)findViewById(R.id.profile_email);

        Intent intent = getIntent();    //데이터수신

        String name = intent.getExtras().getString("name");
        ID.setText(name);
    }

}

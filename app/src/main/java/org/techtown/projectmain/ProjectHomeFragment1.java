package org.techtown.projectmain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.techtown.loginactivity.R;


//프로젝트 홈
public class ProjectHomeFragment1 extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.project_home_recycler, container, false);
        return rootView;

    }
}
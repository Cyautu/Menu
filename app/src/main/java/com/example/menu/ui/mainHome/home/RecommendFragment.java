package com.example.menu.ui.mainHome.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Training;
import com.example.menu.ui.mainHome.training.TrainingViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class RecommendFragment extends Fragment {

    private TrainingViewModel newsViewModel;
    ArrayList<Training> list = new ArrayList<>();

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recommend, container, false);
        AppBarLayout appBarLayout = root.findViewById(R.id.select_bar);
        appBarLayout.setExpanded(false);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        return root;
    }

}
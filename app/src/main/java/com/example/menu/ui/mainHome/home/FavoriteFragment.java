package com.example.menu.ui.mainHome.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.menu.R;
import com.example.menu.ui.mainHome.training.TrainingViewModel;

public class FavoriteFragment extends Fragment {

    private TrainingViewModel newsViewModel;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        return root;
    }
}
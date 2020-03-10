package com.example.menu.ui.mainHome.examination.exam.ui.mockexam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu.R;

public class AnswerFragment extends Fragment {

    private MockExamViewModel mViewModel;

    public static AnswerFragment newInstance() {
        return new AnswerFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_answer, container, false);
        return root;
    }
}
package com.example.menu.ui.mainHome.examination.examinationType;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.menu.R;
import com.example.menu.ui.mainHome.examination.activity.SequentialPracticeActivity;
import com.example.menu.ui.mainHome.examination.exam.MockExamActivity;

public class RefinementFragment extends Fragment {
    private FrameLayout sequential_question;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_examination_refinement, container, false);
        //跳转顺序练习答题页面
        sequential_question = (FrameLayout) root.findViewById(R.id.function_sequential_practice);
        sequential_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m=new Intent(getActivity(), SequentialPracticeActivity.class);
                startActivity(m);
            }
        });
        root.findViewById(R.id.function_mock_exam).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MockExamActivity.class));
        });
        return root;
    }
}


package com.example.menu.ui.mainHome.examination.exam;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.menu.R;
import com.example.menu.ui.mainHome.examination.exam.ui.mockexam.MockExamFragment;

public class MockExamActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mock_exam_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(v -> MockExamActivity.this.finish());
        findViewById(R.id.start_exam).setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MockExamFragment.newInstance())
                    .commit();
            findViewById(R.id.start_exam).setVisibility(View.GONE);
        });
    }
}

package com.example.menu.ui.mainHome.training;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SelectListView extends ListView {
    public SelectListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandedSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandedSpec);
    }
}


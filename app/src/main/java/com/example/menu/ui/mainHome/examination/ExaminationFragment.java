package com.example.menu.ui.mainHome.examination;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.example.menu.R;
import com.example.menu.ui.mainHome.examination.examinationType.FirstConstructorFragment;
import com.example.menu.ui.mainHome.examination.examinationType.RefinementFragment;
import com.example.menu.ui.mainHome.examination.examinationType.SafetyEngineerFragment;
import java.util.ArrayList;

public class ExaminationFragment extends Fragment {
    private ExaminationViewModel examinationViewModel;
    private ViewPager viewPager;
    private Button buttonRefinement,buttonSafetyEngineer,buttonFirstConstructor;
    private float cursorX=0;
    private int[] WidrhArgs;
    private ImageView cursor;
    private Button[] ButtonArgs;
    private ArrayList<Fragment> list;
    private SectionsExaminationrAdapter adapter;
    private FragmentManager fm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examinationViewModel =
                ViewModelProviders.of(this).get(ExaminationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_examination, container, false);
        fm = getChildFragmentManager();
        /*fm=getActivity().getSupportFragmentManager();会导致fragment数据丢失*/
        //获取导航栏按钮
        viewPager = (ViewPager)root.findViewById(R.id.examination_viewPager);
        buttonRefinement = (Button)root.findViewById(R.id.button_refinement);
        buttonSafetyEngineer = (Button)root.findViewById(R.id.button_safety_engineer);
        buttonFirstConstructor =(Button)root.findViewById(R.id.button_first_constructor);
        buttonRefinement.setOnClickListener(this::onClick);
        buttonSafetyEngineer.setOnClickListener(this::onClick);
        buttonFirstConstructor.setOnClickListener(this::onClick);
        //初始化按钮数组
        ButtonArgs = new Button[]{buttonRefinement,buttonSafetyEngineer,buttonFirstConstructor};
        //找到指示标相应的ImageView
        cursor = (ImageView) root.findViewById(R.id.cursor);
        cursor.setBackgroundColor(Color.parseColor( "#0287FF"));
        //添加fragment进list
        list = new ArrayList<>();
        list.add(new RefinementFragment());
        list.add(new SafetyEngineerFragment());
        list.add(new FirstConstructorFragment());
        adapter = new SectionsExaminationrAdapter(fm,list);
        /*设置适配器*/
        viewPager.setAdapter(adapter);
        //viewPager监听事件，重写OnPageSelected()方法，实现左右滑动页面
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (WidrhArgs == null) {
                    WidrhArgs = new int[] { buttonRefinement.getWidth(), buttonSafetyEngineer.getWidth(),
                            buttonFirstConstructor.getWidth() };
                }
                // 根据每次选中的按钮，重置颜色
                resetButtonColor();
                // 将滑动到当前的标签下，改动标签颜色
                ButtonArgs[position].setTextColor(Color.parseColor( "#0287FF" ));
                cursorAnim(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化按钮颜色
        resetButtonColor();
        //设置默认第一页
        viewPager.setCurrentItem(0);
        buttonRefinement.setTextColor(Color.parseColor("#0287FF"));
        cursor.setBackgroundColor(Color.parseColor("#0287FF"));
        cursorAnim(0);
        return root;
    }
    // 设置按钮颜色
    private void resetButtonColor() {
        buttonRefinement.setBackgroundColor(Color.parseColor("#CDECFF"));
        buttonSafetyEngineer.setBackgroundColor(Color.parseColor("#CDECFF"));
        buttonFirstConstructor.setBackgroundColor(Color.parseColor("#CDECFF"));
        buttonRefinement.setTextColor(Color.BLACK);
        buttonSafetyEngineer.setTextColor(Color.BLACK);
        buttonFirstConstructor.setTextColor(Color.BLACK);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_refinement:
                viewPager.setCurrentItem(0);
                cursorAnim(0);
                break;
            case R.id.button_safety_engineer:
                viewPager.setCurrentItem(1);
                cursorAnim(1);
                break;
            case R.id.button_first_constructor:
                viewPager.setCurrentItem(2);
                cursorAnim(2);
                break;
        }
    }
    // 指示器的跳转，传入当前所处的页面的下标
    private void cursorAnim(int curItem) {
        // 每次调用，就将指示器的横坐标设置为0，即开始的位置
        cursorX = 0;
        // 再根据当前的curItem来设置指示器的宽度
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor
                .getLayoutParams();
        // 减去边距*2，以对齐标题栏文字
        if (curItem==0){
            lp.width=360;
        }
        else{
            lp.width = WidrhArgs[curItem] - ButtonArgs[0].getPaddingLeft() * 2;
        }
        cursor.setLayoutParams(lp);
        // 循环获取当前页之前的所有页面的宽度
        for (int i = 0; i < curItem; i++) {
            cursorX = cursorX + ButtonArgs[i].getWidth();
        }
        // 再加上当前页面的左边距，即为指示器当前应处的位置
        cursor.setX(cursorX + ButtonArgs[curItem].getPaddingLeft());
    }
}
package com.example.menu.ui.mainHome.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MineViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("下面添加订阅、修改密码、收藏等功能");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
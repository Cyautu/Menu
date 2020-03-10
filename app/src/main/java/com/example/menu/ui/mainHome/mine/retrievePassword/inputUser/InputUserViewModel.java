package com.example.menu.ui.mainHome.mine.retrievePassword.inputUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InputUserViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InputUserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是验证用户");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
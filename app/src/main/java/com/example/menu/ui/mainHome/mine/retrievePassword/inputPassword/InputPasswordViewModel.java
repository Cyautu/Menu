package com.example.menu.ui.mainHome.mine.retrievePassword.inputPassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InputPasswordViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InputPasswordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是验证密保");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
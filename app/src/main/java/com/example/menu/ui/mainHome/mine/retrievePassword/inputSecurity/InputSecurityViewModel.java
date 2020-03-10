package com.example.menu.ui.mainHome.mine.retrievePassword.inputSecurity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InputSecurityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InputSecurityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是验证密保");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
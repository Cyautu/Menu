package com.example.menu.ui.mainHome.mine.follow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<String> mName = new MutableLiveData<>();

    public void setmName(String name) {
        mName.setValue(name);
    }

    public LiveData<String> getText() {
        return mName;
    }
}
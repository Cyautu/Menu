package com.example.menu.ui.mainHome.examination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExaminationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExaminationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("这是考试页面");
    }

    public LiveData<String> getText() {
        return mText;
    }
    private MutableLiveData<String> mName = new MutableLiveData<>();

}
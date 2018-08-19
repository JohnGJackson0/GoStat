package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ListViewModel extends ViewModel {
    private MutableLiveData<String> mName;

    private MutableLiveData<List<DataPoint>> mList;

    public MutableLiveData<List<DataPoint>> getList() {
        if (mList == null) {
            mList = new MutableLiveData<List<DataPoint>>();
        }
        return mList;
    }

    public MutableLiveData<String> getName() {
        if (mName == null) {
            mName = new MutableLiveData<String>();
        }
        return mName;
    }
}

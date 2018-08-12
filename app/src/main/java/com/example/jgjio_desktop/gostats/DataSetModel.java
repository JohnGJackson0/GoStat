package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class DataSetModel extends ViewModel {

    private MutableLiveData<List<Double>> mList;

    public MutableLiveData<List<Double>> getList() {
        if (mList == null) {
            mList = new MutableLiveData<List<Double>>();
        }
        return mList;
    }


}

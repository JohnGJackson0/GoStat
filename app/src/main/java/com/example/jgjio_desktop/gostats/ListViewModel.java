package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.provider.ContactsContract;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private AppRepository mRepository;


    private LiveData<List<StatisticalList>> mAllStatisticalLists;
    private LiveData<List<DataPoint>> mAllDataPoints;

    public ListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);

        mAllStatisticalLists = mRepository.getAllLists();
        mAllDataPoints = mRepository.getAllDataPoints();
    }

    LiveData<List<StatisticalList>> getAllLists() {
        return mAllStatisticalLists;
    }

    LiveData<List<DataPoint>> getAllDataPoints() {
        return mAllDataPoints;
    }

    public void insertList(StatisticalList statisticalList) {
        mRepository.insertList(statisticalList);
    }

    public void insertDataPoint(DataPoint dataPoint) {
        mRepository.insertDataPoint(dataPoint);
    }

}

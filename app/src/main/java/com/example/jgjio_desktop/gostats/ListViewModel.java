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
    LiveData<List<StatisticalList>> mAllStatisticalLists;

    public ListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllStatisticalLists = mRepository.mAllStatisticalLists;

    }

    LiveData<List<StatisticalList>> getAllLists() {
        return mAllStatisticalLists;
    }

    LiveData<List<DataPoint>> getDataPointsInList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }

    LiveData<String> getListName(int ListId) {
        return mRepository.getListName(ListId);
    }

    void insertDataPoints(List<DataPoint> listDataPoints) {
        mRepository.insertDataPoints(listDataPoints);
    }


}

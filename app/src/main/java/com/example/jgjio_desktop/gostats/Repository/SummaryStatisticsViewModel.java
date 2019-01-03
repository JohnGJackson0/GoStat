package com.example.jgjio_desktop.gostats.Repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class SummaryStatisticsViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    LiveData<List<StatisticalList>> mAllLists;


    public SummaryStatisticsViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllLists = mRepository.getAllStatisticalLists();
    }

    LiveData<List<StatisticalList>> getAllLists() {
        return mAllLists;
    }

    LiveData<String> getListName(int id) {
        return mRepository.getListName(id);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }


}

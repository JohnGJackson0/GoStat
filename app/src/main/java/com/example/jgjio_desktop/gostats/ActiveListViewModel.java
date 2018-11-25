package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ActiveListViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    private LiveData<List<StatisticalList>> mAllLists;

    public ActiveListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);

        mAllLists = mRepository.getAllStatisticalLists();
    }

    LiveData<List<StatisticalList>> getAllLists() {
        return mAllLists;
    }

    //todo fix this cast from long to int

    //return the id
    long insertStatisticalList(StatisticalList newList) { return mRepository.insertStatisticalList(newList); }

    void deleteList(StatisticalList statList) {mRepository.removeStatisticalList(statList); }

    void insertDataPoint(DataPoint newDataPoint) {
        mRepository.insertDataPoint(newDataPoint);
    }

}

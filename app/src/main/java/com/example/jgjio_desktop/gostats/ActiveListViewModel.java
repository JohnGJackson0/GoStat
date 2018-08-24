package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ActiveListViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    LiveData<List<StatisticalList>> mAllLists;

    public ActiveListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);

        mAllLists = mRepository.getAllStatisticalLists();
    }

    LiveData<List<StatisticalList>> getAllLists() {
        return mAllLists;
    }

    int getIdOfLastListEntry() {
        return mRepository.getLastCreatedStatisticalListId();
    }

    void insertStatisticalList(StatisticalList newList) { mRepository.insertStatisticalList(newList);}

}

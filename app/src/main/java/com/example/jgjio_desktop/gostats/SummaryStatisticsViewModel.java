package com.example.jgjio_desktop.gostats;

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

    String getListName(double id) {
        return mRepository.getListName(id);
    }


}

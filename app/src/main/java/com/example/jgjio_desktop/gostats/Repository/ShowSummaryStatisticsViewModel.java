package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ShowSummaryStatisticsViewModel extends AndroidViewModel {
    private AppRepository mRepository;


    public ShowSummaryStatisticsViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }

}

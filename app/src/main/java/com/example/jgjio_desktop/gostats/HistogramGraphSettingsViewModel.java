package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import java.util.List;

public class HistogramGraphSettingsViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public HistogramGraphSettingsViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    long getNumberOfDataPointsInList(double listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }
}

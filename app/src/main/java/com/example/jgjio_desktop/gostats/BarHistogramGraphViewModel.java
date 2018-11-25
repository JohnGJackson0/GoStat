package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

public class BarHistogramGraphViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public BarHistogramGraphViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<DataPoint>> getDataPointList(int listID) {
        return mRepository.getDataPointsInList(listID);
    }

    LiveData<List<FrequencyInterval>> getFrequencyIntervalsInTable(int listID) {
        return mRepository.getFrequencyIntervalsInTable(listID);
    }

    LiveData<Boolean> isListAFrequencyTable(int listID) {
         return mRepository.isListAFrequencyTable(listID);
    }

    int getMaxFrequency(int listID) {
        return mRepository.getMaxFrequency(listID);
    }
}

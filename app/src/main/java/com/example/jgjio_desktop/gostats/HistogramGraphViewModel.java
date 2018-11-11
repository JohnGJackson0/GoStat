package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import java.util.List;

public class HistogramGraphViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public HistogramGraphViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }

    //todo make sure all listID is listID not listId
    double getMaxValue(int listID) {
        return mRepository.getMaxValue(listID);
    }

    double getMinValue(int listID) {
        return mRepository.getMinValue(listID);
    }

    long getNumberOfDataPointsInList(int listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }

    //todo fix errors from long to int (also in other view models)
    int insertStatisticalList(StatisticalList newList) { return (int) mRepository.insertStatisticalList(newList);}


    void insertFrequencyIntervals(List<FrequencyInterval> list){
        mRepository.insertFrequencyIntervals(list);

    }


}

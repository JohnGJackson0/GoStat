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

    LiveData<List<DataPoint>> getList(double listId) {
        return mRepository.getDataPointsInList(listId);
    }

    //todo make sure all listID is listID not listId
    double getMaxValue(double listID) {
        return mRepository.getMaxValue(listID);
    }

    double getMinValue(double listID) {
        return mRepository.getMinValue(listID);
    }

    long getNumberOfDataPointsInList(double listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }

    String getListName(double id) {
        return mRepository.getListName(id);
    }

    long insertStatisticalList(StatisticalList newList) { return mRepository.insertStatisticalList(newList);}

    void insertDataPoints(List<DataPoint> newDataPoints) {
        mRepository.insertDataPoints(newDataPoints);
    }



}

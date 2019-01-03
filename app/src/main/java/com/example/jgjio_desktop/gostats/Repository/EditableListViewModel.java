package com.example.jgjio_desktop.gostats.Repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

public class EditableListViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public EditableListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<PagedList<DataPoint>> getListById(int listId) {
        listDataPoints = new LivePagedListBuilder<>(
                mRepository.getDataPointsInListById(listId), 15)
                .build();
        return listDataPoints;
    }

    void insertDataPoints(List<DataPoint> listDataPoints) {
        mRepository.insertDataPoints(listDataPoints);
    }

    void insertDataPoint(DataPoint newDataPoint) {
        mRepository.insertDataPoint(newDataPoint);
    }

    void updateDataPoint(DataPoint newDataPoint) {
        mRepository.updateDataPoint(newDataPoint);
    }

    LiveData<String> getListName(int listID) {
        return mRepository.getListName(listID);
    }

    void deleteDisabledDataPointsFromList(int listID) {
        mRepository.deleteDisabledDataPointsFromList(listID);
    }

    LiveData<Long> getNumberOfItemsInList(int listID){
        return mRepository.getNumberOfDataPointsInList(listID);
    }
}

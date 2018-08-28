package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class EditableListViewModel extends AndroidViewModel{
    private AppRepository mRepository;

    public EditableListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }


    void insertDataPoints(List<DataPoint> listDataPoints) {
        mRepository.insertDataPoints(listDataPoints);
    }

    void insertDataPoint(DataPoint dataPoint) {
        mRepository.insertDataPoint(dataPoint);
    }

    String getListName(double id) {
        return mRepository.getListName(id);
    }

    LiveData<List<DataPoint>> getDataPointsInList(double listId) {
        return mRepository.getDataPointsInList(listId);
    }
}

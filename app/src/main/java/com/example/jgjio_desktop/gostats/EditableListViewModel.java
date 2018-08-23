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
}

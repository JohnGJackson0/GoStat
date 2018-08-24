package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ViewSingleListViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    private LiveData<String> mName;
    private LiveData<List<DataPoint>> mList;

    public ViewSingleListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    String getName(int listId) {
        return mRepository.getListName(listId);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }



}

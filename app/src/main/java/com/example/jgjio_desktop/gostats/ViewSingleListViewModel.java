package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
//TODO find solution to use Live Data correctly
// right now observers might not be updating

public class ViewSingleListViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    private LiveData<String> mName;
    private LiveData<List<DataPoint>> mList;

    public ViewSingleListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    String getName(double listId) {
        return mRepository.getListName(listId);
    }

    LiveData<List<DataPoint>> getList(double listId) {
        return mRepository.getDataPointsInList(listId);
    }

    void updateListName(String name, long id) {
        mRepository.updateListName(name, id);
    }



}

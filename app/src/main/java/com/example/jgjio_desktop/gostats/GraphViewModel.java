package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

public class GraphViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public GraphViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }
}

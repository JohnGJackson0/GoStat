package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

public class ViewEditableListFragmentViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ViewEditableListFragmentViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<PagedList<DataPoint>> getListById(int listId) {
        LiveData<PagedList<DataPoint>> listDataPoints =
                new LivePagedListBuilder<>(mRepository.getDataPointsInListById(listId), 30).build();
        return listDataPoints;
    }

}

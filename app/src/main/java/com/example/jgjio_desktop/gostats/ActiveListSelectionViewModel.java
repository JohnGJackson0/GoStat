package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.LayoutInflater;

import java.util.List;

import javax.sql.DataSource;

public class ActiveListSelectionViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    private LiveData<List<StatisticalList>> mAllLists;

    public ActiveListSelectionViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);

        mAllLists = mRepository.getAllStatisticalLists();
    }

    LiveData<PagedList<StatisticalList>> getListById() {
        LiveData<PagedList<StatisticalList>> statisticalList =
                new LivePagedListBuilder<>(mRepository.getStatisticalListDetailsByID(), 2).build();
        return statisticalList;
    }


    LiveData<List<StatisticalList>> getAllLists() {
        return mAllLists;
    }

    //todo fix this cast from long to int

    //return the id
    long insertStatisticalList(StatisticalList newList) { return mRepository.insertStatisticalList(newList); }

    void deleteList(StatisticalList statList) {mRepository.removeStatisticalList(statList); }

    void insertDataPoint(DataPoint newDataPoint) {
        mRepository.insertDataPoint(newDataPoint);
    }

}

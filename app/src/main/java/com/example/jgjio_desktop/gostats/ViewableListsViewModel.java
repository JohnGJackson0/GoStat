package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ViewableListsViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<StatisticalList>> mAllLists;

    public ViewableListsViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);

        mAllLists = mRepository.getAllStatisticalLists();
    }

    LiveData<List<StatisticalList>> getAllLists() {
        return mAllLists;
    }

    //todo fix this cast from long to int

    //return the id
    long insertStatisticalList(StatisticalList newList) { return mRepository.insertStatisticalList(newList); }

    void deleteList(int listID) {
        mRepository.removeStatisticalListByID(listID);
    }


}
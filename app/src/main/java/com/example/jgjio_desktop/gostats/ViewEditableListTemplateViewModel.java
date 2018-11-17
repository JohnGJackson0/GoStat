package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

public class ViewEditableListTemplateViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public ViewEditableListTemplateViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    String getListName(int listID) {
        return mRepository.getListName(listID);
    }

    String getName(int listId) {
        return mRepository.getListName(listId);
    }

    void updateListName(String name, int id) {
        mRepository.updateListName(name, id);
    }

    void deleteList(int listID) {
        mRepository.removeStatisticalListByID(listID);
    }
}
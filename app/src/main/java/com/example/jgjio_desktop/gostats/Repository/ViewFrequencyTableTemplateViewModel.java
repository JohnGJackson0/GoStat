package com.example.jgjio_desktop.gostats.Repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

public class ViewFrequencyTableTemplateViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public ViewFrequencyTableTemplateViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<PagedList<FrequencyInterval>> getListById(int listID) {
        return new LivePagedListBuilder<>(mRepository.
                getFrequencyTable(listID), 30).build();
    }

    LiveData<String> getListName(int listID) {
        return mRepository.getListName(listID);
    }

    void deleteList(int listID) {
        mRepository.removeStatisticalListByID(listID);
    }
}

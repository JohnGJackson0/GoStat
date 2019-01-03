package com.example.jgjio_desktop.gostats.Repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ViewFrequencyTableViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ViewFrequencyTableViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<FrequencyInterval>> getTable(int mListID) {
        return mRepository.getFrequencyIntervalsInTable(mListID);
    }

}
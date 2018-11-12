package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.util.Log;

public class CreateFrequencyTableViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public CreateFrequencyTableViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    long getNumberOfDataPointsInList(int listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }

    boolean isFrequencyTable(int listID) {
        return mRepository.isListAFrequencyTable(listID);
    }
}

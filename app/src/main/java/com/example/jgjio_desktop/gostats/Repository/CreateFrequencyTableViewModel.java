package com.example.jgjio_desktop.gostats.Repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class CreateFrequencyTableViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public CreateFrequencyTableViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<Long> getNumberOfDataPointsInList(int listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }

    LiveData<Boolean> isFrequencyTable(int listID) {
        return mRepository.isListAFrequencyTable(listID);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }

    //todo fix errors from long to int (also in other view models)
    int insertStatisticalList(StatisticalList newList) { return (int) mRepository.insertStatisticalList(newList);}


    void insertFrequencyIntervals(List<FrequencyInterval> list){
        mRepository.insertFrequencyIntervals(list);

    }
}

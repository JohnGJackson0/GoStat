package app.goStat.view.createFrequencyTable;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;
import app.goStat.model.FrequencyInterval;
import app.goStat.model.StatisticalList;

public class CreateFrequencyTableViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public CreateFrequencyTableViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<Long> getNumberOfDataPointsInList(int listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }

    String getStaticListName(int listID) {
        return mRepository.getStaticListName(listID);
    }

    //todo fix errors from long to int (also in other view models)
    int insertStatisticalList(StatisticalList newList) { return (int) mRepository.insertStatisticalList(newList); }

    void insertFrequencyIntervals(List<FrequencyInterval> list){
        mRepository.insertFrequencyIntervals(list);
    }
}

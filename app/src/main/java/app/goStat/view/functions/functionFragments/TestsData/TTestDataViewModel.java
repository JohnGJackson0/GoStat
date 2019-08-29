package app.goStat.view.functions.functionFragments.TestsData;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;
import app.goStat.model.StatisticalList;

public class TTestDataViewModel extends AndroidViewModel {

    private AppRepository mRepository;

    public TTestDataViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<List<StatisticalList>> getAllEditableLists() {
        return mRepository.getAllStatisticalLists();
    }


    LiveData<List<DataPoint>> getDataPoints(int id) {return mRepository.getDataPointsInList(id);}
}

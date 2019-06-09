package app.goStat.view.viewSummary;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;

public class ShowSummaryStatisticsViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ShowSummaryStatisticsViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<DataPoint>> getList(int listId) {
        return mRepository.getDataPointsInList(listId);
    }

}

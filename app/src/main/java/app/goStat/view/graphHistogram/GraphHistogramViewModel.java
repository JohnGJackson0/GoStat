package app.goStat.view.graphHistogram;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.FrequencyInterval;

public class GraphHistogramViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public GraphHistogramViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<FrequencyInterval>> getFrequencyIntervalsInTable(int listID) {
        return mRepository.getFrequencyIntervalsInTable(listID);
    }

    LiveData<String> getOnCreatedAssociatedListName(int listID){ return mRepository.getOnCreatedAssociatedListName(listID);}

    int getAssociatedListID(int listID) {
        return mRepository.getAssociatedList(listID);
    }
}

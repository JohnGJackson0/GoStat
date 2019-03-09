package app.goStat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class GraphHistogramViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public GraphHistogramViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<List<FrequencyInterval>> getFrequencyIntervalsInTable(int listID) {
        return mRepository.getFrequencyIntervalsInTable(listID);
    }

    LiveData<String> getName(int listID) {
        return mRepository.getListName(listID);
    }

    String getStaticListName(int listID) {
        return mRepository.getStaticListName(listID);
    }

    int getAssociatedListID(int listID) {
        return mRepository.getAssociatedList(listID);
    }
}

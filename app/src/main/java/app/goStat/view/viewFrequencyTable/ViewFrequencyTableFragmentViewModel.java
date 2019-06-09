package app.goStat.view.viewFrequencyTable;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.FrequencyInterval;

public class ViewFrequencyTableFragmentViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ViewFrequencyTableFragmentViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<PagedList<FrequencyInterval>> getListById(int listID) {
        return new LivePagedListBuilder<>(mRepository.
                getFrequencyTable(listID), 30).build();
    }

    public LiveData<String> getListName(int listID) {
        return mRepository.getListName(listID);
    }

    public LiveData<List<FrequencyInterval>> getTable(int mListID) {
        return mRepository.getFrequencyIntervalsInTable(mListID);
    }
}
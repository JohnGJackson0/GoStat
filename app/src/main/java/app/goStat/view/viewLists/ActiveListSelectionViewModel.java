package app.goStat.view.viewLists;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;
import app.goStat.model.FrequencyInterval;
import app.goStat.model.StatisticalList;

public class ActiveListSelectionViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    private LiveData<List<StatisticalList>> mAllLists;

    public ActiveListSelectionViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllLists = mRepository.getAllStatisticalLists();
    }

    public LiveData<PagedList<StatisticalList>> getListById() {
        LiveData<PagedList<StatisticalList>> statisticalList =
                new LivePagedListBuilder<>(mRepository.getStatisticalListDetailsByID(), 2).build();
        return statisticalList;
    }

    public LiveData<List<DataPoint>> getEditableListPreview(int listID){
        return mRepository.getEditableListPreview(listID);
    }

    public LiveData<List<FrequencyInterval>> getFrequencyTablePreview(int listID) {
        return mRepository.getFrequencyTablePreview(listID);
    }
}

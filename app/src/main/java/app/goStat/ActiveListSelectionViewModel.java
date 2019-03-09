package app.goStat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

public class ActiveListSelectionViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    private LiveData<List<StatisticalList>> mAllLists;

    public ActiveListSelectionViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllLists = mRepository.getAllStatisticalLists();
    }

    LiveData<PagedList<StatisticalList>> getListById() {
        LiveData<PagedList<StatisticalList>> statisticalList =
                new LivePagedListBuilder<>(mRepository.getStatisticalListDetailsByID(), 2).build();
        return statisticalList;
    }

    LiveData<List<DataPoint>> getEditableListPreview(int listID){
        return mRepository.getEditableListPreview(listID);
    }

    LiveData<List<FrequencyInterval>> getFrequencyTablePreview(int listID) {
        return mRepository.getFrequencyTablePreview(listID);
    }
}

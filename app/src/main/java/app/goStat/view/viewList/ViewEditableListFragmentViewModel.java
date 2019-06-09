package app.goStat.view.viewList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;

public class ViewEditableListFragmentViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ViewEditableListFragmentViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<PagedList<DataPoint>> getListById(int listId) {
        LiveData<PagedList<DataPoint>> listDataPoints =
                new LivePagedListBuilder<>(mRepository.getDataPointsInListById(listId), 30).build();
        return listDataPoints;
    }

    public LiveData<Long> getNumberOfDataPointsInList(int listID) {
        return mRepository.getNumberOfDataPointsInList(listID);
    }

}

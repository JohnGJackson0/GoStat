package app.goStat.view.viewList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;

public class ViewSingleEditableListViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public ViewSingleEditableListViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<PagedList<DataPoint>> getListById(int listId) {
        listDataPoints = new LivePagedListBuilder<>(
                mRepository.getDataPointsInListById(listId), 30).build();
        return listDataPoints;
    }

    public void deleteDisabledDataPoints(int listID) {mRepository.deleteDisabledDataPointsFromList(listID); }
}

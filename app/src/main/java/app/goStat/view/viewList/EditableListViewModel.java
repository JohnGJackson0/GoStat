package app.goStat.view.viewList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.List;

import app.goStat.model.AppRepository;
import app.goStat.model.DataPoint;

public class EditableListViewModel extends AndroidViewModel{
    private AppRepository mRepository;
    private LiveData<PagedList<DataPoint>> listDataPoints;

    public EditableListViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<PagedList<DataPoint>> getListById(int listId) {
        listDataPoints = new LivePagedListBuilder<>(
                mRepository.getDataPointsInListById(listId), 15)
                .build();
        return listDataPoints;
    }

    public void insertDataPoints(List<DataPoint> listDataPoints) {
        mRepository.insertDataPoints(listDataPoints);
    }

    public void insertDataPoint(DataPoint newDataPoint) {
        mRepository.insertDataPoint(newDataPoint);
    }

    public void deleteDisabledDataPointsFromList(int listID) {
        mRepository.deleteDisabledDataPointsFromList(listID);
    }

    public LiveData<Long> getNumberOfItemsInList(int listID){
        return mRepository.getNumberOfDataPointsInList(listID);
    }
}

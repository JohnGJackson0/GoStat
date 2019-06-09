package app.goStat.view.viewList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import app.goStat.model.AppRepository;

public class ViewEditableListTemplateViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ViewEditableListTemplateViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<String> getListName(int listID) {
        return mRepository.getListName(listID);
    }

    public void updateListName(String name, int id) {
        mRepository.updateListName(name, id);
    }

    public Long getStaticNumberOfDataPointsInList(int listID) {
        return mRepository.getStaticNumberOfDataPointsInList(listID);
    }
}
package app.goStat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

public class ViewEditableListTemplateViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public ViewEditableListTemplateViewModel(Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    LiveData<String> getListName(int listID) {
        return mRepository.getListName(listID);
    }

    void updateListName(String name, int id) {
        mRepository.updateListName(name, id);
    }

    Long getStaticNumberOfDataPointsInList(int listID) {
        return mRepository.getStaticNumberOfDataPointsInList(listID);
    }
}
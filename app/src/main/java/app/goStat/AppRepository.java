package app.goStat;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.os.AsyncTask;
import java.util.List;

//TODO check for race conditions

public class AppRepository {
    private StatisticalListDao mListDao;
    private DataPointDao mDataPointDao;
    private FrequencyIntervalDao mFrequencyIntervalDao;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        mListDao = db.statisticalListDao();
        mDataPointDao = db.dataPointDao();
        mFrequencyIntervalDao = db.frequencyIntervalDao();
    }

    /*
     *
     * The following methods are created for Lists
     * from StatisticalListDao
     *
     */

    LiveData<Boolean> isListAFrequencyTable(int id) {
        return mListDao.isFrequencyTable(id);
    }

    void removeStatisticalListByID(int listID) {
        mListDao.deleteListById(listID);
    }
    int getAssociatedList(int listID) {return mListDao.getAssociatedListID(listID);}
    String getStaticListName(int listID) {return mListDao.getStaticListName(listID); }

    boolean hasAssociatedList(int listID) {
        if (mListDao.getAssociatedListID(listID) == -1) {
            return false;
        }
        return true;
    }

    LiveData<List<StatisticalList>> getAllStatisticalLists() {
        return mListDao.loadAllLists();
    }

    LiveData<String> getListName(int listId) {
        return mListDao.getListName(listId);
    }

    DataSource.Factory<Integer, StatisticalList> getStatisticalListDetailsByID() { return mListDao.getAllLists(); }

    long insertStatisticalList(StatisticalList list) {
        return mListDao.insert(list);
    }

    void updateListName(String name, int id) { new updateStatisticalListNameAsyncTask(mListDao, id).execute(name); }

    /*
     *
     * The following methods are created for Frequency Intervals
     * from FrequencyIntervalDao
     *
     */

    LiveData<List<FrequencyInterval>> getFrequencyTablePreview(int listID) { return mFrequencyIntervalDao.getFrequencyTablePreview(listID); }

    DataSource.Factory getFrequencyTable(int listId) { return mFrequencyIntervalDao.getListById(listId); }

    LiveData<List<FrequencyInterval>> getFrequencyIntervalsInTable(int listId) {
        return mFrequencyIntervalDao.getList(listId);
    }

    void insertFrequencyIntervals(List<FrequencyInterval> frequencyIntervals) { mFrequencyIntervalDao.insertFrequencyIntervals(frequencyIntervals); }

    /*
    *
    * The following methods are created for DataPoints
    * from DataPointDao
    *
     */
    LiveData<List<DataPoint>> getDataPointsInList(int listId) { return mDataPointDao.getList(listId); }

    long getStaticNumberOfDataPointsInList(int listID) { return mDataPointDao.getStaticNumberOfDataPointsInList(listID); }

    DataSource.Factory getDataPointsInListById(int listId) { return mDataPointDao.getListById(listId); }

    LiveData<Long> getNumberOfDataPointsInList(int listID) { return mDataPointDao.getNumberOfDataPointsInList(listID); }

    void insertDataPoints(List<DataPoint> listDataPoints) { mDataPointDao.insertDataPoints(listDataPoints); }

    void insertDataPoint(DataPoint dataPoint) { mDataPointDao.insertDataPoint(dataPoint); }

    LiveData<List<DataPoint>> getEditableListPreview(int listID) { return mDataPointDao.getEditableListPreview(listID); }

    void updateDataPoint(DataPoint dataPoint) { new updateDataPointAsyncTask(mDataPointDao).execute(dataPoint); }

    void deleteDisabledDataPointsFromList(int listID) { new deleteDisabledDataPointAsyncTask(mDataPointDao).execute(listID); }

    /*
     *
     * The following async tasks support the above methods
     * these are the ones that are currently being used
     *
     */

    private static class deleteDisabledDataPointAsyncTask extends AsyncTask<Integer, Void, Void> {
        private DataPointDao dataPointDao;

        deleteDisabledDataPointAsyncTask(DataPointDao dao) {dataPointDao = dao;}

        @Override
        protected Void doInBackground(final Integer... params) {
            dataPointDao.deleteDisabledDataPoints(params[0]);
            return null;
        }
    }

    private static class updateDataPointAsyncTask extends AsyncTask<DataPoint, Void, Void> {
        private DataPointDao dataPointDao;

        updateDataPointAsyncTask(DataPointDao dao) {dataPointDao = dao;}

        @Override
        protected Void doInBackground(final DataPoint... params) {
            dataPointDao.update(params[0]);
            return null;
        }
    }

    private static class updateStatisticalListNameAsyncTask extends AsyncTask<String, Void, Void> {
        private StatisticalListDao statListDao;
        private int id;

        updateStatisticalListNameAsyncTask(StatisticalListDao dao, int id) {
            statListDao = dao;
            this.id = id;
        }

        @Override
        protected Void doInBackground(final String ... params) {
            statListDao.updateName(params[0], id);
            return null;
        }
    }
}

package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.Date;
import java.util.List;

//TODO check for race conditions

public class AppRepository {
    private StatisticalListDao mListDao;
    private DataPointDao mDataPointDao;
    private FrequencyIntervalDao mFrequencyIntervalDao;
    LiveData<List<StatisticalList>> mAllStatisticalLists;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        mListDao = db.statisticalListDao();
        mDataPointDao = db.dataPointDao();
        mFrequencyIntervalDao = db.frequencyIntervalDao();

        //todo do we need this ?????????????
        mAllStatisticalLists = db.statisticalListDao().loadAllLists();
    }

    /*
     *
     * The following methods are created for Lists
     * from StatisticalListDao
     *
     */

    boolean isListAFrequencyTable(int id) {
        return mListDao.isFrequencyTable(id);
    }

    void removeStatisticalList(StatisticalList statList) {
        new removeStatisticalListAsyncTask(mListDao).execute(statList);
    }

    void removeStatisticalListByID(int listID) {
        mListDao.deleteListById(listID);
    }

    LiveData<List<StatisticalList>> getAllStatisticalLists() {
        return mAllStatisticalLists;
    }

    String getListName(int listId) {
        return mListDao.getListName(listId);
    }

    //todo ids must be long

    //returns ID
    long insertStatisticalList(StatisticalList list) {
        return mListDao.insert(list);
    }

    void updateListName(String name, int id) {
        new updateStatisticalListNameAsyncTask(mListDao, id).execute(name);
    }

    /*
     *
     * The following methods are created for Frequency Intervals
     * from FrequencyIntervalDao
     *
     */

    DataSource.Factory getFrequencyTable(int listId) {
        return mFrequencyIntervalDao.getListById(listId);
    }

    LiveData<List<FrequencyInterval>> getFrequencyIntervalsInTable(int listId) {
        return mFrequencyIntervalDao.getList(listId);
    }

    long getNumberOfFrequencyIntervalsInTable(int listID) {
        return mFrequencyIntervalDao.getNumberOfIntervalsInList(listID);
    }

    void insertFrequencyIntervals(List<FrequencyInterval> frequencyIntervals) {
        mFrequencyIntervalDao.insertFrequencyIntervals(frequencyIntervals);
        //new insertFrequencyIntervalsAsyncTask(mFrequencyIntervalDao).execute(frequencyIntervals);
    }

    void insertFrequencyInterval(FrequencyInterval frequencyInterval) {
        mFrequencyIntervalDao.insert(frequencyInterval);
        //new insertFrequencyIntervalAsyncTask(mFrequencyIntervalDao).execute(frequencyInterval);
    }

    void updateFrequencyInterval(FrequencyInterval frequencyInterval) {
        mFrequencyIntervalDao.update(frequencyInterval);
        //new updateDataPointAsyncTask(mDataPointDao).execute(dataPoint);
    }

    /*
    *
    * The following methods are created for DataPoints
    * from DataPointDao
    *
     */
    LiveData<List<DataPoint>> getDataPointsInList(int listId) {
        return mDataPointDao.getList(listId);
    }

    DataSource.Factory getDataPointsInListById(int listId) {
        return mDataPointDao.getListById(listId);
    }

    double getMaxValue(int listID) {
        return mDataPointDao.getMaxValue(listID);
    }

    double getMinValue(int listID) {
        return mDataPointDao.getMinValue(listID);
    }

    long getNumberOfDataPointsInList(int listID) {
        return mDataPointDao.getNumberOfDataPointsInList(listID);
    }

    void insertDataPoints(List<DataPoint> listDataPoints) {
        new insertDataPointsAsyncTask(mDataPointDao).execute(listDataPoints);
    }

    void insertDataPoint(DataPoint dataPoint) {
        mDataPointDao.insert(dataPoint);
        //new insertDataPointAsyncTask(mDataPointDao).execute(dataPoint);
    }

    void updateDataPoint(DataPoint dataPoint) {
        mDataPointDao.update(dataPoint);
        //new updateDataPointAsyncTask(mDataPointDao).execute(dataPoint);
    }


    /*
     *
     * The following async tasks support the above methods
     * these are the ones that are currently being used
     *
     */

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

    private static class insertDataPointsAsyncTask extends AsyncTask<List<DataPoint>, Void, Void> {
        private DataPointDao dataPointDao;

        insertDataPointsAsyncTask(DataPointDao dao) {dataPointDao = dao;}

        @Override
        protected Void doInBackground(final List<DataPoint>... params) {
            dataPointDao.insertDataPoints(params[0]);
            return null;
        }
    }

    private static class removeStatisticalListAsyncTask extends AsyncTask<StatisticalList, Void, Void> {

        private StatisticalListDao listDao;

        removeStatisticalListAsyncTask(StatisticalListDao dao) {
            listDao = dao;
        }

        @Override
        protected Void doInBackground(final StatisticalList... params) {
            listDao.delete(params[0]);
            return null;
        }
    }

    /*
     *
     * The following Async Tasks are reserved for further use
     * todo These methods if not used should be deleted upon release
     *
     */

    private static class insertDataPointAsyncTask extends AsyncTask<DataPoint, Void, Void> {
        private DataPointDao dataPointDao;

        insertDataPointAsyncTask(DataPointDao dao) {dataPointDao = dao;}

        @Override
        protected Void doInBackground(final DataPoint... params) {
            dataPointDao.insertDataPoint(params[0]);
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

    private static class insertListAsyncTask extends AsyncTask<StatisticalList, Void, Void> {

        private StatisticalListDao listDao;

        insertListAsyncTask(StatisticalListDao dao) {
            listDao = dao;
        }

        @Override
        protected Void doInBackground(final StatisticalList... params) {
            listDao.insert(params[0]);
            return null;
        }


    }


}

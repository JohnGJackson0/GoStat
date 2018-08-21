package com.example.jgjio_desktop.gostats;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

//TODO check for race conditions
//TODO finish so that it properly runs in background thread

public class AppRepository {
    private StatisticalListDao mListDao;
    private DataPointDao mDataPointDao;
    private LiveData<List<StatisticalList>> mAllLists;
    private LiveData<List<DataPoint>> mAllDataPoints;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        mListDao = db.statisticalListDao();
        mDataPointDao = db.dataPointDao();

        mAllLists = mListDao.loadAllLists();
        mAllDataPoints = mDataPointDao.loadAllDataPoints();


    }

    public LiveData<List<DataPoint>> getList(int listId) {
      return mDataPointDao.getList(listId);
    }




    LiveData<List<StatisticalList>> getAllLists() {
        return mAllLists;
    }

    LiveData<List<DataPoint>> getAllDataPoints() {
        return mAllDataPoints;
    }

    public void insertList(StatisticalList list) {
        new insertListAsyncTask(mListDao).execute(list);
    }

    public void insertDataPoint(DataPoint dataPoint) {
        new insertDataPointAsyncTask(mDataPointDao).execute(dataPoint);
    }

    public void updateDataPoint(DataPoint dataPoint) {
        new updateDataPointAsyncTask(mDataPointDao).execute(dataPoint);
    }

    public int getNumberOfLists() {
        return mListDao.getListCount();
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


    private static class insertDataPointAsyncTask extends AsyncTask<DataPoint, Void, Void> {

        private DataPointDao dataPointDao;

        insertDataPointAsyncTask(DataPointDao dao) {
            dataPointDao = dao;
        }

        @Override
        protected Void doInBackground(final DataPoint... params) {
            dataPointDao.insert(params[0]);
            return null;
        }
    }

    private static class updateDataPointAsyncTask extends AsyncTask<DataPoint, Void, Void> {

        private DataPointDao dataPointDao;

        updateDataPointAsyncTask(DataPointDao dao) {
            dataPointDao = dao;
        }

        @Override
        protected Void doInBackground(final DataPoint... params) {
            dataPointDao.update(params[0]);
            return null;
        }
    }


}

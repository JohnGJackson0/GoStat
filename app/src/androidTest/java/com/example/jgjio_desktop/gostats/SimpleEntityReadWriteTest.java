package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private StatisticalListDao mStatListDao;
    private DataPointDao mDataPointDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mDataPointDao = mDb.dataPointDao();
        mStatListDao = mDb.statisticalListDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeListAndReadList() throws Exception {
        StatisticalList statListOne = new StatisticalList(1, "my list");
        mStatListDao.insert(statListOne);

        List<StatisticalList> list = mStatListDao.loadAllLists();

        assertThat(list.get(0).getId(), equalTo(statListOne.getId()));
        assertThat(list.get(0).getName(), equalTo(statListOne.getName()));
    }

    @Test
    public void writeDataPointsAndReadDataPoints() throws Exception {
        StatisticalList statListOne = new StatisticalList(1, "my list");
        mStatListDao.insert(statListOne);

        StatisticalList statListTwo = new StatisticalList(2, "my other list");
        mStatListDao.insert(statListTwo);

        mDataPointDao.insert(new DataPoint(1,true,1.1));
        mDataPointDao.insert(new DataPoint(1,true,1.2));

        mDataPointDao.insert(new DataPoint(2,true,1.3));
        mDataPointDao.insert(new DataPoint(2,true,1.4));

        List<DataPoint> listOneDataPoints = mDataPointDao.getList(1);
        List<DataPoint> listTwoDataPoints = mDataPointDao.getList(2);

        assertThat(listOneDataPoints.get(0).getValue(), equalTo(1.1));
        assertThat(listOneDataPoints.get(0).getListId(), equalTo(1));

        assertThat(listOneDataPoints.get(1).getValue(), equalTo(1.2));
        assertThat(listOneDataPoints.get(1).getListId(), equalTo(1));

        assertThat(listTwoDataPoints.get(0).getValue(), equalTo(1.3));
        assertThat(listTwoDataPoints.get(0).getListId(), equalTo(2));

        assertThat(listTwoDataPoints.get(1).getValue(), equalTo(1.4));
        assertThat(listTwoDataPoints.get(1).getListId(), equalTo(2));

    }


}

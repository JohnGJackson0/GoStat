package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class StatisticalListDaoTest {
    private StatisticalListDao mStatListDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mStatListDao = mDb.statisticalListDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertListSavesData() {
        mStatListDao.insert(new StatisticalList(0, "list"));

        int count = mStatListDao.getListCount();

        assertThat(count, is(1));
    }

}

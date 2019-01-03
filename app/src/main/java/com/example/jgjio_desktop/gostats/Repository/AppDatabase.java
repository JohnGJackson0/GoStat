package com.example.jgjio_desktop.gostats.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {DataPoint.class, StatisticalList.class, FrequencyInterval.class}, version = 1)
@TypeConverters({BigDecimalConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract StatisticalListDao statisticalListDao();
    public abstract DataPointDao dataPointDao();
    public abstract FrequencyIntervalDao frequencyIntervalDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                    //we are stuck with an ID of long and not LiveData<Long> for insert list so allow unless we can find a work around later
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;

    }

    public static void destroyInstance() {
        INSTANCE = null;
    }



}

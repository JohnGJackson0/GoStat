package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {DataPoint.class, StatisticalList.class, FrequencyInterval.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract StatisticalListDao statisticalListDao();
    public abstract DataPointDao dataPointDao();
    public abstract FrequencyIntervalDao frequencyIntervalDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                    //todo remove this thought this was already done
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;

    }

    public static void destroyInstance() {
        INSTANCE = null;
    }



}

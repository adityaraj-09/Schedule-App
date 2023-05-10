package com.example.timble.Util;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.timble.data.ScheduleDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Schedule.class},version = 1)
public abstract class ScheduleDatabase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS=4;
    public static final String  DATABASE_NAME="Schedule db";
    private static  volatile ScheduleDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback dbCallback=
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(() ->{

                       ScheduleDao scheduleDao= INSTANCE.scheduleDao();
                        scheduleDao.deleteAll();
                    });
                }
            };
    public static ScheduleDatabase geDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (ScheduleDatabase.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ScheduleDatabase.class,DATABASE_NAME)
                            .allowMainThreadQueries()
                            .addCallback(dbCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract ScheduleDao scheduleDao();
}

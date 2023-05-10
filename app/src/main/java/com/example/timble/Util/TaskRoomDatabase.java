package com.example.timble.Util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.timble.Task;
import com.example.timble.data.TaskDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class},version = 2)
@TypeConverters({Converter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS=4;
    public static final String  DATABASE_NAME="timbe db";
    private static  volatile TaskRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback dbCallback=
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(() ->{

                        TaskDao taskDao= INSTANCE.taskDao();
                        taskDao.deleteAll();
                    });
                }
            };
    public static TaskRoomDatabase geDatabase(final Context context){
       if(INSTANCE==null){
           synchronized (TaskRoomDatabase.class){
               if (INSTANCE==null){
                   INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                           TaskRoomDatabase.class,DATABASE_NAME)
                           .addCallback(dbCallback)
                           .fallbackToDestructiveMigration()
                           .build();
               }
           }
       }
       return INSTANCE;
    }
    public abstract TaskDao taskDao();
}

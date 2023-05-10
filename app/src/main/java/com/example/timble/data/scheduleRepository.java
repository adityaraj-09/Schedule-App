package com.example.timble.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.timble.Util.Schedule;
import com.example.timble.Util.ScheduleDatabase;

import java.util.List;


public class scheduleRepository {
    private final ScheduleDao scheduleDao;
    private final LiveData<List<Schedule>> allschedules ;

    public scheduleRepository(Application application) {

        ScheduleDatabase database=ScheduleDatabase.geDatabase(application);
        scheduleDao =database.scheduleDao();
        allschedules = scheduleDao.getschs();

    }
    public void insert(Schedule schedule){
       ScheduleDatabase.databaseWriterExecutor.execute(() -> scheduleDao.insertSchedule(schedule));
    }
    public LiveData<List<Schedule>> getAllschedules(){
        return allschedules;
    }
    public Schedule get(String day){
        return scheduleDao.get(day);
    }
    public LiveData<Schedule> getsh(String day){return scheduleDao.getsh(day);}
    public void deleteAll(){
        ScheduleDatabase.databaseWriterExecutor.execute(scheduleDao::deleteAll);
    }
    public void update(Schedule schedule){
        ScheduleDatabase.databaseWriterExecutor.execute(()-> scheduleDao.update(schedule));
    }
    public void delete(String day){
        ScheduleDatabase.databaseWriterExecutor.execute(()->scheduleDao.delete(day));
    }
}

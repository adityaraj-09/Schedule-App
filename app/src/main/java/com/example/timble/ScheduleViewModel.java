package com.example.timble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.timble.Util.Schedule;
import com.example.timble.data.ScheduleDao;
import com.example.timble.data.scheduleRepository;

import java.util.List;


public class ScheduleViewModel extends AndroidViewModel {
    public static scheduleRepository repository ;
    private final LiveData<List<Schedule>> allschedules ;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        repository=new scheduleRepository(application);
        allschedules = repository.getAllschedules();
    }
    public LiveData<List<Schedule>> getAllschedules(){return  allschedules;};
    public LiveData<Schedule> getsh(String day){return repository.getsh(day);}
    public static void insert(Schedule schedule){repository.insert(schedule);}
    public Schedule get(String day){return repository.get(day);}
    public static void update(Schedule schedule){repository.update(schedule);}
    public static void deleteAll(){repository.deleteAll();}
    public static void delete(String day){repository.delete(day);}
}

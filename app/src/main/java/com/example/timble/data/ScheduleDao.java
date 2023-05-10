package com.example.timble.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.timble.Task;
import com.example.timble.Util.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSchedule(Schedule schedule);

    @Query("DELETE FROM scheduletable")
    void deleteAll();


    @Query("SELECT * FROM scheduletable ")
    LiveData<List<Schedule>> getschs();

    @Query("SELECT * FROM scheduletable WHERE scheduletable.Day==:day")
    Schedule get(String day);

    @Query("SELECT * FROM scheduletable WHERE Day==:day")
    LiveData<Schedule> getsh(String day);

    @Update
    void update(Schedule schedule);

    @Query("DELETE FROM scheduletable WHERE Day==:day ")
    void delete(String day);
}

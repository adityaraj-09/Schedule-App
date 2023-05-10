package com.example.timble;




import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.util.Date;

@Entity(tableName = "task_table")
public class Task {
    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true)
    public long taskid;

    public String task;
    public Priority priority;

    @ColumnInfo (name = "due_date")
    public Date duedate;

    @ColumnInfo(name = "created_at")
    public Date dateCreated;

    @ColumnInfo(name = "is_done")
    public boolean isDone;

    @ColumnInfo(name = "alarm_time")
    public String tt;

    public Task(String task, Priority priority, Date duedate, Date dateCreated, boolean isDone,String tt) {
        this.task = task;
        this.priority = priority;
        this.duedate = duedate;
        this.dateCreated = dateCreated;
        this.isDone = isDone;
        this.tt=tt;
    }

    public Task(String task, Priority high, Date time, boolean isDone, String tt) {
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskid=" + taskid +
                ", task='" + task + '\'' +
                ", priority=" + priority +
                ", duedate=" + duedate +
                ", dateCreated=" + dateCreated +
                ", isDone=" + isDone +
                '}';
    }
}

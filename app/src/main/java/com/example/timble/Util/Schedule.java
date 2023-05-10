package com.example.timble.Util;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Scheduletable")
public class Schedule {

    @ColumnInfo(name = "Day")
    @PrimaryKey
    @NonNull
    public String day;

    @ColumnInfo(name = "lec_sch")
    public String lec;

    @ColumnInfo(name = "lab_sch")
    public String lab;

    @ColumnInfo(name = "tut_sch")
    public String tut;

    public Schedule(String day, String lec, String lab, String tut) {
        this.day = day;
        this.lec = lec;
        this.lab = lab;
        this.tut = tut;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLec() {
        return lec;
    }

    public void setLec(String lec) {
        this.lec = lec;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getTut() {
        return tut;
    }

    public void setTut(String tut) {
        this.tut = tut;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "day='" + day + '\'' +
                ", lec='" + lec + '\'' +
                ", lab='" + lab + '\'' +
                ", tut='" + tut + '\'' +
                '}';
    }
}

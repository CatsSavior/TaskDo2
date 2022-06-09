package com.example.android.taskdo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    private static final String TAG = "Task";


    @PrimaryKey(autoGenerate = true)
    private Long ID;

    @ColumnInfo(name = "task_name")
    private String name;

    @ColumnInfo(name = "task_status")
    private Boolean taskStatus;
    @ColumnInfo(name = "hour")
    private int hour;
    @ColumnInfo(name = "minute")
    private int minute;

    @ColumnInfo(name = "day")
    private int day;


    public Task(String name, Boolean taskStatus, int hour, int minute, int day) {
        this.name = name;
        this.taskStatus = taskStatus;
        this.hour = hour;
        this.minute = minute;
        this.day = day;
    }



    public String getName() {
        return name;
    }
    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public Long getID() {
        return ID;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getDay() {
        return this.day;
    }


    public void setID(@NonNull Long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskHour(int hour) {
        if (hour > 0 && hour < 24)
            this.hour = hour;
        else {
            Log.d(TAG, "Hour is not valid.");
        }
    }

    public void setTaskMinute(int minute) {
        if (minute >= 0 && minute <= 60)
            this.minute = minute;
        else {
            Log.d(TAG, "Minute is not valid.");
        }
    }

    public void setDay(int day) {
        if (day < 7 && day >= 0)
            this.day = day;
    }


    public void changeTaskStatus() {
        if (taskStatus == false)
            taskStatus = true;
        else
            taskStatus = false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task Name: " + name + "\nTask day: " + day;
    }
}



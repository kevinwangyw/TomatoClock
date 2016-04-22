package com.kevinwang.tomatoClock;

import java.util.Date;

/**
 * Created by lenovo on 2016/4/22.
 */
public class TimeRecord {
    private static Date taskStartTime;
    private static Date taskEndTime;

    public static Date getTaskEndTime() {
        return taskEndTime;
    }

    public static void setTaskEndTime(Date taskEndTime) {
        TimeRecord.taskEndTime = taskEndTime;
    }

    public static Date getTaskStartTime() {
        return taskStartTime;
    }

    public static void setTaskStartTime(Date taskStartTime) {
        TimeRecord.taskStartTime = taskStartTime;
    }
}

package com.kevinwang.tomatoClock.history;

/**
 * Created by lenovo on 2016/4/22.
 */
public class History {
    private String startTime;
    private String endTiem;
    private String taskContent;
    private int id;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTiem() {
        return endTiem;
    }

    public void setEndTiem(String endTiem) {
        this.endTiem = endTiem;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
// id date start end content
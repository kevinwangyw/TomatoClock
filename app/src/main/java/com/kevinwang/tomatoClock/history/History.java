package com.kevinwang.tomatoClock.history;

/**
 * Created by lenovo on 2016/4/22.
 */
public class History {
    private String date;
    private long listHeaderPosition;
    private String startTime;
    private String endTime;
    private String content;
    private int id;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getListHeaderPosition() {
        return listHeaderPosition;
    }

    public void setListHeaderPosition(long listHeaderPosition) {
        this.listHeaderPosition = listHeaderPosition;
    }
}
// id date start end content
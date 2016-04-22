package com.kevinwang.tomatoClock.history;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/22.
 */
public class HistoryOfDay {
    private ArrayList<History> historyList;
    private String dateStr;

    public HistoryOfDay(String dateStr) {
        this.dateStr = dateStr;
        historyList = new ArrayList<History>();
    }

    public String getDateStr() {
        return dateStr;
    }

    public void addHistory(History history) {
        historyList.add(history);
    }

    public void deleteHistory(History history) {
        historyList.remove(history);
    }
}

package com.kevinwang.tomatoClock.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kevinwang.tomatoClock.TimeRecord;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/23.
 */
public class DAO {
    private SQLiteDatabase db;
    private HistorySQLiteHelper dbHelper;

    public DAO(Context context) {
        dbHelper = HistorySQLiteHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public void reopen() {
        db = dbHelper.getWritableDatabase();
    }

    //Close the db
    public void close() {
        db.close();
    }

    public void createHistory (String content) {
        ContentValues cv = new ContentValues();
        String dateStr = DateFormat.getDateInstance().format(TimeRecord.getTaskStartTime());
        String startTime = DateFormat.getTimeInstance().format(TimeRecord.getTaskStartTime());
        String endTme = DateFormat.getTimeInstance().format(TimeRecord.getTaskEndTime());
        System.out.println("createHistory (String content) :    date   startTime    endTime");
        System.out.println("                                   " + dateStr + "  " + startTime + "  " + endTme);
        cv.put("date", dateStr);
        cv.put("start_time", startTime);
        cv.put("end_time", endTme);
        cv.put("content", content);
        db.insert("taskHistory", null, cv);
    }

    public void deleteHistory (int _id) {
        String whereClause = "_id = " + _id;
        String[] whereArgs = null;
        db.delete("taskHistory", whereClause, whereArgs);
    }

    public ArrayList<History> getHistory () {
        ArrayList<History> list = new ArrayList<History>();

        Boolean dintinct = true;
        String[] args = {"date"};
        long dateCount = (db.query(dintinct, "taskHistory", args, null, null, null, null, null, null)).getCount();
        long headerPosition = dateCount;
        System.out.println("DAO-------> 不相同的日期数 ： " + dateCount);
        Cursor cursor = db.query("taskHistory", null, null, null, null, null, null);

        if (cursor.moveToLast()) {

            String tempDate = cursor.getString(1);

            for (int i = cursor.getCount() - 1; i >= 0; i--) {
                if (cursor.getString(1).compareTo(tempDate) != 0) {
                    dateCount--;
                    tempDate = cursor.getString(1);
                }
                History history = initHistory(cursor, (headerPosition - dateCount));
                list.add(history);
                cursor.moveToPrevious();
            }
        }

        return list;
    }

    private History initHistory(Cursor cursor, long headerPosition) {
        History history = new History();
        history.setListHeaderPosition(headerPosition);
        history.setId(cursor.getInt(0));
        history.setDate(cursor.getString(1));
        history.setStartTime(cursor.getString(2));
        history.setEndTime(cursor.getString(3));
        history.setContent(cursor.getString(4));
        return history;
    }
}

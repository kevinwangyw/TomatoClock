package com.kevinwang.tomatoClock.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/4/22.
 */
public class HistorySQLiteHelper extends SQLiteOpenHelper{

    public HistorySQLiteHelper(Context context) {
        super(context, "history_db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE taskHistory " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, start_time TEXT NOT NULL, " +
                "end_time TEXT NOT NULL, content TEXT NOT NULL);"); //id 在不显式添加的情况下回自动增
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROP table
        db.execSQL("DROP TABLE IF EXISTS todos");
        // Recreate table
        onCreate(db);
    }
}

package com.kevinwang.tomatoClock;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/20.
 */
public class TaskLab {
    private ArrayList<Task> mTasks;
    private static TaskLab sTaskLab;
    private Context mAppContext;
    private static final String TAG = "taskLab";
    private static final String FILENAME = "tasks.json";
    private TaskJSONSerializer mSerializer;

    //使用Context参数，单例可完成启动activity、获取项目资源、查找应用的私有存储空间等任务
    private TaskLab (Context appContext) {
        mAppContext = appContext;
        mSerializer = new TaskJSONSerializer(mAppContext, FILENAME);
        try {
            mTasks = mSerializer.loadTasks();
        }catch (Exception e) {
            mTasks = new ArrayList<Task>();
        }
    }

    public static TaskLab get(Context c) {
        if (sTaskLab == null) {
            sTaskLab = new TaskLab(c.getApplicationContext());
        }
        return  sTaskLab;
    }

    public ArrayList<Task> getTasks() {
        return mTasks;
    }

    public void addTask(Task task) {
        mTasks.add(task);
        System.out.println("当前任务数：" + mTasks.size());
    }

    public void addTask(int position, Task task) {
        mTasks.add(position, task);
        System.out.println("当前任务数：" + mTasks.size());
    }

    public boolean saveTasks() {
        try {
            mSerializer.saveTasks(mTasks);
            System.out.println("Tasks saved to file");
            Log.d(TAG, "Tasks saved to file");
            return true;
        }catch (Exception e) {
            System.out.println("Error saving Tasks:");
            Log.e(TAG,"Error saving Tasks:", e);
            return false;
        }
    }

    public void deleteTask(Task task) {
        mTasks.remove(task);
    }
}

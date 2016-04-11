package com.kevinwang.tomatoClock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lenovo on 2016/4/7.
 */
public class TaskListFragment extends android.support.v4.app.ListFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list, container, false);
        return v;
    }
}

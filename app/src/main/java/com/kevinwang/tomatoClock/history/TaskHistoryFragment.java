package com.kevinwang.tomatoClock.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevinwang.tomatoClock.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/4/7.
 */
public class TaskHistoryFragment extends Fragment {

    private ArrayList<HistoryOfDay> historyLab;
    private HistoryDAO historyDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        historyDAO = new HistoryDAO(getActivity());
        historyLab = historyDAO.getHistory();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_list, container, false);
        return v;
    }
}

package com.kevinwang.tomatoClock.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevinwang.tomatoClock.R;

/**
 * Created by lenovo on 2016/4/24.
 */
public class AboutAuthorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about_author, container, false);
        return view;
    }
}

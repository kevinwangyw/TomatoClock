package com.kevinwang.tomatoClock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by lenovo on 2016/4/15.
 */
public class CircleTimeActivity extends AppCompatActivity {
    private static final String fragmentTag = "circle_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_time);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_circle_time);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.circle_time_fragment_container,CircleTimeFragment.newInstance(savedInstanceState), fragmentTag).commit();

    }
}

package com.kevinwang.tomatoClock.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kevinwang.tomatoClock.R;

/**
 * Created by lenovo on 2016/4/24.
 */
public class AboutAuthorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_not_main_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_not_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("关于作者");

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.not_main_fragment_container, new AboutAuthorFragment()).commit();
    }
}

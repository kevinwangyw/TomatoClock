package com.kevinwang.tomatoClock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lenovo on 2016/4/7.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected Fragment fragment;
    protected FragmentManager fragmentManager;

    protected abstract android.support.v4.app.Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        fragmentManager = getSupportFragmentManager();
        /*android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }*/
    }
}

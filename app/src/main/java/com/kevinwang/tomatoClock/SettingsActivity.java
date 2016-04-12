package com.kevinwang.tomatoClock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by lenovo on 2016/4/9.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting_container);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_setting_root);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
/*        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.setting_fragment_container,SettingFragment.newInstance(savedInstanceState)).commit();
    }

}

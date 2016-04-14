package com.kevinwang.tomatoClock;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends AppCompatActivity {
    private Boolean working_state = false; //false代表没在进行番茄计时，true代表正在进行番茄计时
    private TextView clock_toolbar_text;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private MyCountDown countDownClock;
    private long tomatoTimeSpan;
    private long restTimeSpan;
    private long longRestTimeSpan;
    private static int tomatoCount;
    private SharedPreferences sharedPreferences;
    private static final String KEY_RINGTONE_USING_STATE = "ringtone_using_state";
    private static final String KEY_RINGTONE_SETTING = "ringtone_setting";
    private static final String KEY_IS_VIBRATE = "is_vibrate";
    private static final String KEY_IS_LONG_VIBRATE = "is_long_vibrate";
    private static final String KEY_TOMATO_LENGTH = "tomato_length";
    private static final String KEY_REST_LENGTH = "rest_length";
    private static final String KEY_LONG_REST_LENGTH = "long_rest_length";
    private static final String KEY_COUNT_INTERVAL = "long_rest_interval_count";

    public static Boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        active = true;

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.fragmentPager);

        viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));

        TabPageIndicator tabPageIndicator = (TabPageIndicator) findViewById(R.id.tabsTitle);
        tabPageIndicator.setViewPager(viewPager);

        /* Inside the activity */
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Get access to the custom title view
        clock_toolbar_text = (TextView) toolbar.findViewById(R.id.clock_toolbar_text);

        init(sharedPreferences);

        clock_toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void init(SharedPreferences sharedPreferences) {
        if (sharedPreferences.getInt(KEY_TOMATO_LENGTH, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_TOMATO_LENGTH, 25).commit();
        }
        if (sharedPreferences.getInt(KEY_COUNT_INTERVAL, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_COUNT_INTERVAL, 4).commit();
        }
        if (sharedPreferences.getInt(KEY_LONG_REST_LENGTH, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_LONG_REST_LENGTH, 15).commit();
        }
        if (sharedPreferences.getInt(KEY_REST_LENGTH, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_REST_LENGTH, 5).commit();
        }
        countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000, 1000);
        countDownClock.setContext(this);
        countDownClock.setTextView(clock_toolbar_text);
        clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
    }

    private void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                View menuItemView = findViewById(R.id.menu_settings);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "偏好设置");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 1:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            case R.id.menu_work:
                final MenuItem workItem = item;
                if (!working_state) {
                    working_state = true;
                    countDownClock.start();
                    //Toast.makeText(getApplicationContext(),"change working state to " + working_state.toString(),Toast.LENGTH_SHORT).show();
                    item.setIcon(R.mipmap.ic_action_cancel);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    AlertDialog alertDialog = alertDialogBuilder.setTitle("放弃番茄").setMessage("确定要放弃这个番茄时间吗？")
                            .setPositiveButton("放弃番茄", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    working_state = false;
                                    workItem.setIcon(R.mipmap.ic_action_playback_play);
                                    //Toast.makeText(getApplicationContext(), "change working state to " + working_state.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("取消",null).create();
                    alertDialog.show();
                }
                if (!working_state) {
                    item.setIcon(R.mipmap.ic_action_playback_play);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        if (!working_state) {
            clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
        tomatoCount = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}

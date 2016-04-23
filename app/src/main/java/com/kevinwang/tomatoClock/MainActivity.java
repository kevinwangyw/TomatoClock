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

import com.kevinwang.tomatoClock.history.TaskPostActivity;
import com.kevinwang.tomatoClock.setting.SettingsActivity;
import com.viewpagerindicator.TabPageIndicator;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView clock_toolbar_text;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private MyCountDown countDownClock;
    private long tomatoTimeSpan;
    private long restTimeSpan;
    private long longRestTimeSpan;
    private SharedPreferences sharedPreferences;

    private static Menu menu;

    private static final String KEY_RINGTONE_USING_STATE = "ringtone_using_state";
    private static final String KEY_RINGTONE_SETTING = "ringtone_setting";
    private static final String KEY_IS_VIBRATE = "is_vibrate";
    private static final String KEY_IS_LONG_VIBRATE = "is_long_vibrate";
    private static final String KEY_TOMATO_LENGTH = "tomato_length";
    private static final String KEY_REST_LENGTH = "rest_length";
    private static final String KEY_LONG_REST_LENGTH = "long_rest_length";
    private static final String KEY_COUNT_INTERVAL = "long_rest_interval_count";
    private static final String STATE = "state";
    private static final String APP_CLOSE_TIME = "app_close_time";
    private static final String LEFT_TIME_WHEN_CLOSE = "left_time_when_close";
    private static Boolean active = false;
    private static int state = 0;
    private static Boolean justStart = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        System.out.println("MainActivity---->onCreate()");
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
        state = sharedPreferences.getInt(STATE, 0);
        // Get access to the custom title view
        clock_toolbar_text = (TextView) toolbar.findViewById(R.id.clock_toolbar_text);

        init(sharedPreferences);

        clock_toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CircleTimeActivity.class);
                startActivity(i);
            }
        });

        System.out.println("justStart : " + justStart);

    }

    private void init(SharedPreferences sharedPreferences) {
        //测试用
        if (sharedPreferences.getInt(KEY_TOMATO_LENGTH, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_TOMATO_LENGTH, 1).commit();
        }
        if (sharedPreferences.getInt(KEY_COUNT_INTERVAL, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_COUNT_INTERVAL, 2).commit();
        }
        if (sharedPreferences.getInt(KEY_LONG_REST_LENGTH, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_LONG_REST_LENGTH, 1).commit();
        }
        if (sharedPreferences.getInt(KEY_REST_LENGTH, -1) == -1) {
            sharedPreferences.edit().putInt(KEY_REST_LENGTH, 1).commit();
        }

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
        System.out.println("MainActivity---->onCreateOptionsMenu(Menu menu)");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        switch (state % 4) {
            case 0:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_playback_play);
                break;
            case 1:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_cancel);
                break;
            case 2:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_tick);
                break;
            case 3:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_cancel);
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        System.out.println("MainActivity----> onPrepareOptionsMenu(Menu menu)");
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        System.out.println("MainActivity----> invalidateOptionsMenu()");
        switch (state % 4) {
            case 2:
                System.out.println("state : " + state);
                clock_toolbar_text.setText("已完成番茄时间");
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_tick);
                break;
            case 3:
                System.out.println("state : " + state);
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_playback_play);
                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);  //测试
                clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                countDownClock.setContext(this);
                countDownClock.setTextView(clock_toolbar_text);
                if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 1)) {
                    state = 0;
                }
                else {
                    state++;
                }
                break;
        }
        super.invalidateOptionsMenu();
    }

    public static Boolean getJustStart() {
        return justStart;
    }

    public static void setJustStart(Boolean justStart) {
        MainActivity.justStart = justStart;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("MainActivity----> onOptionsItemSelected(MenuItem item)");
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
                switch (state % 4) {
                    //到达长休息时间前有count * 4 - 1 - 1 个状态变化
                    //当state == count * 4 - 1 - 1的时候，休息时间设置为长休息时间
                    case 0:  //0:代表处在可以开始番茄时间状态
                        System.out.println("click case 0");
                        TimeRecord.setTaskStartTime(new Date());
                        state++;
                        System.out.println("开始番茄时间, state :" + state);
                        item.setIcon(R.mipmap.ic_action_cancel);
                        countDownClock.start();
                        //Toast.makeText(getApplicationContext(),"change working state to " + working_state.toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case 1: //1：代表处在番茄时间内
                        System.out.println("click case 1");
                        System.out.println("处于番茄时间中, state :" + state);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        AlertDialog alertDialog = alertDialogBuilder.setTitle("放弃番茄").setMessage("确定要放弃这个番茄时间吗？")
                                .setPositiveButton("放弃番茄", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.out.println("放弃番茄时间，state：" + state);
                                        state--;
                                        workItem.setIcon(R.mipmap.ic_action_playback_play);
                                        countDownClock.cancel();
                                        clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                                        //Toast.makeText(getApplicationContext(), "change working state to " + working_state.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("取消",null).create();
                        alertDialog.show();
                        break;
                    case 2:  //2：代表显示番茄时间完成，标志为钩
                        System.out.println("click case 2");
                        System.out.println("提交番茄时间，state：" + state);

                        if (justStart) {
/*                            justStart = false;
                            state = 0;
                            clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");*/
/*                            countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);
                            countDownClock.setContext(this);
                            countDownClock.setTextView(clock_toolbar_text);*/
                            //item.setIcon(R.mipmap.ic_action_playback_play);
                        }
                        else {
                            //item.setIcon(R.mipmap.ic_action_cancel);
                            if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 2)) {
                                //测试
                                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_LONG_REST_LENGTH, 15)  * 60 * 1000 / 4, 1000);
                                countDownClock.setContext(this);
                                countDownClock.setTextView(clock_toolbar_text);
                                //System.out.println("开始长休息时间, state: " + state);
                            }
                            else {
                                //System.out.println("开始短休息时间, state: " + state);
                                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_REST_LENGTH, 5)  * 60 * 1000 / 5, 1000);  //测试
                                countDownClock.setContext(this);
                                countDownClock.setTextView(clock_toolbar_text);
                            }

/*                            state++;
                            countDownClock.start();*/
                        }
                        Intent intent = new Intent(this, TaskPostActivity.class);
                        intent.putExtra("lastActivity", 0);
                        startActivity(intent);
                        break;
                    case 3:  //3：代表处在休息时间
                        System.out.println("click case 3");
                        countDownClock.cancel();
                        clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                        countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);  //测试
                        countDownClock.setContext(this);
                        countDownClock.setTextView(clock_toolbar_text);
                        if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 1)) {
                            state = 0;
                        }
                        else {
                            state++;
                        }
                        System.out.println("点击直接结束休息时间，state：" + state);
                        item.setIcon(R.mipmap.ic_action_playback_play);
                        break;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //setupUI(findViewById(R.id.main_layout_parent));
        System.out.println("MainActivity---->onResume()");

        switch (state % 4) {
            case 0:
                if (justStart) {
                    justStart = false;
                    state = 0;
                }
                else if (state == sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4) {
                    state = 0;
                }
                //测试，时间除以2, 30秒
                clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);
                countDownClock.setTextView(clock_toolbar_text);
                countDownClock.setContext(this);
                break;
            case 1:
                if (justStart) {
                    justStart = false;
                    state = 0;
                    clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                    countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);
                }else {
                    countDownClock = MyCountDown.getInstance();
                }
                countDownClock.setTextView(clock_toolbar_text);
                countDownClock.setContext(this);
                break;
            case 2:
                clock_toolbar_text.setText("已完成番茄时间");
                if (justStart) {
                    countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);
                    countDownClock.setContext(this);
                    countDownClock.setTextView(clock_toolbar_text);
                    TimeRecord.setTaskStartTime(new Date(sharedPreferences.getLong("start", new Date().getTime())));
                    TimeRecord.setTaskEndTime(new Date(sharedPreferences.getLong("end", new Date().getTime())));
                }
                break;
            case 3:
                if (justStart) {
                    justStart = false;
                    state = 0;
                    clock_toolbar_text.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                    countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);
                }else {
                    countDownClock = MyCountDown.getInstance();
                }
                countDownClock.setTextView(clock_toolbar_text);
                countDownClock.setContext(this);
                break;
        }
        active = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity---->onDestory()");
        sharedPreferences.edit().putInt(STATE, state).commit();
        active = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("MainActivity----->onPause()");
        sharedPreferences.edit().putInt(STATE, state).commit();
        active = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MainActivity---->onStart()");
        active = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("MainActivity---->onStop()");
        sharedPreferences.edit().putInt(STATE, state).commit();
        if (state == 2) {
            sharedPreferences.edit().putLong("start", TimeRecord.getTaskStartTime().getTime());
            sharedPreferences.edit().putLong("end", TimeRecord.getTaskEndTime().getTime());
        }
        active = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("MainActivity---->onRestart()");
        switch (state % 4) {
            case 0:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_playback_play);
                break;
            case 1:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_cancel);
                break;
            case 2:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_tick);
                break;
            case 3:
                menu.findItem(R.id.menu_work).setIcon(R.mipmap.ic_action_cancel);
                break;
        }
    }

    public static void setState(int state) {
        MainActivity.state = state;

    }
    public static Boolean getActive() {
        return active;
    }

    public static int getState() {
        return state;
    }

    public static void setActive(Boolean active) {
        MainActivity.active = active;
    }

/*    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity
                .INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            System.out.println("点击非Edittext区域");
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }*/
}

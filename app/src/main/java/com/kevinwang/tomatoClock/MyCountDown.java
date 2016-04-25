package com.kevinwang.tomatoClock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/4/13.
 */
public class MyCountDown extends CountDownTimer {
    private MediaPlayer mediaPlayer;
    private static MyCountDown instance = null;
    private Context context;
    private TextView textView;
    private static long totalTime;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    protected MyCountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public static MyCountDown getInstance(long millisInFuture, long countDownInterval) {
/*        if (instance == null) {
            instance = new MyCountDown(millisInFuture, countDownInterval);
        }*/
        instance = new MyCountDown(millisInFuture, countDownInterval);
        totalTime = millisInFuture;
        return instance;
    }

    public static MyCountDown getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
        System.out.println("倒计时类------>setContext() and the context is: " + context.getClass().getName());
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String countDownTime = String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),

                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
        );

        if (MainActivity.getActive()) {
            showCountDownTime(countDownTime, millisUntilFinished);
        }
        else if (CircleTimeFragment.getActive()) {
            showCountDownTime(countDownTime, millisUntilFinished);
        }
        else {
            //System.out.println("onTick()");
        }

        NotificationAdmin notificationAdmin = NotificationAdmin.getNotification();
        Intent intent = new Intent(context, MainActivity.class);
        if (MainActivity.getState() % 4 == 1) {
            notificationAdmin.showNotification(intent, R.mipmap.logo, "工作中", countDownTime);
        }
        if (MainActivity.getState() % 4 == 3) {
            notificationAdmin.showNotification(intent, R.mipmap.logo, "休息中", countDownTime);
        }
    }

    private void showCountDownTime (String countDownTime, long millisUntilFinished) {
        //System.out.println("倒计时类------>onTick() and the context is: " + context.getClass().getName());
        textView.setText(countDownTime);
        if (CircleTimeFragment.getActive()) {
            CircleTimeFragment fragment = (CircleTimeFragment) ((CircleTimeActivity)context)
                    .getSupportFragmentManager().findFragmentByTag("circle_fragment");
            fragment.setCircleProgress(millisUntilFinished, totalTime);
        }
    }


    @Override
    public void onFinish() {
/*        Menu menu = newMenuInstance(context);
        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.menu_main, menu);*/
        SharedPreferences sharedPreferences = context.getSharedPreferences((context.getPackageName() + "_preferences"), Context.MODE_PRIVATE);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        NotificationAdmin notificationAdmin = NotificationAdmin.getNotification();
        Intent intent = new Intent(context, MainActivity.class);
        int state = MainActivity.getState();
        System.out.println("state : " + state);
        if (MainActivity.getActive()) {
            switch (state % 4) {
                case 1:
                    MainActivity.setState(++state);
                    System.out.println("番茄倒计时完成，state：" + state);
                    //((MainActivity)context).onPrepareOptionsMenu(menu);
                    ((MainActivity)context).invalidateOptionsMenu();
                    TimeRecord.setTaskEndTime(new Date());
                    notificationAdmin.showNotification(intent, R.mipmap.logo, "完成番茄", "请提交任务");
                    break;
                case 3:
                    notificationAdmin.showNotification(intent, R.mipmap.logo, "休息结束", "");
                    //((MainActivity)context).onPrepareOptionsMenu(menu);
                    ((MainActivity)context).invalidateOptionsMenu();
                    System.out.println("休息倒计时完成，state：" + MainActivity.getState());
                    break;
            }
        }
        else if (CircleTimeFragment.getActive()) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.fragment_circle_time, null);
            CircleTimeFragment fragment = (CircleTimeFragment) ((CircleTimeActivity)context)
                    .getSupportFragmentManager().findFragmentByTag("circle_fragment");
            fragment.setCircleProgress(0, totalTime);
            switch (state % 4) {
                case 1:
                    TimeRecord.setTaskEndTime(new Date());
                    MainActivity.setState(++state);
                    System.out.println("番茄倒计时完成，state：" + state);
                    fragment.updateViewForOnFinish();
                    textView.setText("已完成番茄时间");
                    notificationAdmin.showNotification(intent, R.mipmap.logo, "完成番茄", "请提交任务");
                    ((ImageView)view.findViewById(R.id.worrking_state)).setImageResource(R.drawable.ic_action_tick);
                    break;
                case 3:
                    notificationAdmin.showNotification(intent, R.mipmap.logo, "休息结束", "");
                    System.out.println("休息倒计时完成，state：" + MainActivity.getState());
                    fragment.updateViewForOnFinish();
                    break;
            }
        }
        else {
            switch (state % 4) {
                case 1:
                    TimeRecord.setTaskEndTime(new Date());
                    MainActivity.setState(++state);
                    System.out.println("番茄倒计时完成，state：" + state);
                    notificationAdmin.showNotification(intent, R.mipmap.logo, "完成番茄", "请提交任务");
                    //从home启动某一个activity
                    /*Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);*/
                    sharedPreferences.edit().putInt("state", MainActivity.getState()).commit();
                    break;
                case 3:
                    notificationAdmin.showNotification(intent, R.mipmap.logo, "休息结束", "");
                    System.out.println("休息倒计时完成，state：" + MainActivity.getState());
                    MainActivity.setState(++state);
                    break;
            }
        }
        if (sharedPreferences.getBoolean("is_long_vibrate", false))
        {
            // The following numbers represent millisecond lengths
            int dash = 500;     // Length of a Morse Code "dash" in milliseconds
            int short_gap = 250;    // Length of Gap Between dots/dashes
            long[] pattern = {
                    0,  // Start immediately
                    dash, short_gap, dash, short_gap, dash,
                    short_gap, dash, short_gap, dash,short_gap,
                    dash, short_gap, dash
            };
            // Only perform this pattern one time (-1 means "do not repeat")
            vibrator.vibrate(pattern, -1);
        }
        else if (sharedPreferences.getBoolean("is_vibrate", false)) {
            vibrator.vibrate(1000);
        }
        if (sharedPreferences.getBoolean("ringtone_using_state", false)) {
            String alarms = sharedPreferences.getString("ringtone_setting", "");
            Uri uri = Uri.parse(alarms);
            playNotification(context, uri);
        }

        //System.out.println("test if it is executed at the same time with or after vibrate");
    }

    private void playNotification (Context context, Uri alert) {
        long ringtoneDelay = 3500;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mediaPlayer.stop();
            }
        };
        Timer timer = new Timer();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        //activate a task after a specific delay
        timer.schedule(timerTask, ringtoneDelay);
    }

/*    protected Menu newMenuInstance(Context context) {
        try {
            Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");

            Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);

            return (Menu) constructor.newInstance(context);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }*/
}
//┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃ 　
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//┃　　　┃  神兽保佑　　　　　　　　
//┃　　　┃  一遍通过！
//┃　　　┗━━━┓
//┃　　　　　　　┣┓
//┃　　　　　　　┏┛
//┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛
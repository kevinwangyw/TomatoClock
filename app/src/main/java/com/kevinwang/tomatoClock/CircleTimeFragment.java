package com.kevinwang.tomatoClock;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 2016/4/15.
 */
public class CircleTimeFragment extends Fragment implements View.OnClickListener{

    private static Boolean active =false;
    private MyCountDown countDownClock;
    private TextView textView;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private CircleProgressBar circleProgressBar;

    private static final String KEY_RINGTONE_USING_STATE = "ringtone_using_state";
    private static final String KEY_RINGTONE_SETTING = "ringtone_setting";
    private static final String KEY_IS_VIBRATE = "is_vibrate";
    private static final String KEY_IS_LONG_VIBRATE = "is_long_vibrate";
    private static final String KEY_TOMATO_LENGTH = "tomato_length";
    private static final String KEY_REST_LENGTH = "rest_length";
    private static final String KEY_LONG_REST_LENGTH = "long_rest_length";
    private static final String KEY_COUNT_INTERVAL = "long_rest_interval_count";

    public static Boolean getActive() {
        return active;
    }

    public static void setActive(Boolean active) {
        CircleTimeFragment.active = active;
    }

    public static CircleTimeFragment newInstance(Bundle bundle) {
        CircleTimeFragment circleTimeFragment = new CircleTimeFragment();
        circleTimeFragment.setArguments(bundle);
        return circleTimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("CircleTimeFragment----->onCreate()");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        System.out.println("CircleTimeFragment----->onCreateView()");
        View view = inflater.inflate(R.layout.fragment_circle_time, container, false);
        textView = (TextView) view.findViewById(R.id.time_text);
        imageView = (ImageView) view.findViewById(R.id.worrking_state);
        imageView.setOnClickListener(this);
        circleProgressBar = (CircleProgressBar) view.findViewById(R.id.circle_progress);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("CircleTimeFragment----->onResume()");
        countDownClock = MyCountDown.getInstance();
        countDownClock.setContext(getActivity());
        countDownClock.setTextView(textView);

        switch (MainActivity.getState() % 4) {
            case 0:
                if (MainActivity.getState() == sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4) {
                    MainActivity.setState(0);
                }
                circleProgressBar.setProgress(0);
                textView.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                //测试用
                imageView.setImageResource(R.drawable.ic_action_playback_play);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_action_cancel);
                break;
            case 2:
                circleProgressBar.setProgress(circleProgressBar.getMaxProgress());
                imageView.setImageResource(R.drawable.ic_action_tick);
                textView.setText("已完成番茄时间");
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_action_cancel);
        }
        active = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("CircleTimeFragment----->onPause()");
        active = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("CircleTimeFragment----->onStop()");
        active = false;
    }

    @Override
    public void onClick(View v) {
        System.out.println("CircleTimeFragment----->imageView.onClick()");
        final int state = MainActivity.getState();
        switch (state % 4) {
            //到达长休息时间前有count * 4 - 1 - 1 个状态变化
            //当state == count * 4 - 1 - 1的时候，休息时间设置为长休息时间
            case 0:  //0:代表处在可以开始番茄时间状态
                System.out.println("click case 0");

                MainActivity.setState(state + 1);
                circleProgressBar.setProgress(0);
                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);
                countDownClock.setContext(getActivity());
                countDownClock.setTextView(textView);
                System.out.println("开始番茄时间, state :" + state);
                imageView.setImageResource(R.drawable.ic_action_cancel);
                countDownClock.start();
                //Toast.makeText(getApplicationContext(),"change working state to " + working_state.toString(),Toast.LENGTH_SHORT).show();
                break;
            case 1: //1：代表处在番茄时间内
                System.out.println("click case 1");
                System.out.println("处于番茄时间中, state :" + state);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                AlertDialog alertDialog = alertDialogBuilder.setTitle("放弃番茄").setMessage("确定要放弃这个番茄时间吗？")
                        .setPositiveButton("放弃番茄", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("放弃番茄时间，state：" + state);
                                MainActivity.setState(state - 1);
                                imageView.setImageResource(R.drawable.ic_action_playback_play);
                                countDownClock.cancel();
                                textView.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                                circleProgressBar.setProgress(0);
                                //Toast.makeText(getApplicationContext(), "change working state to " + working_state.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消",null).create();
                alertDialog.show();
                break;
            case 2:  //2：代表显示番茄时间完成，标志为钩
                circleProgressBar.setProgress(0);
                System.out.println("click case 2");
                System.out.println("提交番茄时间，state：" + state);
                //imageView.setImageResource(R.drawable.ic_action_cancel);
                if (MainActivity.getJustStart()) {

                }
                else {
                    if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 2)) {
                        //测试
                       // MainActivity.setState(state + 1);
                        countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_LONG_REST_LENGTH, 15)  * 60 * 1000 / 4, 1000);
                        countDownClock.setContext(getActivity());
                        countDownClock.setTextView(textView);
                        //countDownClock.start();
                        //System.out.println("开始长休息时间, state: " + state);
                    }
                    else {
                       // MainActivity.setState(state + 1);
                        //System.out.println("开始短休息时间, state: " + state);
                        countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_REST_LENGTH, 5)  * 60 * 1000 / 5, 1000);  //测试
                        countDownClock.setContext(getActivity());
                        countDownClock.setTextView(textView);
                        //countDownClock.start();
                    }
                }
                Intent intent = new Intent(getActivity(), TaskPostActivity.class);
                intent.putExtra("lastActivity", 1);
                startActivity(intent);
                break;
            case 3:  //3：代表处在休息时间
                System.out.println("click case 3");
                countDownClock.cancel();
                circleProgressBar.setProgress(0);
                textView.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);  //测试
                countDownClock.setContext(getActivity());
                countDownClock.setTextView(textView);
                if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 1)) {
                    MainActivity.setState(0);
                }
                else {
                    MainActivity.setState(state + 1);
                }
                System.out.println("点击直接结束休息时间，state：" + state);
                imageView.setImageResource(R.drawable.ic_action_playback_play);
                break;
        }
    }

    public void updateViewForOnFinish() {
        int state = MainActivity.getState();
        switch (state % 4) {
            case 2:
                textView.setText("已完成番茄时间");
                imageView.setImageResource(R.drawable.ic_action_tick);
                System.out.println("工作倒计时结束，此时的progress是："
                        + circleProgressBar.getProgress() + " , 是否等于maxProgress: "
                        + (circleProgressBar.getProgress()==circleProgressBar.getMaxProgress()));
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_action_playback_play);
                countDownClock = MyCountDown.getInstance(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2, 1000);  //测试
                textView.setText(sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) + " : 00");
                countDownClock.setContext(getActivity());
                countDownClock.setTextView(textView);
                System.out.println("休息倒计时结束，此时的progress是："
                        + circleProgressBar.getProgress() + " , 是否等于maxProgress: "
                        + (circleProgressBar.getProgress()==circleProgressBar.getMaxProgress()));
                circleProgressBar.setProgress(circleProgressBar.getMaxProgress());
                circleProgressBar.setProgress(0);
                if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 1)) {
                    state = 0;
                }
                else {
                    state++;
                }
                MainActivity.setState(state);
                break;
        }
    }

    public void setCircleProgress(long millisUntilFinished) {
        final int state = MainActivity.getState();
        int progress;
        long totalTime;
        switch (state % 4) {
            case 1:
                //测试
                totalTime = sharedPreferences.getInt(KEY_TOMATO_LENGTH, 25) * 60 * 1000 / 2 ;
                progress = (int)((circleProgressBar.getMaxProgress()) *(totalTime - millisUntilFinished)  / totalTime);
                circleProgressBar.setProgressNotInUiThread(progress);
                break;
            case 3:
                if (state == (sharedPreferences.getInt(KEY_COUNT_INTERVAL, 4) * 4 - 1)) {
                    //测试
                    totalTime = sharedPreferences.getInt(KEY_LONG_REST_LENGTH, 15)  * 60 * 1000 / 4;
                    progress = (int)((circleProgressBar.getMaxProgress()) *(totalTime - millisUntilFinished)  / totalTime);
                    System.out.println("长休息，progress = " + circleProgressBar.getProgress());
                    circleProgressBar.setProgressNotInUiThread(progress);
                }
                else {
                    totalTime = sharedPreferences.getInt(KEY_REST_LENGTH, 5)  * 60 * 1000 / 5;  //测试
                    progress = (int)((circleProgressBar.getMaxProgress()) *(totalTime - millisUntilFinished)  / totalTime);
                    System.out.println("短休息，progress = " + circleProgressBar.getProgress());
                    circleProgressBar.setProgressNotInUiThread(progress);
                }
                break;
        }

    }
}

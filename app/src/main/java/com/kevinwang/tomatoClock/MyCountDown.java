package com.kevinwang.tomatoClock;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/4/13.
 */
public class MyCountDown extends CountDownTimer {
    private static MyCountDown instance = null;
    private Context context;
    private TextView textView;
    private MenuItem menuItem;

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

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (MainActivity.getActive()) {
/*                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_fragment_container, null);
                final TextView textView = (TextView)view.findViewById(R.id.clock_toolbar_text);*/
            showCountDownTime(millisUntilFinished);
        }
        if (CircleTimeFragment.getActive()) {
            showCountDownTime(millisUntilFinished);
        }
    }

    private void showCountDownTime (long millisUntilFinished) {
        System.out.println("倒计时类------>onTick() and the context is: " + context.getClass().getName());
        textView.setText(String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),

                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
        ));
        if (CircleTimeFragment.getActive()) {
            CircleTimeFragment fragment = (CircleTimeFragment) ((CircleTimeActivity)context)
                    .getSupportFragmentManager().findFragmentByTag("circle_fragment");
            fragment.setCircleProgress(millisUntilFinished);
        }
    }


    @Override
    public void onFinish() {
/*        Menu menu = newMenuInstance(context);
        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.menu_main, menu);*/
        int state = MainActivity.getState();
        System.out.println("state : " + state);
        if (MainActivity.getActive()) {
            switch (state % 4) {
                case 1:
                    MainActivity.setState(++state);
                    System.out.println("番茄倒计时完成，state：" + state);
                    //((MainActivity)context).onPrepareOptionsMenu(menu);
                    ((MainActivity)context).invalidateOptionsMenu();

                    break;
                case 3:
                    //((MainActivity)context).onPrepareOptionsMenu(menu);
                    ((MainActivity)context).invalidateOptionsMenu();
                    System.out.println("休息倒计时完成，state：" + MainActivity.getState());
                    break;
            }
        }
        if (CircleTimeFragment.getActive()) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.fragment_circle_time, null);
            CircleTimeFragment fragment = (CircleTimeFragment) ((CircleTimeActivity)context)
                    .getSupportFragmentManager().findFragmentByTag("circle_fragment");
            fragment.setCircleProgress(0);
            switch (state % 4) {
                case 1:
                    MainActivity.setState(++state);
                    System.out.println("番茄倒计时完成，state：" + state);
                    fragment.updateView();
                    textView.setText("已完成番茄时间");
                    ((ImageView)view.findViewById(R.id.worrking_state)).setImageResource(R.drawable.ic_action_tick);
                    break;
                case 3:
                    System.out.println("休息倒计时完成，state：" + MainActivity.getState());
                    fragment.updateView();
                    break;
            }
        }

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
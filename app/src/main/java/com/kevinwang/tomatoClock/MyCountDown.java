package com.kevinwang.tomatoClock;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2016/4/13.
 */
public class MyCountDown extends CountDownTimer {
        private static MyCountDown instance = null;
        private Context context;
        private TextView textView;

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
            if (instance == null) {
                instance = new MyCountDown(millisInFuture, countDownInterval);
            }
            return instance;
        }

        public void setContext (Context context) {
            this.context = context;
        }

        public void setTextView(TextView textView) { this.textView = textView; }

        @Override
        public void onTick(long millisUntilFinished) {
            if (MainActivity.active) {
/*                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_fragment_container, null);
                final TextView textView = (TextView)view.findViewById(R.id.clock_toolbar_text);*/
                System.out.println("------> no changed " + textView.getText());
                textView.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),

                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                ));
                System.out.println("------> changed to the current countdown time " + textView.getText());
                System.out.println(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),

                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                        ));
            }
        }

        @Override
        public void onFinish() {

        }


}

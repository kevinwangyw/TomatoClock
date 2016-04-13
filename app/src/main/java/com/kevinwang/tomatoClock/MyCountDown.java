package com.kevinwang.tomatoClock;

import android.os.CountDownTimer;

/**
 * Created by lenovo on 2016/4/13.
 */
public class MyCountDown extends CountDownTimer {
        private static MyCountDown instance = null;

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

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

        }
}

package com.kevinwang.tomatoClock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

/**
 * Created by lenovo on 2016/4/24.
 */
public class NotificationAdmin {
    private static NotificationAdmin notificationAdmin;
    private static int NOTIFICATION_ID;
    private NotificationManager notificationManager;
    private Notification notification;

    private NotificationCompat.Builder notificationBuilder;
    private Context context;
    private int requestCode = (int) SystemClock.uptimeMillis();
    private static final int FLAG = PendingIntent.FLAG_UPDATE_CURRENT;

    public NotificationAdmin(Context context, int id) {
        this.context = context;
        NOTIFICATION_ID = id;
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(context);
    }

    public static NotificationAdmin getNotification (Context context, int id) {
        notificationAdmin = new NotificationAdmin(context, id);
        return notificationAdmin;
    }

    public static NotificationAdmin getNotification() {
        return notificationAdmin;
    }

    /**
     * 设置在顶部通知栏中的各种信息
     *
     * @param intent
     * @param smallIcon
     */
    private void setCompatBuilder(Intent intent, int smallIcon, String title, String msg) {
        // 如果当前Activity启动在前台，则不开启新的Activity。
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
        // 将Intent封装进PendingIntent中，点击通知的消息后，就会启动对应的程序
        PendingIntent pIntent = PendingIntent.getActivity(context, requestCode, intent, FLAG);

        notificationBuilder.setContentIntent(pIntent);// 该通知要启动的Intent

        notificationBuilder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        notificationBuilder.setContentTitle(title);// 设置通知中心的标题
        notificationBuilder.setContentText(msg);// 设置通知中心中的内容

        /*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
         * 不设置的话点击消息后也不清除，但可以滑动删除
         */
        //notificationBuilder.setAutoCancel(true);
        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
         * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
         */
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
         * Notification.DEFAULT_SOUND：系统默认铃声。
         * Notification.DEFAULT_VIBRATE：系统默认震动。
         * Notification.DEFAULT_LIGHTS：系统默认闪光。
         * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
         */
        //notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
    }

    public void showNotification (Intent intent, int smallIcon, String title, String msg) {
        setCompatBuilder(intent, smallIcon, title, msg);
        sent();
    }

    private void sent() {
        notification = notificationBuilder.build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}

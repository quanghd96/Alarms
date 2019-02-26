package com.practice.mcasey.myapplication.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.practice.mcasey.myapplication.AlarmList.AlarmListFragment;
import com.practice.mcasey.myapplication.R;
import com.practice.mcasey.myapplication.TriggerAlarm.TriggerAlarmFragment;

public class NotificationService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "4655";

    String description;
    String time;

    NotificationManager mNotificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        description = intent.getStringExtra(AlarmListFragment.NOTIFICATION_ALARM_DESC);
        time = intent.getStringExtra(AlarmListFragment.NOTIFICATION_ALARM_TIME);
        initNotify();
        createNotification();
        return START_NOT_STICKY;
    }

    private void initNotify(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void createNotification(){
        CharSequence channelName = "my_channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(notificationChannel);

            Intent intentTriggerAlarm = new Intent(this.getApplicationContext(), TriggerAlarmFragment.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentTriggerAlarm, 0);
            Notification notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(description).setContentText(time)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_check_primary)
                    .setAutoCancel(true).build();
            mNotificationManager.notify(0, notification);
        }
    }
}

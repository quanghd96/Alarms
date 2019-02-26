package com.practice.mcasey.myapplication.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.practice.mcasey.myapplication.AlarmList.AlarmListFragment;
import com.practice.mcasey.myapplication.Model.Alarm;
import com.practice.mcasey.myapplication.Model.AlarmSingleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmService extends Service {

    public static final String ALARM_TRIGGERED = "ALARM_TRIGGERED";
    public static final String NOTIFICATION_ALARM_TIME = "NOTIFICATION_ALARM_TIME";

    private List<Alarm> mAlarms = new ArrayList<>();
    private Intent mAlarmIntent;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initIntents();
        createAlarms(getApplicationContext());
        return START_NOT_STICKY;
    }

    private void initIntents(){
        if(mAlarmIntent == null)
            mAlarmIntent = new Intent(getApplicationContext(), AlarmListFragment.AlarmReceiver.class);
        if(mAlarmManager == null)
            mAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void getAlarms() {
        mAlarms = AlarmSingleton.getInstance(getApplicationContext()).getAlarms();
    }

    private void createAlarms(Context context){
        if(!mAlarms.isEmpty())
            mAlarms.clear();
        getAlarms();
        for(int i=0;i<mAlarms.size();i++){
            if(mAlarms.get(i).isEnabled() && Calendar.getInstance().getTimeInMillis()<mAlarms.get(i).getTimeLong()){
                mAlarmIntent.putExtra(ALARM_TRIGGERED, mAlarms.get(i).getAlarmDescription());
                mAlarmIntent.putExtra(NOTIFICATION_ALARM_TIME, mAlarms.get(i).getTime());
                mPendingIntent = PendingIntent.getBroadcast(context, i, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, mAlarms.get(i).getTimeLong(), mPendingIntent);
            }
        }
    }
}

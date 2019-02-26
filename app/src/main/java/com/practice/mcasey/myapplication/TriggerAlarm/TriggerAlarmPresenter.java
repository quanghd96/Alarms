package com.practice.mcasey.myapplication.TriggerAlarm;

import com.practice.mcasey.myapplication.AlarmList.AlarmListFragment;

public class TriggerAlarmPresenter {

    TriggerAlarmView mView;

    public TriggerAlarmPresenter(TriggerAlarmView view){mView = view;}

    public void updateTime(){
        mView.updateTimeText();
    }

    public void turnOffAlarm(){
        if(AlarmListFragment.ringtone.isPlaying())
            AlarmListFragment.ringtone.stop();
        mView.onClickTurnOffAlarm();
    }
}

package com.practice.mcasey.myapplication.CreateAlarm;

public interface CreateAlarmView {

    void onClickCancelAlarm();

    void updateTime(String format);

    void updateDescription();

    void updateDays();

    void onClickCreateAlarm();
}

package com.practice.mcasey.myapplication.Model;

import org.parceler.Parcel;

import java.util.UUID;

@Parcel
public class Alarm {

    private UUID mUUID;
    private String mAlarmDescription;
    private String mTime;
    private long mTimeLong;
    private String mDays;
    private boolean mEnabled;
    private boolean mRecurring;

    public Alarm() {
        mUUID = UUID.randomUUID();
    }

    public Alarm(UUID uuid){
        mUUID = uuid;
    }

    public Alarm(String alarmDescription, String time, String days, UUID uuid, boolean enabled, boolean recurring) {
        mUUID = uuid;
        mAlarmDescription = alarmDescription;
        mTime = time;
        mDays = days;
        mEnabled = enabled;
        mRecurring = recurring;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getAlarmDescription() {
        return mAlarmDescription;
    }

    public void setAlarmDescription(String alarmDescription) {
        mAlarmDescription = alarmDescription;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public long getTimeLong() {
        return mTimeLong;
    }

    public void setTimeLong(long timeLong) {
        mTimeLong = timeLong;
    }

    public String getDays() {
        return mDays;
    }

    public void setDays(String days) {
        mDays = days;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public boolean isRecurring() {
        return mRecurring;
    }

    public void setRecurring(boolean recurring) {
        mRecurring = recurring;
    }
}

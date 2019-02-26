package com.practice.mcasey.myapplication.Model;

import static com.practice.mcasey.myapplication.SQLite.AlarmDBSchema.AlarmTable.*;
import static com.practice.mcasey.myapplication.SQLite.AlarmDBSchema.AlarmTable.Cols.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.practice.mcasey.myapplication.SQLite.AlarmBaseHelper;
import com.practice.mcasey.myapplication.SQLite.AlarmCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlarmSingleton {
    private static AlarmSingleton sAlarm;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static AlarmSingleton getInstance(Context context) {
        if(sAlarm == null)
            sAlarm = new AlarmSingleton(context);
        return sAlarm;
    }

    private AlarmSingleton(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AlarmBaseHelper(mContext).getWritableDatabase();
    }

    public List<Alarm> getAlarms() {
        List<Alarm> crimes = new ArrayList<>();
        AlarmCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getAlarm());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Alarm getAlarm(Alarm alarm) {
        AlarmCursorWrapper cursor = queryCrimes(Cols.UUID + " = ?", new String[]{alarm.getUUID().toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getAlarm();
        } finally {
            cursor.close();
        }
    }

    public void updateAlarm(Alarm alarm) {
        ContentValues values = getContentValues(alarm);
        mDatabase.update(NAME, values, Cols.UUID + " = ?", new String[]{alarm.getUUID().toString()});
        Log.i("SINGLETON_ALARMS", getAlarms().toString());
    }

    public void addAlarm(Alarm alarm){
        ContentValues contentValues = getContentValues(alarm);
        mDatabase.insert(NAME, null, contentValues);
    }

    public void deleteAlarm(Alarm alarm){
        mDatabase.delete(NAME, Cols.UUID + " = ?", new String[]{alarm.getUUID().toString()});
    }

    private AlarmCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new AlarmCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(UUID, alarm.getUUID().toString());
        values.put(DESCRIPTION, alarm.getAlarmDescription());
        values.put(TIME, alarm.getTime());
        values.put(TIME_LONG, alarm.getTimeLong());
        values.put(DAYS, alarm.getDays());
        values.put(ENABLED, alarm.isEnabled() ? 1:0);
        values.put(RECURRING, alarm.isRecurring() ? 1:0);
        return values;
    }
}

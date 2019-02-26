package com.practice.mcasey.myapplication.SQLite;

import static com.practice.mcasey.myapplication.SQLite.AlarmDBSchema.AlarmTable.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "alarmBase.db";

    public AlarmBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.UUID + ", " +
                Cols.DESCRIPTION + ", " +
                Cols.TIME + ", " +
                Cols.TIME_LONG + ", " +
                Cols.DAYS + ", " +
                Cols.ENABLED + ", " +
                Cols.RECURRING +
                ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

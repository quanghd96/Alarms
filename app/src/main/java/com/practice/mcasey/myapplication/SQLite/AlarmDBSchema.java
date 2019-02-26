package com.practice.mcasey.myapplication.SQLite;

public class AlarmDBSchema {
    public static final class AlarmTable {
        public static final String NAME = "alarms";

        public static final class Cols{
            public static final String UUID = "UUID";
            public static final String DESCRIPTION = "DESCRIPTION";
            public static final String TIME = "TIME";
            public static final String TIME_LONG = "TIME_LONG";
            public static final String DAYS = "DAYS";
            public static final String ENABLED = "ENABLED";
            public static final String RECURRING = "RECURRING";
        }
    }
}

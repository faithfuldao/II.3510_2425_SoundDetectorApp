package com.example.sounddetector.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
This class is written with orientation from this documentation and
example from: https://developer.android.com/training/data-storage/sqlite#java
 */
public class SoundDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SoundDetection.db";
    private static final int DATABASE_VERSION = 1;

    public SoundDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSIONS_TABLE = "CREATE TABLE Sessions (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "start_time TEXT)";

        String CREATE_MEASUREMENTS_TABLE = "CREATE TABLE Measurements (" +
                "measurement_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "session_id INTEGER, " +
                "timestamp TEXT, " +
                "decibel_level REAL, " +
                "FOREIGN KEY(session_id) REFERENCES Sessions(session_id))";

        db.execSQL(CREATE_SESSIONS_TABLE);
        db.execSQL(CREATE_MEASUREMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Measurements");
        db.execSQL("DROP TABLE IF EXISTS Sessions");
        onCreate(db);
    }
}

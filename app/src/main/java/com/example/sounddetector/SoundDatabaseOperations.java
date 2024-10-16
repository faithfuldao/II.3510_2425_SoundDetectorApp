package com.example.sounddetector;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
This class is written with orientation from this documentation and
example from: https://developer.android.com/training/data-storage/sqlite#java
 */
public class SoundDatabaseOperations {
    private SoundDatabaseHelper dbHelper;

    public SoundDatabaseOperations(Context context) {
        dbHelper = new SoundDatabaseHelper(context);
    }

    public long insertSession(String startTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("start_time", startTime);

        long sessionId = db.insert("Sessions", null, values);
        db.close();
        return sessionId;
    }

    public void insertMeasurement(long sessionId, String timestamp, double decibelLevel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("session_id", sessionId);
        values.put("timestamp", timestamp);
        values.put("decibel_level", decibelLevel);

        db.insert("Measurements", null, values);
        db.close();
    }

    public List<Double> getMeasurementsForSession(long sessionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Double> decibelLevels = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT decibel_level FROM Measurements WHERE session_id = ?", new String[]{String.valueOf(sessionId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") double decibelLevel = cursor.getDouble(cursor.getColumnIndex("decibel_level"));
                decibelLevels.add(decibelLevel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return decibelLevels;
    }

    // Add more methods as needed for other CRUD operations
}

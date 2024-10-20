package com.example.sounddetector.database;

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

    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Drop the tables if they exist
        db.execSQL("DROP TABLE IF EXISTS Measurements;");
        db.execSQL("DROP TABLE IF EXISTS Sessions;");

        // Recreate the Sessions table
        db.execSQL("CREATE TABLE Sessions (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "start_time TEXT)");

        // Recreate the Measurements table
        db.execSQL("CREATE TABLE Measurements (" +
                "measurement_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "session_id INTEGER, " +
                "timestamp TEXT, " +
                "decibel_level REAL, " +
                "FOREIGN KEY(session_id) REFERENCES Sessions(session_id))");

        db.close();  // Close the database connection
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

    /**
     * This method is used to get the record sessions with the two measures "avg-decibel"
     * and "max-decibel". This method is used to get the needed data for our history view.
     * @return List of recording session object with added fields "avg-decibel" and "max-decibel"
     */
    public List<RecordingSession> getRecordingSessions() {
        List<RecordingSession> sessions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.session_id, s.start_time, " +
                "AVG(m.decibel_level) AS avg_decibel_level, " +
                "MAX(m.decibel_level) AS max_decibel_level " +
                "FROM Sessions s " +
                "LEFT JOIN Measurements m ON s.session_id = m.session_id " +
                "GROUP BY s.session_id, s.start_time";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int sessionId = cursor.getInt(cursor.getColumnIndex("session_id"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("start_time"));
                @SuppressLint("Range") float avgDecibelLevel = cursor.getFloat(cursor.getColumnIndex("avg_decibel_level"));
                @SuppressLint("Range") float maxDecibelLevel = cursor.getFloat(cursor.getColumnIndex("max_decibel_level"));

                RecordingSession session = new RecordingSession(sessionId, startTime, avgDecibelLevel, maxDecibelLevel);
                sessions.add(session);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return sessions;
    }

    // Methods to get the measurements for specific dates and date ranges
    // for graph visualisation in fragment 2

    /**
     * Retrieves all measurements from the database for a specific day.
     *
     * @param db   The readable SQLiteDatabase instance.
     * @param date The specific day in the format 'YYYY-MM-DD'.
     * @return A Cursor containing the results of the query, or null if no results found.
     */
    public Cursor getMeasurementsForDay(SQLiteDatabase db, String date) {
        String query = "SELECT * FROM Measurements WHERE DATE(timestamp) = ?";
        String[] selectionArgs = { date };
        return db.rawQuery(query, selectionArgs);
    }

    /**
     * Retrieves all measurements from the database for a specific week of the year.
     *
     * @param db          The readable SQLiteDatabase instance.
     * @param year        The year in the format 'YYYY'.
     * @param weekNumber  The week number in the format 'WW' (e.g., '42' for the 42nd week).
     * @return A Cursor containing the results of the query, or null if no results found.
     */
    public Cursor getMeasurementsForWeek(SQLiteDatabase db, String year, String weekNumber) {
        String query = "SELECT * FROM Measurements WHERE strftime('%Y', timestamp) = ? AND strftime('%W', timestamp) = ?";
        String[] selectionArgs = { year, weekNumber };
        return db.rawQuery(query, selectionArgs);
    }

    /**
     * Retrieves all measurements from the database for a specific month of the year.
     *
     * @param db    The readable SQLiteDatabase instance.
     * @param year  The year in the format 'YYYY'.
     * @param month The month in the format 'MM' (e.g., '10' for October).
     * @return A Cursor containing the results of the query, or null if no results found.
     */
    public Cursor getMeasurementsForMonth(SQLiteDatabase db, String year, String month) {
        String query = "SELECT * FROM Measurements WHERE strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ?";
        String[] selectionArgs = { year, month };
        return db.rawQuery(query, selectionArgs);
    }

    /**
     * Retrieves all measurements from the database for a specific year.
     *
     * @param db   The readable SQLiteDatabase instance.
     * @param year The year in the format 'YYYY'.
     * @return A Cursor containing the results of the query, or null if no results found.
     */
    public Cursor getMeasurementsForYear(SQLiteDatabase db, String year) {
        String query = "SELECT * FROM Measurements WHERE strftime('%Y', timestamp) = ?";
        String[] selectionArgs = { year };
        return db.rawQuery(query, selectionArgs);
    }

}

package com.example.sounddetector.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles database operations for managing sound recording sessions
 * and measurements. It provides methods for inserting, retrieving, and managing
 * data in the Sessions and Measurements tables.
 *
 * This class is based on the official SQLite documentation from:
 * https://developer.android.com/training/data-storage/sqlite#java
 */
public class SoundDatabaseOperations {
    private SoundDatabaseHelper dbHelper;

    /**
     * Constructor for SoundDatabaseOperations.
     *
     * @param context The context of the application (used to create the database helper).
     */
    public SoundDatabaseOperations(Context context) {
        dbHelper = new SoundDatabaseHelper(context);
    }

    /**
     * Resets the database by dropping and recreating the Sessions and Measurements tables.
     * This should be used with caution, as it will remove all data from the tables.
     */
    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS Measurements;");
        db.execSQL("DROP TABLE IF EXISTS Sessions;");

        // Recreate the Sessions and Measurements tables
        db.execSQL("CREATE TABLE Sessions (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "start_time TEXT)");

        db.execSQL("CREATE TABLE Measurements (" +
                "measurement_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "session_id INTEGER, " +
                "timestamp TEXT, " +
                "decibel_level REAL, " +
                "FOREIGN KEY(session_id) REFERENCES Sessions(session_id))");

        db.close();  // Close the database connection
    }

    /**
     * Inserts a new recording session into the Sessions table.
     *
     * @param startTime The start time of the session as a String.
     * @return The ID of the newly inserted session.
     */
    public long insertSession(String startTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("start_time", startTime);

        long sessionId = db.insert("Sessions", null, values);
        db.close();
        return sessionId;
    }

    /**
     * Inserts a new measurement into the Measurements table.
     *
     * @param sessionId    The ID of the session this measurement belongs to.
     * @param timestamp    The timestamp of the measurement in the format 'YYYY-MM-DD HH:MM:SS'.
     * @param decibelLevel The decibel level recorded during the measurement.
     */
    public void insertMeasurement(long sessionId, String timestamp, double decibelLevel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("session_id", sessionId);
        values.put("timestamp", timestamp);
        values.put("decibel_level", decibelLevel);

        db.insert("Measurements", null, values);
        db.close();
    }

    /**
     * Retrieves all decibel measurements for a specific session.
     *
     * @param sessionId The ID of the session for which measurements are retrieved.
     * @return A list of decibel levels associated with the specified session.
     */
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
     * Retrieves a list of recording sessions with their average and maximum decibel levels.
     *
     * @return A list of RecordingSession objects, each containing session ID, start time,
     *         average decibel level, and maximum decibel level.
     */
    public List<RecordingSession> getRecordingSessions() {
        List<RecordingSession> sessions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.session_id, s.start_time, " +
                "AVG(m.decibel_level) AS avg_decibel_level, " +
                "MAX(m.decibel_level) AS max_decibel_level " +
                "FROM Sessions s " +
                "LEFT JOIN Measurements m ON s.session_id = m.session_id " +
                "GROUP BY s.session_id, s.start_time " +
                "ORDER BY s.session_id DESC";

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

    // Methods to retrieve measurements for specific dates or date ranges

    /**
     * Retrieves all measurements from the database for a specific day.
     *
     * @param db   The readable SQLiteDatabase instance.
     * @param date The specific day in the format 'YYYY-MM-DD'.
     * @return A Cursor containing the results of the query.
     */
    public Cursor getMeasurementsForDay(SQLiteDatabase db, String date) {
        String query = "SELECT * FROM Measurements WHERE DATE(timestamp) = ?";
        String[] selectionArgs = { date };
        return db.rawQuery(query, selectionArgs);
    }

    /**
     * Retrieves all measurements from the database for a specific week of the year.
     *
     * @param db         The readable SQLiteDatabase instance.
     * @param year       The year in the format 'YYYY'.
     * @param weekNumber The week number in the format 'WW'.
     * @return A Cursor containing the results of the query.
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
     * @param month The month in the format 'MM'.
     * @return A Cursor containing the results of the query.
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
     * @return A Cursor containing the results of the query.
     */
    public Cursor getMeasurementsForYear(SQLiteDatabase db, String year) {
        String query = "SELECT * FROM Measurements WHERE strftime('%Y', timestamp) = ?";
        String[] selectionArgs = { year };
        return db.rawQuery(query, selectionArgs);
    }
}

package com.example.sounddetector.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for managing the SQLite database used for storing sound detection data.
 * It handles database creation and version management, providing a way to store
 * sessions and sound level measurements.
 *
 * This class is based on the official documentation from:
 * https://developer.android.com/training/data-storage/sqlite#java
 */
public class SoundDatabaseHelper extends SQLiteOpenHelper {
    // Name of the database file
    private static final String DATABASE_NAME = "SoundDetection.db";

    // Version of the database (increment if the schema changes)
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor for the SoundDatabaseHelper.
     *
     * @param context The context where the database will be used, typically an Activity or Application context.
     */
    public SoundDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This method is used to
     * create the necessary tables for storing session and measurement data.
     *
     * @param db The SQLiteDatabase where the SQL commands will be executed.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL command to create the Sessions table
        String CREATE_SESSIONS_TABLE = "CREATE TABLE Sessions (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "start_time TEXT)";

        // SQL command to create the Measurements table
        String CREATE_MEASUREMENTS_TABLE = "CREATE TABLE Measurements (" +
                "measurement_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "session_id INTEGER, " +
                "timestamp TEXT, " +
                "decibel_level REAL, " +
                "FOREIGN KEY(session_id) REFERENCES Sessions(session_id))";

        // Execute the SQL commands to create the tables
        db.execSQL(CREATE_SESSIONS_TABLE);
        db.execSQL(CREATE_MEASUREMENTS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. This method will drop the existing
     * tables and recreate them with the updated schema if needed.
     *
     * @param db The SQLiteDatabase where the SQL commands will be executed.
     * @param oldVersion The old version of the database (before the upgrade).
     * @param newVersion The new version of the database (after the upgrade).
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS Measurements");
        db.execSQL("DROP TABLE IF EXISTS Sessions");

        // Create new tables with the updated schema
        onCreate(db);
    }
}

package com.example.sounddetector.database;

/**
 * This class is supposed to represent a recording session.
 * It stores some data like session ID, start time, and decibel levels.
 * Yeah, not too complicated... I hope.
 */
public class RecordingSession {
    private int sessionId; // The ID of the session (like a number, duh)
    private String startTime; // When the session started, as a String because... yeah, I didn't use Date or anything
    private float avgDecibel; // The average sound level, measured in decibels. Louder means higher!
    private float maxDecibel; // The loudest sound during the session. Pretty much the peak.

    /**
     * The constructor for creating a recording session.
     * @param sessionId This is just the ID of the session. It's an int, so... yeah.
     * @param startTime When the session started. I decided to use a String here.
     * @param avgDecibel The average decibel level recorded during the session. It's a float!
     * @param maxDecibel The loudest decibel recorded. Also a float.
     */
    public RecordingSession(int sessionId, String startTime, float avgDecibel, float maxDecibel) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.avgDecibel = avgDecibel;
        this.maxDecibel = maxDecibel;
    }

    /**
     * Get the session ID.
     * @return The session ID (it's an int).
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * Get when the session started.
     * @return The start time as a String, since I guess I should've used a Date but... oh well.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Get the average decibel level of the session.
     * @return The average decibel as a float, because decimals are a thing.
     */
    public float getAvgDecibel() {
        return avgDecibel;
    }

    /**
     * Get the loudest decibel recorded during the session.
     * @return The maximum decibel level (float).
     */
    public float getMaxDecibel() {
        return maxDecibel;
    }
}

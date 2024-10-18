package com.example.sounddetector.database;

public class RecordingSession {
    private int sessionId;
    private String startTime;
    private float avgDecibel;
    private float maxDecibel;

    public RecordingSession(int sessionId, String startTime, float avgDecibel, float maxDecibel) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.avgDecibel = avgDecibel;
        this.maxDecibel = maxDecibel;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public float getAvgDecibel() {
        return avgDecibel;
    }

    public float getMaxDecibel() {
        return maxDecibel;
    }
}


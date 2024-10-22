package com.example.sounddetector.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sounddetector.R;

import java.util.List;

/**
 * Adapter class for displaying a list of recording sessions in a RecyclerView.
 * It connects the data (a list of RecordingSession objects) to the UI.
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {
    private List<RecordingSession> sessionList; // List of recording sessions to be displayed

    /**
     * Constructor for the SessionAdapter.
     *
     * @param sessionList The list of RecordingSession objects that will be displayed in the RecyclerView.
     */
    public SessionAdapter(List<RecordingSession> sessionList) {
        this.sessionList = sessionList;
    }

    /**
     * This method is called when a new ViewHolder is needed. It inflates the layout for individual items in the RecyclerView.
     *
     * @param parent The parent ViewGroup which will contain the new item views.
     * @param viewType The view type of the new View (not used in this case).
     * @return A new instance of SessionViewHolder containing the inflated item layout.
     */
    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each session item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_list_item, parent, false);
        return new SessionViewHolder(itemView);
    }

    /**
     * Binds data from a RecordingSession object to the corresponding views in the ViewHolder.
     * This is where the TextViews are populated with session data like ID, start time,
     * average decibel, and maximum decibel.
     *
     * @param holder The ViewHolder which contains the views to be populated with session data.
     * @param position The position of the current session in the list.
     */
    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        RecordingSession session = sessionList.get(position);
        holder.sessionIdText.setText(String.valueOf(session.getSessionId())); // Set session ID
        holder.startTimeText.setText(session.getStartTime()); // Set session start time
        holder.avgDecibelText.setText(String.format("%.1f dB", session.getAvgDecibel())); // Set average decibel level
        holder.maxDecibelText.setText(String.format("%.1f dB", session.getMaxDecibel())); // Set maximum decibel level
    }

    /**
     * Returns the total number of sessions in the list.
     *
     * @return The number of RecordingSession objects in the session list.
     */
    @Override
    public int getItemCount() {
        return sessionList.size(); // Return the number of sessions
    }

    /**
     * ViewHolder class for holding the views of each item in the RecyclerView.
     * This class is responsible for managing the individual session item views.
     */
    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public TextView sessionIdText, startTimeText, avgDecibelText, maxDecibelText; // TextViews to display session data

        /**
         * Constructor for the SessionViewHolder. It initializes the TextView elements
         * that will be used to display session data.
         *
         * @param view The View representing an individual item in the RecyclerView.
         */
        public SessionViewHolder(View view) {
            super(view);
            sessionIdText = view.findViewById(R.id.session_id); // TextView for session ID
            startTimeText = view.findViewById(R.id.start_time); // TextView for start time
            avgDecibelText = view.findViewById(R.id.avg_decibel); // TextView for average decibel
            maxDecibelText = view.findViewById(R.id.max_decibel); // TextView for maximum decibel
        }
    }
}

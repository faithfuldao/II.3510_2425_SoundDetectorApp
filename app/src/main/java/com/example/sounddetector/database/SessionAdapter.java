package com.example.sounddetector.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sounddetector.R;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {
    private List<RecordingSession> sessionList;

    public SessionAdapter(List<RecordingSession> sessionList) {
        this.sessionList = sessionList;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_list_item, parent, false);
        return new SessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        RecordingSession session = sessionList.get(position);
        holder.sessionIdText.setText(String.valueOf(session.getSessionId()));
        holder.startTimeText.setText(session.getStartTime());
        holder.avgDecibelText.setText(String.format("%.2f dB", session.getAvgDecibel()));
        holder.maxDecibelText.setText(String.format("%.2f dB", session.getMaxDecibel()));
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public TextView sessionIdText, startTimeText, avgDecibelText, maxDecibelText;

        public SessionViewHolder(View view) {
            super(view);
            sessionIdText = view.findViewById(R.id.session_id);
            startTimeText = view.findViewById(R.id.start_time);
            avgDecibelText = view.findViewById(R.id.avg_decibel);
            maxDecibelText = view.findViewById(R.id.max_decibel);
        }
    }
}

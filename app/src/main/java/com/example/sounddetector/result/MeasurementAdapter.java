package com.example.sounddetector.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sounddetector.R;

import java.util.List;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder> {
    private List<String[]> measurementList;

    public MeasurementAdapter(List<String[]> measurementList) {
        this.measurementList = measurementList;
    }

    @Override
    public MeasurementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.measurement_list_item, parent, false);
        return new MeasurementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeasurementViewHolder holder, int position) {
        String[] measurement = measurementList.get(position);
        holder.measurementIdText.setText("ID: " + measurement[0]);
        holder.decibelLevelText.setText(String.format("%.1f dB", Float.parseFloat(measurement[1])));
        holder.timestampText.setText(measurement[2]);
    }

    @Override
    public int getItemCount() {
        return measurementList.size();
    }

    public class MeasurementViewHolder extends RecyclerView.ViewHolder {
        public TextView measurementIdText, decibelLevelText, timestampText;

        public MeasurementViewHolder(View view) {
            super(view);
            measurementIdText = view.findViewById(R.id.measurement_id);
            decibelLevelText = view.findViewById(R.id.decibel_level);
            timestampText = view.findViewById(R.id.timestamp);
        }
    }
}
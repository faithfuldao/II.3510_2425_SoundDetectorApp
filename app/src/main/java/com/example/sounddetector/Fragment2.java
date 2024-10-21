package com.example.sounddetector;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class Fragment2 extends Fragment {

    private TextView startDateTextView;
    private TextView startTimeTextView;
    private TextView endDateTextView;
    private TextView endTimeTextView;

    private Button pickStartDateButton;
    private Button pickStartTimeButton;
    private Button pickEndDateButton;
    private Button pickEndTimeButton;

    private Button plotButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        startDateTextView = view.findViewById(R.id.start_date_view);
        startTimeTextView = view.findViewById(R.id.start_time_view);
        pickStartDateButton = view.findViewById(R.id.pick_start_date_button);
        pickStartTimeButton = view.findViewById(R.id.pick_time_button);

        endDateTextView = view.findViewById(R.id.end_date_view);
        endTimeTextView = view.findViewById(R.id.end_time_view);
        pickEndDateButton = view.findViewById(R.id.pick_end_date_button);
        pickEndTimeButton = view.findViewById(R.id.pick_end_time_button);
        plotButton = view.findViewById(R.id.plot_button);


        pickStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePicker();
            }
        });
        pickStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartTimePicker();
            }
        });
        pickEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDatePicker();
            }
        });
        pickEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndTimePicker();
            }
        });

        plotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plotGraph();
            }
        });

        return view;
    }


    private void openStartDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Showing the picked value in the textView
                month++;
                String dateString = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                startDateTextView.setText(dateString);
            }
        }, 2024, 10, 20);
        datePickerDialog.show();
    }

    private void openStartTimePicker () {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //Showing the picked value in the textView
                String timeString = String.valueOf(hour) + ":" + String.valueOf(minute) + ":00";
                startTimeTextView.setText(timeString);
            }
        }, 15, 30, true);
        timePickerDialog.show();
    }

    private void openEndDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Showing the picked value in the textView
                month++;
                String dateString = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                endDateTextView.setText(dateString);
            }
        }, 2024, 10, 20);
        datePickerDialog.show();
    }

    private void openEndTimePicker () {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //Showing the picked value in the textView
                String timeString = String.valueOf(hour) + ":" + String.valueOf(minute) + ":00";
                endTimeTextView.setText(timeString);
            }
        }, 15, 30, true);
        timePickerDialog.show();
    }

    private void plotGraph() {
        String startDate = startDateTextView.getText().toString();
        String startTime = startTimeTextView.getText().toString();
        String endDate = endDateTextView.getText().toString();
        String endTime = endTimeTextView.getText().toString();

        String start = startDate + " " + startTime;
        String end = endDate + " " + endTime;
    }
}



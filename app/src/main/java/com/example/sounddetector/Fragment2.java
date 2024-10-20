package com.example.sounddetector;

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

import java.util.Objects;

public class Fragment2 extends Fragment {

    private TextView dateTextView;
    private TextView timeTextView;
    private Button pickDateAndTimeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        dateTextView = view.findViewById(R.id.date_view);
        timeTextView = view.findViewById(R.id.time_view);
        pickDateAndTimeButton = view.findViewById(R.id.pick_date_and_time_button);


        pickDateAndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDatePicker(); // Open date picker dialog

                openTimePicker(); //Open time picker dialog
            }
        });
        return view;
    }


    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Showing the picked value in the textView
                String dateString = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
                dateTextView.setText(dateString);

            }
        }, 2024, 10, 20);

        datePickerDialog.show();
    }


    private void openTimePicker () {

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {


                //Showing the picked value in the textView
                String timeString = String.valueOf(hour) + ":" + String.valueOf(minute) + ":00";
                timeTextView.setText(timeString);

            }
        }, 15, 30, true);

        timePickerDialog.show();
    }
}



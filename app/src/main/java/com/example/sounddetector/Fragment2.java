package com.example.sounddetector;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sounddetector.database.SoundDatabaseHelper;
import com.example.sounddetector.database.SoundDatabaseOperations;
import com.example.sounddetector.result.ResultsActivity;

import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {

    private TextView dateTextView;
    private Button pickDateButton;
    private RadioGroup radioGroup;
    private Button searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        dateTextView = view.findViewById(R.id.date_view);
        pickDateButton = view.findViewById(R.id.pick_date_button);
        radioGroup = view.findViewById(R.id.radioGroup);
        searchButton = view.findViewById(R.id.search_button);

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePicker();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayResults();
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
                dateTextView.setText(dateString);
            }
        }, 2024, 10, 20);
        datePickerDialog.show();
    }

    @SuppressLint("Range")
    private void displayResults() {
        String date = dateTextView.getText().toString();
        String selectedDate = null;
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        SoundDatabaseHelper dbHelper = new SoundDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SoundDatabaseOperations dbOperations = new SoundDatabaseOperations(getContext());
        Cursor cursor = null;

        if (selectedRadioButtonId == R.id.radio_button1) {
            String year = date.split("-")[0];
            cursor = dbOperations.getMeasurementsForYear(db, year);
            selectedDate = year;
        } else if (selectedRadioButtonId == R.id.radio_button2) {
            String[] dateParts = date.split("-");
            String year = dateParts[0];
            String month = String.format("%02d", Integer.parseInt(dateParts[1]));
            cursor = dbOperations.getMeasurementsForMonth(db, year, month);
            selectedDate = year + "-" + month;
        } else if (selectedRadioButtonId == R.id.radio_button3) {
            cursor = dbOperations.getMeasurementsForDay(db, date);
            selectedDate = date;
        }

        List<String[]> measurements = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("measurement_id"));
                float value = cursor.getFloat(cursor.getColumnIndex("decibel_level"));
                String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
                measurements.add(new String[]{String.valueOf(id), String.valueOf(value), timestamp});
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (measurements.isEmpty()) {
            Toast.makeText(getContext(), "No data found.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), ResultsActivity.class);
            intent.putExtra("measurements", (ArrayList<String[]>) measurements);
            intent.putExtra("selected_date", selectedDate);
            startActivity(intent);
        }

    }
}



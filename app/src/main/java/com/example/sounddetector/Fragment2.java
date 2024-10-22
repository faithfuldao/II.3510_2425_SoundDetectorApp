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

/**
 * Fragment2 allows the user to select a date and filter measurements by year, month, or day.
 * Users can view sound measurement results based on the selected date range.
 * It interacts with the database to retrieve the corresponding measurement data.
 */
public class Fragment2 extends Fragment {

    private TextView dateTextView;
    private Button pickDateButton;
    private RadioGroup radioGroup;
    private Button searchButton;

    /**
     * Initializes the layout and sets up event listeners for the buttons.
     *
     * @param inflater  The LayoutInflater used to inflate the fragment's view.
     * @param container The ViewGroup that will contain the fragment's UI.
     * @param savedInstanceState A Bundle containing the fragment's saved state (if any).
     * @return The inflated view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        dateTextView = view.findViewById(R.id.date_view);
        pickDateButton = view.findViewById(R.id.pick_date_button);
        radioGroup = view.findViewById(R.id.radioGroup);
        searchButton = view.findViewById(R.id.search_button);

        // Set up event listener for picking a date
        pickDateButton.setOnClickListener(view1 -> openStartDatePicker());

        // Set up event listener for searching and displaying results
        searchButton.setOnClickListener(view12 -> displayResults());

        return view;
    }

    /**
     * Opens a DatePicker dialog for selecting a date.
     * The selected date is displayed in the TextView.
     */
    private void openStartDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme,
                (datePicker, year, month, day) -> {
                    // Increment month since it's zero-based
                    month++;
                    // Format and display the selected date in the TextView
                    String dateString = year + "-" + month + "-" + day;
                    dateTextView.setText(dateString);
                }, 2024, 10, 20);
        datePickerDialog.show();
    }

    /**
     * Retrieves the selected date and radio button option, queries the database for
     * the corresponding measurements, and displays the results in a new activity.
     */
    @SuppressLint("Range")
    private void displayResults() {
        String date = dateTextView.getText().toString();
        String selectedDate = null;
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        SoundDatabaseHelper dbHelper = new SoundDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SoundDatabaseOperations dbOperations = new SoundDatabaseOperations(getContext());
        Cursor cursor = null;

        // Determine the selected radio button and retrieve data accordingly
        if (selectedRadioButtonId == R.id.radio_button1) { // Year
            String year = date.split("-")[0];
            cursor = dbOperations.getMeasurementsForYear(db, year);
            selectedDate = year;
        } else if (selectedRadioButtonId == R.id.radio_button2) { // Month
            String[] dateParts = date.split("-");
            String year = dateParts[0];
            String month = String.format("%02d", Integer.parseInt(dateParts[1]));
            cursor = dbOperations.getMeasurementsForMonth(db, year, month);
            selectedDate = year + "-" + month;
        } else if (selectedRadioButtonId == R.id.radio_button3) { // Day
            cursor = dbOperations.getMeasurementsForDay(db, date);
            selectedDate = date;
        }

        // Collect the retrieved measurements into a list
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

        // Show a message if no data is found, otherwise start a new activity to display the results
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

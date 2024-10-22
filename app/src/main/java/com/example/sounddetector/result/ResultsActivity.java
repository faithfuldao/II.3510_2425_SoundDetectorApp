package com.example.sounddetector.result;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sounddetector.R;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView selectedDateView = findViewById(R.id.selected_date);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        String selectedDate = getIntent().getStringExtra("selected_date");
        ArrayList<String[]> measurements = (ArrayList<String[]>) getIntent().getSerializableExtra("measurements");

        selectedDateView.setText("Selected date: " + selectedDate);

        MeasurementAdapter adapter = new MeasurementAdapter(measurements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
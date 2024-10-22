package com.example.sounddetector.result;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sounddetector.R;
import com.example.sounddetector.database.RecordingSession;
import com.example.sounddetector.database.SessionAdapter;
import com.example.sounddetector.database.SoundDatabaseOperations;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private SessionAdapter adapter;
    private List<RecordingSession> sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView selectedDateView = findViewById(R.id.selected_date);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_measurements);

        SoundDatabaseOperations dbOperations = new SoundDatabaseOperations(this);
        sessions = dbOperations.getRecordingSessions();

        adapter = new SessionAdapter(sessions);
        recyclerView.setAdapter(adapter);

        String selectedDate = getIntent().getStringExtra("selected_date");
        ArrayList<String[]> measurements = (ArrayList<String[]>) getIntent().getSerializableExtra("measurements");

        selectedDateView.setText("Selected date: " + selectedDate);
    }
}
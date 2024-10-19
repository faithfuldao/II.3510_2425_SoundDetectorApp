package com.example.sounddetector;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sounddetector.database.SoundDatabaseOperations;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SoundDatabaseOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new Fragment1(), getString(R.string.home));
        viewPagerAdapter.addFragment(new Fragment2(), getString(R.string.graph));
        viewPagerAdapter.addFragment(new Fragment3(), getString(R.string.history));
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();


        examplesForDatabaseUse();
    }

    protected void examplesForDatabaseUse(){
        // Initialize the database operations classes
        dbOperations = new SoundDatabaseOperations(this);

        // Insert a new session
        String startTime = "2024-10-16 14:30:00";
        long sessionId = dbOperations.insertSession(startTime);

        // clear tables in database and insert test data
        dbOperations.resetDatabase();
        insertTestData();

        // Retrieve and print measurements
        List<Double> decibelLevels = dbOperations.getMeasurementsForSession(sessionId);
        for (double level : decibelLevels) {
            System.out.println("Decibel Level: " + level);
            Log.d("Debug DB", "Decibel Level from Database is: " + level);
        }
    }

    private void insertTestData() {
        // Insert test Sessions and Measurements
        long sessionId1 = dbOperations.insertSession("2024-10-16 14:30:00");  // Session 1
        dbOperations.insertMeasurement(sessionId1, "2024-10-16 14:30:05", 70.5);
        dbOperations.insertMeasurement(sessionId1, "2024-10-16 14:35:10", 72.3);
        dbOperations.insertMeasurement(sessionId1, "2024-10-16 14:40:15", 68.7);

        long sessionId2 = dbOperations.insertSession("2024-10-17 09:15:00");  // Session 2
        dbOperations.insertMeasurement(sessionId2, "2024-10-17 09:15:05", 0.0);
        dbOperations.insertMeasurement(sessionId2, "2024-10-17 09:20:10", 5.0);
        dbOperations.insertMeasurement(sessionId2, "2024-10-17 09:25:15", 10.0);

        long sessionId3 = dbOperations.insertSession("2024-10-18 11:45:00");  // Session 3
        dbOperations.insertMeasurement(sessionId3, "2024-10-18 11:45:05", 80.5);
        dbOperations.insertMeasurement(sessionId3, "2024-10-18 11:50:10", 82.3);
        dbOperations.insertMeasurement(sessionId3, "2024-10-18 11:55:15", 78.9);

        long sessionId4 = dbOperations.insertSession("2024-10-19 16:00:00");  // Session 4
        dbOperations.insertMeasurement(sessionId4, "2024-10-19 16:00:05", 55.5);
        dbOperations.insertMeasurement(sessionId4, "2024-10-19 16:05:10", 58.0);
        dbOperations.insertMeasurement(sessionId4, "2024-10-19 16:10:15", 60.3);

        long sessionId5 = dbOperations.insertSession("2024-10-20 08:00:00");  // Session 5
        dbOperations.insertMeasurement(sessionId5, "2024-10-20 08:00:05", 75.6);
        dbOperations.insertMeasurement(sessionId5, "2024-10-20 08:05:10", 77.8);
        dbOperations.insertMeasurement(sessionId5, "2024-10-20 08:10:15", 80.1);
    }
}
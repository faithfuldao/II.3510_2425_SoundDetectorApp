package com.example.sounddetector;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

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
        // Initialize the database operations class
        dbOperations = new SoundDatabaseOperations(this);

        // Insert a new session
        String startTime = "2024-10-16 14:30:00";
        long sessionId = dbOperations.insertSession(startTime);

        // Insert a measurement for that session
        dbOperations.insertMeasurement(sessionId, "2024-10-16 14:30:05", 70.5);

        // Retrieve and print measurements
        List<Double> decibelLevels = dbOperations.getMeasurementsForSession(sessionId);
        for (double level : decibelLevels) {
            System.out.println("Decibel Level: " + level);
            Log.d("Debug DB", "Decibel Level from Database is: " + level);
        }
    }
}
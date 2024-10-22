package com.example.sounddetector;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sounddetector.database.SoundDatabaseOperations;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

/**
 * MainActivity is the entry point of the application. It sets up the tabbed interface
 * using a ViewPager2 with three fragments: Home, Search, and History. It also includes
 * example methods for interacting with the database, such as inserting test data and
 * retrieving sound measurements.
 */
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout; // TabLayout for navigation between fragments
    private ViewPager2 viewPager; // ViewPager2 for swiping between fragments
    private SoundDatabaseOperations dbOperations; // Database operations for interacting with the sound database

    /**
     * Called when the activity is first created. Initializes the tab layout, sets up the ViewPager2,
     * and attaches the fragments (Home, Search, and History) to the ViewPager.
     * Also runs database operations to demonstrate data insertion and retrieval.
     *
     * @param savedInstanceState Bundle containing the saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TabLayout and ViewPager2
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Set up the ViewPagerAdapter with three fragments
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new Fragment1(), getString(R.string.home)); // Home fragment
        viewPagerAdapter.addFragment(new Fragment2(), getString(R.string.search)); // Search fragment
        viewPagerAdapter.addFragment(new Fragment3(), getString(R.string.history)); // History fragment

        // Set the adapter for the ViewPager and connect it with TabLayout using TabLayoutMediator
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();

        // Run database operations for testing
        examplesForDatabaseUse();
    }

    /**
     * Demonstrates database usage by resetting the database, inserting test data,
     * and retrieving decibel levels for a specific session.
     */
    protected void examplesForDatabaseUse() {
        // Initialize the database operations
        dbOperations = new SoundDatabaseOperations(this);

        // Insert a new session into the database
        String startTime = "2024-10-16 14:30:00";
        long sessionId = dbOperations.insertSession(startTime);

        // Clear the tables in the database and insert test data
        dbOperations.resetDatabase();
        insertTestData();

        // Retrieve and log the decibel levels for the inserted session
        List<Double> decibelLevels = dbOperations.getMeasurementsForSession(sessionId);
        for (double level : decibelLevels) {
            System.out.println("Decibel Level: " + level); // Print to console
            Log.d("Debug DB", "Decibel Level from Database is: " + level); // Log the data
        }
    }

    /**
     * Inserts predefined test data into the database. This includes multiple recording
     * sessions with associated measurements to simulate real-world data.
     */
    private void insertTestData() {
        // Insert test Sessions and Measurements into the database

        long sessionId1 = dbOperations.insertSession("2024-08-16 14:30:00");
        dbOperations.insertMeasurement(sessionId1, "2024-08-16 14:30:05", -70.5);
        dbOperations.insertMeasurement(sessionId1, "2024-08-16 14:35:10", -72.3);
        dbOperations.insertMeasurement(sessionId1, "2024-08-16 14:40:15", -68.7);

        long sessionId2 = dbOperations.insertSession("2024-09-17 09:15:00");
        dbOperations.insertMeasurement(sessionId2, "2024-09-17 09:15:05", -0.0);
        dbOperations.insertMeasurement(sessionId2, "2024-09-17 09:20:10", -5.0);
        dbOperations.insertMeasurement(sessionId2, "2024-09-17 09:25:15", -10.0);

        long sessionId3 = dbOperations.insertSession("2024-10-18 11:45:00");
        dbOperations.insertMeasurement(sessionId3, "2024-10-18 11:45:05", -80.5);
        dbOperations.insertMeasurement(sessionId3, "2024-10-18 11:50:10", -82.3);
        dbOperations.insertMeasurement(sessionId3, "2024-10-18 11:55:15", -78.9);

        long sessionId4 = dbOperations.insertSession("2024-10-19 16:00:00");
        dbOperations.insertMeasurement(sessionId4, "2024-10-19 16:00:05", -55.5);
        dbOperations.insertMeasurement(sessionId4, "2024-10-19 16:05:10", -58.0);
        dbOperations.insertMeasurement(sessionId4, "2024-10-19 16:10:15", -60.3);

        long sessionId5 = dbOperations.insertSession("2024-10-20 08:00:00");
        dbOperations.insertMeasurement(sessionId5, "2024-10-20 08:00:05", -75.6);
        dbOperations.insertMeasurement(sessionId5, "2024-10-20 08:05:10", -77.8);
        dbOperations.insertMeasurement(sessionId5, "2024-10-20 08:10:15", -80.1);

        long sessionId6 = dbOperations.insertSession("2024-10-21 08:00:00");
        dbOperations.insertMeasurement(sessionId6, "2024-10-21 08:00:05", -75.6);
        dbOperations.insertMeasurement(sessionId6, "2024-10-21 08:05:10", -77.8);
        dbOperations.insertMeasurement(sessionId6, "2024-10-21 08:10:15", -80.1);

        long sessionId7 = dbOperations.insertSession("2024-10-22 14:30:00");
        dbOperations.insertMeasurement(sessionId7, "2024-10-22 14:30:05", -70.5);
        dbOperations.insertMeasurement(sessionId7, "2024-10-22 14:35:10", -72.3);
        dbOperations.insertMeasurement(sessionId7, "2024-10-22 14:40:15", -68.7);

        long sessionId8 = dbOperations.insertSession("2024-10-23 09:15:00");
        dbOperations.insertMeasurement(sessionId8, "2024-10-23 09:15:05", -0.0);
        dbOperations.insertMeasurement(sessionId8, "2024-10-23 09:20:10", -5.0);
        dbOperations.insertMeasurement(sessionId8, "2024-10-23 09:25:15", -10.0);
    }
}

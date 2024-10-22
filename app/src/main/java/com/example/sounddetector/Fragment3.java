package com.example.sounddetector;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sounddetector.database.RecordingSession;
import com.example.sounddetector.database.SessionAdapter;
import com.example.sounddetector.database.SoundDatabaseOperations;

import java.util.List;

/**
 * Fragment3 displays a list of recorded sessions using a RecyclerView.
 * It retrieves the session data from the database and updates the view accordingly.
 */
public class Fragment3 extends Fragment {

    private SessionAdapter adapter; // Adapter for managing the display of sessions
    private List<RecordingSession> sessions; // List of recording sessions

    /**
     * Default constructor for Fragment3.
     * This constructor is required for fragment creation.
     */
    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Called to create the fragment's view hierarchy. This method inflates the layout
     * and initializes the RecyclerView for displaying the list of recording sessions.
     *
     * @param inflater LayoutInflater to inflate the fragment's view.
     * @param container ViewGroup that contains the fragment's view.
     * @param savedInstanceState Bundle containing the saved state of the fragment.
     * @return The created view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get the list of sessions from the database
        SoundDatabaseOperations dbOperations = new SoundDatabaseOperations(getContext());
        sessions = dbOperations.getRecordingSessions();

        // Set up the adapter with the session list
        adapter = new SessionAdapter(sessions);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Called when the fragment becomes visible to the user.
     * This method refreshes the session list by retrieving updated data from the database.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Get the updated list of sessions from the database
        updateSessionList();
    }

    /**
     * Updates the list of recording sessions by querying the database
     * and notifying the adapter of the changes.
     */
    private void updateSessionList() {
        SoundDatabaseOperations dbOperations = new SoundDatabaseOperations(getContext());
        sessions.clear();
        sessions.addAll(dbOperations.getRecordingSessions());
        adapter.notifyDataSetChanged();
    }
}

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

public class Fragment3 extends Fragment {

    public Fragment3() {
        // Required empty public constructor
    }

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
        List<RecordingSession> sessions = dbOperations.getRecordingSessions();  // Methode von oben

        // Set up the adapter with the session list
        SessionAdapter adapter = new SessionAdapter(sessions);
        recyclerView.setAdapter(adapter);

        return view;
    }


}
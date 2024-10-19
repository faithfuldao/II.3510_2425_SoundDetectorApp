package com.example.sounddetector;

import static android.Manifest.permission.RECORD_AUDIO;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import com.example.sounddetector.database.SoundDatabaseOperations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fragment1 extends Fragment {

    private Button startStopBtn;
    private TextView statusTV, dbValueTV;
    private AudioRecord audioRecord;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    private boolean isRecording = false;
    private static final int THRESHOLD_DB_LOW = -20;
    private static final int THRESHOLD_DB_HIGH = -10;

    private static final int SAMPLE_RATE = 44100;
    private static final int DURATION = 2;
    private static final int BUFFER_SIZE = SAMPLE_RATE * DURATION;

    private short[] audioBuffer = new short[BUFFER_SIZE];

    // for usage of the database
    private SoundDatabaseOperations dbOperations;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                        startRecording();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                });

        initializeDatabase();
    }

    private void initializeDatabase() {
        dbOperations = new SoundDatabaseOperations(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        statusTV = view.findViewById(R.id.status);
        startStopBtn = view.findViewById(R.id.startStopBtn);
        dbValueTV = view.findViewById(R.id.dbValue);

        startStopBtn.setOnClickListener(v -> {
            if (!isRecording) {
                if (checkPermissions()) {
                    startRecording();
                } else {
                    requestPermissionLauncher.launch(RECORD_AUDIO);
                }
            } else {
                stopRecording();
            }
        });

        return view;
    }

    // True if permission granted
    // False if permission denied
    private boolean checkPermissions() {
        int result1 = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void startRecording() {
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE);

        audioRecord.startRecording();
        isRecording = true;

        //add an entry for this recording session into the database
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        long sessionId = dbOperations.insertSession(startTime);


        new Thread(() -> {
            while (isRecording) {
                int numberOfShort = audioRecord.read(audioBuffer, 0, BUFFER_SIZE);

                // Calculate avg amplitude for
                double sum = 0;
                for (int i = 0; i < numberOfShort; i++) {
                    sum += audioBuffer[i] * audioBuffer[i];
                }
                double amplitude = Math.sqrt(sum / numberOfShort);
                // Convert to dB
                float amplitudeDB =  Math.round(20 * Math.log10(amplitude / 32767.0) * 10) / 10;
                Log.d("AudioStats", "Amplitude: " + amplitude + " dB: " + amplitudeDB);

                if (amplitudeDB > THRESHOLD_DB_HIGH) {
//                    sendNotification();
                    dbValueTV.setTextColor(getResources().getColor(R.color.red));
                    Log.d("AudioStats", "High sound level!");
                } else if (THRESHOLD_DB_LOW < amplitudeDB && amplitudeDB < THRESHOLD_DB_HIGH) {
                    dbValueTV.setTextColor(getResources().getColor(R.color.orange));
                } else {
                    dbValueTV.setTextColor(getResources().getColor(R.color.black));
                }

                //add an entry for this specific record into the database
                String timeOfRecording = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                dbOperations.insertMeasurement(sessionId, timeOfRecording, amplitudeDB);

                requireActivity().runOnUiThread(() -> {
                    dbValueTV.setText(amplitudeDB + " dB");
                });
            }
        }).start();

        startStopBtn.setText(getResources().getString(R.string.stopRecording));
        statusTV.setText(getResources().getString(R.string.recording));
    }

    private void stopRecording() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;

            startStopBtn.setText(getResources().getString(R.string.startRecording));
            statusTV.setText(getResources().getString(R.string.stopped));
        }
    }


//    private void sendNotification() {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String channelId  = "Sound_Notification_Channel";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "Sound Notification", NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        Notification notification = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.icnotification)
//                .setContentTitle("Sound Alert")
//                .setContentText("Ambiant sound crossed 70db")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .build();
//
//        notificationManager.notify(1, notification);
//    }
}




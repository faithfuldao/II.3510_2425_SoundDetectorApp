package com.example.sounddetector;

import static android.Manifest.permission.RECORD_AUDIO;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;

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
import java.io.IOException;

public class Fragment1 extends Fragment {

    private Button startStopBtn;
    private TextView statusTV;
    private MediaRecorder mediaRecorder;
    private static String mFileName = null;

    private boolean isRecording = false;

    //defining threshold for the alert notifications
    private static final int THRESHOLD_DB = 70;
    private Handler handler = new Handler();

    private ActivityResultLauncher<String> requestPermissionLauncher;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_1, container, false);

        statusTV = view.findViewById(R.id.status);
        startStopBtn = view.findViewById(R.id.startStopBtn);

        startStopBtn.setOnClickListener(v -> {
            if (!isRecording) {
                if (checkPermissions()) {
                    startRecording();
                } else {
                    requestPermissionLauncher.launch(RECORD_AUDIO);
                }
            } else {
                pauseRecording();
            }
        });
        return view;
    }

    // Method to check permissions
    private boolean checkPermissions() {
        int result1 = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void startRecording() {
        mFileName = requireActivity().getExternalFilesDir(null).getAbsolutePath() + "/tempRecording.3gp";
        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(mFileName);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d("recordStatus", "Recording started");
        } catch (IOException e) {
            Log.e("recordingStatus", "prepare() failed", e);
        }
        startStopBtn.setText(getResources().getString(R.string.stopRecording));
        statusTV.setText(getResources().getString(R.string.recordingStarted));
        isRecording = true;
    }

    private void pauseRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        startStopBtn.setText(getResources().getString(R.string.startRecording));
        statusTV.setText(getResources().getString(R.string.recordingStopped));
        isRecording = false;
    }

//    //converting the amplitude measured to decibel for our app
//    public double calculateDecibels(int amplitude) {
//        if (amplitude > 0) {
//            return 20 * Math.log10((double) amplitude / 32767);
//        } else {
//            return 0;
//        }
//    }
//
//
//    private Runnable soundLevelChecker = new Runnable() {
//        @Override
//        public void run() {
//            if (mRecorder != null) {
//                int amplitude = mRecorder.getMaxAmplitude();
//                double soundDecibelMeasured = calculateDecibels(amplitude); //convert it to decibel
//                Log.e("SoundLevel", "Current Sound Level: " + soundDecibelMeasured + " dB");
//
//                if (soundDecibelMeasured > THRESHOLD_DB) {
//                    sendNotification();  //send notification if sound crosses 70 db
//                }
//                handler.postDelayed(this, 1000);  // Re-check every second
//            } else {
//                Log.e("MainActivity", "MediaRecorder is not initialized.");
//            }
//
//        }
//    };
//
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




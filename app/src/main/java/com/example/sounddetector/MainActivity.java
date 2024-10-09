package com.example.sounddetector;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;


import static android.Manifest.permission.RECORD_AUDIO;


public class MainActivity extends AppCompatActivity {

    private TextView startTV, stopTV, playTV, stopplayTV, statusTV;

    private MediaRecorder mRecorder;

    private static String mFileName = null;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    //defining threshold for the alert notifications
    private static final int THRESHOLD_DB = 70;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTV = findViewById(R.id.idTVstatus);
        startTV = findViewById(R.id.btnRecord);
        stopTV = findViewById(R.id.btnStop);
       // stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        //playTV.setBackgroundColor(getResources().getColor(R.color.gray));
        //stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));

        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        stopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseRecording();

            }
        });

        //check frequently the sound measured by the app
        handler.postDelayed(soundLevelChecker, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermissions() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        String[] permissions = {RECORD_AUDIO};
        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_AUDIO_PERMISSION_CODE);
    }

    private void startRecording() {
        if(checkPermissions()){
           // stopTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
            //startTV.setBackgroundColor(getResources().getColor(R.color.gray));
            mFileName = getExternalFilesDir(null).getAbsolutePath() + "/tempRecording.3gp";
            mRecorder = new MediaRecorder();


            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);

            try {
                mRecorder.prepare();
                mRecorder.start();
                statusTV.setText("Recording Started");
                Log.e("TAG", "Recording started");
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed", e);
            }
            statusTV.setText("Recording Started");


        } else {
            requestPermissions();
        }
        }

    public void pauseRecording() {
       // stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
        //startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        //playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        //stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));

        mRecorder.stop();

        mRecorder.release();
        mRecorder = null;
        statusTV.setText("Recording Stopped");
    }

    //converting the amplitude measured to decibel for our app
    public double calculateDecibels(int amplitude) {
        if (amplitude > 0) {
            return 20 * Math.log10((double) amplitude / 32767);
        } else {
            return 0;
        }
    }


    private Runnable soundLevelChecker = new Runnable() {
        @Override
        public void run() {
            if (mRecorder != null) {
                int amplitude = mRecorder.getMaxAmplitude();
                double soundDecibelMeasured = calculateDecibels(amplitude); //convert it to decibel
                Log.e("SoundLevel", "Current Sound Level: " + soundDecibelMeasured + " dB");

                if (soundDecibelMeasured > THRESHOLD_DB) {
                    sendNotification();  //send notification if sound crosses 70 db
                }
                handler.postDelayed(this, 1000);  // Re-check every second
            } else {
                Log.e("MainActivity", "MediaRecorder is not initialized.");
            }

        }
    };

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId  = "Sound_Notification_Channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Sound Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icnotification)
                .setContentTitle("Sound Alert")
                .setContentText("Ambiant sound crossed 70db")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1, notification);
    }
}








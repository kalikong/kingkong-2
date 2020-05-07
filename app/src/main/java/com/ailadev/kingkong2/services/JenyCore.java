package com.ailadev.kingkong2.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.sac.speech.GoogleVoiceTypingDisabledException;
import com.sac.speech.Speech;
import com.sac.speech.SpeechDelegate;
import com.sac.speech.SpeechRecognitionNotAvailable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class JenyCore extends Service implements SpeechDelegate, Speech.stopDueToDelay {

    private static final String TAG = "JenyCore";

    public static SpeechDelegate delegate;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: jenycorestart");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Speech.init(this);
        delegate = this;
        Speech.getInstance().setListener(this);

        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
            muteBeepSoundOfRecorder();
        } else {
            try {
                Speech.getInstance().stopTextToSpeech();
                Speech.getInstance().startListening(null, this);
            } catch (SpeechRecognitionNotAvailable exc) {
                //showSpeechNotSupportedDialog();

            } catch (GoogleVoiceTypingDisabledException exc) {
                //showEnableGoogleVoiceTyping();
            }
            muteBeepSoundOfRecorder();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        delegate = null;
        Speech.getInstance().stopListening();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSpecifiedCommandPronounced(String event) {
        Log.d(TAG, "onSpecifiedCommandPronounced: "+Speech.getInstance().isListening());
        if (Speech.getInstance().isListening()) {
            muteBeepSoundOfRecorder();
            Speech.getInstance().stopListening();
        }else{
            try {
                Speech.getInstance().stopTextToSpeech();
                Speech.getInstance().startListening(null, this);
            } catch (GoogleVoiceTypingDisabledException e) {
                e.printStackTrace();
            } catch (SpeechRecognitionNotAvailable speechRecognitionNotAvailable) {
                speechRecognitionNotAvailable.printStackTrace();
            }
            muteBeepSoundOfRecorder();
        }
    }

    @Override
    public void onStartOfSpeech() {
        Log.d(TAG, "onStartOfSpeech: ");
    }

    @Override
    public void onSpeechRmsChanged(float value) {
        Log.d(TAG, "onSpeechRmsChanged: "+value);
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        Log.d(TAG, "onSpeechPartialResults: ");
        for (String partial : results) {
            Log.d(TAG, partial+"");
        }
    }

    @Override
    public void onSpeechResult(String result) {
        Log.d("result", result+"");
        if (!TextUtils.isEmpty(result)) {
            String res = result.toLowerCase();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            if (res.contains("oke") || res.contains("Oke")){
                sendBroadcastToJenyCoreReciever();
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
//        Log.d(TAG, "onTaskRemoved: ");
//        PendingIntent service =
//                PendingIntent.getService(getApplicationContext(), new Random().nextInt(),
//                        new Intent(getApplicationContext(), JenyCore.class), PendingIntent.FLAG_ONE_SHOT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
//        super.onTaskRemoved(rootIntent);
    }

    private void muteBeepSoundOfRecorder() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (amanager != null) {
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            amanager.setStreamMute(AudioManager.STREAM_RING, true);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    private void sendBroadcastToJenyCoreReciever(){
        Intent jenyCore = new Intent("JenyCoreReciever");
        jenyCore.putExtra("action","stop");
        sendBroadcast(jenyCore);
    }
}

package com.ailadev.kingkong2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ailadev.kingkong2.services.JenyCore;
import com.ailadev.kingkong2.services.TestService;
import com.ailadev.kingkong2.util.PermissionUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Intent voice;
    private static final String TAG = "MainActivity";
    private SpeechRecognizer speechRecognizer;
    private PermissionUtil permissionUtil = new PermissionUtil(this);
    private static final String[] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionUtil
                .setPermissions(permissions)
                .check();
        button = findViewById(R.id.pencetan);
        startJenyCore();
//        testService();
        setSpeechRecognizer();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(),TestService.class));
            }
        });
    }

    private void testService(){
        Intent intent = new Intent(this, TestService.class);
        startService(intent);
    }

    //    Speech recognizer intent
    public void setIntentSpeech(){
        String language = "id_ID";
        voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getClass().getPackage().getName());
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,language);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE,language);
        voice.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
    }

//    init speech recognizer
    public void setSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        setIntentSpeech();
        speechRecognizer.startListening(voice);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {
                Log.d(TAG, "onRmsChanged: "+v);
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList results = bundle.getStringArrayList
                        (SpeechRecognizer.RESULTS_RECOGNITION);
                if (results.get(0).equals("lihat saldo")){
//                    startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                }
                Toast.makeText(MainActivity.this, results.get(0).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void startJenyCore(){
        Log.d(TAG, "startJenyService: ");
        startService(new Intent(getApplicationContext(),JenyCore.class));
    }

}

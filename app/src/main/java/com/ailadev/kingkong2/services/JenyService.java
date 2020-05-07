package com.ailadev.kingkong2.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

public class JenyService implements RecognitionListener {

    private Context context;
    private static final String TAG = "JenyService";
    private Intent voice;
    private SpeechRecognizer speechRecognizer;

    public JenyService(Context context){
        this.context = context;
    }

    private void setIntentSpeech(){
        String language = "id_ID";
        voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getClass().getPackage().getName());
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,language);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE,language);
        voice.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
    }

    public void setSpeechRecognizer(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        setIntentSpeech();
        speechRecognizer.startListening(voice);
        speechRecognizer.setRecognitionListener(this);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(TAG, "onReadyForSpeech: ");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech: ");
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

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}

package com.ailadev.kingkong2.reciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ailadev.kingkong2.services.JenyCore;
import com.ailadev.kingkong2.services.JenyService;

import java.util.Random;

public class JenyCoreReciever extends BroadcastReceiver {
    private static final String TAG = "JenyCoreReciever";
    private static JenyService jenyService;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: "+intent.getStringExtra("action"));
        if (intent.getStringExtra("action").equals("stop")){
            jenyService = new JenyService(context);
            jenyService.setSpeechRecognizer();
            context.stopService(new Intent(context, JenyCore.class));
        }else{
            context.startService(new Intent(context, JenyCore.class));
        }
    }
}

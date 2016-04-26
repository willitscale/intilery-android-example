package com.intilery.android.example;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class Receiver extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d("onMessageReceived", "SDFSDFSDFSDFSDFSDFDSF");
        Log.d("onMessageReceived", from);
        Log.d("onMessageReceived", data.toString());
    }
}

package com.drdistributor.dr;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.widget.Toast;

public class Autostart extends BroadcastReceiver {

    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent arg1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, User_location_Services.class));
        } else {
            context.startService(new Intent(context, User_location_Services.class));
        }
        //Toast.makeText(context, "Autostart", Toast.LENGTH_SHORT).show();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, Myservice2.class));
        } else {
            context.startService(new Intent(context, Myservice2.class));
        }*/
    }
}

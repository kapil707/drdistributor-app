package com.drdistributor.dr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //https://www.androidtonight.com/2019/07/run-android-sevice-in-background.html
        //Toast.makeText(context, "RestartService", Toast.LENGTH_SHORT).show();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, Myservice2.class));
        } else {
            context.startService(new Intent(context, Myservice2.class));
        }*/
    }
}

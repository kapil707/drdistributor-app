package com.drdistributor.dr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;



public class BootCompleteReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "BootCompleteReceiver", Toast.LENGTH_SHORT).show();

        /*Intent i = new Intent(context, Test_page.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, User_location_Services.class));
        } else {
            context.startService(new Intent(context, User_location_Services.class));
        }
        /*Intent serviceIntent = new Intent(context, User_location_Services.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        startForegroundService(context, serviceIntent);*/
    }
}
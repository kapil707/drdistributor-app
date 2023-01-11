package com.drdistributor.dr;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class User_location_Services extends Service {

    String session_id = "";

    String main_url = "", page_url1 = "", device_id = "";
    String result = "";
    UserSessionManager session;

    String gettime = "", getdate = "";
    GPSTracker mGPS;
    double latitude1, longitude1;
    String getlatitude = "", getlongitude = "";

    public void mGPS_info() {
        mGPS = new GPSTracker(this);
        mGPS.getLocation();

        latitude1 = mGPS.getLatitude();
        longitude1 = mGPS.getLongitude();

        getlatitude = String.valueOf(latitude1);
        getlongitude = String.valueOf(longitude1);

        java.util.Date noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm:ss"; // 12:00
        //tvTime.setText(DateFormat.format(time, noteTS));

        String date = "dd-MM-yyyy"; // 01 January 2013
        //tvDate.setText(DateFormat.format(date, noteTS));

        gettime = DateFormat.format(time, noteTS) + "";
        getdate = DateFormat.format(date, noteTS) + "";

        startLocationUpdates();
    }

    private LocationRequest mLocationRequest;

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(1);
        mLocationRequest.setNumUpdates(1);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        getlatitude = location.getLatitude()+"";
        getlongitude = location.getLongitude()+"";

        new JSONAsyncTask().execute();
        //String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //Toast.makeText(getApplicationContext(), "start 1", Toast.LENGTH_LONG).show();
        try
        {
            session = new UserSessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            session_id = user.get(UserSessionManager.KEY_USERID);

            MainActivity ma = new MainActivity();
            main_url = ma.main_url;
            page_url1 = main_url+"user_location_services/post";

            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            device_id = android_id;
            mGPS_info();
        }catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "myservice_01"+e.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("D R Distributors Pvt Ltd")
                /*.setContentText("DRD Order Delivery App")*/
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /*@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onBind", Toast.LENGTH_LONG).show();
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "D.R. Distributor",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private class JSONAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            result = "";
            InputStream isr = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url1);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-kavita-zxy"));

                nameValuePairs.add(new BasicNameValuePair("session_id", session_id));

                nameValuePairs.add(new BasicNameValuePair("gettime", gettime));
                nameValuePairs.add(new BasicNameValuePair("getdate", getdate));

                nameValuePairs.add(new BasicNameValuePair("getlatitude", getlatitude));
                nameValuePairs.add(new BasicNameValuePair("getlongitude", getlongitude));

                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
                nameValuePairs.add(new BasicNameValuePair("submit","98c08565401579448aad7c64033dcb4081906dcb"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                isr.close();
                result = stringBuilder.toString();
            } catch (Exception e) {
                // TODO: handle exception
                //mProgressDialog.dismiss();
            }
            try {
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data" + e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            stopForeground(true);
            stopSelf();
        }
    }
}
package com.drdistributor.dr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;

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

public class Test_page extends AppCompatActivity {

    String session_id="",user_password="",user_type="";

    String main_url = "",page_url1="",device_id="";
    String result ="";
    UserSessionManager session;

    String gettime="",getdate = "";
    GPSTracker mGPS;
    double latitude1, longitude1;
    String getlatitude="",getlongitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);

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
    }

    public void mGPS_info()
    {
        mGPS = new GPSTracker(this);
        mGPS.getLocation();

        latitude1 = mGPS.getLatitude();
        longitude1 = mGPS.getLongitude();

        getlatitude  = String.valueOf(latitude1);
        getlongitude = String.valueOf(longitude1);

        java.util.Date noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm:ss"; // 12:00
        //tvTime.setText(DateFormat.format(time, noteTS));

        String date = "dd-MM-yyyy"; // 01 January 2013
        //tvDate.setText(DateFormat.format(date, noteTS));

        gettime = DateFormat.format(time, noteTS)+"";
        getdate = DateFormat.format(date, noteTS)+"";

        new JSONAsyncTask().execute();
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
            finish();
        }
    }
}
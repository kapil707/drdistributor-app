package com.drdistributor.dr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    public String main_url = "https://www.drdistributor.com/new_api/api_mobile45/";
    public String main_url_webview = "https://www.drdistributor.com/new_api/api_mobile_html41/"; // Notification_Service ke page par be alag sau use ho raha ha so plz wha be kar layna change

    //public String main_url = "http://192.168.1.23/drd/drdistributor_com/new_api/api_mobile41/";
    //public String main_url_webview = "http://192.168.1.23/drd/drdistributor_com/new_api/api_mobile_html41/";

    UserSessionManager session;
    String user_session = "", user_password = "", user_type = "", user_altercode = "", user_code = "";

    int versioncode = 0;
    String mainurl = "", page_url1 = "";
    String result = "", device_id = "", firebase_token = "";

    Database db;
    SQLiteDatabase sql;

    String gettime = "", getdate = "";

    String chemist_id = "";
    //https://www.geeksforgeeks.org/session-management-in-android-with-example/
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_CART = "user_cart";
    public static final String USER_CART_TOTAL = "user_cart_total";
    public static final String USER_CART_JSON = "user_cart_json";
    public static final String USER_CHEMIST_ID = "user_chemist_id";
    public static final String USER_CHEMIST_NAME = "user_chemist_name";
    SharedPreferences sharedpreferences;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.menu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        View view = getSupportActionBar().getCustomView();

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_session = user.get(UserSessionManager.KEY_USERID);
        user_code = user.get(UserSessionManager.KEY_USERCODE);
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);
        firebase_token = user.get(UserSessionManager.KEY_firebase_token);
        versioncode = BuildConfig.VERSION_CODE;

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        TextView android_v = findViewById(R.id.android_v);
        int versioncode = BuildConfig.VERSION_CODE;
        android_v.setText("App version " + versioncode);

        /*String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id = android_id;*/

        mGPS_info();

        db = new Database(MainActivity.this);
        sql = db.getWritableDatabase();

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl + "home_page_api/post";

        java.util.Date noteTS = Calendar.getInstance().getTime();
        String time = "hh:mm:ss"; // 12:00
        //tvTime.setText(DateFormat.format(time, noteTS));
        String date = "dd-MM-yyyy"; // 01 January 2013
        //tvDate.setText(DateFormat.format(date, noteTS));
        gettime = DateFormat.format(time, noteTS) + "";
        getdate = DateFormat.format(date, noteTS) + "";

        try {
            if (session.checkLogin()) {
                finish();
            } else {
                if (user_type.equals("corporate")) {
                    Intent in = new Intent();
                    in.setClass(MainActivity.this, Home_page_corporate.class);
                    startActivity(in);
                    finish();
                } else {

                    try {
                        home_page_function();
                    } catch (Exception e) {
                        // TODO: handle exception
                        Toast.makeText(MainActivity.this, "Updating Application please wait....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        try {
            if (!user_type.equals("")) {
                if (user_type.equals("sales")) {
                    chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
                }
            }
        } catch (Exception e) {

        }
    }

    public void home_page_function() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();

            Function_class fc = new Function_class();
            String internet_status = fc.check_internet_status();

            if (ni == null || internet_status.equals("offline")) {

                Intent in = new Intent();
                in.setClass(MainActivity.this, No_internet_page.class);
                startActivity(in);
                finish();

                Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            } else {
                new json_main_function().execute();
            }
        } catch (Exception e) {
            // TODO: handle exception

            Intent in = new Intent();
            in.setClass(MainActivity.this, Under_construction.class);
            startActivity(in);
            finish();
        }
    }

    GPSTracker mGPS;
    double latitude1, longitude1;
    String getlatitude, getlongitude;

    public void mGPS_info() {
        mGPS = new GPSTracker(this);
        mGPS.getLocation();

        latitude1 = mGPS.getLatitude();
        longitude1 = mGPS.getLongitude();

        getlatitude = String.valueOf(latitude1);
        getlongitude = String.valueOf(longitude1);
    }

    private class json_main_function extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {

            result = "";
            InputStream isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url1);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-kavita-zxy"));
                nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));

                nameValuePairs.add(new BasicNameValuePair("phone_type", "Android"));
                nameValuePairs.add(new BasicNameValuePair("firebase_token", firebase_token));
                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));
                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

                nameValuePairs.add(new BasicNameValuePair("versioncode", String.valueOf(versioncode)));
                nameValuePairs.add(new BasicNameValuePair("getlatitude", getlatitude));
                nameValuePairs.add(new BasicNameValuePair("getlongitude", getlongitude));
                nameValuePairs.add(new BasicNameValuePair("gettime", gettime));
                nameValuePairs.add(new BasicNameValuePair("getdate", getdate));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                //Log.e("log_tag","Error in connection"+e.toString());
                //textView3.setText("couldn't connect to the database");
                //mProgressDialog.dismiss();
                //user_alert = "Check your internet";
                //timtim2();
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
                //timtim2();
                //textView3.setText("big error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                JSONArray jArray = new JSONArray(result);

                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String force_update = jsonObject.getString("force_update");
                    String force_update_title = jsonObject.getString("force_update_title");
                    String force_update_message = jsonObject.getString("force_update_message");
                    /**************************************************************/
                    String under_construction = jsonObject.getString("under_construction");
                    String under_construction_message = jsonObject.getString("under_construction_message");

                    if (under_construction.equals("1")) {

                        Intent in = new Intent();
                        in.setClass(MainActivity.this, Under_construction.class);
                        in.putExtra("under_construction_message", under_construction_message);
                        startActivity(in);
                        finish();

                    } else {

                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ni = cm.getActiveNetworkInfo();

                        Function_class fc = new Function_class();
                        String internet_status = fc.check_internet_status();

                        if (ni == null || internet_status.equals("offline")) {

                            Toast.makeText(MainActivity.this, "Offline Mode Activate", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent();
                            in.setClass(MainActivity.this, No_internet_page.class);
                            startActivity(in);
                            finish();

                        } else {
                            if (force_update.equals("1")) {

                                String versioncode1 = jsonObject.getString("versioncode");
                                if (versioncode == Integer.parseInt(versioncode1)) {

                                    Intent in = new Intent();
                                    in.setClass(MainActivity.this, Home_page.class);
                                    in.putExtra("result", result);
                                    startActivity(in);
                                    finish();

                                } else {

                                    Intent in = new Intent();
                                    in.setClass(MainActivity.this, Force_update.class);
                                    in.putExtra("force_update_title", force_update_title);
                                    in.putExtra("force_update_message", force_update_message);
                                    startActivity(in);
                                    finish();

                                }
                            } else {

                                String ratingbarpage = jsonObject.getString("ratingbarpage");
                                if (ratingbarpage.equals("1")) {

                                    Intent in = new Intent();
                                    in.setClass(MainActivity.this, Ratingbar.class);
                                    in.putExtra("result", result);
                                    startActivity(in);
                                    finish();

                                } else {

                                    Intent in = new Intent();
                                    in.setClass(MainActivity.this, Home_page.class);
                                    in.putExtra("result", result);
                                    startActivity(in);
                                    finish();

                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                Intent in = new Intent();
                in.setClass(MainActivity.this, Under_construction.class);
                startActivity(in);
                finish();
            }
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
package com.drdistributor.dr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Place_order extends AppCompatActivity {
    String mobilenumber = "", modalnumber = "";

    String TAG = "PhoneActivityTAG";
    Activity activity = Place_order.this;
    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;

    String user_altercode = "",user_password="",user_type="";
    UserSessionManager session;
    String  chemist_id = "", remarks = "", result = "", main_url = "", page_url1 = "", device_id = "";

    //https://www.geeksforgeeks.org/session-management-in-android-with-example/
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_CART = "user_cart";
    public static final String USER_CART_TOTAL = "user_cart_total";
    public static final String USER_CART_JSON = "user_cart_json";
    public static final String USER_CHEMIST_ID = "user_chemist_id";
    public static final String USER_CHEMIST_NAME = "user_chemist_name";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order);

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

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        if (user_type.equals("sales")) {
            chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
        }

        TextView android_v = findViewById(R.id.android_v);
        int versioncode = BuildConfig.VERSION_CODE;
        android_v.setText("App version " + versioncode);

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("D R Distributor");

        ImageView mysearchbtn = findViewById(R.id.newmysearchbtn);
        mysearchbtn.setVisibility(View.GONE);

        LinearLayout cart_LinearLayout = findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setVisibility(View.GONE);

        MainActivity ma = new MainActivity();
        main_url = ma.main_url;
        page_url1 = main_url + "place_order_api/post";

        try {
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            device_id = android_id;
        } catch (Exception e) {
            device_id = "";
        }
        try {
            modalnumber = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        } catch (Exception e) {
            modalnumber = "";
        }
        try {
            mGPS_info();
        } catch (Exception e) {
        }

        mobilenumber = "000000";
        /*
        try {
            mobilenumber = getPhone();
        } catch (Exception e) {
            mobilenumber = "000000";
        }*/

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        Function_class fc = new Function_class();
        String internet_status = fc.check_internet_status();

        try {
            if (ni == null || internet_status.equals("offline")) {

                Intent myIntent = new Intent(Place_order.this,
                        No_internet_page.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                finish();

            } else {
                byte[] data = new byte[0];
                try {
                    data = remarks.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //e.printStackTrace();
                }
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                remarks = base64;
                new JSON_place_order_api().execute();
            }
        } catch (Exception e) {
            finish();
        }
    }

    GPSTracker mGPS;
    double latitude1, longitude1;
    String latitude="", longitude="";

    public void mGPS_info() {
        try {
            mGPS = new GPSTracker(this);
            mGPS.getLocation();

            latitude1 = mGPS.getLatitude();
            longitude1 = mGPS.getLongitude();

            latitude = String.valueOf(latitude1);
            longitude = String.valueOf(longitude1);
        }catch (Exception e)
        {
            latitude = "";
            longitude = "";
        }
    }

    private class JSON_place_order_api extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));

                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

                nameValuePairs.add(new BasicNameValuePair("remarks", remarks));
                nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
                nameValuePairs.add(new BasicNameValuePair("mobilenumber", mobilenumber));
                nameValuePairs.add(new BasicNameValuePair("modalnumber", modalnumber));

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
            LinearLayout loading_div = findViewById(R.id.loading_div);
            LinearLayout loading_div2 = findViewById(R.id.loading_div2);
            LinearLayout footer_div = findViewById(R.id.footer_div);
            Button go_home_btn = findViewById(R.id.go_home_btn);
            TextView place_order_message_tv = findViewById(R.id.place_order_message);

            loading_div.setVisibility(View.GONE);
            loading_div2.setVisibility(View.VISIBLE);
            footer_div.setVisibility(View.GONE);
            go_home_btn.setVisibility(View.VISIBLE);

            go_home_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
            try {
                if (!result.isEmpty()) {
                    JSONArray jArray0 = new JSONArray(result);
                    JSONObject jsonObject0 = jArray0.getJSONObject(0);
                    String items = jsonObject0.getString("items");

                    JSONArray jArray1 = new JSONArray(items);
                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject jsonObject = jArray1.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        String place_order_message = jsonObject.getString("place_order_message");

                        place_order_message_tv.setText(Html.fromHtml(place_order_message));
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    String place_order_message = "<font color='red'>Sorry your order has been failed please try again.</font>";
                    //place_order_message = Base64.encodeToString(place_order_message.getBytes(), Base64.DEFAULT);

                    place_order_message_tv.setText(Html.fromHtml(place_order_message));
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                String place_order_message = "<font color='red'>Sorry your order has been failed please try again.</font>";
                //place_order_message = Base64.encodeToString(place_order_message.getBytes(), Base64.DEFAULT);

                place_order_message_tv.setText(Html.fromHtml(place_order_message));
            }

            /************************************************************/
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(USER_CART, "0");
            editor.putString(USER_CART_TOTAL, "0.00");
            editor.putString(USER_CART_JSON, "");
            editor.apply();
            /************************************************************/
        }
    }

    @Override
    public void onBackPressed() {
        //finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /*private String getPhone() {
        try {
            TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(activity, wantPermission) != PackageManager.PERMISSION_GRANTED) {
                return "0000000000";
            }
            return phoneMgr.getLine1Number();
        }catch (Exception e)
        {
            return "0000000000";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new JSON_save_order_to_server().execute();
                } else {
                    new JSON_save_order_to_server().execute();
                }
                break;
        }
    }*/
}
package com.drdistributor.dr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class Login extends AppCompatActivity {

    String result ="",firebase_token="";
    String mainurl = "",page_url1="",main_url_webview="";
    Button login_btn,login_btn1;
    EditText user_name,password;
    TextView alert,termsofservice,create_new_btn;
    String user_session="",user_code="",user_fname="",user_altercode="",user_password="",user_type="",user_name1="",password1="",user_image="",user_alert="",user_return="",user_division="",user_compcode="",user_compname="";
    UserSessionManager session;
    ProgressBar progressBar2;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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

        /**************location parmisson************/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        /*******************************************/

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText(getString(R.string.main_title_name));

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        ImageButton action_bar_back= (ImageButton)view.findViewById(R.id.action_bar_back);
        action_bar_back.setVisibility(View.GONE);

        LinearLayout cart_LinearLayout= (LinearLayout)view.findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);
        TextView action_bar_cart_total= (TextView)view.findViewById(R.id.action_bar_cart_total);
        action_bar_cart_total.setVisibility(View.GONE);

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        main_url_webview = ma.main_url_webview;
        page_url1 = mainurl+"login/post";

        try {
            TextView android_v = findViewById(R.id.android_v);
            int versioncode = BuildConfig.VERSION_CODE;
            android_v.setText("App version " + versioncode);
        }catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "Error parsing data"+e.toString());
            //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
        }

        login_btn = findViewById(R.id.login_btn);
        login_btn1 = findViewById(R.id.login_btn1);

        alert = (TextView) findViewById(R.id.user_alert);
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.user_password);
        termsofservice = findViewById(R.id.termsofservice);
        create_new_btn = findViewById(R.id.create_new);
        create_new_btn.setText(Html.fromHtml("Don't have an account? <font color='#27ae60'>Create account</font>"));

        termsofservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*Uri uriUrl = Uri.parse("http://drdmail.xyz/termsofservice");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);*/

                Intent myIntent = new Intent(Login.this,
                        Only_webview.class);
                myIntent.putExtra("page_url", main_url_webview+"termsofservice");
                myIntent.putExtra("page_title", "Terms of services");
                startActivity(myIntent);
            }
        });

        create_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(Login.this,
                        Create_New.class);
                startActivity(myIntent);
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Sucessfull";

                        firebase_token = FirebaseInstanceId.getInstance().getToken();
                        //firebase_token = "";
                        if (firebase_token.equals("")) {
                        } else {

                        }
                        if (!task.isSuccessful()) {
                            msg = "fail";
                        }
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        final CheckBox ck = findViewById(R.id.checkBox);

        session = new UserSessionManager(getApplicationContext());
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    user_name1 = user_name.getText().toString();
                    password1 = password.getText().toString();

                    if (user_name1.length() > 0) {
                        if (password1.length() > 0) {
                            if(ck.isChecked()) {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo ni = cm.getActiveNetworkInfo();
                                if (ni != null) {
                                    try {
                                        new login().execute();
                                        login_btn1.setVisibility(View.VISIBLE);
                                        login_btn.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }

                                } else {
                                    alert.setText(Html.fromHtml("<font color='red'>Check your internet connection</font>"));
                                    Toast.makeText(Login.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                alert.setText(Html.fromHtml("<font color='red'>Check terms of service</font>"));
                                Toast.makeText(Login.this, "Check terms of service", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            alert.setText(Html.fromHtml("<font color='red'>Enter password</font>"));
                            Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        alert.setText(Html.fromHtml("<font color='red'>Enter username</font>"));
                        Toast.makeText(Login.this, "Enter username", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    alert.setText(Html.fromHtml("<font color='red'>button click error</font>"));
                    Toast.makeText(Login.this, "button click error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ImageView eyes = findViewById(R.id.eyes);
        final ImageView eyes1 = findViewById(R.id.eyes1);
        eyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                eyes1.setVisibility(View.GONE);
                eyes.setVisibility(View.VISIBLE);
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                password.setSelection(password.getText().length());
            }
        });

        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                eyes.setVisibility(View.GONE);
                eyes1.setVisibility(View.VISIBLE);
                password.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.getText().length());
            }
        });
    }

    private class login extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            user_altercode = user_name.getText().toString();
            user_password = password.getText().toString();
            result ="";
            InputStream isr = null;
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url1);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id","apiidkapil707sharma-kavita-zxy"));

                nameValuePairs.add(new BasicNameValuePair("user_altercode",user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password",user_password));
                    nameValuePairs.add(new BasicNameValuePair("submit","98c08565401579448aad7c64033dcb4081906dcb"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            }
            catch (Exception e) {
                //Log.e("log_tag","Error in connection"+e.toString());
                //tv.setText("couldn't connect to the database");
                user_alert = "Check your internet";
            }

            try {
                BufferedReader reader= new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine())!=null) {
                    stringBuilder.append(line+"\n");
                }
                isr.close();
                result = stringBuilder.toString();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            //Toast.makeText(Login.this,result,Toast.LENGTH_SHORT).show();
            login_btn.setVisibility(View.VISIBLE);
            login_btn1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            String otp_type = "0";
            String otp_sms = "";
            String otp_massage_txt = "";
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    user_session    = jsonObject.getString("user_session");
                    user_fname      = jsonObject.getString("user_fname");
                    user_code       = jsonObject.getString("user_code");
                    user_altercode  = jsonObject.getString("user_altercode");
                    user_type       = jsonObject.getString("user_type");
                    user_password   = jsonObject.getString("user_password");
                    user_image      = jsonObject.getString("user_image");
                    user_alert      = jsonObject.getString("user_alert");
                    user_return     = jsonObject.getString("user_return");
                    otp_type        = jsonObject.getString("otp_type");
                    otp_sms         = jsonObject.getString("otp_sms");
                    otp_massage_txt = jsonObject.getString("otp_massage_txt");
                    /*user_division   = jsonObject.getString("user_division");
                    user_compcode   = jsonObject.getString("user_compcode");
                    user_compname   = jsonObject.getString("user_compname");*/
                }
            }
            catch (Exception e) {
                // TODO: handle exception
                //Log.e("log_tag", "Error parsing data"+e.toString());
                //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(Login.this,user_alert,Toast.LENGTH_SHORT).show();
            if(user_return.equals("1"))
            {
                alert.setText(Html.fromHtml("<font color='#28a745'>"+user_alert+"</font>"));

                if(otp_type.equals("0")) {
                    session.createUserLoginSession(user_session, user_password, user_type, user_fname, user_code, user_altercode, user_image, user_division, user_compcode, user_compname, firebase_token);

                    Intent in = new Intent();
                    in.setClass(Login.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }
                else
                {
                    Intent in = new Intent();
                    in.setClass(Login.this, Otp_page.class);
                    in.putExtra("result", result);
                    in.putExtra("firebase_token", firebase_token);
                    startActivity(in);
                    finish();
                }
            }
            else
            {
                alert.setText(Html.fromHtml("<font color='red'>"+user_alert+"</font>"));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try
        {
            finish();
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
    public String getLocalIpAddress()
    {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        //return inetAddress.getHostAddress().toString();
                        @SuppressWarnings("deprecation")
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (Exception ex) {
            //Log.e("IP Address", ex.toString());
        }
        return null;
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
}
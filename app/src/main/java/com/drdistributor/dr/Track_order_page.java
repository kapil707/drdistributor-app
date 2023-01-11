package com.drdistributor.dr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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
import java.util.HashMap;
import java.util.List;

public class Track_order_page extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener , OnMapReadyCallback, TaskLoadedCallback {


    //https://demonuts.com/android-google-map-draw-path-between-current-location-and-destination/https://demonuts.com/android-google-map-draw-path-between-current-location-and-destination/
    //variables for map and route

    //variables for map and route

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection1, getDirection2;
    private Polyline currentPolyline;
    private MapFragment mapFragment;
    private boolean isFirstTime = true;

    //variables for current location
    private static final String TAG = "MainActivity";

    private LinearLayout sec1,sec2,sec3;
    private Button biker_mobile,searchmedicine;
    private TextView map_time,biker_name,page_msg,page_msg1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private final Handler handler_myservice_page = new Handler();
    private long UPDATE_INTERVAL = 15 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    String result = "";
    double latitude1, longitude1;
    double latitude2=28.5183163, longitude2= 77.279475;

    int loadmap1 = 0, loadmap2 = 0;

    ProgressBar menu_loading1;
    UserSessionManager session;

    String user_altercode="",user_type="",user_fname="",user_password = "";
    String mainurl="",page_url1="";
    int page_off = 0,page_off_1 = 0;
    int showonlyone = 1;

    //https://www.geeksforgeeks.org/session-management-in-android-with-example/
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_CART = "user_cart";
    public static final String USER_CART_TOTAL = "user_cart_total";
    public static final String USER_CART_JSON = "user_cart_json";
    public static final String USER_CHEMIST_ID = "user_chemist_id";
    public static final String USER_CHEMIST_NAME = "user_chemist_name";
    SharedPreferences sharedpreferences;
    String chemist_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_order_page);

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

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("Track order");

        menu_loading1 = (ProgressBar) findViewById(R.id.menu_loading1);

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_fname = properCase(user.get(UserSessionManager.KEY_USERFNAME));
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        if (user_type.equals("sales")) {
            chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
        }

        user_fname = "Your location "+user_fname + "("+user_altercode+")";

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl + "track_my_order/post/";

        page_off_1 = 0;
        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_off_1 = 1;
                finish();
            }
        });

        ImageView mysearchbtn = findViewById(R.id.newmysearchbtn);
        mysearchbtn.setVisibility(View.VISIBLE);
        mysearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Track_order_page.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Track_order_page.this, My_cart.class);
                startActivity(in);
            }
        });

        //code for getting current location
        requestMultiplePermissions();

        map_time = (TextView) findViewById((R.id.map_time));
        biker_name = (TextView) findViewById((R.id.biker_name));
        biker_mobile = findViewById((R.id.biker_mobile));
        biker_mobile.setVisibility(View.GONE);
        page_msg = findViewById((R.id.page_msg));
        page_msg1 = findViewById((R.id.page_msg1));

        sec1 = findViewById((R.id.sec1));
        sec2 = findViewById((R.id.sec2));
        sec3 = findViewById((R.id.sec3));
        sec1.setVisibility(View.GONE);
        sec2.setVisibility(View.GONE);
        sec3.setVisibility(View.GONE);
        searchmedicine = findViewById((R.id.searchmedicine));
        searchmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Track_order_page.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        /* place1 = new MarkerOptions().position(new LatLng(x, y)).title("Your Location");
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);*/

        getDirection1 = findViewById(R.id.btnGetDirection1);
        getDirection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new track_my_order().execute();

                BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.marker_a);
                BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_b);

                GPSTracker mGPS = new GPSTracker(Track_order_page.this);
                mGPS.getLocation();
                latitude1 = mGPS.getLatitude();
                longitude1 = mGPS.getLongitude();
                //editText1.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());

                place1 = new MarkerOptions().position(new LatLng(latitude1, longitude1)).title(user_fname).icon(icon1);
                place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("Delivery Boy").icon(icon2);
                mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
                mapFragment.getMapAsync(Track_order_page.this);

                Double xx = distance1(latitude1, longitude1, latitude2, longitude2, "K");
                String xyz = String.format("%.2f", xx);

                double total_sec = cal_time(xx, 80);

                int seconds = (int) total_sec;
                int p1 = seconds % 60;
                int p2 = seconds / 60;
                int p3 = p2 % 60;
                p2 = p2 / 60;

                //tvLocation.setText("Distance: " + xyz + " km \n Time: " + p2 + " Hr " + p3 + " Min " + p1+" Sec ");
                //map_time.setText("Your Rider is: " + xyz + " KM away \nExpected Time: " + p2 + " Hr " + p3 + " Min ");
                if(p2==0) {
                    map_time.setText(p3 + " Mins ");
                }else{
                    map_time.setText(p2 + " Hr " + p3 + " Mins ");
                }
                loadmap1 = 1;
                page_off_1 = 0;
            }
        });

        getDirection2 = findViewById(R.id.btnGetDirection2);
        getDirection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new track_my_order().execute();

                BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.marker_a);
                BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_b);

                GPSTracker mGPS = new GPSTracker(Track_order_page.this);
                mGPS.getLocation();
                latitude1 = mGPS.getLatitude();
                longitude1 = mGPS.getLongitude();
                //editText1.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());

                place1 = new MarkerOptions().position(new LatLng(latitude1, longitude1)).title(user_fname).icon(icon1);
                place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("Delivery Boy").icon(icon2);
                mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
                mapFragment.getMapAsync(Track_order_page.this);

                Double xx = distance1(latitude1, longitude1, latitude2, longitude2, "K");
                String xyz = String.format("%.2f", xx);

                double total_sec = cal_time(xx, 80);

                int seconds = (int) total_sec;
                int p1 = seconds % 60;
                int p2 = seconds / 60;
                int p3 = p2 % 60;
                p2 = p2 / 60;

                //tvLocation.setText("Distance: " + xyz + " km \n Time: " + p2 + " Hr " + p3 + " Min " + p1+" Sec ");
                //map_time.setText("Your Rider is: " + xyz + " KM away \nExpected Time: " + p2 + " Hr " + p3 + " Min ");
                if(p2==0) {
                    map_time.setText(p3 + " Mins ");
                }else{
                    map_time.setText(p2 + " Hr " + p3 + " Mins ");
                }
                loadmap2 = 1;
                page_off_1 = 0;
            }
        });

        handler_myservice_page.postDelayed(myservice_page,UPDATE_INTERVAL);
        getDirection1.callOnClick();
        //new track_my_order().execute();
    }

    String properCase(String inputVal) {
        if (inputVal.length() == 0) return "";
        if (inputVal.length() == 1) return inputVal.toUpperCase();
        return inputVal.substring(0, 1).toUpperCase()
                + inputVal.substring(1).toLowerCase();
    }

    //code for drawing route

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        //Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        mMap.addMarker(place2);

        mMap.getUiSettings().setTiltGesturesEnabled(false);
        if (loadmap1 == 1) {
            LatLng latLng = new LatLng(latitude1, longitude1);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(latitude1, longitude1))
                    .zoom(12)
                    .bearing(0)
                    .tilt(45)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
            loadmap1 = 2;

            /*mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(latitude1,longitude1), new LatLng(Float.parseFloat(latitude), Float.parseFloat(longitude)))
                    .width(5)
                    .color(Color.RED));*/
        }

        if (loadmap2 == 1) {

            LatLng latLng = new LatLng(latitude2, longitude2);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(latitude2, longitude2))
                    .zoom(12)
                    .bearing(0)
                    .tilt(45)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
            loadmap2 = 2;
        }

        /*mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(latitude1, longitude1), new LatLng(latitude2, longitude2))
                .width(5)
                .color(Color.RED));

        /*mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(place1..getLatitude(), location.getLongitude()), new LatLng(29.5816642,74.3333333))
                .width(5)
                .color(Color.RED));*/

        /*CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(Float.parseFloat(latitude), Float.parseFloat(longitude)))
                .zoom(7)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);*/
    }

    /*
    @NonNull
    private String get_loc_fun(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + this.getString(R.string.google_maps_key);
        return url;
    }*/

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        //PolylineOptions.color(Color.RED);
    }

    //runtime permission method

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Track_order_page.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    //methods for getting current location

    @Override
    public void onConnected(Bundle bundle) {
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

        //startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            //startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /*protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }*/

    @Override
    public void onLocationChanged(Location location) {

    }

    private static double distance1(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    private double cal_time(double dist, double speed) {
        return speed * dist;
    }

    private class track_my_order extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_password));

                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                //Log.e("log_tag","Error in connection"+e.toString());
                //tv.setText("couldn't connect to the database");
                //user_alert = "Check your internet";
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            //Toast.makeText(Track_order_page.this,result,Toast.LENGTH_SHORT).show();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);

                    String latitude = jsonObject.getString("latitude");
                    String longitude = jsonObject.getString("longitude");
                    String _page_off = jsonObject.getString("page_off");
                    String _page_msg = jsonObject.getString("page_msg");
                    String _page_msg1 = jsonObject.getString("page_msg1");
                    String _biker_name = jsonObject.getString("biker_name");
                    final String _biker_mobile = jsonObject.getString("biker_mobile");
                    String _biker_image = jsonObject.getString("biker_image");

                    latitude2 = Double.parseDouble(latitude);
                    longitude2 = Double.parseDouble(longitude);
                    page_off = Integer.parseInt(_page_off);

                    sec3.setVisibility(View.GONE);
                    if(page_off==0) {
                        sec1.setVisibility(View.VISIBLE);
                        sec2.setVisibility(View.VISIBLE);
                    }
                    if(page_off==1) {
                        sec1.setVisibility(View.GONE);
                        sec2.setVisibility(View.GONE);
                        sec3.setVisibility(View.VISIBLE);
                    }
                    if(page_off==2) {
                        sec1.setVisibility(View.GONE);
                        sec2.setVisibility(View.GONE);
                    }

                    biker_name.setText(_biker_name);
                    page_msg.setText(_page_msg);
                    page_msg1.setText(_page_msg1);
                    if(!_biker_mobile.isEmpty()){
                        biker_mobile.setVisibility(View.VISIBLE);
                    }
                    biker_mobile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri u = Uri.parse("tel:" + _biker_mobile);
                            Intent i = new Intent(Intent.ACTION_DIAL, u);
                            try {
                                startActivity(i);
                            } catch (SecurityException s) {
                                Toast.makeText(getApplicationContext(), "error phone", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("log_tag", "Error parsing data"+e.toString());
                Toast.makeText(Track_order_page.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(Map_page.this,"okok h",Toast.LENGTH_SHORT).show();

            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.marker_a);
            BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_b);

            //new FetchURL(Track_order_page.this).execute(get_loc_fun(place1.getPosition(), place2.getPosition(), "driving"), "driving");

            place1 = new MarkerOptions().position(new LatLng(latitude1, longitude1)).title(user_fname).icon(icon1);
            place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("Delivery Boy").icon(icon2);
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
            mapFragment.getMapAsync(Track_order_page.this);

            Double xx = distance1(latitude1, longitude1, latitude2, longitude2, "K");
            String xyz = String.format("%.2f", xx);

            double total_sec = cal_time(xx, 80);

            int seconds = (int) total_sec;
            int p1 = seconds % 60;
            int p2 = seconds / 60;
            int p3 = p2 % 60;
            p2 = p2 / 60;

            //tvLocation.setText("Distance: " + xyz + " km \n Time: " + p2 + " Hr " + p3 + " Min " + p1+" Sec ");
            //map_time.setText("Your Rider is: " + xyz + " KM away \nExpected Time: " + p2 + " Hr " + p3 + " Min ");
            if(p2==0) {
                map_time.setText(p3 + " Mins. ");
            }else{
                map_time.setText(p2 + " Hr. " + p3 + " Mins. ");
            }
            if(showonlyone==1)
            {
                showonlyone = 2;
                getDirection2.callOnClick();
            }
        }
    }

    private Runnable myservice_page = new Runnable()
    {
        public void run()
        {
            try
            {
                handler_myservice_page.postDelayed(myservice_page,UPDATE_INTERVAL);
                //Toast.makeText(getApplicationContext(), "chemist_id", Toast.LENGTH_LONG).show();
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null) {
                    try {
                        if(page_off==0 && page_off_1==0) {
                            new track_my_order().execute();
                        }
                    } catch (Exception e) {

                    }
                }
            }catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "myservice_03"+e.toString());
            }
        }
    };

    private void get_user_cart() {
        try {
            String user_cart = sharedpreferences.getString(USER_CART, null);

            TextView action_bar_cart_total = findViewById(R.id.action_bar_cart_total);
            action_bar_cart_total.setText(" " + user_cart + " ");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error get_user_cart" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        get_user_cart();
    }

    @Override
    public void onBackPressed() {
        page_off_1 = 1;
        finish();
    }
}
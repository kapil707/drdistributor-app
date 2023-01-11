package com.drdistributor.dr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.Locale;

public class Home_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String android_mobile1 = "+919899133989", android_email1 = "vipul@drdindia.com", android_whatsapp1 = "", android_noti1 = "";
    double latitude1, longitude1;

    final Context context = this;
    LinearLayout menu_search_div_btn;
    //private Myservice2 mYourService;
    NavigationView navigationView;
    WebView top_flash_webView, bottom_flash_webView;

    String internet_status = "";

    String mainurl = "", main_url_webview = "";
    String user_fname = "", user_altercode = "", user_type = "", user_code = "", user_password = "", user_image = "";
    UserSessionManager session;

    String result = "";
    String page_url4 = "",device_id = "";
    int versioncode = 0;

    List<Homepage_box_get_or_set> Homepage_menu_get_or_set_List = new ArrayList<Homepage_box_get_or_set>();
    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List0 = new ArrayList<Homepage_box_get_or_set>();

    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List1 = new ArrayList<Homepage_box_get_or_set>();
    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List2 = new ArrayList<Homepage_box_get_or_set>();
    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List3 = new ArrayList<Homepage_box_get_or_set>();
    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List4 = new ArrayList<Homepage_box_get_or_set>();
    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List5 = new ArrayList<Homepage_box_get_or_set>();
    List<Homepage_box_get_or_set> Homepage_box_get_or_set_List6 = new ArrayList<Homepage_box_get_or_set>();

    RecyclerView Homepage_menu_recyclerview, Homepage_box_recyclerview0,
            Homepage_box_recyclerview1, Homepage_box_recyclerview2,
            Homepage_box_recyclerview3, Homepage_box_recyclerview4,
            Homepage_box_recyclerview5,Homepage_box_recyclerview6;
    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter Homepage_menu_adapter,Homepage_box_adapter0,
            Homepage_box_adapter1,Homepage_box_adapter2,
            Homepage_box_adapter3,Homepage_box_adapter4,
            Homepage_box_adapter5,Homepage_box_adapter6;

    ListView My_invoice_listView;
    My_invoice_Adapter My_invoice_adapter;
    List<My_invoice_get_or_set> My_invoice_List = new ArrayList<My_invoice_get_or_set>();

    ListView My_notification_listView;
    My_notification_Adapter My_notification_adapter;
    List<My_notification_get_or_set> My_notification_List = new ArrayList<My_notification_get_or_set>();

    TextToSpeech t1s;

    //https://www.geeksforgeeks.org/session-management-in-android-with-example/
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_CART = "user_cart";
    public static final String USER_CART_TOTAL = "user_cart_total";
    public static final String USER_CART_JSON = "user_cart_json";
    public static final String USER_CHEMIST_ID = "user_chemist_id";
    public static final String USER_CHEMIST_NAME = "user_chemist_name";
    SharedPreferences sharedpreferences;
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        //getActionBar().setElevation(0);
        //getSupportActionBar().setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        Menu menu1 = navigationView1.getMenu();
        MenuItem tools1 = menu1.findItem(R.id.tools1);
        SpannableString s1 = new SpannableString(tools1.getTitle());
        s1.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s1.length(), 0);
        tools1.setTitle(s1);
        navigationView1.setNavigationItemSelectedListener(this);

        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
        Menu menu2 = navigationView2.getMenu();
        MenuItem tools2 = menu2.findItem(R.id.tools2);
        SpannableString s2 = new SpannableString(tools2.getTitle());
        s2.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s2.length(), 0);
        tools2.setTitle(s2);
        navigationView2.setNavigationItemSelectedListener(this);

        /**************mobile number parmisson*******
         if (!checkPermission(wantPermission)) {
         requestPermission(wantPermission);
         }
         /*******************************************/

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("student");

        HashMap<String,String> student=new HashMap<>();

        student.put("RollNo","1");

        student.put("Name","Jayesh");

        databaseReference.setValue(student);*/


        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        main_url_webview = ma.main_url_webview;
        //page_url1 = mainurl + "check_noti/post";
        page_url4 = mainurl + "logout_online/post";

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_fname = (user.get(UserSessionManager.KEY_USERFNAME));
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_code = user.get(UserSessionManager.KEY_USERCODE);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);
        user_image = user.get(UserSessionManager.KEY_USERIMAGE);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        TextView home_title = (TextView) findViewById(R.id.home_title);
        home_title.setText("Welcome " + user_fname);

        View headerView = navigationView.getHeaderView(0);
        TextView leftmenu_name1 = (TextView) headerView.findViewById(R.id.leftmenu_name1);
        TextView u_name = (TextView) headerView.findViewById(R.id.u_name);
        TextView u_code = (TextView) headerView.findViewById(R.id.u_code);
        u_name.setText(user_fname);
        u_code.setText("Code : " + user_altercode);

        try {
            ImageView left_logo = headerView.findViewById(R.id.left_logo);
            Picasso.with(this).load(user_image).into(left_logo);
        } catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        Function_class fc = new Function_class();
        internet_status = fc.check_internet_status();

        TextView action_bar_title = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title.setText("Delivering to");
        if (ni == null || internet_status.equals("offline")) {
            internet_status = "offline";
        } else {
            internet_status = "Online";
        }

        TextView action_bar_title1 = findViewById(R.id.action_bar_title1);
        action_bar_title1.setText(user_fname);
        action_bar_title1.setVisibility(View.VISIBLE);

        String chemist_id = "";
        try {
            if (!user_type.equals("")) {
                if (user_type.equals("sales")) {
                    chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
                    if (chemist_id.equals("")) {
                        Intent inn = new Intent();
                        inn.setClass(Home_page.this, Chemist_search.class);
                        startActivity(inn);
                        finish();
                    }
                    action_bar_title1.setText("Code : " + chemist_id +" | ");

                    ImageView edit_chemist = findViewById(R.id.edit_chemist);
                    edit_chemist.setVisibility(View.VISIBLE);

                    action_bar_title1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /****************logout only chemist user ke liya sales man ke time
                             * ********************/
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent inn = new Intent();
                            inn.setClass(Home_page.this, MainActivity.class);
                            startActivity(inn);
                            finish();
                            /************************************************************/
                        }
                    });

                    edit_chemist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /****************logout only chemist user ke liya sales man ke time
                             * ********************/
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent inn = new Intent();
                            inn.setClass(Home_page.this, Chemist_search.class);
                            startActivity(inn);
                            finish();
                            /************************************************************/
                        }
                    });
                }
            }
        }catch (Exception e)
        {
            Intent in = new Intent();
            in.setClass(Home_page.this, Chemist_search.class);
            startActivity(in);
            finish();
        }

        /************************************************/
        Intent in2 = getIntent();
        result = in2.getStringExtra("result");
        /************************************************/

        Homepage_menu_recyclerview = (RecyclerView) findViewById(R.id.Homepage_menu_recyclerview);
        Homepage_box_recyclerview0 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview0);

        Homepage_box_recyclerview1 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview1);
        Homepage_box_recyclerview2 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview2);
        Homepage_box_recyclerview3 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview3);
        Homepage_box_recyclerview4 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview4);
        Homepage_box_recyclerview5 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview5);
        Homepage_box_recyclerview6 = (RecyclerView) findViewById(R.id.Homepage_box_recyclerview6);

        Homepage_menu_recyclerview.setHasFixedSize(true);
        Homepage_box_recyclerview0.setHasFixedSize(true);

        Homepage_box_recyclerview1.setHasFixedSize(true);
        Homepage_box_recyclerview2.setHasFixedSize(true);
        Homepage_box_recyclerview3.setHasFixedSize(true);
        Homepage_box_recyclerview4.setHasFixedSize(true);
        Homepage_box_recyclerview5.setHasFixedSize(true);
        Homepage_box_recyclerview6.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(recyclerViewlayoutManager);

        Homepage_menu_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_menu_adapter = new RecyclerViewAdapter0(Homepage_menu_get_or_set_List, this);
        Homepage_menu_recyclerview.setAdapter(Homepage_menu_adapter);

        Homepage_box_recyclerview0.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter0 = new RecyclerViewAdapter1(Homepage_box_get_or_set_List0, this);
        Homepage_box_recyclerview0.setAdapter(Homepage_box_adapter0);

        Homepage_box_recyclerview1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter1 = new RecyclerViewAdapter2(Homepage_box_get_or_set_List1, this);
        Homepage_box_recyclerview1.setAdapter(Homepage_box_adapter1);

        Homepage_box_recyclerview2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter2 = new RecyclerViewAdapter2(Homepage_box_get_or_set_List2, this);
        Homepage_box_recyclerview2.setAdapter(Homepage_box_adapter2);

        Homepage_box_recyclerview3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter3 = new RecyclerViewAdapter2(Homepage_box_get_or_set_List3, this);
        Homepage_box_recyclerview3.setAdapter(Homepage_box_adapter3);

        Homepage_box_recyclerview4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter4 = new RecyclerViewAdapter2(Homepage_box_get_or_set_List4, this);
        Homepage_box_recyclerview4.setAdapter(Homepage_box_adapter4);

        Homepage_box_recyclerview5.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter5 = new RecyclerViewAdapter2(Homepage_box_get_or_set_List5, this);
        Homepage_box_recyclerview5.setAdapter(Homepage_box_adapter5);

        Homepage_box_recyclerview6.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Homepage_box_adapter6 = new RecyclerViewAdapter2(Homepage_box_get_or_set_List6, this);
        Homepage_box_recyclerview6.setAdapter(Homepage_box_adapter6);


        /**********************new box create in 03-01-2023***********/
        My_invoice_listView = findViewById(R.id.My_invoice_listView1);
        My_invoice_adapter = new My_invoice_Adapter(Home_page.this, My_invoice_List);
        My_invoice_listView.setAdapter(My_invoice_adapter);

        My_notification_listView = findViewById(R.id.My_notification_listView1);
        My_notification_adapter = new My_notification_Adapter(Home_page.this, My_notification_List);
        My_notification_listView.setAdapter(My_notification_adapter);
        /*************************************************************/

        ImageButton mic_btn = findViewById(R.id.mic_btn);
        mic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1s=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            t1s.setLanguage(new Locale("hin", "IN"));
                        }
                    }
                });

                String toSpeak = "mic me medicine ka name bolo";
                t1s.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                Intent myIntent = new Intent(Home_page.this,
                        Medicine_search_mic.class);
                startActivity(myIntent);
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Home_page.this,
                        My_cart.class);
                startActivity(myIntent);
            }
        });

        ImageButton logobtn = (ImageButton) findViewById(R.id.logobtn);
        logobtn.setVisibility(View.VISIBLE);
        logobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        menu_search_div_btn = (LinearLayout) findViewById(R.id.menu_search_div_btn);
        menu_search_div_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Home_page.this, Medicine_search.class);
                startActivity(myIntent);
            }
        });

        LinearLayout home_page_btn_div1 = (LinearLayout) findViewById(R.id.home_page_btn_div1);
        LinearLayout home_page_btn_div2 = (LinearLayout) findViewById(R.id.home_page_btn_div2);
        LinearLayout home_page_btn_div3 = (LinearLayout) findViewById(R.id.home_page_btn_div3);
        LinearLayout home_page_btn_div4 = (LinearLayout) findViewById(R.id.home_page_btn_div4);
        LinearLayout home_page_btn_div5 = (LinearLayout) findViewById(R.id.home_page_btn_div5);
        LinearLayout home_page_btn_div6 = (LinearLayout) findViewById(R.id.home_page_btn_div6);

        LinearLayout footer_user_account_btn = (LinearLayout) findViewById(R.id.footer_user_account_btn);
        LinearLayout footer_track_order_btn = (LinearLayout) findViewById(R.id.footer_track_order_btn);
        LinearLayout footer_add_new_btn = (LinearLayout) findViewById(R.id.footer_add_new_btn);
        LinearLayout footer_notification_btn = (LinearLayout) findViewById(R.id.footer_notification_btn);
        LinearLayout footer_reload_btn = (LinearLayout) findViewById(R.id.footer_reload_btn);

        home_page_btn_div1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (user_type.equals("sales")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Chemist_search.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this, Medicine_search.class);
                    startActivity(myIntent);
                }
            }
        });

        home_page_btn_div2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(Home_page.this,
                        My_cart.class);
                startActivity(myIntent);
            }
        });

        home_page_btn_div3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            My_order.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        home_page_btn_div4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            My_invoice.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        home_page_btn_div5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Track_order_page.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        home_page_btn_div6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            My_notification.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        top_flash_webView = (WebView) findViewById(R.id.top_flash_webView);
        top_flash_webView.loadUrl(main_url_webview + "slider");

        bottom_flash_webView = (WebView) findViewById(R.id.bottom_flash_webView);
        bottom_flash_webView.loadUrl(main_url_webview + "slider2");

        /*************offline mode value****************************/
        if (internet_status.equals("Online")) {

        } else {

            ImageView can_not_internet_img = findViewById(R.id.can_not_internet_img);
            can_not_internet_img.setVisibility(View.VISIBLE);

            TextView internet_offline_txt = findViewById(R.id.internet_offline_txt);
            internet_offline_txt.setVisibility(View.VISIBLE);

            top_flash_webView.setVisibility(View.GONE);
            bottom_flash_webView.setVisibility(View.GONE);

            LinearLayout topmenu = findViewById(R.id.topmenu);
            topmenu.setVisibility(View.GONE);

            LinearLayout Homepage_box0 = findViewById(R.id.Homepage_box0);
            Homepage_box0.setVisibility(View.GONE);

            LinearLayout Homepage_box1 = findViewById(R.id.Homepage_box1);
            Homepage_box1.setVisibility(View.GONE);

            LinearLayout Homepage_box2 = findViewById(R.id.Homepage_box2);
            Homepage_box2.setVisibility(View.GONE);

            LinearLayout Homepage_box3 = findViewById(R.id.Homepage_box3);
            Homepage_box3.setVisibility(View.GONE);

            LinearLayout Homepage_box4 = findViewById(R.id.Homepage_box4);
            Homepage_box4.setVisibility(View.GONE);

            LinearLayout footer_call_btn = findViewById(R.id.footer_call_btn);
            footer_call_btn.setVisibility(View.GONE);

            LinearLayout footer_email_btn = findViewById(R.id.footer_email_btn);
            footer_email_btn.setVisibility(View.GONE);

            LinearLayout footer_share_btn = findViewById(R.id.footer_share_btn);
            footer_share_btn.setVisibility(View.GONE);

            LinearLayout footer_version_available_btn = findViewById(R.id.footer_version_available_btn);
            footer_version_available_btn.setVisibility(View.GONE);

            LinearLayout top_flash_webView_div = findViewById(R.id.top_flash_webView_div);
            top_flash_webView_div.setVisibility(View.GONE);

            LinearLayout bottom_flash_webView_div = findViewById(R.id.bottom_flash_webView_div);
            bottom_flash_webView_div.setVisibility(View.GONE);
        }

        top_flash_webView.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface"); // To call methods in Android from using js in the html, AndroidInterface.showToast, AndroidInterface.getAndroidVersion etc
        WebSettings webSettings = top_flash_webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        top_flash_webView.setWebViewClient(new MyWebViewClient());
        top_flash_webView.getSettings().setDomStorageEnabled(true);
        top_flash_webView.addJavascriptInterface(new WebAppInterface(this), "android");
        top_flash_webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    setTitle(R.string.app_name);
            }
        });

        bottom_flash_webView.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface"); // To call methods in Android from using js in the html, AndroidInterface.showToast, AndroidInterface.getAndroidVersion etc
        WebSettings webSettings1 = bottom_flash_webView.getSettings();
        webSettings1.setJavaScriptEnabled(true);
        bottom_flash_webView.setWebViewClient(new MyWebViewClient());
        bottom_flash_webView.getSettings().setDomStorageEnabled(true);
        bottom_flash_webView.addJavascriptInterface(new WebAppInterface(this), "android");
        bottom_flash_webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    setTitle(R.string.app_name);
            }
        });
        versioncode = BuildConfig.VERSION_CODE;
        leftmenu_name1.setText("App Version " + versioncode);
        /*TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        device_id = telephonyManager.getDeviceId();*/

        /*String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id = android_id;*/

        /**************************************/
        home_page_json_result();
        /*************************************/

        footer_user_account_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            User_Account.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        footer_track_order_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Track_order_page.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        footer_add_new_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (user_type.equals("sales")) {
                        Intent myIntent = new Intent(Home_page.this,
                                Chemist_search.class);
                        startActivity(myIntent);
                    } else {
                        //Intent myIntent = new Intent(Home_page.this, Search_medicine.class);
                        Intent myIntent = new Intent(Home_page.this, Medicine_search.class);
                        startActivity(myIntent);
                    }
                } catch (SecurityException s) {
                    Toast.makeText(getApplicationContext(), "error footer_add_new_btn", Toast.LENGTH_LONG).show();
                }
            }
        });

        footer_notification_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            My_notification.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        footer_reload_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent myIntent = new Intent(Home_page.this,
                        MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });


        //new JSONAsyncTask().execute();
        //Toast.makeText(getApplicationContext(), kp, Toast.LENGTH_LONG).show();

        LinearLayout footer_share_btn = findViewById(R.id.footer_share_btn);
        footer_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent it = new Intent(android.content.Intent.ACTION_VIEW);
                it.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.drdistributor.dr"));
                startActivity(it);
                finish();*/

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.drdistributor.dr";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download The New D R Distributor App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        TextView Homepage_title1_view_all = findViewById(R.id.Homepage_title1_view_all);
        TextView Homepage_title2_view_all = findViewById(R.id.Homepage_title2_view_all);
        TextView Homepage_title3_view_all = findViewById(R.id.Homepage_title3_view_all);
        TextView Homepage_title4_view_all = findViewById(R.id.Homepage_title4_view_all);
        TextView Homepage_title5_view_all = findViewById(R.id.Homepage_title5_view_all);
        TextView Homepage_title6_view_all = findViewById(R.id.Homepage_title6_view_all);

        TextView invoice_box_view_all = findViewById(R.id.invoice_box_view_all);
        TextView notification_box_view_all = findViewById(R.id.notification_box_view_all);

        Homepage_title1_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_page_type", "medicine_category1");
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        Homepage_title2_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_page_type", "medicine_category2");
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        Homepage_title3_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_page_type", "medicine_category3");
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        Homepage_title4_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_page_type", "medicine_category4");
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        Homepage_title5_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_page_type", "medicine_category5");
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        Homepage_title6_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_page_type", "medicine_category6");
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        invoice_box_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            My_invoice.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        notification_box_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_status.equals("Online")) {
                    Intent myIntent = new Intent(Home_page.this,
                            My_notification.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Home_page.this,
                            No_internet_page.class);
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void fun_Get_single_medicine_info(String item_code) {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent in = new Intent();
            in.setClass(Home_page.this, Medicine_details.class);
            in.putExtra("item_code", item_code);
            startActivity(in);
        }

        @JavascriptInterface
        public void fun_Featured_brand_medicine_division(String id, String compname, String image, String division) {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Home_page.this,
                    Medicine_category.class);
            myIntent.putExtra("item_code", id);
            myIntent.putExtra("item_division", division);
            myIntent.putExtra("item_page_type","featured_brand");
            startActivity(myIntent);
        }
    }

    //https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page
            view.loadUrl("javascript:alert(showVersion('called by Android'))");
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.my_account) {
            if (internet_status.equals("Online")) {
                Intent myIntent = new Intent(Home_page.this,
                        User_Account.class);
                startActivity(myIntent);
            } else {
                Intent myIntent = new Intent(Home_page.this,
                        No_internet_page.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.my_account_edit) {
            if (internet_status.equals("Online")) {
                Intent myIntent = new Intent(Home_page.this,
                        User_Account_edit.class);
                startActivity(myIntent);
            } else {
                Intent myIntent = new Intent(Home_page.this,
                        No_internet_page.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.my_account_image_uploading) {
            if (internet_status.equals("Online")) {
                Intent myIntent = new Intent(Home_page.this,
                        User_image_uploading.class);
                startActivity(myIntent);
            } else {
                Intent myIntent = new Intent(Home_page.this,
                        No_internet_page.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.my_account_change_password) {
            if (internet_status.equals("Online")) {
                Intent myIntent = new Intent(Home_page.this,
                        User_Change_Password.class);
                startActivity(myIntent);
            } else {
                Intent myIntent = new Intent(Home_page.this,
                        No_internet_page.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_phone) {
            try {
                //+919899133989
                Uri u = Uri.parse("tel:" + android_mobile1);
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                try {
                    startActivity(i);
                } catch (SecurityException s) {
                    Toast.makeText(getApplicationContext(), "error phone", Toast.LENGTH_LONG).show();
                }
            } catch (SecurityException s) {
                Toast.makeText(getApplicationContext(), "error phone", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_email) {
            try {
                //vipul@drdindia.com
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", android_email1, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            } catch (SecurityException s) {
                Toast.makeText(getApplicationContext(), "error email", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_privacy_policy) {
            Intent myIntent = new Intent(Home_page.this,
                    Chemist_Webview.class);
            myIntent.putExtra("page_url",
                    main_url_webview + "privacy_policy");
            myIntent.putExtra("page_title", "Privacy policy");
            startActivity(myIntent);
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=com.drdistributor.dr";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_logout) {
            alertMessage_logoutUser();
        } else if (id == R.id.nav_map) {
            latitude1 = 28.5182055;
            longitude1 = 77.2793033;
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", latitude1, longitude1);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void alertMessage_logoutUser() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        try {
                            new logout_online_function().execute();
                            logout_function();
                            finish();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to logout?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        get_user_cart();

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_image = user.get(UserSessionManager.KEY_USERIMAGE);

        try {
            View headerView = navigationView.getHeaderView(0);
            ImageView left_logo = headerView.findViewById(R.id.left_logo);
            Picasso.with(this).load(user_image).into(left_logo);
        } catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }

        if (session.checkLogin()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        try {
            exit_app();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void home_page_json_result() {
        String menu_json = "", medicine_json0 = "", medicine_json1 = "",
                medicine_json2 = "", medicine_json3 = "", medicine_json4 = "",
                medicine_json5 = "", medicine_json6 = "";
        String medicine_title0 = "";

        String box_json1="",box_json2="";

        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);

                String android_versioncode = jsonObject.getString("versioncode");
                String force_update_title = jsonObject.getString("force_update_title");
                String force_update_message = jsonObject.getString("force_update_message");

                if (versioncode == Integer.parseInt(android_versioncode)) {

                } else {
                    LinearLayout footer_version_available_btn = findViewById(R.id.footer_version_available_btn);
                    footer_version_available_btn.setVisibility(View.VISIBLE);
                    TextView new_version_available_msg = findViewById(R.id.new_version_available_msg);

                    try {
                        byte[] data = Base64.decode(force_update_title, Base64.DEFAULT);
                        force_update_title = new String(data, "UTF-8");

                        byte[] data1 = Base64.decode(force_update_message, Base64.DEFAULT);
                        force_update_message = new String(data1, "UTF-8");
                    } catch (Exception e) {
                        // TODO: handle exception
                        //mProgressDialog.dismiss();

                        force_update_title = "New Version Available";
                        force_update_message = "Update Your Application Now!";
                    }
                    new_version_available_msg.setText(Html.fromHtml(force_update_title));
                    update_app(force_update_title, force_update_message);

                    footer_version_available_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(android.content.Intent.ACTION_VIEW);
                            it.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.drdistributor.dr"));
                            startActivity(it);
                        }
                    });

                    /*LinearLayout footer_share_btn = findViewById(R.id.footer_share_btn);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    params.setMargins(15, 10, 15, 0);
                    footer_share_btn.setLayoutParams(params);*/
                }

                String logout = jsonObject.getString("logout");
                if (logout.equals("1")) {
                    new logout_online_function().execute();
                    logout_function();
                }

                String broadcast_title = jsonObject.getString("broadcast_title");
                String broadcast = jsonObject.getString("broadcast");
                if (!broadcast.equals("")) {
                    //Toast.makeText(getApplicationContext(), broadcast, Toast.LENGTH_LONG).show();
                    try {
                        byte[] data = Base64.decode(broadcast_title, Base64.DEFAULT);
                        broadcast_title = new String(data, "UTF-8");

                        byte[] data1 = Base64.decode(broadcast, Base64.DEFAULT);
                        broadcast = new String(data1, "UTF-8");

                        simpleAlert(broadcast_title, broadcast);
                    } catch (Exception e) {
                        // TODO: handle exception
                        //mProgressDialog.dismiss();
                    }
                }

                LinearLayout footer_call_btn = findViewById(R.id.footer_call_btn);
                footer_call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //+919899133989
                            Uri u = Uri.parse("tel:" + android_mobile1);
                            Intent i = new Intent(Intent.ACTION_DIAL, u);
                            try {
                                startActivity(i);
                            } catch (SecurityException s) {
                                Toast.makeText(getApplicationContext(), "error phone", Toast.LENGTH_LONG).show();
                            }
                        } catch (SecurityException s) {
                            Toast.makeText(getApplicationContext(), "error phone", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                LinearLayout footer_email_btn = findViewById(R.id.footer_email_btn);
                footer_email_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //vipul@drdindia.com
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", android_email1, null));
                            intent.putExtra(Intent.EXTRA_SUBJECT, "");
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                        } catch (SecurityException s) {
                            Toast.makeText(getApplicationContext(), "error email", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                medicine_title0 = jsonObject.getString("medicine_title0");

                menu_json = jsonObject.getString("menu_json");

                medicine_json0 = jsonObject.getString("medicine_json0");
                medicine_json1 = jsonObject.getString("medicine_json1");
                medicine_json2 = jsonObject.getString("medicine_json2");
                medicine_json3 = jsonObject.getString("medicine_json3");
                medicine_json4 = jsonObject.getString("medicine_json4");
                medicine_json5 = jsonObject.getString("medicine_json5");
                medicine_json6 = jsonObject.getString("medicine_json6");

                String user_cart_json0 = jsonObject.getString("user_cart_json0");
                String user_cart_json1 = jsonObject.getString("user_cart_json1");

                /******************************************************/
                box_json1 = jsonObject.getString("box1");
                box_json2 = jsonObject.getString("box2");
                /******************************************************/

                JSONArray jArray1 = new JSONArray(user_cart_json1);
                for (int ii = 0; ii < jArray1.length(); ii++) {
                    JSONObject jsonObject1 = jArray1.getJSONObject(ii);

                    String items_total = jsonObject1.getString("items_total");
                    String items_price = jsonObject1.getString("items_price");

                    TextView action_bar_cart_total = findViewById(R.id.action_bar_cart_total);
                    action_bar_cart_total.setText(" " + items_total + " ");

                    TextView home_cart_tv = (TextView) findViewById(R.id.home_cart_tv);
                    home_cart_tv.setText("Draft (" + items_total + ")");

                    if(user_cart_json0.equals("[]"))
                    {
                        user_cart_json0 = "";
                    }

                    /************************************************************/
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(USER_CART, items_total);
                    editor.putString(USER_CART_TOTAL, items_price);
                    editor.putString(USER_CART_JSON, user_cart_json0);
                    editor.apply();
                    /************************************************************/
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "e1" + e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show header menu for this code****************************/
        try{
            JSONArray jArray = new JSONArray(menu_json);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_company = jsonObject.getString("item_company");
                String item_image = jsonObject.getString("item_image");

                Homepage_box_get_or_set Homepage_box_set = new Homepage_box_get_or_set();
                Homepage_box_set.item_code(item_code);
                Homepage_box_set.item_company(item_company);
                Homepage_box_set.item_image(item_image);
                Homepage_menu_get_or_set_List.add(Homepage_box_set);
            }
            Homepage_menu_recyclerview.setAdapter(Homepage_menu_adapter);
            Homepage_menu_recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_menu_recyclerview, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_menu_get_or_set_List.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_code", Homepage_box_get.item_code());
                    myIntent.putExtra("item_page_type","medicine_category");
                    startActivity(myIntent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "e2" + e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show box0 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json0);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box0 = findViewById(R.id.Homepage_box0);
                Homepage_box0.setVisibility(View.GONE);
            } else {
                TextView Homepage_title0 = findViewById(R.id.Homepage_title0);
                Homepage_title0.setText(medicine_title0);
            }

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_company = jsonObject.getString("item_company");
                String item_division = jsonObject.getString("item_division");
                String item_image = jsonObject.getString("item_image");

                Homepage_box_get_or_set Homepage_box_set = new Homepage_box_get_or_set();
                Homepage_box_set.item_code(item_code);
                Homepage_box_set.item_company(item_company);
                Homepage_box_set.item_division(item_division);
                Homepage_box_set.item_image(item_image);
                Homepage_box_get_or_set_List0.add(Homepage_box_set);
            }
            Homepage_box_recyclerview0.setAdapter(Homepage_box_adapter0);
            Homepage_box_recyclerview0.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview0, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List0.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(Home_page.this,
                            Medicine_category.class);
                    myIntent.putExtra("item_code", Homepage_box_get.item_code());
                    myIntent.putExtra("item_division", Homepage_box_get.item_division());
                    myIntent.putExtra("item_page_type","featured_brand");
                    startActivity(myIntent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show medicine_json1 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json1);
            TextView item_header_title = findViewById(R.id.Homepage_title1);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box1 = findViewById(R.id.Homepage_box1);
                Homepage_box1.setVisibility(View.GONE);
            }

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_image = jsonObject.getString("item_image");
                String item_name = jsonObject.getString("item_name");
                String item_packing = jsonObject.getString("item_packing");
                String item_company = jsonObject.getString("item_company");
                String item_quantity = jsonObject.getString("item_quantity");
                String item_mrp = jsonObject.getString("item_mrp");
                String item_ptr = jsonObject.getString("item_ptr");
                String item_price = jsonObject.getString("item_price");
                String item_margin = jsonObject.getString("item_margin");
                String item_featured = jsonObject.getString("item_featured");
                String item_header_title_s = jsonObject.getString("item_header_title");

                item_header_title.setText(item_header_title_s);

                Homepage_box_get_or_set GetDataAdapter_set = new Homepage_box_get_or_set();
                GetDataAdapter_set.item_code(item_code);
                GetDataAdapter_set.item_image(item_image);
                GetDataAdapter_set.item_name(item_name);
                GetDataAdapter_set.item_packing(item_packing);
                GetDataAdapter_set.item_company(item_company);
                GetDataAdapter_set.item_quantity(item_quantity);
                GetDataAdapter_set.item_mrp(item_mrp);
                GetDataAdapter_set.item_ptr(item_ptr);
                GetDataAdapter_set.item_price(item_price);
                GetDataAdapter_set.item_margin(item_margin);
                GetDataAdapter_set.item_featured(item_featured);
                Homepage_box_get_or_set_List1.add(GetDataAdapter_set);
            }
            Homepage_box_recyclerview1.setAdapter(Homepage_box_adapter1);
            Homepage_box_recyclerview1.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview1, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List1.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent in = new Intent();
                    in.setClass(Home_page.this, Medicine_details.class);
                    in.putExtra("item_code", Homepage_box_get.item_code());
                    startActivity(in);
                }
                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "medicine_json1:"+e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show medicine_json2 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json2);
            TextView item_header_title = findViewById(R.id.Homepage_title2);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box2 = findViewById(R.id.Homepage_box2);
                Homepage_box2.setVisibility(View.GONE);
            }

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_image = jsonObject.getString("item_image");
                String item_name = jsonObject.getString("item_name");
                String item_packing = jsonObject.getString("item_packing");
                String item_company = jsonObject.getString("item_company");
                String item_quantity = jsonObject.getString("item_quantity");
                String item_mrp = jsonObject.getString("item_mrp");
                String item_ptr = jsonObject.getString("item_ptr");
                String item_price = jsonObject.getString("item_price");
                String item_margin = jsonObject.getString("item_margin");
                String item_featured = jsonObject.getString("item_featured");
                String item_header_title_s = jsonObject.getString("item_header_title");

                item_header_title.setText(item_header_title_s);

                Homepage_box_get_or_set GetDataAdapter_set = new Homepage_box_get_or_set();
                GetDataAdapter_set.item_code(item_code);
                GetDataAdapter_set.item_image(item_image);
                GetDataAdapter_set.item_name(item_name);
                GetDataAdapter_set.item_packing(item_packing);
                GetDataAdapter_set.item_company(item_company);
                GetDataAdapter_set.item_quantity(item_quantity);
                GetDataAdapter_set.item_mrp(item_mrp);
                GetDataAdapter_set.item_ptr(item_ptr);
                GetDataAdapter_set.item_price(item_price);
                GetDataAdapter_set.item_margin(item_margin);
                GetDataAdapter_set.item_featured(item_featured);
                Homepage_box_get_or_set_List2.add(GetDataAdapter_set);
            }
            Homepage_box_recyclerview2.setAdapter(Homepage_box_adapter2);
            Homepage_box_recyclerview2.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview2, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List2.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent in = new Intent();
                    in.setClass(Home_page.this, Medicine_details.class);
                    in.putExtra("item_code", Homepage_box_get.item_code());
                    startActivity(in);
                }
                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "medicine_json2:"+e.toString(), Toast.LENGTH_LONG).show();
        }


        /**************show medicine_json3 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json3);
            TextView item_header_title = findViewById(R.id.Homepage_title3);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box3 = findViewById(R.id.Homepage_box3);
                Homepage_box3.setVisibility(View.GONE);
            }

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_image = jsonObject.getString("item_image");
                String item_name = jsonObject.getString("item_name");
                String item_packing = jsonObject.getString("item_packing");
                String item_company = jsonObject.getString("item_company");
                String item_quantity = jsonObject.getString("item_quantity");
                String item_mrp = jsonObject.getString("item_mrp");
                String item_ptr = jsonObject.getString("item_ptr");
                String item_price = jsonObject.getString("item_price");
                String item_margin = jsonObject.getString("item_margin");
                String item_featured = jsonObject.getString("item_featured");
                String item_header_title_s = jsonObject.getString("item_header_title");

                item_header_title.setText(item_header_title_s);

                Homepage_box_get_or_set GetDataAdapter_set = new Homepage_box_get_or_set();
                GetDataAdapter_set.item_code(item_code);
                GetDataAdapter_set.item_image(item_image);
                GetDataAdapter_set.item_name(item_name);
                GetDataAdapter_set.item_packing(item_packing);
                GetDataAdapter_set.item_company(item_company);
                GetDataAdapter_set.item_quantity(item_quantity);
                GetDataAdapter_set.item_mrp(item_mrp);
                GetDataAdapter_set.item_ptr(item_ptr);
                GetDataAdapter_set.item_price(item_price);
                GetDataAdapter_set.item_margin(item_margin);
                GetDataAdapter_set.item_featured(item_featured);
                Homepage_box_get_or_set_List3.add(GetDataAdapter_set);
            }
            Homepage_box_recyclerview3.setAdapter(Homepage_box_adapter3);
            Homepage_box_recyclerview3.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview3, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List3.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent in = new Intent();
                    in.setClass(Home_page.this, Medicine_details.class);
                    in.putExtra("item_code", Homepage_box_get.item_code());
                    startActivity(in);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "medicine_json3:"+e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show medicine_json4 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json4);
            TextView item_header_title = findViewById(R.id.Homepage_title4);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box4 = findViewById(R.id.Homepage_box4);
                Homepage_box4.setVisibility(View.GONE);
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_image = jsonObject.getString("item_image");
                String item_name = jsonObject.getString("item_name");
                String item_packing = jsonObject.getString("item_packing");
                String item_company = jsonObject.getString("item_company");
                String item_quantity = jsonObject.getString("item_quantity");
                String item_mrp = jsonObject.getString("item_mrp");
                String item_ptr = jsonObject.getString("item_ptr");
                String item_price = jsonObject.getString("item_price");
                String item_margin = jsonObject.getString("item_margin");
                String item_featured = jsonObject.getString("item_featured");
                String item_header_title_s = jsonObject.getString("item_header_title");

                item_header_title.setText(item_header_title_s);

                Homepage_box_get_or_set GetDataAdapter_set = new Homepage_box_get_or_set();
                GetDataAdapter_set.item_code(item_code);
                GetDataAdapter_set.item_image(item_image);
                GetDataAdapter_set.item_name(item_name);
                GetDataAdapter_set.item_packing(item_packing);
                GetDataAdapter_set.item_company(item_company);
                GetDataAdapter_set.item_quantity(item_quantity);
                GetDataAdapter_set.item_mrp(item_mrp);
                GetDataAdapter_set.item_ptr(item_ptr);
                GetDataAdapter_set.item_price(item_price);
                GetDataAdapter_set.item_margin(item_margin);
                GetDataAdapter_set.item_featured(item_featured);
                Homepage_box_get_or_set_List4.add(GetDataAdapter_set);
            }
            Homepage_box_recyclerview4.setAdapter(Homepage_box_adapter4);
            Homepage_box_recyclerview4.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview4, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List4.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent in = new Intent();
                    in.setClass(Home_page.this, Medicine_details.class);
                    in.putExtra("item_code", Homepage_box_get.item_code());
                    startActivity(in);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "medicine_json4:"+e.toString(), Toast.LENGTH_LONG).show();
        }


        /**************show medicine_json5 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json5);
            TextView item_header_title = findViewById(R.id.Homepage_title5);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box5 = findViewById(R.id.Homepage_box5);
                Homepage_box5.setVisibility(View.GONE);
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_image = jsonObject.getString("item_image");
                String item_name = jsonObject.getString("item_name");
                String item_packing = jsonObject.getString("item_packing");
                String item_company = jsonObject.getString("item_company");
                String item_quantity = jsonObject.getString("item_quantity");
                String item_mrp = jsonObject.getString("item_mrp");
                String item_ptr = jsonObject.getString("item_ptr");
                String item_price = jsonObject.getString("item_price");
                String item_margin = jsonObject.getString("item_margin");
                String item_featured = jsonObject.getString("item_featured");
                String item_header_title_s = jsonObject.getString("item_header_title");

                item_header_title.setText(item_header_title_s);

                Homepage_box_get_or_set GetDataAdapter_set = new Homepage_box_get_or_set();
                GetDataAdapter_set.item_code(item_code);
                GetDataAdapter_set.item_image(item_image);
                GetDataAdapter_set.item_name(item_name);
                GetDataAdapter_set.item_packing(item_packing);
                GetDataAdapter_set.item_company(item_company);
                GetDataAdapter_set.item_quantity(item_quantity);
                GetDataAdapter_set.item_mrp(item_mrp);
                GetDataAdapter_set.item_ptr(item_ptr);
                GetDataAdapter_set.item_price(item_price);
                GetDataAdapter_set.item_margin(item_margin);
                GetDataAdapter_set.item_featured(item_featured);
                Homepage_box_get_or_set_List5.add(GetDataAdapter_set);
            }
            Homepage_box_recyclerview5.setAdapter(Homepage_box_adapter5);
            Homepage_box_recyclerview5.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview5, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List5.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent in = new Intent();
                    in.setClass(Home_page.this, Medicine_details.class);
                    in.putExtra("item_code", Homepage_box_get.item_code());
                    startActivity(in);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "medicine_json5:"+e.toString(), Toast.LENGTH_LONG).show();
        }


        /**************show medicine_json6 data ****************************/
        try{
            JSONArray jArray = new JSONArray(medicine_json6);
            TextView item_header_title = findViewById(R.id.Homepage_title6);
            if (jArray.length() == 0) {
                LinearLayout Homepage_box6 = findViewById(R.id.Homepage_box6);
                Homepage_box6.setVisibility(View.GONE);
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_code = jsonObject.getString("item_code");
                String item_image = jsonObject.getString("item_image");
                String item_name = jsonObject.getString("item_name");
                String item_packing = jsonObject.getString("item_packing");
                String item_company = jsonObject.getString("item_company");
                String item_quantity = jsonObject.getString("item_quantity");
                String item_mrp = jsonObject.getString("item_mrp");
                String item_ptr = jsonObject.getString("item_ptr");
                String item_price = jsonObject.getString("item_price");
                String item_margin = jsonObject.getString("item_margin");
                String item_featured = jsonObject.getString("item_featured");
                String item_header_title_s = jsonObject.getString("item_header_title");

                item_header_title.setText(item_header_title_s);

                Homepage_box_get_or_set GetDataAdapter_set = new Homepage_box_get_or_set();
                GetDataAdapter_set.item_code(item_code);
                GetDataAdapter_set.item_image(item_image);
                GetDataAdapter_set.item_name(item_name);
                GetDataAdapter_set.item_packing(item_packing);
                GetDataAdapter_set.item_company(item_company);
                GetDataAdapter_set.item_quantity(item_quantity);
                GetDataAdapter_set.item_mrp(item_mrp);
                GetDataAdapter_set.item_ptr(item_ptr);
                GetDataAdapter_set.item_price(item_price);
                GetDataAdapter_set.item_margin(item_margin);
                GetDataAdapter_set.item_featured(item_featured);
                Homepage_box_get_or_set_List6.add(GetDataAdapter_set);
            }
            Homepage_box_recyclerview6.setAdapter(Homepage_box_adapter6);
            Homepage_box_recyclerview6.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    Homepage_box_recyclerview6, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Homepage_box_get_or_set Homepage_box_get = Homepage_box_get_or_set_List6.get(position);
                    //Toast.makeText(getApplicationContext(), movie.getcompcode() + " is selected!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), ""+position , Toast.LENGTH_SHORT).show();

                    Intent in = new Intent();
                    in.setClass(Home_page.this, Medicine_details.class);
                    in.putExtra("item_code", Homepage_box_get.item_code());
                    startActivity(in);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "medicine_json6:"+e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show box_json1 data ****************************/
        try{
            JSONArray jArray = new JSONArray(box_json1);
            if (jArray.length() == 0) {
                LinearLayout invoice_box = findViewById(R.id.invoice_box);
                invoice_box.setVisibility(View.GONE);
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_id = jsonObject.getString("item_id");
                String item_title = jsonObject.getString("item_title");
                String item_message = jsonObject.getString("item_message");
                String item_date_time = jsonObject.getString("item_date_time");
                String item_image = jsonObject.getString("item_image");

                My_invoice_get_or_set My_invoice_set = new My_invoice_get_or_set();
                My_invoice_set.item_id(item_id);
                My_invoice_set.item_title(item_title);
                My_invoice_set.item_message(item_message);
                My_invoice_set.item_date_time(item_date_time);
                My_invoice_set.item_image(item_image);
                My_invoice_List.add(My_invoice_set);
            }
            My_invoice_adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "box_json1:"+e.toString(), Toast.LENGTH_LONG).show();
        }

        /**************show box_json2 data ****************************/
        try{
            JSONArray jArray = new JSONArray(box_json2);
            if (jArray.length() == 0) {
                LinearLayout notification_box = findViewById(R.id.notification_box);
                notification_box.setVisibility(View.GONE);
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String item_id = jsonObject.getString("item_id");
                String item_title = jsonObject.getString("item_title");
                String item_message = jsonObject.getString("item_message");
                String item_date_time = jsonObject.getString("item_date_time");
                String item_image = jsonObject.getString("item_image");

                My_notification_get_or_set My_notification_set = new My_notification_get_or_set();
                My_notification_set.item_id(item_id);
                My_notification_set.item_title(item_title);
                My_notification_set.item_message(item_message);
                My_notification_set.item_date_time(item_date_time);
                My_notification_set.item_image(item_image);
                My_notification_List.add(My_notification_set);
            }
            My_notification_adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "box_json2:"+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void simpleAlert(String broadcast_title, String broadcast) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(broadcast_title);
        builder.setMessage(broadcast);

        LinearLayout broadcast_div = findViewById(R.id.broadcast_div);
        broadcast_div.setVisibility(View.VISIBLE);

        TextView broadcast_div_title = findViewById(R.id.broadcast_div_title);
        TextView broadcast_div_msg = findViewById(R.id.broadcast_div_msg);
        broadcast_div_title.setText(broadcast_title);
        broadcast_div_msg.setText(broadcast);

        /*builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "OK was clicked",
                                Toast.LENGTH_SHORT).show();
                    }
                });*/
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*Toast.makeText(getApplicationContext(),
                        android.R.string.no, Toast.LENGTH_SHORT).show();*/
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void update_app(String broadcast_title, String broadcast) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml(broadcast_title));
        builder.setMessage(Html.fromHtml(broadcast));
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(android.content.Intent.ACTION_VIEW);
                it.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.drdistributor.dr"));
                startActivity(it);
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    Database db;
    SQLiteDatabase sql;

    private void logout_function() {
        db = new Database(Home_page.this);
        sql = db.getWritableDatabase();

        try {
            sql.execSQL("DROP TABLE tbl_medicine");
            sql.execSQL("DROP TABLE tbl_medicine_last_id");
            sql.execSQL("DROP TABLE tbl_place_order");
            sql.execSQL("DROP TABLE tbl_acm");
            sql.execSQL("DROP TABLE tbl_user_status");
            sql.execSQL("DROP TABLE tbl_order_done");
        } catch (Exception ee) {
            // TODO: handle exception
            //Toast.makeText(MainActivity.this, "error 1", Toast.LENGTH_LONG).show();
        }

        try {
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine");
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine_last_id");
            sql.execSQL("DROP TABLE IF EXISTS tbl_place_order");
            sql.execSQL("DROP TABLE IF EXISTS tbl_acm");
            sql.execSQL("DROP TABLE IF EXISTS tbl_user_status");
            sql.execSQL("DROP TABLE IF EXISTS tbl_order_done");
        } catch (Exception ee) {
            // TODO: handle exception
            //Toast.makeText(MainActivity.this, "error 2", Toast.LENGTH_LONG).show();
        }
        session.logoutUser();

        // calling method to edit values in shared prefs.
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // below line will clear
        // the data in shared prefs.
        editor.clear();
        // below line will apply empty
        // data to shared prefs.
        editor.apply();
    }

    private class logout_online_function extends AsyncTask<Void, Void, Void> {
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
                HttpPost httppost = new HttpPost(page_url4);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-kavita-zxy"));

                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));

                nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                //Log.e("log_tag","Error in connection"+e.toString());
                //tv.setText("couldn't connect to the database");
                //mProgressDialog.dismiss();
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
                //mProgressDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

        }
    }

    public void exit_app() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked

                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void get_user_cart() {
        try {
            String user_cart = sharedpreferences.getString(USER_CART, null);

            TextView action_bar_cart_total = findViewById(R.id.action_bar_cart_total);
            action_bar_cart_total.setText(" " + user_cart + " ");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error get_user_cart" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /********mobile phone parmssion*****************************/
    String TAG = "PhoneActivityTAG";
    Activity activity = Home_page.this;
    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;

    /*
    private String getPhone() {
        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, wantPermission) != PackageManager.PERMISSION_GRANTED) {
            return "0000000000";
        }
        return phoneMgr.getLine1Number();
    }

    private void requestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
            //Toast.makeText(activity, "Phone state permission allows us to get phone number. Please allow it for additional functionality.", Toast.LENGTH_LONG).show();
            //new JSON_save_order_to_server().execute();
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission},PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }*/
}
package com.drdistributor.dr;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Home_page_corporate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    WebView webView;

    String mainurl="";
    String user_fname="",user_altercode="",user_type="";
    String user_session="",user_division="",user_compcode="",user_compname="";
    UserSessionManager session;
    Database db;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_corporate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#10847e"));
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.menu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10847e")));
        View view = getSupportActionBar().getCustomView();


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = new Database(Home_page_corporate.this);
        sql = db.getWritableDatabase();

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_session = properCase(user.get(UserSessionManager.KEY_USERID));
        user_fname = properCase(user.get(UserSessionManager.KEY_USERFNAME));
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_type = user.get(UserSessionManager.KEY_USERTYPE);

        /*user_division = user.get(UserSessionManager.KEY_user_division);
        user_compcode = user.get(UserSessionManager.KEY_user_compcode);*/

        /*TextView home_title = (TextView) findViewById(R.id.home_title);
        home_title.setText("Welcome "+user_fname +" ("+user_altercode+")");*/

        View headerView = navigationView.getHeaderView(0);
        TextView leftmenu_name1 = (TextView) headerView.findViewById(R.id.leftmenu_name1);
        leftmenu_name1.setText("Welcome "+user_fname);

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("D R Distributors Pvt Ltd");

        ImageButton action_bar_back= (ImageButton)view.findViewById(R.id.action_bar_back);
        action_bar_back.setVisibility(View.GONE);

        LinearLayout cart_LinearLayout= (LinearLayout)view.findViewById(R.id.cart_LinearLayout);
            cart_LinearLayout.setVisibility(View.GONE);

        TextView action_bar_cart_total1 = (TextView) findViewById(R.id.action_bar_cart_total);
        action_bar_cart_total1.setVisibility(View.GONE);

        LinearLayout item_wise_report = findViewById(R.id.item_wise_report);
        LinearLayout chemist_wise_report = findViewById(R.id.chemist_wise_report);
        LinearLayout stock_and_sales_analysis = findViewById(R.id.stock_and_sales_analysis);
        LinearLayout item_wise_report_month = findViewById(R.id.item_wise_report_month);
        LinearLayout chemist_wise_report_month = findViewById(R.id.chemist_wise_report_month);
        LinearLayout notification = findViewById(R.id.notification);

        item_wise_report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Home_page_corporate.this,Webview_page.class);
                in.putExtra("page_url",mainurl+"android/Api_mobile_html20/item_wise_report?user_session="+user_session+"&user_division="+user_division+"&user_compcode="+user_compcode);
                in.putExtra("page_title","Item Wise Report");
                startActivity(in);
            }
        });

        chemist_wise_report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Home_page_corporate.this,Webview_page.class);
                in.putExtra("page_url",mainurl+"android/Api_mobile_html20/chemist_wise_report?user_session="+user_session+"&user_division="+user_division+"&user_compcode="+user_compcode);
                in.putExtra("page_title","Chemist Report");
                startActivity(in);
            }
        });

        stock_and_sales_analysis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Home_page_corporate.this,Webview_page.class);
                in.putExtra("page_url",mainurl+"android/Api_mobile_html20/stock_and_sales_analysis?user_session="+user_session+"&user_division="+user_division+"&user_compcode="+user_compcode);
                in.putExtra("page_title","Stock And Sales Analysis");
                startActivity(in);
            }
        });

        item_wise_report_month.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Home_page_corporate.this,Webview_page.class);
                in.putExtra("page_url",mainurl+"android/Api_mobile_html20/item_wise_report_month?user_session="+user_session+"&user_division="+user_division+"&user_compcode="+user_compcode);
                in.putExtra("page_title","Item Wise Report Month");
                startActivity(in);
            }
        });

        chemist_wise_report_month.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Home_page_corporate.this,Webview_page.class);
                in.putExtra("page_url",mainurl+"android/Api_mobile_html20/chemist_wise_report_month?user_session="+user_session+"&user_division="+user_division+"&user_compcode="+user_compcode);
                in.putExtra("page_title","Chemist Report Month");
                startActivity(in);
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(mainurl+"Api_mobile_html/slider");

        webView.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface"); // To call methods in Android from using js in the html, AndroidInterface.showToast, AndroidInterface.getAndroidVersion etc
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    setTitle(R.string.app_name);
            }
        });
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished (WebView view, String url) {
            //Calling a javascript function in html page
            view.loadUrl("javascript:alert(showVersion('called by Android'))");
        }
    }
    String properCase (String inputVal) {
        if (inputVal.length() == 0) return "";
        if (inputVal.length() == 1) return inputVal.toUpperCase();
        return inputVal.substring(0,1).toUpperCase()
                + inputVal.substring(1).toLowerCase();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_report) {

        } else if (id == R.id.item_report_monthly) {

        } else if (id == R.id.chemist_report) {

        } else if (id == R.id.chemist_report_monthly) {

        } else if (id == R.id.nav_phone) {
            try {
                Uri u = Uri.parse("tel:+919899133989");
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
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "vipul@drdindia.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            } catch (SecurityException s) {
                Toast.makeText(getApplicationContext(), "error email", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_whatsapp) {
            try {
                Uri mUri = Uri.parse("smsto:+919899133989");
                Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                mIntent.setPackage("com.whatsapp");
                mIntent.putExtra("sms_body", "The text goes here");
                mIntent.putExtra("chat", true);
                startActivity(mIntent);
            } catch (SecurityException s) {
                Toast.makeText(getApplicationContext(), "error whatsapp", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_privacy_policy) {
            Toast.makeText(getApplicationContext(), "error privacy_policy", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_logout) {
            alertMessage_logoutUser();
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
                        try
                        {
                            finish();
                            session.logoutUser();

                        }catch (Exception e) {
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
        builder.setMessage("Are you sure to Log Out?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}

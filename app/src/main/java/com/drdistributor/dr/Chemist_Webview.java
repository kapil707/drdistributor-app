package com.drdistributor.dr;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.HashMap;

public class Chemist_Webview extends AppCompatActivity {

    String user_altercode="",user_type="",main_url_webview="",user_code="";
    UserSessionManager session;
    Database db;
    SQLiteDatabase sql;

    String page_url="";
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chemist_webview);

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

        MainActivity ma = new MainActivity();
        main_url_webview = ma.main_url_webview;

        db = new Database(Chemist_Webview.this);
        sql = db.getWritableDatabase();

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_code = user.get(UserSessionManager.KEY_USERCODE);

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("D R Distributors Pvt Ltd");

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        LinearLayout homebigsearch = findViewById(R.id.homebigsearch);
        homebigsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Chemist_Webview.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout= (LinearLayout)findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Chemist_Webview.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        Intent in = getIntent();
        page_url = in.getStringExtra("page_url");
        action_bar_title1.setText(in.getStringExtra("page_title"));


        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(page_url);

        webView.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface"); // To call methods in Android from using js in the html, AndroidInterface.showToast, AndroidInterface.getAndroidVersion etc
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new Chemist_Webview.MyWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "android");
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
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void fun_Get_single_medicine_info(String id) {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent in = new Intent();
            in.setClass(Chemist_Webview.this, Medicine_details.class);
            in.putExtra("i_code", id);
            startActivity(in);
        }

        @JavascriptInterface
        public void fun_Featured_brand_medicine_division(String id, String compname, String image,String division) {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Chemist_Webview.this,
                    Medicine_category.class);
            myIntent.putExtra("compcode", id);
            myIntent.putExtra("company_full_name", compname);
            myIntent.putExtra("image", image);
            myIntent.putExtra("division", division);
            startActivity(myIntent);
        }

        @JavascriptInterface
        public void Map_loading_page_open() {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Chemist_Webview.this,
                    Track_order_page.class);
            startActivity(myIntent);
        }

        @JavascriptInterface
        public void orders_page_open() {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Chemist_Webview.this,
                    Chemist_Webview.class);
            myIntent.putExtra("page_url", main_url_webview + "my_orders/?user_type=" + user_type + "&user_altercode=" + user_altercode);
            myIntent.putExtra("page_title", "My Orders");
            startActivity(myIntent);
        }

        @JavascriptInterface
        public void invoice_page_open() {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Chemist_Webview.this,
                    Chemist_Webview.class);
            myIntent.putExtra("page_url", main_url_webview + "my_invoice/?user_type=" + user_type + "&user_altercode=" + user_altercode);
            myIntent.putExtra("page_title", "My Invoices");
            startActivity(myIntent);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished (WebView view, String url) {
            //Calling a javascript function in html page
            view.loadUrl("javascript:alert(showVersion('called by Android'))");
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("LogTag", message);
            result.confirm();
            return true;
        }
    }

    public String count_cart() {
        String x = "0";
        try {
            if(user_type.equals("sales")) {
                Cursor tbl_place_order = sql.rawQuery("Select DISTINCT chemist_id from tbl_place_order where status='0'", null);
                x = String.valueOf(tbl_place_order.getCount());
            }
            else
            {
                Cursor tbl_place_order = sql.rawQuery("Select * from tbl_place_order where status='0'", null);
                x = String.valueOf(tbl_place_order.getCount());
            }
        } catch (Exception e) {
        }
        return x;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        TextView action_bar_cart_total1 = (TextView) findViewById(R.id.action_bar_cart_total);
        action_bar_cart_total1.setText(" "+count_cart()+" ");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

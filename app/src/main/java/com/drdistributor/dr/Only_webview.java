package com.drdistributor.dr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Only_webview extends AppCompatActivity {
    String page_url = "";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_webview);

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
        action_bar_title1.setText("D R Distributors Pvt Ltd");

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);

        TextView action_bar_cart_total = (TextView) findViewById(R.id.action_bar_cart_total);
        action_bar_cart_total.setVisibility(View.GONE);

        Intent in = getIntent();
        page_url = in.getStringExtra("page_url");
        action_bar_title1.setText(in.getStringExtra("page_title"));


        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(page_url);

        webView.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface"); // To call methods in Android from using js in the html, AndroidInterface.showToast, AndroidInterface.getAndroidVersion etc
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "android");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    setTitle(R.string.app_name);
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
        public void callandroidfun(String id) {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();

        /*Intent in = new Intent();
        in.setClass(this, Get_single_medicine_info.class);
        in.putExtra("med_item_id", id);
        startActivity(in);
        finish();*/
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
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
}

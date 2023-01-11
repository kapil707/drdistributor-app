package com.drdistributor.dr;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

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
import java.util.List;

public class Ratingbar extends AppCompatActivity {
    RatingBar ratingbar;
    Button button,skipbutton;

    String result = "";
    String device_id = "";
    String mainurl = "",page_url1="";
    String rating="";
    String result1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingbar);

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


        addListenerOnButtonClick();

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id = android_id;

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl + "ratingbar_done/post";

        Intent in = getIntent();
        result1 = in.getStringExtra("result");

        skipbutton = (Button) findViewById(R.id.skipbutton);
        //Performing action on Button Click
        skipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Ratingbar.this, Home_page.class);
                in.putExtra("result", result1);
                startActivity(in);
                finish();
            }
        });
    }

    public void addListenerOnButtonClick() {
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        button = (Button) findViewById(R.id.button);
        //Performing action on Button Click
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                rating = String.valueOf(ratingbar.getRating());
                new ratingbar_done().execute();
                if (ratingbar.getRating() >= 4) {
                    //Toast.makeText(getApplicationContext(), rating +"ok h", Toast.LENGTH_LONG).show();
                    Intent in = new Intent();
                    in.setClass(Ratingbar.this, Home_page.class);
                    in.putExtra("result", result1);
                    startActivity(in);
                    finish();

                    Intent it = new Intent(android.content.Intent.ACTION_VIEW);
                    it.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.drdistributor.dr"));
                    startActivity(it);

                } else {
                    Intent in = new Intent();
                    in.setClass(Ratingbar.this, Ratingbar2.class);
                    in.putExtra("result", result1);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    private class ratingbar_done extends AsyncTask<Void, Void, Void> {
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
                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
                nameValuePairs.add(new BasicNameValuePair("rating", rating));
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
            //Toast.makeText(Ratingbar.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}

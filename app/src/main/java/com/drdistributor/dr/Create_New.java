package com.drdistributor.dr;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.List;

public class Create_New extends AppCompatActivity {
    final Context context = this;
    String result = "";
    String mainurl = "", page_url1 = "";
    Button create_new_btn,create_new_btn1;
    TextView go_back_btn,alert;
    EditText chemist_code1, phone_number1;
    String chemist_code = "", phone_number = "";
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new);
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

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("D R Distributors Pvt Ltd");

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        ImageButton action_bar_back = (ImageButton) view.findViewById(R.id.action_bar_back);
        action_bar_back.setVisibility(View.GONE);

        LinearLayout cart_LinearLayout = (LinearLayout) view.findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);
        TextView action_bar_cart_total = (TextView) view.findViewById(R.id.action_bar_cart_total);
        action_bar_cart_total.setVisibility(View.GONE);

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl + "create_new/post";

        try {
            TextView android_v = findViewById(R.id.android_v);
            int versioncode = BuildConfig.VERSION_CODE;
            android_v.setText("App version " + versioncode);
        }catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "Error parsing data"+e.toString());
            //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
        }

        create_new_btn = findViewById(R.id.create_new_btn);
        create_new_btn1 = findViewById(R.id.create_new_btn1);

        alert = (TextView) findViewById(R.id.user_alert);
        chemist_code1 = (EditText) findViewById(R.id.chemist_code1);
        phone_number1 = (EditText) findViewById(R.id.phone_number1);
        go_back_btn = findViewById(R.id.go_back_btn);
        go_back_btn.setText(Html.fromHtml("Already have an account? <font color='#27ae60'>Login</font>"));

        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        create_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    chemist_code = chemist_code1.getText().toString();
                    phone_number = phone_number1.getText().toString();

                    if (chemist_code.length() > 0) {
                        if (phone_number.length() > 0) {
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo ni = cm.getActiveNetworkInfo();
                            if (ni != null) {
                                try {
                                    new Create_New_fun().execute();
                                    create_new_btn.setVisibility(View.GONE);
                                    create_new_btn1.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }

                            } else {
                                alert.setText(Html.fromHtml("<font color='red'>Check your internet connection</font>"));
                                Toast.makeText(Create_New.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            alert.setText(Html.fromHtml("<font color='red'>Enter mobile number</font>"));
                            Toast.makeText(Create_New.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        alert.setText(Html.fromHtml("<font color='red'>Enter chemist code</font>"));
                        Toast.makeText(Create_New.this, "Enter chemist code", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    alert.setText(Html.fromHtml("<font color='red'>button click error</font>"));
                    Toast.makeText(Create_New.this, "button click error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class Create_New_fun extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            chemist_code = chemist_code1.getText().toString();
            phone_number = phone_number1.getText().toString();
            result ="";
            InputStream isr = null;
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url1);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id","apiidkapil707sharma-kavita-zxy"));

                nameValuePairs.add(new BasicNameValuePair("chemist_code",chemist_code));
                nameValuePairs.add(new BasicNameValuePair("phone_number",phone_number));
                nameValuePairs.add(new BasicNameValuePair("submit","98c08565401579448aad7c64033dcb4081906dcb"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            }
            catch (Exception e) {
                //Log.e("log_tag","Error in connection"+e.toString());
                //tv.setText("couldn't connect to the database");
                //user_alert = "Check your internet";
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
            create_new_btn.setVisibility(View.VISIBLE);
            create_new_btn1.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String status = jsonObject.getString("status");
                    String status1 = jsonObject.getString("status1");
                    if(status1.equals("1")) {
                        Intent in = new Intent();
                        in.setClass(Create_New.this, Create_New_thank_you.class);
                        in.putExtra("create_new_message", status);
                        startActivity(in);
                        finish();
                    }
                    else {
                        alert.setText(Html.fromHtml("<font color='red'>"+status+"</font>"));
                        Toast.makeText(Create_New.this,status,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e) {
                // TODO: handle exception
                //Log.e("log_tag", "Error parsing data"+e.toString());
                //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
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
}
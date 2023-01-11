package com.drdistributor.dr;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

public class Otp_page extends AppCompatActivity {

    String result ="";
    UserSessionManager session;
    ProgressBar progressBar2;

    String mainurl = "",page_url1="";

    String otp_sms="",user_altercode="",user_password="",user_alert="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);
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

        /************************************************/
        Intent in = getIntent();
        String result1 = in.getStringExtra("result");
        String firebase_token = in.getStringExtra("firebase_token");
        /************************************************/

        session = new UserSessionManager(getApplicationContext());

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl+"otp_resent_api/post";

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

        //Toast.makeText(Otp_page.this,result,Toast.LENGTH_SHORT).show();

        try {
            JSONArray jArray = new JSONArray(result1);
            for (int i = 0; i < jArray.length(); i++)
            {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String otp_massage_txt = jsonObject.getString("otp_massage_txt");

                otp_sms = jsonObject.getString("otp_sms");

                TextView user_alert1 = findViewById(R.id.user_alert);
                user_alert1.setText(Html.fromHtml(otp_massage_txt));

                user_altercode = jsonObject.getString("user_altercode");
                user_password = jsonObject.getString("user_password");
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "Error parsing data"+e.toString());
            //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
        }

        TextView otp_resent = findViewById(R.id.otp_resent);
        otp_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new otp_resent_json().execute();
            }
        });

        EditText user_otp = findViewById(R.id.user_otp);
        Button verify_btn = findViewById(R.id.verify_btn);
        Button verify_btn1 = findViewById(R.id.verify_btn1);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                TextView otp_resent1 = findViewById(R.id.otp_resent1);
                otp_resent1.setVisibility(View.VISIBLE);

                String user_otp1 = user_otp.getText().toString();
                if(user_otp1.equals(otp_sms))
                {
                    verify_btn.setVisibility(View.GONE);
                    verify_btn1.setVisibility(View.VISIBLE);

                    otp_resent1.setText("Logged in successfully");
                    Toast.makeText(Otp_page.this,"Logged in successfully",Toast.LENGTH_SHORT).show();

                    try {
                        JSONArray jArray = new JSONArray(result1);
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject jsonObject   = jArray.getJSONObject(i);
                            String user_session     = jsonObject.getString("user_session");
                            String user_fname       = jsonObject.getString("user_fname");
                            String user_code        = jsonObject.getString("user_code");
                            user_altercode          = jsonObject.getString("user_altercode");
                            String user_type        = jsonObject.getString("user_type");
                            user_password           = jsonObject.getString("user_password");
                            String user_image       = jsonObject.getString("user_image");
                            user_alert              = jsonObject.getString("user_alert");
                            String user_return      = jsonObject.getString("user_return");

                            String user_division = "",user_compcode = "",user_compname="";
                            session.createUserLoginSession(user_session, user_password, user_type, user_fname, user_code, user_altercode, user_image, user_division, user_compcode, user_compname, firebase_token);

                            Intent in = new Intent();
                            in.setClass(Otp_page.this, MainActivity.class);
                            startActivity(in);
                            finish();
                        }
                    }
                    catch (Exception e) {
                        // TODO: handle exception
                        //Log.e("log_tag", "Error parsing data"+e.toString());
                        //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    otp_resent1.setText("entered otp does not matches");
                }
            }
        });
    }

    private class otp_resent_json extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);

            TextView otp_resent = findViewById(R.id.otp_resent);
            otp_resent.setVisibility(View.GONE);

            TextView otp_resent1 = findViewById(R.id.otp_resent1);
            otp_resent1.setVisibility(View.VISIBLE);
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            result = "";
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
            //Toast.makeText(Otp_page.this,result,Toast.LENGTH_SHORT).show();
            progressBar2.setVisibility(View.GONE);

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String otp_massage_txt = jsonObject.getString("otp_massage_txt");

                    otp_sms = jsonObject.getString("otp_sms");

                    TextView user_alert1 = findViewById(R.id.user_alert);
                    user_alert1.setText(Html.fromHtml(otp_massage_txt));
                }
            }
            catch (Exception e) {
                // TODO: handle exception
                //Log.e("log_tag", "Error parsing data"+e.toString());
                //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
            }

            TextView otp_resent1 = findViewById(R.id.otp_resent1);
            otp_resent1.setText("OTP resend successfully");
        }
    }
}
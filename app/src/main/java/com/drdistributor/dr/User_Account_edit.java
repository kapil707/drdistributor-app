package com.drdistributor.dr;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class User_Account_edit extends AppCompatActivity {

    String result="";
    String session_id="",user_password="",user_type="",user_fname="",user_code="",user_altercode="",user_image="",user_phone="",user_email="",user_address="",firebase_token="";
    TextView alert;
    Button updatebtn,updatebtn1;
    UserSessionManager session;
    Database db;
    SQLiteDatabase sql;
    ProgressBar menu_loading1;
    String mainurl="",page_url1="",page_url2="";

    TextView header_result_found;

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
        setContentView(R.layout.user_account_edit);

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

        db = new Database(User_Account_edit.this);
        sql = db.getWritableDatabase();

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        session_id = user.get(UserSessionManager.KEY_USERID);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_fname = user.get(UserSessionManager.KEY_USERFNAME);

        user_code = user.get(UserSessionManager.KEY_USERCODE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_image = user.get(UserSessionManager.KEY_USERIMAGE);

        user_phone = user.get(UserSessionManager.KEY_user_phone);
        user_email = user.get(UserSessionManager.KEY_user_email);
        user_address = user.get(UserSessionManager.KEY_user_address);

        firebase_token = user.get(UserSessionManager.KEY_firebase_token);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        if (user_type.equals("sales")) {
            Toast.makeText(User_Account_edit.this,"This facility is not available for salesman",Toast.LENGTH_SHORT).show();
            finish();
        }

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl+"check_user_account_api/post";
        page_url2 = mainurl+"update_user_account_api/post";

        final EditText user_mobile_text = findViewById(R.id.user_mobile_text);
        final EditText user_email_text = findViewById(R.id.user_email_text);
        final EditText user_address_text = findViewById(R.id.user_address_text);
        try {
            if (user_phone.equals("")) {

            } else {
                user_mobile_text.setText(user_phone);
                user_email_text.setText(user_email);
                user_address_text.setText(user_address);
            }
        }catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "Error parsing data"+e.toString());
            //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
        }

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("Update account");

        menu_loading1 = (ProgressBar) findViewById(R.id.menu_loading1);

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                onBackPressed();
            }
        });

        LinearLayout menu_search_div_btn = findViewById(R.id.menu_search_div_btn);
        menu_search_div_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account_edit.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout= (LinearLayout)findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account_edit.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        alert = (TextView) findViewById(R.id.user_alert);
        updatebtn = (Button) findViewById(R.id.updatebtn);
        updatebtn1 = (Button) findViewById(R.id.updatebtn1);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                user_phone  = user_mobile_text.getText().toString();
                user_email  = user_email_text.getText().toString();
                user_address = user_address_text.getText().toString();
                if (user_phone.length() > 0) {
                    if (user_email.length() > 0) {
                        if (user_address.length() > 0) {
                            if (user_phone.length() == 10) {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo ni = cm.getActiveNetworkInfo();
                                if (ni != null) {
                                    try {
                                        new JSON_update_user_account_api().execute();
                                        updatebtn.setVisibility(View.GONE);
                                        updatebtn1.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                } else {
                                    alert.setText(Html.fromHtml("<font color='red'>Check your internet connection</font>"));
                                    Toast.makeText(User_Account_edit.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                alert.setText(Html.fromHtml("<font color='red'>Enter valid mobile number</font>"));
                                Toast.makeText(User_Account_edit.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            alert.setText(Html.fromHtml("<font color='red'>Enter address</font>"));
                            Toast.makeText(User_Account_edit.this, "Enter address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        alert.setText(Html.fromHtml("<font color='red'>Enter email</font>"));
                        Toast.makeText(User_Account_edit.this, "Enter email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    alert.setText(Html.fromHtml("<font color='red'>Enter mobile</font>"));
                    Toast.makeText(User_Account_edit.this, "Enter mobile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        header_result_found = findViewById(R.id.header_result_found);
        new json_check_user_account_api().execute();
    }

    private class JSON_update_user_account_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menu_loading1.setVisibility(View.VISIBLE);
            header_result_found.setText("Loading....");
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            result = "";
            InputStream isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url2);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-kavita-zxy"));
                nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_phone", user_phone));
                nameValuePairs.add(new BasicNameValuePair("user_email", user_email));
                nameValuePairs.add(new BasicNameValuePair("user_address", user_address));

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
            header_result_found.setText("Found result");

            updatebtn.setVisibility(View.VISIBLE);
            updatebtn1.setVisibility(View.GONE);
            menu_loading1.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String status = jsonObject.getString("status");
                    String status1 = jsonObject.getString("status1");
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    if(status1.equals("1")) {
                        new json_check_user_account_api().execute();
                        alert.setText(Html.fromHtml("<font color='#28a745'>"+status+"</font>"));
                    }
                    else
                    {
                        alert.setText(Html.fromHtml("<font color='red'>"+status+"</font>"));
                    }
                }
            }catch (Exception e) {

            }
        }
    }

    private class json_check_user_account_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menu_loading1.setVisibility(View.VISIBLE);
            header_result_found.setText("Loading....");
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
        protected void onPostExecute(Void args)
        {
            header_result_found.setText("Found result");
            menu_loading1.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String user_phone = jsonObject.getString("user_phone");
                    String user_email = jsonObject.getString("user_email");
                    String user_address = jsonObject.getString("user_address");
                    String user_update = jsonObject.getString("user_update");
                    if(user_update.equals("1"))
                    {
                        LinearLayout user_data = findViewById(R.id.user_data);
                        user_data.setVisibility(View.VISIBLE);
                        TextView u_mobile = findViewById(R.id.u_mobile);
                        TextView u_email = findViewById(R.id.u_email);
                        TextView u_address = findViewById(R.id.u_address);
                        u_mobile.setText(user_phone);
                        u_email.setText(user_email);
                        u_address.setText(user_address);
                    }
                }
            }catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "e2", Toast.LENGTH_LONG).show();
            }
        }
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        TextView u_name = findViewById(R.id.u_name);
        u_name.setText(user_fname);
        TextView u_code = findViewById(R.id.u_code);
        u_code.setText("Code : "+user_altercode);

        try {
            ImageView u_img = findViewById(R.id.u_img);
            Picasso.with(this).load(user_image).into(u_img);
        }catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }

        get_user_cart();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
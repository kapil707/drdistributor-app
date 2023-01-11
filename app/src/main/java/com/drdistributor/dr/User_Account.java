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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class User_Account extends AppCompatActivity {
    String result="";
    String session_id="",user_password="",user_type="",user_fname="",user_code="",user_altercode="",user_image="",user_phone="",user_email="",user_address="",firebase_token="";

    UserSessionManager session;
    Database db;
    SQLiteDatabase sql;
    ProgressBar menu_loading1;
    String mainurl="",page_url1="";

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
        setContentView(R.layout.user_account);

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

        db = new Database(User_Account.this);
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

        try {
            TextView android_v = findViewById(R.id.android_v);
            int versioncode = BuildConfig.VERSION_CODE;
            android_v.setText("App version " + versioncode);
        }catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "Error parsing data"+e.toString());
            //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
        }

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl+"user_account_api/post";

        LinearLayout user_data = (LinearLayout) findViewById(R.id.user_data);
        try {
            if (user_phone.equals("")) {
                user_data.setVisibility(View.GONE);
            } else {
                TextView u_mobile = findViewById(R.id.u_mobile);
                TextView u_email = findViewById(R.id.u_email);
                TextView u_address = findViewById(R.id.u_address);
                u_mobile.setText(user_phone);
                u_email.setText(user_email);
                u_address.setText(user_address);
            }
        }catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "Error parsing data"+e.toString());
            //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
        }

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("Account");

        menu_loading1 = (ProgressBar) findViewById(R.id.menu_loading1);

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout menu_search_div_btn = findViewById(R.id.menu_search_div_btn);
        menu_search_div_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout= (LinearLayout)findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        ImageView u_edit = findViewById(R.id.u_edit);
        u_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account.this, User_Account_edit.class);
                startActivity(in);
            }
        });

        LinearLayout change_image = findViewById(R.id.change_image);
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account.this, User_image_uploading.class);
                startActivity(in);
            }
        });

        LinearLayout change_password = findViewById(R.id.change_password);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Account.this, User_Change_Password.class);
                startActivity(in);
            }
        });

        LinearLayout my_orders= (LinearLayout)findViewById(R.id.my_orders);
        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null) {
                    Intent myIntent = new Intent(User_Account.this,
                            Chemist_Webview.class);
                    myIntent.putExtra("page_url", mainurl + "my_orders/?user_type=" + user_type + "&user_altercode=" + user_altercode);
                    myIntent.putExtra("page_title", "My Orders");
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connaction", Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout my_invoices= (LinearLayout)findViewById(R.id.my_invoices);
        my_invoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null) {
                    Intent myIntent = new Intent(User_Account.this,
                            Chemist_Webview.class);
                    myIntent.putExtra("page_url", mainurl + "my_invoice/?user_type=" + user_type + "&user_altercode=" + user_altercode);
                    myIntent.putExtra("page_title", "My Invoices");
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connaction", Toast.LENGTH_LONG).show();
                }
            }
        });

        header_result_found = findViewById(R.id.header_result_found);
        new json_user_account_api().execute();
    }

    private class json_user_account_api extends AsyncTask<Void, Void, Void> {
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
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String altercode = jsonObject.getString("altercode");
                    String mobile = jsonObject.getString("mobile");
                    String email = jsonObject.getString("email");
                    String gstno = jsonObject.getString("gstno");
                    String address = jsonObject.getString("address");
                    String status = jsonObject.getString("status");

                    user_phone = mobile;
                    user_email = email;
                    user_address = address;

                    /*byte[] data = Base64.decode(name, Base64.DEFAULT);
                    name = new String(data, "UTF-8");

                    byte[] data1 = Base64.decode(address, Base64.DEFAULT);
                    address = new String(data1, "UTF-8");*/

                    TextView u_name = findViewById(R.id.u_name);
                    TextView u_code = findViewById(R.id.u_code);
                    TextView u_mobile = findViewById(R.id.u_mobile);
                    TextView u_email = findViewById(R.id.u_email);
                    TextView u_gstno = findViewById(R.id.u_gstno);
                    TextView u_address = findViewById(R.id.u_address);
                    TextView u_status = findViewById(R.id.u_status);
                    u_name.setText(name);
                    u_code.setText("Code : "+altercode);
                    u_mobile.setText(mobile);
                    u_email.setText(email);
                    u_address.setText(address);
                    u_gstno.setText("Gst No. : "+gstno);
                    u_status.setText("Status : "+status);

                    if(address.equals(""))
                    {
                        u_address.setVisibility(View.GONE);
                    }

                    if(gstno.equals(""))
                    {
                        u_gstno.setVisibility(View.GONE);
                    }
                }
                LinearLayout user_data = (LinearLayout) findViewById(R.id.user_data);
                user_data.setVisibility(View.VISIBLE);

                session.createUserLoginSession(session_id,user_password,user_type,user_fname,user_code,user_altercode,user_image,user_phone,user_email,user_address,firebase_token);

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

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        user_image = user.get(UserSessionManager.KEY_USERIMAGE);

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

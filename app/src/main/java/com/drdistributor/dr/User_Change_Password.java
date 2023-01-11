package com.drdistributor.dr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
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

public class User_Change_Password extends AppCompatActivity {
    String user_fname="",user_altercode = "", user_type = "",user_image="";
    TextView alert;
    Button change_password,change_password1;
    UserSessionManager session;
    Database db;
    SQLiteDatabase sql;
    String result = "";
    String mainurl = "", page_url1 = "";
    ProgressBar menu_loading1;

    String password = "", password1 = "", password2 = "";

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
        setContentView(R.layout.user_change_password);

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

        db = new Database(User_Change_Password.this);
        sql = db.getWritableDatabase();

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_fname = user.get(UserSessionManager.KEY_USERFNAME);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_image = user.get(UserSessionManager.KEY_USERIMAGE);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl + "change_password_api/post";

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

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("Update password");

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
                in.setClass(User_Change_Password.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_Change_Password.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        alert = (TextView) findViewById(R.id.user_alert);
        final EditText oldpassword = (EditText) findViewById(R.id.oldpassword);
        final EditText newpassword = (EditText) findViewById(R.id.newpassword);
        final EditText repassword = (EditText) findViewById(R.id.repassword);

        change_password = (Button) findViewById(R.id.change_password);
        change_password1 = (Button) findViewById(R.id.change_password1);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                password = oldpassword.getText().toString();
                password1 = newpassword.getText().toString();
                password2 = repassword.getText().toString();
                if (password.length() > 0) {
                    if (password1.length() > 0) {
                        if (password2.length() > 0) {
                            if (password1.length() >= 8) {
                                if (password1.equals(password2)) {
                                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo ni = cm.getActiveNetworkInfo();
                                    if (ni != null) {
                                        try {
                                            new JSON_change_password_api().execute();
                                            change_password.setVisibility(View.GONE);
                                            change_password1.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    } else {
                                        alert.setText(Html.fromHtml("<font color='red'>Check your internet connection</font>"));
                                        Toast.makeText(User_Change_Password.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    alert.setText(Html.fromHtml("<font color='red'>Password doesn't match</font>"));
                                    Toast.makeText(User_Change_Password.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                alert.setText(Html.fromHtml("<font color='red'>Password must contain 8 characters (e.g. A,a or 1,2 or !,$,@)</font>"));
                                Toast.makeText(User_Change_Password.this, "Password must contain 8 characters (e.g. A,a or 1,2 or !,$,@)", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            alert.setText(Html.fromHtml("<font color='red'>Enter Re-enter newpassword</font>"));
                            Toast.makeText(User_Change_Password.this, "Enter Re-enter newpassword", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        alert.setText(Html.fromHtml("<font color='red'>Enter newpassword</font>"));
                        Toast.makeText(User_Change_Password.this, "Enter newpassword", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    alert.setText(Html.fromHtml("<font color='red'>Enter oldpassword</font>"));
                    Toast.makeText(User_Change_Password.this, "Enter oldpassword", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ImageView old_eyes = findViewById(R.id.old_eyes);
        final ImageView old_eyes1 = findViewById(R.id.old_eyes1);
        old_eyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                old_eyes1.setVisibility(View.GONE);
                old_eyes.setVisibility(View.VISIBLE);
                oldpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                oldpassword.setSelection(oldpassword.getText().length());
            }
        });

        old_eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                old_eyes.setVisibility(View.GONE);
                old_eyes1.setVisibility(View.VISIBLE);
                oldpassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                oldpassword.setSelection(oldpassword.getText().length());
            }
        });

        final ImageView new_eyes = findViewById(R.id.new_eyes);
        final ImageView new_eyes1 = findViewById(R.id.new_eyes1);
        new_eyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new_eyes1.setVisibility(View.GONE);
                new_eyes.setVisibility(View.VISIBLE);
                newpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                newpassword.setSelection(newpassword.getText().length());
            }
        });

        new_eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new_eyes.setVisibility(View.GONE);
                new_eyes1.setVisibility(View.VISIBLE);
                newpassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newpassword.setSelection(newpassword.getText().length());
            }
        });

        final ImageView renew_eyes = findViewById(R.id.renew_eyes);
        final ImageView renew_eyes1 = findViewById(R.id.renew_eyes1);
        renew_eyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                renew_eyes1.setVisibility(View.GONE);
                renew_eyes.setVisibility(View.VISIBLE);
                repassword.setInputType(InputType.TYPE_CLASS_TEXT);
                repassword.setSelection(repassword.getText().length());
            }
        });

        renew_eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                renew_eyes.setVisibility(View.GONE);
                renew_eyes1.setVisibility(View.VISIBLE);
                repassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                repassword.setSelection(repassword.getText().length());
            }
        });

        header_result_found = findViewById(R.id.header_result_found);
        header_result_found.setText("Found result");
    }

    private class JSON_change_password_api extends AsyncTask<Void, Void, Void> {
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

                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));

                nameValuePairs.add(new BasicNameValuePair("old_password", password));
                nameValuePairs.add(new BasicNameValuePair("new_password", password1));

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
            header_result_found.setText("");

            change_password.setVisibility(View.VISIBLE);
            change_password1.setVisibility(View.GONE);
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
                        EditText oldpassword = (EditText) findViewById(R.id.oldpassword);
                        EditText newpassword = (EditText) findViewById(R.id.newpassword);
                        EditText repassword = (EditText) findViewById(R.id.repassword);
                        oldpassword.setText("");
                        newpassword.setText("");
                        repassword.setText("");
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
        finish();
    }
}

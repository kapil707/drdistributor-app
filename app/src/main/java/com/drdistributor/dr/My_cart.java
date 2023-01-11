package com.drdistributor.dr;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;

public class My_cart extends AppCompatActivity {

    UserSessionManager session;
    String user_type="",user_altercode="",user_password="",selesman_id="",chemist_name="",chemist_id = "";
    String result = "",mainurl = "", page_url = "" ,page_url2="",device_id="";

    ListView listview;
    My_cart_Adapter adapter;
    List<My_cart_get_or_set> My_cart_List = new ArrayList<My_cart_get_or_set>();

    ImageView cart_empty_img;
    TextView place_order_message_tv,header_result_found;
    Button cart_place_order_btn,cart_place_order_btn_dis;

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
        setContentView(R.layout.my_cart);

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

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        TextView select_chemist = findViewById(R.id.select_chemist);
        ImageView edit_chemist = findViewById(R.id.edit_chemist);
        if (user_type.equals("sales")) {
            chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
            chemist_name = sharedpreferences.getString(USER_CHEMIST_NAME, null);

            TextView action_bar_title1 = findViewById(R.id.action_bar_title1);
            action_bar_title1.setText(chemist_name);
            action_bar_title1.setVisibility(View.VISIBLE);

            select_chemist.setText(" | Code : " + chemist_id + " | ");
            edit_chemist.setVisibility(View.VISIBLE);
            select_chemist.setVisibility(View.VISIBLE);
            //edit_chemist1.setText("Change Chemist");
            if (chemist_id.equals("")) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();

                Intent myIntent = new Intent(My_cart.this,
                        Chemist_search.class);
                startActivity(myIntent);
                finish();
            }
        }

        edit_chemist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();

                Intent myIntent = new Intent(My_cart.this,
                        Chemist_search.class);
                startActivity(myIntent);
                finish();
            }
        });

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url = mainurl + "my_cart_api/post";
        page_url2 = mainurl + "delete_all_medicine_api/post";

        /*String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id = android_id;*/

        TextView action_bar_title = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title.setText("Draft");

        ImageButton action_bar_back = (ImageButton) findViewById(R.id.action_bar_back);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
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
                in.setClass(My_cart.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        cart_empty_img = findViewById(R.id.cart_empty_img);
        header_result_found = findViewById(R.id.header_result_found);
        place_order_message_tv = findViewById(R.id.place_order_message_tv);

        cart_place_order_btn = findViewById(R.id.cart_place_order_btn);
        cart_place_order_btn_dis = findViewById(R.id.cart_place_order_btn_dis);

        listview = findViewById(R.id.listView1);
        adapter = new My_cart_Adapter(My_cart.this, My_cart_List);
        listview.setAdapter(adapter);

        /*linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);*/
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                My_cart_get_or_set clickedCategory = My_cart_List.get(arg2);
                /*med_item_id = clickedCategory.item_code();
                chemist_id = clickedCategory.user_altercode();*/
            }
        });

        listview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "Position", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        cart_place_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMessage_cart();
            }
        });

        ImageButton delete_all_btn = findViewById(R.id.delete_all_btn);
        delete_all_btn.setVisibility(View.VISIBLE);
        delete_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMessage_deleteall();
            }
        });
    }

    public void alertMessage_deleteall() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            EditText enter_remarks = (EditText) findViewById(R.id.enter_remarks);
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        try
                        {
                            new json_delete_all_medicine_api().execute();
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
        builder.setMessage("Are you sure to delete all medicines?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void alertMessage_cart() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            EditText enter_remarks = (EditText) findViewById(R.id.enter_remarks);
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        try
                        {
                            Intent in = new Intent();
                            in.setClass(My_cart.this, Place_order.class);
                            startActivity(in);
                            finish();
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
        builder.setMessage("Are you sure to place order?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private class json_my_cart_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            header_result_found.setText("Loading....");

            /*
            mProgressBar.setCancelable(false);
            mProgressBar.setMessage("Synchronizing your cart please wait");
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.show();*/

            cart_place_order_btn.setVisibility(View.GONE);
            cart_place_order_btn_dis.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            result = "";
            InputStream isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-kavita-zxy"));
                nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));
                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));
                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

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
            //Toast.makeText(getApplicationContext(), items, Toast.LENGTH_LONG).show();
            try {
                JSONArray jArray0 = new JSONArray(result);
                JSONObject jsonObject0 = jArray0.getJSONObject(0);
                String items = jsonObject0.getString("items");
                String other_items = jsonObject0.getString("other_items");
                //Toast.makeText(getApplicationContext(), items, Toast.LENGTH_LONG).show();

                String user_cart_json0 = items;
                JSONArray jArray2 = new JSONArray(other_items);
                for (int i = 0; i < jArray2.length(); i++) {
                    JSONObject jsonObject = jArray2.getJSONObject(i);

                    String items_total = jsonObject.getString("items_total");
                    String items_price = jsonObject.getString("items_price");

                    if (user_cart_json0.equals("[]")) {
                        user_cart_json0 = "";
                    }

                    /************************************************************/
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(USER_CART, items_total);
                    editor.putString(USER_CART_TOTAL, items_price);
                    editor.putString(USER_CART_JSON, user_cart_json0);
                    editor.apply();
                    /************************************************************/

                    String place_order_button = jsonObject.getString("place_order_button");
                    String place_order_message = jsonObject.getString("place_order_message");

                    place_order_message_tv.setText(Html.fromHtml(place_order_message));
                    if(place_order_message.equals(""))
                    {
                        place_order_message_tv.setText("Place order");
                    }

                    if(place_order_button.equals("1"))
                    {
                        place_order_message_tv.setText("Place order");
                        cart_place_order_btn.setVisibility(View.VISIBLE);
                        cart_place_order_btn_dis.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"error -- json_my_cart_api --" + e.toString(), Toast.LENGTH_LONG).show();
            }
            get_user_cart();
            get_user_cart_list();
        }
    }

    private class json_delete_all_medicine_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{

                /************************************************************/
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(USER_CART, "0");
                editor.putString(USER_CART_TOTAL, "0.00");
                editor.putString(USER_CART_JSON, "");
                editor.apply();
                /************************************************************/

                get_user_cart();
                get_user_cart_list();
            }
            catch (Exception e){

            }
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

                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));

                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

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
            try {
                JSONArray jArray0 = new JSONArray(result);
                JSONObject jsonObject0 = jArray0.getJSONObject(0);
                String items = jsonObject0.getString("items");

                JSONArray jArray1 = new JSONArray(items);
                for (int i = 0; i < jArray1.length(); i++) {
                    JSONObject jsonObject = jArray1.getJSONObject(i);

                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Medicines deleted successfully", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Medicines not deleted", Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "error -- json_delete_all_medicine_api" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void get_user_cart() {
        try {
            String user_cart = sharedpreferences.getString(USER_CART, null);
            String user_cart_total = sharedpreferences.getString(USER_CART_TOTAL, null);

            header_result_found.setText("Current order (" + user_cart + ")");

            TextView action_bar_cart_total = findViewById(R.id.action_bar_cart_total);
            action_bar_cart_total.setText(" " + user_cart + " ");

            TextView footer_user_cart = findViewById(R.id.footer_user_cart);
            footer_user_cart.setText(user_cart + " items");

            TextView footer_user_cart_total = findViewById(R.id.footer_user_cart_total);
            footer_user_cart_total.setText("₹ " + user_cart_total + "/-");

            ImageButton delete_all_btn = findViewById(R.id.delete_all_btn);
            delete_all_btn.setVisibility(View.VISIBLE);
            if(user_cart.equals("0"))
            {
                delete_all_btn.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error get_user_cart" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void get_user_cart_list() {
        String items = sharedpreferences.getString(USER_CART_JSON, null);
        cart_empty_img.setVisibility(View.GONE);
        try {
            My_cart_List.clear();
            if(items.equals(""))
            {
                cart_empty_img.setVisibility(View.VISIBLE);
            }
            else {
                JSONArray jArray1 = new JSONArray(items);
                for (int i = 0; i < jArray1.length(); i++) {
                    JSONObject jsonObject = jArray1.getJSONObject(i);
                    String item_code = jsonObject.getString("item_code");
                    String item_quantity = jsonObject.getString("item_quantity");
                    String item_image = jsonObject.getString("item_image");
                    String item_name = jsonObject.getString("item_name");
                    String item_packing = jsonObject.getString("item_packing");
                    String item_expiry = jsonObject.getString("item_expiry");
                    String item_company = jsonObject.getString("item_company");
                    String item_scheme = jsonObject.getString("item_scheme");
                    String item_margin = jsonObject.getString("item_margin");
                    String item_featured = jsonObject.getString("item_featured");
                    String item_price = jsonObject.getString("item_price");
                    String item_datetime = jsonObject.getString("item_datetime");
                    String item_modalnumber = jsonObject.getString("item_modalnumber");

                    My_cart_get_or_set My_cart_get = new My_cart_get_or_set();
                    My_cart_get.item_code(item_code);
                    My_cart_get.item_quantity(item_quantity);
                    My_cart_get.item_image(item_image);
                    My_cart_get.item_name(item_name);
                    My_cart_get.item_packing(item_packing);
                    My_cart_get.item_expiry(item_expiry);
                    My_cart_get.item_company(item_company);
                    My_cart_get.item_scheme(item_scheme);
                    My_cart_get.item_margin(item_margin);
                    My_cart_get.item_featured(item_featured);
                    My_cart_get.item_price(item_price);
                    My_cart_get.item_datetime(item_datetime);
                    My_cart_get.item_modalnumber(item_modalnumber);
                    My_cart_List.add(My_cart_get);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error get_user_cart_list" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            get_user_cart();
            get_user_cart_list();
            new json_my_cart_api().execute();
        } catch (Exception e) {
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
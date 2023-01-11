package com.drdistributor.dr;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

public class Chemist_search extends AppCompatActivity {

    String main_url="",page_url1="",page_url2="";
    String user_type="",user_altercode="",result ="";
    UserSessionManager session;
    EditText edt;

    ListView listview;
    Chemist_search_Adapter adapter;
    List<Chemist_search_get_or_set> Select_chemist_List = new ArrayList<Chemist_search_get_or_set>();

    ImageView cancel_btn;

    TextView header_result_found;
    LinearLayout loading_div;
    ImageView no_record_found_img;

    //https://www.geeksforgeeks.org/session-management-in-android-with-example/
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_CART = "user_cart";
    public static final String USER_CART_TOTAL = "user_cart_total";
    public static final String USER_CART_JSON = "user_cart_json";
    public static final String USER_CHEMIST_ID = "user_chemist_id";
    public static final String USER_CHEMIST_NAME = "user_chemist_name";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chemist_search);
        getSupportActionBar().setElevation(0);

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
        main_url = ma.main_url;
        page_url1 =  main_url + "salesman_my_cart_api/post";
        page_url2 =  main_url + "chemist_search_api/post";

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("Search chemist");

        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);
        imageButton.setVisibility(View.GONE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout cart_LinearLayout= (LinearLayout)view.findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Chemist_search.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        header_result_found = findViewById(R.id.header_result_found);
        loading_div = findViewById(R.id.loading_div);
        no_record_found_img = findViewById(R.id.no_record_found_img);

        listview = findViewById(R.id.listView1);
        adapter = new Chemist_search_Adapter(Chemist_search.this, Select_chemist_List);
        listview.setAdapter(adapter);

        cancel_btn = findViewById(R.id.cancel_btn);

        edt = (EditText) findViewById(R.id.editText1);
        edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                String msg = edt.getText().toString();
                if (msg.length()>0)
                {
                    if (msg.length()>2) {
                        new json_search_chemist_api().execute();
                        cancel_btn.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Select_chemist_List.clear();
                    adapter.notifyDataSetChanged();

                    cancel_btn.setVisibility(View.GONE);
                    new json_salesman_my_cart_api().execute();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                edt.setText("");
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                try {
                    Chemist_search_get_or_set clickedCategory = Select_chemist_List.get(arg2);
                    String chemist_altercode = clickedCategory.chemist_altercode();
                    String chemist_name = clickedCategory.chemist_name();

                    /************************************************************/
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(USER_CHEMIST_ID, chemist_altercode);
                    editor.putString(USER_CHEMIST_NAME, chemist_name);
                    editor.apply();
                    /************************************************************/

                    Intent in = new Intent();
                    in.setClass(Chemist_search.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error listview " + e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

        listview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
				/* Toast.makeText(getApplicationContext(), "Position",Toast.LENGTH_LONG).show(); */
                return false;
            }
        });
    }

    private class json_search_chemist_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            header_result_found.setText("Loading....");
            loading_div.setVisibility(View.VISIBLE);
            no_record_found_img.setVisibility(View.GONE);
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            String keyword = edt.getText().toString();
            if (keyword.length()>2) {
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

                    nameValuePairs.add(new BasicNameValuePair("keyword", keyword));

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
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args)
        {
            header_result_found.setText("Found result");
            loading_div.setVisibility(View.GONE);
            no_record_found_img.setVisibility(View.GONE);

            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                Select_chemist_List.clear();
                if(!result.equals("")) {
                    JSONArray jArray0 = new JSONArray(result);
                    JSONObject jsonObject0 = jArray0.getJSONObject(0);
                    String items = jsonObject0.getString("items");

                    JSONArray jArray1 = new JSONArray(items);
                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject jsonObject = jArray1.getJSONObject(i);

                        String chemist_altercode = jsonObject.getString("chemist_altercode");
                        String chemist_name = jsonObject.getString("chemist_name");
                        String chemist_image = jsonObject.getString("chemist_image");
                        String user_cart = jsonObject.getString("user_cart");
                        String user_cart_total = jsonObject.getString("user_cart_total");

                        Chemist_search_get_or_set Select_chemist_set = new Chemist_search_get_or_set();

                        Select_chemist_set.chemist_altercode(chemist_altercode);
                        Select_chemist_set.chemist_name(chemist_name);
                        Select_chemist_set.chemist_image(chemist_image);
                        Select_chemist_set.user_cart(user_cart);
                        Select_chemist_set.user_cart_total(user_cart_total);

                        Select_chemist_List.add(Select_chemist_set);
                    }
                }
                adapter.notifyDataSetChanged();
                if (Select_chemist_List.isEmpty()) {
                    no_record_found_img.setVisibility(View.VISIBLE);
                    header_result_found.setText("No record found");
                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                no_record_found_img.setVisibility(View.VISIBLE);
                header_result_found.setText("No record found");
            }
        }
    }

    private class json_salesman_my_cart_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            header_result_found.setText("Loading....");
            loading_div.setVisibility(View.VISIBLE);
            no_record_found_img.setVisibility(View.GONE);
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
        protected void onPostExecute(Void args) {
            header_result_found.setText("Current order");
            loading_div.setVisibility(View.GONE);
            no_record_found_img.setVisibility(View.GONE);

            Select_chemist_List.clear();
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (!result.equals("")) {
                    JSONArray jArray0 = new JSONArray(result);
                    JSONObject jsonObject0 = jArray0.getJSONObject(0);
                    String items = jsonObject0.getString("items");

                    JSONArray jArray1 = new JSONArray(items);
                    header_result_found.setText("Current order (" + jArray1.length() + ")");

                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject jsonObject = jArray1.getJSONObject(i);

                        String chemist_altercode = jsonObject.getString("chemist_altercode");
                        String chemist_name = jsonObject.getString("chemist_name");
                        String chemist_image = jsonObject.getString("chemist_image");
                        String user_cart = jsonObject.getString("user_cart");
                        String user_cart_total = jsonObject.getString("user_cart_total");

                        Chemist_search_get_or_set Select_chemist_set = new Chemist_search_get_or_set();

                        Select_chemist_set.chemist_altercode(chemist_altercode);
                        Select_chemist_set.chemist_name(chemist_name);
                        Select_chemist_set.chemist_image(chemist_image);
                        Select_chemist_set.user_cart(user_cart);
                        Select_chemist_set.user_cart_total(user_cart_total);

                        Select_chemist_List.add(Select_chemist_set);
                    }

                    adapter.notifyDataSetChanged();
                    if (Select_chemist_List.isEmpty()) {
                        no_record_found_img.setVisibility(View.VISIBLE);
                        header_result_found.setText("No record found");
                    }
                } else {
                    no_record_found_img.setVisibility(View.VISIBLE);
                    header_result_found.setText("No record found");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                no_record_found_img.setVisibility(View.VISIBLE);
                header_result_found.setText("No record found");
            }
        }
    }

    @Override
    protected void onResume(){
        // TODO Auto-generated method stub
        super.onResume();
        new json_salesman_my_cart_api().execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

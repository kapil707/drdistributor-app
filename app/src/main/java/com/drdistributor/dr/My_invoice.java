package com.drdistributor.dr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

public class My_invoice extends AppCompatActivity {

    String user_type = "", user_altercode = "", user_password = "";
    UserSessionManager session;
    String mainurl = "", page_url1 = "", result = "", get_record = "0";

    ListView listview;
    My_invoice_Adapter adapter;
    List<My_invoice_get_or_set> My_invoice_List = new ArrayList<My_invoice_get_or_set>();

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
    SharedPreferences sharedpreferences;
    String chemist_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_invoice);

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
        mainurl = ma.main_url;
        page_url1 = mainurl + "My_invoice_api/post";

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        user_altercode = user.get(UserSessionManager.KEY_USERALTERCODE);
        user_password = user.get(UserSessionManager.KEY_PASSWORD);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        if (user_type.equals("sales")) {
            chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
        }

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("My invoices");

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
                in.setClass(My_invoice.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(My_invoice.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });
        header_result_found = findViewById(R.id.header_result_found);
        loading_div = findViewById(R.id.loading_div);
        no_record_found_img = findViewById(R.id.no_record_found_img);

        listview = findViewById(R.id.listView1);
        adapter = new My_invoice_Adapter(My_invoice.this, My_invoice_List);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                My_invoice_get_or_set clickedCategory = My_invoice_List.get(arg2);
                String item_id = clickedCategory.item_id();

                Intent open_page = new Intent();
                open_page.setClass(My_invoice.this, My_invoice_details.class);
                open_page.putExtra("item_id", item_id);

                startActivity(open_page);
                finish();
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
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (query_work == 0) {
                        new json_My_invoice().execute();
                    }
                }
            }
        });

        new My_invoice.json_My_invoice().execute();
    }

    int query_work = 0;

    private class json_My_invoice extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            query_work = 1;
            header_result_found.setText("Loading....");
            loading_div.setVisibility(View.VISIBLE);
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
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));

                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

                nameValuePairs.add(new BasicNameValuePair("get_record", get_record));

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
            loading_div.setVisibility(View.GONE);
            no_record_found_img.setVisibility(View.GONE);

            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (!result.equals("")) {
                    JSONArray jArray0 = new JSONArray(result);
                    JSONObject jsonObject0 = jArray0.getJSONObject(0);
                    String items = jsonObject0.getString("items");
                    //String other_items = jsonObject0.getString("other_items");

                    JSONArray jArray1 = new JSONArray(items);
                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject jsonObject = jArray1.getJSONObject(i);
                        String item_id = jsonObject.getString("item_id");
                        String item_title = jsonObject.getString("item_title");
                        String item_message = jsonObject.getString("item_message");
                        String item_date_time = jsonObject.getString("item_date_time");
                        String item_image = jsonObject.getString("item_image");

                        get_record = jsonObject.getString("get_record");
                        if (Integer.parseInt(get_record) % 12 == 0) {
                            query_work = 0;
                        }

                        My_invoice_get_or_set My_invoice_set = new My_invoice_get_or_set();
                        My_invoice_set.item_id(item_id);
                        My_invoice_set.item_title(item_title);
                        My_invoice_set.item_message(item_message);
                        My_invoice_set.item_date_time(item_date_time);
                        My_invoice_set.item_image(item_image);
                        My_invoice_List.add(My_invoice_set);
                    }
                    adapter.notifyDataSetChanged();
                    if (My_invoice_List.isEmpty()) {
                        no_record_found_img.setVisibility(View.VISIBLE);
                        header_result_found.setText("No record found");
                    }
                } else {
                    no_record_found_img.setVisibility(View.VISIBLE);
                    header_result_found.setText("No record found");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "error -- json_My_invoice --" + e.toString(), Toast.LENGTH_LONG).show();

                no_record_found_img.setVisibility(View.VISIBLE);
                header_result_found.setText("No record found");
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

        get_user_cart();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
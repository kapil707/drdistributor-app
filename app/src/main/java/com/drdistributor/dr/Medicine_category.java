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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Medicine_category extends AppCompatActivity {

    String user_type = "", user_altercode = "", user_password = "";
    UserSessionManager session;
    String mainurl = "", page_url1 = "", result = "", get_record = "0";

    GridView listview;
    Medicine_category_Adapter adapter;
    List<Medicine_category_get_or_set> Medicine_category_list = new ArrayList<Medicine_category_get_or_set>();

    TextView header_result_found;
    LinearLayout loading_div;
    ImageView no_record_found_img;

    String item_code = "", item_division = "", item_page_type = "";

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
        setContentView(R.layout.medicine_category);
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

        Intent in = getIntent();
        item_code = in.getStringExtra("item_code");
        item_division = in.getStringExtra("item_division");
        item_page_type = in.getStringExtra("item_page_type");

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url1 = mainurl + "medicine_category_api/post";

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
        action_bar_title.setText("Medicine category");

        ImageButton action_bar_back = findViewById(R.id.action_bar_back);
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
                in.setClass(Medicine_category.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Medicine_category.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        header_result_found = findViewById(R.id.header_result_found);
        loading_div = findViewById(R.id.loading_div);
        no_record_found_img = findViewById(R.id.no_record_found_img);

        listview = findViewById(R.id.listView1);
        adapter = new Medicine_category_Adapter(Medicine_category.this, Medicine_category_list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                Medicine_category_get_or_set clickedCategory = Medicine_category_list.get(arg2);
                String item_code = clickedCategory.item_code();

                Intent in = new Intent();
                in.setClass(Medicine_category.this, Medicine_details.class);
                in.putExtra("item_code", item_code);
                startActivity(in);
                //finish();
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
                        new json_featured_brand().execute();
                    }
                }
            }
        });

        new json_featured_brand().execute();
    }

    int query_work = 0;

    private class json_featured_brand extends AsyncTask<Void, Void, Void> {
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
            try {
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

                    nameValuePairs.add(new BasicNameValuePair("item_code", item_code));
                    nameValuePairs.add(new BasicNameValuePair("item_division", item_division));
                    nameValuePairs.add(new BasicNameValuePair("item_page_type", item_page_type));

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

                JSONArray jArray0 = new JSONArray(result);
                JSONObject jsonObject0 = jArray0.getJSONObject(0);
                String items = jsonObject0.getString("items");
                //String other_items = jsonObject0.getString("other_items");

                JSONArray jArray1 = new JSONArray(items);
                for (int i = 0; i < jArray1.length(); i++) {

                    JSONObject jsonObject = jArray1.getJSONObject(i);
                    String item_code = jsonObject.getString("item_code");
                    String item_image = jsonObject.getString("item_image");
                    String item_name = jsonObject.getString("item_name");
                    String item_packing = jsonObject.getString("item_packing");
                    String item_company = jsonObject.getString("item_company");
                    String item_quantity = jsonObject.getString("item_quantity");
                    String item_ptr = jsonObject.getString("item_ptr");
                    String item_mrp = jsonObject.getString("item_mrp");
                    String item_price = jsonObject.getString("item_price");
                    String item_margin = jsonObject.getString("item_margin");
                    String item_featured = jsonObject.getString("item_featured");
                    String item_header_title = jsonObject.getString("item_header_title");

                    get_record = jsonObject.getString("get_record");
                    if (!get_record.equals("")) {
                        if (Integer.parseInt(get_record) % 12 == 0) {
                            query_work = 0;
                        }
                    }

                    TextView action_bar_title = findViewById(R.id.action_bar_title);
                    action_bar_title.setText(item_header_title);

                    Medicine_category_get_or_set Featured_brand_set = new Medicine_category_get_or_set();
                    Featured_brand_set.item_code(item_code);
                    Featured_brand_set.item_image(item_image);
                    Featured_brand_set.item_name(item_name);
                    Featured_brand_set.item_packing(item_packing);
                    Featured_brand_set.item_company(item_company);
                    Featured_brand_set.item_quantity(item_quantity);
                    Featured_brand_set.item_ptr(item_ptr);
                    Featured_brand_set.item_mrp(item_mrp);
                    Featured_brand_set.item_price(item_price);
                    Featured_brand_set.item_margin(item_margin);
                    Featured_brand_set.item_featured(item_featured);
                    Medicine_category_list.add(Featured_brand_set);
                }
                adapter.notifyDataSetChanged();

                if (Medicine_category_list.isEmpty()) {
                    no_record_found_img.setVisibility(View.VISIBLE);
                    header_result_found.setText("No record found");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "error -- json_Medicine_category --" + e.toString(), Toast.LENGTH_LONG).show();
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

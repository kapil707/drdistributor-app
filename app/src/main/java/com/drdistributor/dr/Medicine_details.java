package com.drdistributor.dr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class Medicine_details extends AppCompatActivity {

    String mobilenumber = "", modalnumber = "",item_order_quantity = "",device_id="";
    String user_type="",user_altercode="",user_password="",chemist_id = "";
    String result = "",mainurl = "", page_url = "" ,page_url2="";

    String item_code="",item_image="",item_name="",item_packing="",item_expiry="",
            item_company="",item_quantity="",item_stock="",item_ptr="",item_mrp="",
            item_price="",item_scheme="",item_margin="",item_featured="",item_description1="";

    String item_date_time="",item_image2="",item_image3="",item_image4="",
            item_batch_no="",item_gst="",item_discount="",item_description2="";

    EditText item_quantity_txt;
    Button add_to_cart,add_to_cart_dis;
    LinearLayout footer_div;

    TextView header_result_found;

    UserSessionManager session;

    //https://www.geeksforgeeks.org/session-management-in-android-with-example/
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_CART = "user_cart";
    public static final String USER_CART_TOTAL = "user_cart_total";
    public static final String USER_CART_JSON = "user_cart_json";
    public static final String USER_CHEMIST_ID = "user_chemist_id";
    public static final String USER_CHEMIST_NAME = "user_chemist_name";
    SharedPreferences sharedpreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_details);

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

        if (user_type.equals("sales")) {
            chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
        }

        MainActivity ma = new MainActivity();
        mainurl = ma.main_url;
        page_url = mainurl + "medicine_details_api/post";
        page_url2 = mainurl + "medicine_add_to_cart_api/post";

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("Medicine details");

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
                in.setClass(Medicine_details.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Medicine_details.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        //Toast.makeText(getApplicationContext(), item_item_id,Toast.LENGTH_LONG).show();

        Intent in = getIntent();
        item_code = in.getStringExtra("item_code");
        item_image = in.getStringExtra("item_image");
        item_name = in.getStringExtra("item_name");
        item_packing = in.getStringExtra("item_packing");
        item_expiry = in.getStringExtra("item_expiry");
        item_company = in.getStringExtra("item_company");
        item_quantity = in.getStringExtra("item_quantity");
        item_stock = in.getStringExtra("item_stock");
        item_ptr = in.getStringExtra("item_ptr");
        item_mrp = in.getStringExtra("item_mrp");
        item_price = in.getStringExtra("item_price");
        item_scheme = in.getStringExtra("item_scheme");
        item_margin = in.getStringExtra("item_margin");
        item_featured = in.getStringExtra("item_featured");
        item_description1 = in.getStringExtra("item_description1");

        /*String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id = android_id;*/

        modalnumber = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

        add_to_cart = findViewById(R.id.add_to_cart);
        add_to_cart_dis = findViewById(R.id.add_to_cart_dis);
        item_quantity_txt = findViewById(R.id.item_quantity_txt);
        footer_div = findViewById(R.id.footer_div);
        header_result_found = findViewById(R.id.header_result_found);

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new json_medicine_add_to_cart_api().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*Intent in = new Intent();
        in.setClass(Medicine_add.this,Search_medicine.class);
        in.putExtra("acm_name",acm_name);
        in.putExtra("chemist_id",chemist_id);
        startActivity(in);*/
        finish();
    }

    private class json_medicine_details_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            header_result_found.setText("Loading....");
            footer_div.setVisibility(View.GONE);
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

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));

                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

                nameValuePairs.add(new BasicNameValuePair("item_code", item_code));

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

            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jArray0 = new JSONArray(result);
                JSONObject jsonObject0 = jArray0.getJSONObject(0);
                String items = jsonObject0.getString("items");

                JSONArray jArray1 = new JSONArray(items);
                for (int i = 0; i < jArray1.length(); i++) {
                    JSONObject jsonObject = jArray1.getJSONObject(i);

                    item_date_time = jsonObject.getString("item_date_time");

                    header_result_found.setText("as on " +item_date_time);

                    item_code = jsonObject.getString("item_code");
                    item_image = jsonObject.getString("item_image");
                    item_image2 = jsonObject.getString("item_image2");
                    item_image3 = jsonObject.getString("item_image3");
                    item_image4 = jsonObject.getString("item_image4");
                    item_name = jsonObject.getString("item_name");
                    item_packing = jsonObject.getString("item_packing");
                    item_expiry = jsonObject.getString("item_expiry");
                    item_batch_no = jsonObject.getString("item_batch_no");
                    item_company = jsonObject.getString("item_company");
                    item_stock = jsonObject.getString("item_stock");
                    item_quantity = jsonObject.getString("item_quantity");
                    item_ptr = jsonObject.getString("item_ptr");
                    item_mrp = jsonObject.getString("item_mrp");
                    item_price = jsonObject.getString("item_price");
                    item_scheme = jsonObject.getString("item_scheme");
                    item_margin = jsonObject.getString("item_margin");
                    item_gst = jsonObject.getString("item_gst");
                    item_featured = jsonObject.getString("item_featured");
                    item_discount = jsonObject.getString("item_discount");
                    item_description1 = jsonObject.getString("item_description1");
                    item_description2 = jsonObject.getString("item_description2");

                    item_order_quantity = jsonObject.getString("item_order_quantity");

                    //items1 = jsonObject.getString("items1");

                    footer_div.setVisibility(View.VISIBLE);
                    load_page_data();
                }
            } catch (Exception e) {
                //clear_database();
                Toast.makeText(getApplicationContext(), "Error--json_medicine_details_api" + e.toString(), Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(getApplicationContext(), item_item_id, Toast.LENGTH_LONG).show();
        }
    }

    private void load_page_data() {
        try {
            final ImageView item_image_img = findViewById(R.id.item_image);
            ImageView item_featured_img = findViewById(R.id.item_featured);
            ImageView item_out_of_stock_img = findViewById(R.id.item_out_of_stock);

            ImageView item_image1_img = findViewById(R.id.item_image1);
            ImageView item_image2_img = findViewById(R.id.item_image2);
            ImageView item_image3_img = findViewById(R.id.item_image3);
            ImageView item_image4_img = findViewById(R.id.item_image4);

            Picasso.with(Medicine_details.this).load(item_image).into(item_image_img);
            Picasso.with(Medicine_details.this).load(item_image).into(item_image1_img);
            if(!item_image2.equals("")) {
                Picasso.with(Medicine_details.this).load(item_image2).into(item_image2_img);
                Picasso.with(Medicine_details.this).load(item_image3).into(item_image3_img);
                Picasso.with(Medicine_details.this).load(item_image4).into(item_image4_img);
            }

            final String finalImage1 = item_image;
            item_image1_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Picasso.with(Medicine_details.this).load(finalImage1).into(item_image_img);
                }
            });

            final String finalImage2 = item_image2;
            item_image2_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Picasso.with(Medicine_details.this).load(finalImage2).into(item_image_img);
                }
            });

            final String finalImage3 = item_image3;
            item_image3_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Picasso.with(Medicine_details.this).load(finalImage3).into(item_image_img);
                }
            });

            final String finalImage4 = item_image4;
            item_image4_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Picasso.with(Medicine_details.this).load(finalImage4).into(item_image_img);
                }
            });

            if (item_featured.equals("1")) {
                item_featured_img.setVisibility(View.VISIBLE);
            } else {
                item_featured_img.setVisibility(View.GONE);
            }

            TextView item_name_tv = (TextView) findViewById(R.id.item_name);
            TextView item_packing_tv = (TextView) findViewById(R.id.item_packing);
            TextView item_batch_no_tv = (TextView) findViewById(R.id.item_batch_no);
            TextView item_margin_tv = (TextView) findViewById(R.id.item_margin);
            TextView item_expiry_tv = (TextView) findViewById(R.id.item_expiry);
            TextView item_company_tv = (TextView) findViewById(R.id.item_company);
            TextView item_stock_tv = (TextView) findViewById(R.id.item_stock);
            TextView item_scheme_tv = (TextView) findViewById(R.id.item_scheme);
            TextView item_scheme_line = (TextView) findViewById(R.id.item_scheme_line);
            TextView item_ptr_tv = (TextView) findViewById(R.id.item_ptr);
            TextView item_mrp_tv = (TextView) findViewById(R.id.item_mrp);
            TextView item_gst_tv = (TextView) findViewById(R.id.item_gst);
            TextView item_price_tv = (TextView) findViewById(R.id.item_price);

            item_name_tv.setText(item_name);
            item_packing_tv.setText("Packing : " + item_packing);
            item_batch_no_tv.setText("Batch no : " + item_batch_no);
            item_margin_tv.setText("Margin : " + item_margin + "%");
            item_expiry_tv.setText("Expiry : " + item_expiry);
            item_company_tv.setText("By : " + item_company);
            item_stock_tv.setText("Stock : " + item_quantity);
            item_scheme_tv.setText("Scheme : " + item_scheme);
            item_ptr_tv.setText("PTR : ₹ " + item_ptr + "/-");
            item_mrp_tv.setText("MRP : ₹ " + item_mrp + "/-");
            item_gst_tv.setText("GST : " + item_gst + "%");
            item_price_tv.setText("*Approximate Value ~ : ₹ " + item_price + "/-");

            LinearLayout item_scheme_line_div = findViewById(R.id.item_scheme_line_div);
            item_scheme_line_div.setVisibility(View.VISIBLE);
            item_scheme_tv.setVisibility(View.VISIBLE);
            item_scheme_line.setVisibility(View.VISIBLE);
            if (item_scheme.equals("0+0")) {
                item_scheme_line_div.setVisibility(View.GONE);
                item_scheme_tv.setVisibility(View.GONE);
                item_scheme_line.setVisibility(View.GONE);
            }

            item_stock_tv.setTextColor(getResources().getColor(R.color.item_stock_color));
            if (item_stock.equals("Available")) {
                item_stock_tv.setText("Stock : Available");
            }

            item_out_of_stock_img.setVisibility(View.GONE);
            if (item_quantity.equals("0")) {
                item_stock_tv.setText("Out of stock");
                item_stock_tv.setTextColor(getResources().getColor(R.color.item_out_of_stock_color));
                item_out_of_stock_img.setVisibility(View.VISIBLE);
                footer_div.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Out of stock", Toast.LENGTH_LONG).show();
            }

            if(!item_order_quantity.equals("")) {
                item_quantity_txt.setText(item_order_quantity);
                add_to_cart.setText("Update cart");
                item_quantity_txt.setSelection(item_quantity_txt.getText().length());
            }

            LinearLayout item_description1_div = findViewById(R.id.item_description1_div);
            TextView item_description1_tv = findViewById(R.id.item_description1);
            item_description1_div.setVisibility(View.GONE);
            item_description1_tv.setVisibility(View.GONE);
            if(!item_description1.equals("")){
                item_description1_tv.setText(item_description1);
                item_description1_div.setVisibility(View.VISIBLE);
                item_description1_tv.setVisibility(View.VISIBLE);
            }

            LinearLayout item_description2_div = findViewById(R.id.item_description2_div);
            TextView item_description2_tv = findViewById(R.id.item_description2);
            item_description2_div.setVisibility(View.GONE);
            item_description2_tv.setVisibility(View.GONE);
            if(!item_description2.equals("")){
                item_description2_tv.setText(item_description2);
                item_description2_div.setVisibility(View.VISIBLE);
                item_description2_tv.setVisibility(View.VISIBLE);
            }

            ScrollView scrollView = findViewById(R.id.scrollView1);
            scrollView.setVisibility(View.VISIBLE);

            LinearLayout loading_div = findViewById(R.id.loading_div);
            loading_div.setVisibility(View.GONE);

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error--load_page_data" + e.toString(), Toast.LENGTH_LONG).show();
            ScrollView scrollView = findViewById(R.id.scrollView1);
            scrollView.setVisibility(View.GONE);

            LinearLayout loading_div = findViewById(R.id.loading_div);
            loading_div.setVisibility(View.VISIBLE);
        }
    }

    public String remove_last_from_string(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void update_cart_json_list()
    {
        try {
            String items_total = sharedpreferences.getString(USER_CART, null);
            int a = Integer.parseInt(items_total) + 1;
            items_total = String.valueOf(a);

            float b = Float.parseFloat(item_price) * Float.parseFloat(item_order_quantity);
            String items_price = sharedpreferences.getString(USER_CART_TOTAL, null);
            float c = Float.parseFloat(items_price) + b;
            items_price = String.format("%.2f",c);

            String item_id = "";
            String item_quantity_price = String.valueOf(b);

            String user_cart_json = sharedpreferences.getString(USER_CART_JSON, null);
            if (!user_cart_json.equals("")) {
                user_cart_json = user_cart_json.substring(0, user_cart_json.length() - 1);
                user_cart_json = user_cart_json + ",";
            } else {
                user_cart_json = "[";
            }

            String user_cart_json0 = user_cart_json + "{\n" +
                    "\"item_id\": \"" + item_id + "\",\n" +
                    "\"item_code\": \"" + item_code + "\",\n" +
                    "\"item_quantity\": \"" + item_order_quantity + "\",\n" +
                    "\"item_image\": \""+item_image+"\",\n" +
                    "\"item_name\": \"" + item_name + "\",\n" +
                    "\"item_packing\": \"" + item_packing + "\",\n" +
                    "\"item_expiry\": \"" + item_expiry + "\",\n" +
                    "\"item_company\": \"" + item_company + "\",\n" +
                    "\"item_scheme\": \"" + item_scheme + "\",\n" +
                    "\"item_margin\": \"" + item_margin + "\",\n" +
                    "\"item_featured\": \"" + item_featured + "\",\n" +
                    "\"item_price\": \"" + item_price + "\",\n" +
                    "\"item_quantity_price\": \"" + item_quantity_price + "\",\n" +
                    "\"item_datetime\": \"Just now\",\n" +
                    "\"item_modalnumber\": \"" + modalnumber + "\"\n" +
                    "}]";

            //Toast.makeText(getApplicationContext(), user_cart_json0, Toast.LENGTH_LONG).show();

            /************************************************************/
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(USER_CART, items_total);
            editor.putString(USER_CART_TOTAL, items_price);
            editor.putString(USER_CART_JSON, user_cart_json0);
            editor.apply();
            /************************************************************/
        }catch (Exception e){

        }
    }

    int get_cart_list = 0;
    private class json_medicine_add_to_cart_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getApplicationContext(), item_item_id, Toast.LENGTH_LONG).show();

            footer_div.setVisibility(View.GONE);

            if(item_order_quantity.equals("")) {
                item_order_quantity = item_quantity_txt.getText().toString();
                add_to_cart.setVisibility(View.GONE);
                add_to_cart_dis.setVisibility(View.VISIBLE);
                add_to_cart_dis.setText("Loading....");
                update_cart_json_list();
                get_cart_list = 0;
                finish();
            }
            else{
                get_cart_list = 1;
                item_order_quantity = item_quantity_txt.getText().toString();

                ScrollView scrollView = findViewById(R.id.scrollView1);
                scrollView.setVisibility(View.GONE);

                LinearLayout loading_div = findViewById(R.id.loading_div);
                loading_div.setVisibility(View.VISIBLE);
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
                nameValuePairs.add(new BasicNameValuePair("mobilenumber", mobilenumber));
                nameValuePairs.add(new BasicNameValuePair("modalnumber", modalnumber));

                nameValuePairs.add(new BasicNameValuePair("user_type", user_type));
                nameValuePairs.add(new BasicNameValuePair("user_altercode", user_altercode));
                nameValuePairs.add(new BasicNameValuePair("user_password", user_password));

                nameValuePairs.add(new BasicNameValuePair("chemist_id", chemist_id));

                nameValuePairs.add(new BasicNameValuePair("item_order_quantity", item_order_quantity));
                nameValuePairs.add(new BasicNameValuePair("item_code", item_code));
                nameValuePairs.add(new BasicNameValuePair("order_type", "Android"));

                nameValuePairs.add(new BasicNameValuePair("get_cart_list", String.valueOf(get_cart_list)));
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
            //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jArray0 = new JSONArray(result);
                JSONObject jsonObject0 = jArray0.getJSONObject(0);
                String items = jsonObject0.getString("items");
                String user_cart_json0 = jsonObject0.getString("user_cart_json0");
                String user_cart_json1 = jsonObject0.getString("user_cart_json1");

                JSONArray jArray = new JSONArray(items);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);

                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        if (get_cart_list == 1) {
                            Toast.makeText(getApplicationContext(), "Medicine updated successfully", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "Medicine added successfully", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Medicine added error", Toast.LENGTH_LONG).show();
                    }

                    add_to_cart.setVisibility(View.VISIBLE);
                    add_to_cart_dis.setVisibility(View.GONE);
                    add_to_cart_dis.setText("Add to cart");
                }

                if (get_cart_list == 1) {
                    JSONArray jArray1 = new JSONArray(user_cart_json1);
                    for (int ii = 0; ii < jArray1.length(); ii++) {
                        JSONObject jsonObject1 = jArray1.getJSONObject(ii);

                        String items_total = jsonObject1.getString("items_total");
                        String items_price = jsonObject1.getString("items_price");

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
                        finish();
                    }
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "error -- json_medicine_add_to_cart_api" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void get_user_cart() {
        try {
            String user_cart = sharedpreferences.getString(USER_CART, null);

            TextView action_bar_cart_total = findViewById(R.id.action_bar_cart_total);
            action_bar_cart_total.setText(" " + user_cart + " ");

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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        get_user_cart();
        load_page_data();
        new json_medicine_details_api().execute();
    }
}
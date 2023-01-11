package com.drdistributor.dr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
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
import java.util.Locale;

public class Medicine_search_mic extends AppCompatActivity {

    public static final Integer RecordAudioRequestCode = 1036;
    private SpeechRecognizer speechRecognizer;
    private ImageView mic_btn;
    TextToSpeech t1;

    String user_type = "", user_altercode = "", user_password = "", chemist_name = "", chemist_id = "", device_id = "";
    ;
    String main_url = "", page_url1 = "", page_url2 = "";
    UserSessionManager session;
    String result = "";
    EditText edt;

    ListView listView1;
    Medicine_search_Adapter adapter;
    List<Medicine_search_get_or_set> Search_medicine_List = new ArrayList<Medicine_search_get_or_set>();

    ListView listView2;
    My_cart_Adapter Medicine_cart_adapter;
    List<My_cart_get_or_set> My_cart_List = new ArrayList<My_cart_get_or_set>();

    ImageView cart_empty_img;
    ImageView cancel_btn;

    TextView header_result_found;
    LinearLayout loading_div;
    ImageView no_record_found_img;

    LinearLayout footer_div;

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
        setContentView(R.layout.medicine_search_mic);

        edt = findViewById(R.id.editText1);
        mic_btn = findViewById(R.id.mic_btn);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("hin", "IN"));
                }
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
                mic_btn.setImageResource(R.drawable.mic_o);
                edt.setText("");
                edt.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                mic_btn.setImageResource(R.drawable.mic_b);
                edt.setHint("Search medicines / company");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                mic_btn.setImageResource(R.drawable.mic_b);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                edt.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        mic_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    mic_btn.setImageResource(R.drawable.mic_o);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        //getSupportActionBar().hide();
        getSupportActionBar().setElevation(0);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        user_password = user.get(UserSessionManager.KEY_USERALTERCODE);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        /*******************************************/

        TextView select_chemist = findViewById(R.id.select_chemist);
        ImageView edit_chemist = findViewById(R.id.edit_chemist);
        if (user_type.equals("sales")) {
            chemist_id = sharedpreferences.getString(USER_CHEMIST_ID, null);
            chemist_name = sharedpreferences.getString(USER_CHEMIST_NAME, null);

            TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title1);
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

                Intent myIntent = new Intent(Medicine_search_mic.this,
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

                Intent myIntent = new Intent(Medicine_search_mic.this,
                        Chemist_search.class);
                startActivity(myIntent);
                finish();
            }
        });

        MainActivity ma = new MainActivity();
        main_url = ma.main_url;
        page_url1 = main_url + "medicine_search_api/post";
        page_url2 = main_url + "delete_all_medicine_api/post";

        /*String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_id = android_id;*/

        TextView action_bar_title = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title.setText("Search medicines");

        ImageButton action_bar_back = (ImageButton) findViewById(R.id.action_bar_back);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(Medicine_search_mic.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

        cart_empty_img = findViewById(R.id.cart_empty_img);

        header_result_found = findViewById(R.id.header_result_found);
        loading_div = findViewById(R.id.loading_div);
        no_record_found_img = findViewById(R.id.no_record_found_img);
        footer_div = findViewById(R.id.footer_div);

        listView1 = findViewById(R.id.listView1);
        adapter = new Medicine_search_Adapter(Medicine_search_mic.this, Search_medicine_List);
        listView1.setAdapter(adapter);

        listView2 = findViewById(R.id.listView2);
        Medicine_cart_adapter = new My_cart_Adapter(Medicine_search_mic.this, My_cart_List);
        listView2.setAdapter(Medicine_cart_adapter);

        cancel_btn = findViewById(R.id.cancel_btn);

        edt = findViewById(R.id.editText1);
        //showSoftKeyboard(edt);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String msg = edt.getText().toString();
                if (msg.length() > 0) {
                    if (msg.length() > 2) {
                        try {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new JSON_search_medicine().execute();
                                }
                            }, 1000);
                        } catch (Exception e) {
                            new JSON_search_medicine().execute();
                        }
                    }
                }
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
                if (msg.length() > 0) {
                    if (msg.length() > 2) {
                        listView1.setVisibility(View.VISIBLE);
                        listView2.setVisibility(View.GONE);

                        footer_div.setVisibility(View.GONE);
                        cancel_btn.setVisibility(View.VISIBLE);

                        Search_medicine_List.clear();
                        adapter.notifyDataSetChanged();

                        cart_empty_img.setVisibility(View.GONE);
                    }
                } else {
                    listView1.setVisibility(View.GONE);
                    listView2.setVisibility(View.VISIBLE);

                    footer_div.setVisibility(View.VISIBLE);
                    cancel_btn.setVisibility(View.GONE);

                    get_user_cart();
                    get_user_cart_list();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                edt.setText("");

                get_user_cart();
                get_user_cart_list();
            }
        });


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                Medicine_search_get_or_set clickedCategory = Search_medicine_List.get(arg2);
                String item_code = clickedCategory.item_code();
                String item_image = clickedCategory.item_image();
                String item_name = clickedCategory.item_name();
                String item_packing = clickedCategory.item_packing();
                String item_expiry = clickedCategory.item_expiry();
                String item_company = clickedCategory.item_company();
                String item_quantity = clickedCategory.item_quantity();
                String item_stock = clickedCategory.item_stock();
                String item_ptr = clickedCategory.item_ptr();
                String item_mrp = clickedCategory.item_mrp();
                String item_price = clickedCategory.item_price();
                String item_scheme = clickedCategory.item_scheme();
                String item_margin = clickedCategory.item_margin();
                String item_featured = clickedCategory.item_featured();
                String item_description1 = clickedCategory.item_description1();

                Intent in = new Intent();
                in.setClass(Medicine_search_mic.this, Medicine_details.class);
                in.putExtra("item_code", item_code);
                in.putExtra("item_image", item_image);
                in.putExtra("item_name", item_name);
                in.putExtra("item_packing", item_packing);
                in.putExtra("item_expiry", item_expiry);
                in.putExtra("item_company", item_company);
                in.putExtra("item_quantity", item_quantity);
                in.putExtra("item_stock", item_stock);
                in.putExtra("item_ptr", item_ptr);
                in.putExtra("item_mrp", item_mrp);
                in.putExtra("item_price", item_price);
                in.putExtra("item_scheme", item_scheme);
                in.putExtra("item_margin", item_margin);
                in.putExtra("item_featured", item_featured);
                in.putExtra("item_description1", item_description1);

                startActivity(in);

                //finish();
            }
        });

        listView1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                /* Toast.makeText(getApplicationContext(), "Position",Toast.LENGTH_LONG).show(); */
                return false;
            }
        });

        Button search_medicine_order_btn = (Button) findViewById(R.id.search_medicine_order_btn);
        search_medicine_order_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent in = new Intent();
                in.setClass(Medicine_search_mic.this, My_cart.class);
                startActivity(in);
                finish();
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

    private class JSON_search_medicine extends AsyncTask<Void, Void, Void> {
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
            if (keyword.length() > 2) {
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
        protected void onPostExecute(Void args) {
            header_result_found.setText("Found result");
            loading_div.setVisibility(View.GONE);
            no_record_found_img.setVisibility(View.GONE);

            Search_medicine_List.clear();
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (!result.equals("")) {

                    JSONArray jArray0 = new JSONArray(result);
                    JSONObject jsonObject0 = jArray0.getJSONObject(0);
                    String items = jsonObject0.getString("items");

                    JSONArray jArray1 = new JSONArray(items);
                    if(jArray1.length()==0) {
                        String toSpeak = "kuch nahi mila";
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }else{
                        if(jArray1.length()==1)
                        {
                            String toSpeak = "medicine mil ghai";
                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else
                        {
                            String toSpeak = "ine "+jArray1.length()+" medicine me say sahi medicine ko select kero";
                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }

                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject jsonObject = jArray1.getJSONObject(i);
                        String item_code = jsonObject.getString("item_code");
                        String item_image = jsonObject.getString("item_image");
                        String item_name = jsonObject.getString("item_name");
                        String item_packing = jsonObject.getString("item_packing");
                        String item_expiry = jsonObject.getString("item_expiry");
                        String item_company = jsonObject.getString("item_company");
                        String item_quantity = jsonObject.getString("item_quantity");
                        String item_stock = jsonObject.getString("item_stock");
                        String item_ptr = jsonObject.getString("item_ptr");
                        String item_mrp = jsonObject.getString("item_mrp");
                        String item_price = jsonObject.getString("item_price");
                        String item_scheme = jsonObject.getString("item_scheme");
                        String item_margin = jsonObject.getString("item_margin");
                        String item_featured = jsonObject.getString("item_featured");
                        String item_description1 = jsonObject.getString("item_description1");
                        String similar_items = jsonObject.getString("similar_items");

                        Medicine_search_get_or_set Search_medicine_set = new Medicine_search_get_or_set();
                        Search_medicine_set.item_code(item_code);
                        Search_medicine_set.item_image(item_image);
                        Search_medicine_set.item_name(item_name);
                        Search_medicine_set.item_packing(item_packing);
                        Search_medicine_set.item_expiry(item_expiry);
                        Search_medicine_set.item_company(item_company);
                        Search_medicine_set.item_quantity(item_quantity);
                        Search_medicine_set.item_stock(item_stock);
                        Search_medicine_set.item_ptr(item_ptr);
                        Search_medicine_set.item_mrp(item_mrp);
                        Search_medicine_set.item_price(item_price);
                        Search_medicine_set.item_scheme(item_scheme);
                        Search_medicine_set.item_margin(item_margin);
                        Search_medicine_set.item_featured(item_featured);

                        Search_medicine_set.item_description1(item_description1);
                        Search_medicine_set.similar_items(similar_items);
                        Search_medicine_List.add(Search_medicine_set);

                        if(jArray1.length()==1) {
                            Intent in = new Intent();
                            in.setClass(Medicine_search_mic.this, Medicine_details.class);
                            in.putExtra("item_code", item_code);

                            startActivity(in);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if(Search_medicine_List.isEmpty()) {
                        no_record_found_img.setVisibility(View.VISIBLE);
                        header_result_found.setText("No record found");
                    }
                }
                else{
                    no_record_found_img.setVisibility(View.VISIBLE);
                    header_result_found.setText("No record found");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "error - JSON_search_medicine - " + e.toString(), Toast.LENGTH_LONG).show();

                no_record_found_img.setVisibility(View.VISIBLE);
                header_result_found.setText("No record found");
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void alertMessage_deleteall() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            EditText enter_remarks = (EditText) findViewById(R.id.enter_remarks);

            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        try {
                            new json_delete_all_medicine_api().execute();
                        } catch (Exception e) {
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
            if (items.equals("")) {
                cart_empty_img.setVisibility(View.VISIBLE);
            } else {
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
            Medicine_cart_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error get_user_cart_list" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        edt.setText("");

        get_user_cart();
        get_user_cart_list();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
package com.drdistributor.dr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class User_image_uploading extends AppCompatActivity {

    Bitmap bitmap;
    boolean check = true;
    Button UploadImageServer,UploadImageServer1;
    LinearLayout SelectImageGallery;
    ImageView imageView;
    ProgressDialog progressDialog ;
    String GetImageNameEditText;

    String ImageName = "image_name" ;

    String ImagePath = "image_path" ;

    String ServerUploadPath ="" ;

    UserSessionManager session;
    Database db;
    SQLiteDatabase sql;

    String session_id="",user_password="",user_type="",user_fname="",user_code="",user_altercode="",user_image="",user_phone="",user_email="",user_address="",firebase_token="";

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
        setContentView(R.layout.user_image_uploading);

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

        TextView action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("Update image");

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
                in.setClass(User_image_uploading.this, Medicine_search.class);
                startActivity(in);
                finish();
            }
        });

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(User_image_uploading.this, My_cart.class);
                startActivity(in);
                finish();
            }
        });

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

        MainActivity ma = new MainActivity();
        String mainurl = ma.main_url;
        ServerUploadPath = mainurl+"user_image_upload/post";

        imageView = (ImageView)findViewById(R.id.imageView);
        SelectImageGallery = findViewById(R.id.SelectImageGallery);
        UploadImageServer = (Button)findViewById(R.id.buttonUpload);
        UploadImageServer1 = (Button)findViewById(R.id.buttonUpload1);

        UploadImageServer.setVisibility(View.GONE);
        UploadImageServer1.setVisibility(View.VISIBLE);

        TextView u_name = findViewById(R.id.u_name);
        u_name.setText(user_fname);
        TextView u_code = findViewById(R.id.u_code);
        u_code.setText("Code : "+user_altercode);

        try {
            ImageView u_img = findViewById(R.id.u_img);
            Picasso.with(this).load(user_image).into(u_img);
            Picasso.with(this).load(user_image).into(imageView);
        }catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }

        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image from gallery"), 1);
            }
        });

        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUploadToServerFunction();
            }
        });

        header_result_found = findViewById(R.id.header_result_found);
        header_result_found.setText("Found result");
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {
        super.onActivityResult(RC, RQC, I);
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
            Uri uri = I.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                UploadImageServer.setVisibility(View.VISIBLE);
                UploadImageServer1.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ImageUploadToServerFunction(){
        ByteArrayOutputStream byteArrayOutputStreamObject;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                header_result_found.setText("Loading....");

                progressDialog = ProgressDialog.show(User_image_uploading.this,"Uploading","Please Wait",false,false);

                UploadImageServer.setVisibility(View.GONE);
                UploadImageServer1.setVisibility(View.VISIBLE);
            }
            @Override
            protected void onPostExecute(String user_image_server) {
                super.onPostExecute(user_image_server);
                // Dismiss the progress dialog after done uploading.
                header_result_found.setText("Found result");

                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.
                Toast.makeText(User_image_uploading.this, "Uploaded successfully", Toast.LENGTH_LONG).show();
                //session.clear_data();
                session.createUserLoginSession(session_id, user_password, user_type, user_fname, user_code, user_altercode, user_image_server, user_phone, user_email, user_address, firebase_token);

                try {
                    ImageView u_img = findViewById(R.id.u_img);
                    Picasso.with(User_image_uploading.this).load(user_image_server).into(u_img);
                } catch (Exception e) {
                    // TODO: handle exception
                    //mProgressDialog.dismiss();
                }

                //onBackPressed();
                // Setting image as transparent after done uploading.
                //imageView.setImageResource(android.R.color.transparent);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImagePath, ConvertImage);
                HashMapParams.put("user_type", user_type);
                HashMapParams.put("user_altercode", user_altercode);
                HashMapParams.put("user_code", user_code);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{
        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
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
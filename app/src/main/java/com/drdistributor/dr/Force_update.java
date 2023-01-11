package com.drdistributor.dr;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Force_update extends AppCompatActivity {

    Database db;
    SQLiteDatabase sql;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.force_update);

        getSupportActionBar().hide();

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

        db = new Database(Force_update.this);
        sql = db.getWritableDatabase();

        session = new UserSessionManager(getApplicationContext());

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("D R Distributors Pvt Ltd");

        ImageButton action_bar_back = (ImageButton) findViewById(R.id.action_bar_back);
        action_bar_back.setVisibility(View.GONE);

        LinearLayout cart_LinearLayout = (LinearLayout) findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);

        Button new_version_available = findViewById(R.id.new_version_available);
        new_version_available.setVisibility(View.VISIBLE);
        new_version_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_database();
                logout_function();
                Intent it = new Intent(android.content.Intent.ACTION_VIEW);
                it.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.drdistributor.dr"));
                startActivity(it);
                finish();
            }
        });

        Intent in = getIntent();
        String force_update_title = in.getStringExtra("force_update_title");
        String force_update_message = in.getStringExtra("force_update_message");

        try {
            byte[] data = Base64.decode(force_update_title, Base64.DEFAULT);
            force_update_title = new String(data, "UTF-8");

            if(!force_update_title.equals("")) {
                TextView force_update_title_tv = findViewById(R.id.force_update_title_tv);
                force_update_title_tv.setText(Html.fromHtml(force_update_title));
            }
        }catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }

        try {
            byte[] data = Base64.decode(force_update_message, Base64.DEFAULT);
            force_update_message = new String(data, "UTF-8");

            if(!force_update_message.equals("")) {
                TextView force_update_message_tv = findViewById(R.id.force_update_message_tv);
                force_update_message_tv.setText(Html.fromHtml(force_update_message));
            }
        }catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }
    }

    private void logout_function() {
        try {
            sql.execSQL("DROP TABLE tbl_medicine");
            sql.execSQL("DROP TABLE tbl_medicine_last_id");
            sql.execSQL("DROP TABLE tbl_place_order");
            sql.execSQL("DROP TABLE tbl_acm");
            sql.execSQL("DROP TABLE tbl_user_status");
            sql.execSQL("DROP TABLE tbl_order_done");
        } catch (Exception ee) {
            // TODO: handle exception
            //Toast.makeText(MainActivity.this, "error 1", Toast.LENGTH_LONG).show();
        }

        try {
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine");
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine_last_id");
            sql.execSQL("DROP TABLE IF EXISTS tbl_place_order");
            sql.execSQL("DROP TABLE IF EXISTS tbl_acm");
            sql.execSQL("DROP TABLE IF EXISTS tbl_user_status");
            sql.execSQL("DROP TABLE IF EXISTS tbl_order_done");
        } catch (Exception ee) {
            // TODO: handle exception
            //Toast.makeText(MainActivity.this, "error 2", Toast.LENGTH_LONG).show();
        }
        session.logoutUser();
    }

    private void clear_database() {
        try {
            sql.execSQL("DROP TABLE tbl_medicine");
            sql.execSQL("DROP TABLE tbl_medicine_last_id");
            sql.execSQL("DROP TABLE tbl_place_order");
            sql.execSQL("DROP TABLE tbl_acm");
            sql.execSQL("DROP TABLE tbl_user_status");
            sql.execSQL("DROP TABLE tbl_order_done");
        } catch (Exception ee) {
            // TODO: handle exception
            //Toast.makeText(MainActivity.this, "error 1", Toast.LENGTH_LONG).show();
        }

        try {
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine");
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine_last_id");
            sql.execSQL("DROP TABLE IF EXISTS tbl_place_order");
            sql.execSQL("DROP TABLE IF EXISTS tbl_acm");
            sql.execSQL("DROP TABLE IF EXISTS tbl_user_status");
            sql.execSQL("DROP TABLE IF EXISTS tbl_order_done");
        } catch (Exception ee) {
            // TODO: handle exception
            //Toast.makeText(MainActivity.this, "error 2", Toast.LENGTH_LONG).show();
        }
    }
}

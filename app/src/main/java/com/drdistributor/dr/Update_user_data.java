package com.drdistributor.dr;

import java.io.InputStream;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class Update_user_data extends AppCompatActivity {

    Database db;
    SQLiteDatabase sql;
    UserSessionManager session;

    String user_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_data);

        db = new Database(Update_user_data.this);
        sql = db.getWritableDatabase();

        try {
            sql.execSQL("DROP TABLE tbl_medicine");
            sql.execSQL("DROP TABLE tbl_medicine_last_id");
            sql.execSQL("DROP TABLE tbl_place_order");
            sql.execSQL("DROP TABLE tbl_acm");
            sql.execSQL("DROP TABLE tbl_user_status");
            sql.execSQL("DROP TABLE tbl_order_done");
        } catch (Exception e) {
            // TODO: handle exception
            //Toast.makeText(Update_user_data.this, "Empty database not working", Toast.LENGTH_LONG).show();
        }

        try {
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine");
            sql.execSQL("DROP TABLE IF EXISTS tbl_medicine_last_id");
            sql.execSQL("DROP TABLE IF EXISTS tbl_place_order");
            sql.execSQL("DROP TABLE IF EXISTS tbl_acm");
            sql.execSQL("DROP TABLE IF EXISTS tbl_user_status");
            sql.execSQL("DROP TABLE IF EXISTS tbl_order_done");
        } catch (Exception e) {
            // TODO: handle exception
            //Toast.makeText(Update_user_data.this, "Empty database not working", Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(Update_user_data.this, count_tbl_medicine(), Toast.LENGTH_LONG).show();
        try {
            db.onUpgrade(sql, 1, 2);
            //Toast.makeText(Update_user_data.this, "ok1", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO: handle exception
            //Toast.makeText(Update_user_data.this, "ok2", Toast.LENGTH_LONG).show();
        }


        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(UserSessionManager.KEY_USERTYPE);
        try {
            finish();
            if (user_type.equals("corporate")) {
                Intent in = new Intent();
                in.setClass(Update_user_data.this, MainActivity.class);
                startActivity(in);
            } else {
                Intent in = new Intent();
                in.setClass(Update_user_data.this, MainActivity.class);
                startActivity(in);
            }

        } catch (Exception e) {
            // TODO: handle exception
            //Log.e("log_tag", "myservice_01" + e.toString());
        }
    }
}

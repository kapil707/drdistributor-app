package com.drdistributor.dr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class Function_class {
    Database db;
    SQLiteDatabase sql;

    public String count_cart(String chemist_id, String _user_type, Context contextpg) {
        db = new Database(contextpg);
        sql = db.getWritableDatabase();
        String x = "0";
        try {
            if (_user_type.equals("sales")) {
                Cursor tbl_place_order = sql.rawQuery("Select * from tbl_place_order where status='0' and chemist_id='" + chemist_id + "'", null);
                x = String.valueOf(tbl_place_order.getCount());
            } else {
                Cursor tbl_place_order = sql.rawQuery("Select * from tbl_place_order where status='0'", null);
                x = String.valueOf(tbl_place_order.getCount());
            }
        } catch (Exception e) {
        }
        return x;
    }

    public String count_cart_1(String _user_type, Context contextpg) {
        db = new Database(contextpg);
        sql = db.getWritableDatabase();
        String x = "0";
        try {
            if (_user_type.equals("sales")) {
                Cursor tbl_place_order = sql.rawQuery("Select DISTINCT chemist_id from tbl_place_order where status='0'", null);
                x = String.valueOf(tbl_place_order.getCount());
            } else {
                Cursor tbl_place_order = sql.rawQuery("Select * from tbl_place_order where status='0'", null);
                x = String.valueOf(tbl_place_order.getCount());
            }
        } catch (Exception e) {
        }
        return x;
    }

    public String subtotal_cart(String chemist_id, String _user_type, Context contextpg) {
        db = new Database(contextpg);
        sql = db.getWritableDatabase();
        String x = "0";
        float subtotalcart = 0;
        try {
            Cursor tbl_place_order = null;
            if (_user_type.equals("sales")) {
                tbl_place_order = sql.rawQuery("Select tbl_medicine.final_price as sale_rate,tbl_place_order.item_qty from tbl_place_order,tbl_medicine where tbl_place_order.i_code=tbl_medicine.i_code and tbl_place_order.status='0' and tbl_place_order.chemist_id='" + chemist_id + "'", null);
            } else {
                tbl_place_order = sql.rawQuery("Select tbl_medicine.final_price as sale_rate,tbl_place_order.item_qty from tbl_place_order,tbl_medicine where tbl_place_order.i_code=tbl_medicine.i_code and tbl_place_order.status='0'", null);
            }
            if (tbl_place_order.getCount() != 0) {
                if (tbl_place_order.moveToFirst()) {
                    do {
                        String sale_rate = tbl_place_order.getString(tbl_place_order.getColumnIndex("sale_rate"));
                        String item_qty = tbl_place_order.getString(tbl_place_order.getColumnIndex("item_qty"));
                        subtotalcart = subtotalcart + (Float.parseFloat(sale_rate) * Float.parseFloat(item_qty));
                    }
                    while (tbl_place_order.moveToNext());
                }
            }
        } catch (Exception e) {
        }
        x = String.format("%.2f", subtotalcart);
        return x;
    }

    public String check_internet_status() {
        String s1 = null;
        try {
            s1 = Boolean.toString(isConnected());
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        if (s1.equals("true")) {
            s1 = "online";
        }
        if (s1.equals("false")) {
            s1 = "offline";
        }
        return s1;
    }

    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public String count_tbl_medicine(Context contextpg) {
        db = new Database(contextpg);
        sql = db.getWritableDatabase();
        String x = "0";
        try {
            Cursor tbl_place_order = sql.rawQuery("Select item_id from tbl_medicine", null);
            x = String.valueOf(tbl_place_order.getCount());
        } catch (Exception e) {
        }
        return x;
    }

    public String cart_empty(String chemist_id, String _user_type, Context contextpg) {
        db = new Database(contextpg);
        sql = db.getWritableDatabase();
        Cursor c_user_tbl = null;
        if (_user_type.equals("sales")) {
            c_user_tbl = sql.rawQuery("Select * from tbl_place_order,tbl_medicine where tbl_place_order.i_code=tbl_medicine.i_code and tbl_place_order.status='0' and tbl_place_order.chemist_id='" + chemist_id + "' order by tbl_place_order.chemist_id", null);
        } else {
            c_user_tbl = sql.rawQuery("Select * from tbl_place_order,tbl_medicine where tbl_place_order.i_code=tbl_medicine.i_code and tbl_place_order.status='0' order by tbl_place_order.chemist_id", null);
        }
        if (c_user_tbl.getCount() != 0) {
            if (c_user_tbl.moveToFirst()) {
                do {
                    String _i_code = c_user_tbl.getString(c_user_tbl.getColumnIndex("i_code"));
                    if (_user_type.equals("sales")) {
                        sql.delete("tbl_place_order", "i_code=? and chemist_id=?", new String[]{_i_code, chemist_id});
                    } else {
                        sql.delete("tbl_place_order", "i_code=? ", new String[]{_i_code});
                    }
                }
                while (c_user_tbl.moveToNext());
            }
        }
        return "ok";
    }

    public String cart_empty_all_data(Context contextpg) {
        db = new Database(contextpg);
        sql = db.getWritableDatabase();
        Cursor c_user_tbl = sql.rawQuery("Select * from tbl_place_order,tbl_medicine where tbl_place_order.i_code=tbl_medicine.i_code and tbl_place_order.status='0' order by tbl_place_order.chemist_id", null);
        if (c_user_tbl.getCount() != 0) {
            if (c_user_tbl.moveToFirst()) {
                do {
                    String _i_code = c_user_tbl.getString(c_user_tbl.getColumnIndex("i_code"));
                    sql.delete("tbl_place_order", "i_code=? ", new String[]{_i_code});
                }
                while (c_user_tbl.moveToNext());
            }
        }
        return "ok";
    }
}

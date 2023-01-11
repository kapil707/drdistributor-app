package com.drdistributor.dr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public Database(Context context) {
		super(context, "drdistributor_db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table tbl_medicine(_id Integer Primary Key autoincrement,i_code Text,item_code Text,title Text,item_name Text,company_name Text,company_full_name Text,date_time Text,image1 Text,image2 Text,image3 Text,image4 Text,batchqty Text,sale_rate Text,mrp Text,final_price Text,batch_no Text,packing Text,expiry Text,scheme Text,margin Text,featured Text,gstper Text,discount Text,misc_settings Text,hotdeals int,hotdeals_short int,present float,description1 Text,description2 Text,itemjoinid Text)");
		db.execSQL("create table tbl_medicine_last_id(_id Integer Primary Key autoincrement,item_id Integer)");
		db.execSQL("create table tbl_place_order(_id Integer Primary Key autoincrement,i_code Integer,item_qty Integer,status Text,chemist_id Text,remarks Text,offlineorder Text,ordertype Text,datetime Text,place_order_btn Text,place_order_message Text)");
		db.execSQL("create table tbl_acm(_id Integer Primary Key autoincrement,id Integer,name Text,code Text,altercode Text,image Text)");
		db.execSQL("create table tbl_user_status(_id Integer Primary Key autoincrement,status Text,user_error_message Text)");
		db.execSQL("create table tbl_order_done(_id Integer Primary Key autoincrement,status Integer,remarks Text,chemist_id Text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("create table tbl_medicine(_id Integer Primary Key autoincrement,i_code Text,item_code Text,title Text,item_name Text,company_name Text,company_full_name Text,date_time Text,image1 Text,image2 Text,image3 Text,image4 Text,batchqty Text,sale_rate Text,mrp Text,final_price Text,batch_no Text,packing Text,expiry Text,scheme Text,margin Text,featured Text,gstper Text,discount Text,misc_settings Text,hotdeals int,hotdeals_short int,present float,description1 Text,description2 Text,itemjoinid Text)");
		db.execSQL("create table tbl_medicine_last_id(_id Integer Primary Key autoincrement,item_id Integer)");
		db.execSQL("create table tbl_place_order(_id Integer Primary Key autoincrement,i_code Integer,item_qty Integer,status Text,chemist_id Text,remarks Text,offlineorder Text,ordertype Text,datetime Text,place_order_btn Text,place_order_message Text)");
		db.execSQL("create table tbl_acm(_id Integer Primary Key autoincrement,id Integer,name Text,code Text,altercode Text,image Text)");
		db.execSQL("create table tbl_user_status(_id Integer Primary Key autoincrement,status Text,user_error_message Text)");
		db.execSQL("create table tbl_order_done(_id Integer Primary Key autoincrement,status Integer,remarks Text,chemist_id Text)");
	}
}

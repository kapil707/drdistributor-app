package com.drdistributor.dr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "Drdistributor";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_USERID = "userid";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_USERFNAME = "KEY_USERFNAME";
    public static final String KEY_USERCODE = "KEY_USERCODE";
    public static final String KEY_USERALTERCODE = "KEY_USERALTERCODE";
    public static final String KEY_USERIMAGE = "KEY_USERIMAGE";
    public static final String KEY_user_phone = "KEY_user_phone";
    public static final String KEY_user_email = "KEY_user_email";
    public static final String KEY_user_address = "KEY_user_address";
    public static final String KEY_firebase_token = "KEY_firebase_token";


    public static final String KEY_TIMEZONE = "timezone";

    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String userid, String password,String user_type,String user_fname,String user_code,String user_altercode,String user_image,String user_phone,String user_email,String user_address,String firebase_token){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USERTYPE, user_type);
        editor.putString(KEY_USERFNAME, user_fname);
        editor.putString(KEY_USERCODE, user_code);
        editor.putString(KEY_USERALTERCODE, user_altercode);
        editor.putString(KEY_USERIMAGE, user_image);
        editor.putString(KEY_user_phone, user_phone);
        editor.putString(KEY_user_email, user_email);
        editor.putString(KEY_user_address, user_address);
        editor.putString(KEY_firebase_token, firebase_token);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login_before.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    public void clear_data(){
        // Check login status
        editor.clear();
        editor.commit();
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));
        user.put(KEY_USERFNAME, pref.getString(KEY_USERFNAME, null));
        user.put(KEY_USERCODE, pref.getString(KEY_USERCODE, null));
        user.put(KEY_USERALTERCODE, pref.getString(KEY_USERALTERCODE, null));
        user.put(KEY_USERIMAGE, pref.getString(KEY_USERIMAGE, null));
        user.put(KEY_user_phone, pref.getString(KEY_user_phone, null));
        user.put(KEY_user_email, pref.getString(KEY_user_email, null));
        user.put(KEY_user_address, pref.getString(KEY_user_address, null));
        user.put(KEY_firebase_token, pref.getString(KEY_firebase_token, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login_before.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}

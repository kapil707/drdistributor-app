package com.drdistributor.dr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Login_before extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_before);

        Intent ini = new Intent();
        ini.setClass(Login_before.this, Login.class);
        startActivity(ini);
        this.finish();
    }
}

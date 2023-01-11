package com.drdistributor.dr;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Under_construction extends AppCompatActivity {
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.under_construction);

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

        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        Intent in = getIntent();
        String under_construction_message = in.getStringExtra("under_construction_message");

        try {
            byte[] data = Base64.decode(under_construction_message, Base64.DEFAULT);
            under_construction_message = new String(data, "UTF-8");

            if(!under_construction_message.equals("")) {
                TextView under_construction_message_tv = findViewById(R.id.under_construction_message_tv);
                under_construction_message_tv.setText(Html.fromHtml(under_construction_message));
            }
        }catch (Exception e) {
            // TODO: handle exception
            //mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
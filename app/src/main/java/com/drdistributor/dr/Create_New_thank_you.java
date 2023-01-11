package com.drdistributor.dr;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Create_New_thank_you extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_thank_you);
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

        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText("D R Distributor");

        ImageView mysearchbtn = findViewById(R.id.newmysearchbtn);
        mysearchbtn.setVisibility(View.GONE);

        LinearLayout cart_LinearLayout= findViewById(R.id.cart_LinearLayout);
        cart_LinearLayout.setVisibility(View.GONE);

        ImageButton imageButton = (ImageButton) findViewById(R.id.action_bar_back);
        imageButton.setVisibility(View.GONE);

        Button go_home_btn = (Button) findViewById(R.id.go_home_btn);
        go_home_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        Intent in = getIntent();
        String create_new_message = in.getStringExtra("create_new_message");

        try {
            byte[] data = Base64.decode(create_new_message, Base64.DEFAULT);
            create_new_message = new String(data, "UTF-8");

            if(!create_new_message.equals("")) {
                TextView create_new_message_tv = findViewById(R.id.create_new_message_tv);
                create_new_message_tv.setText(Html.fromHtml(create_new_message));
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
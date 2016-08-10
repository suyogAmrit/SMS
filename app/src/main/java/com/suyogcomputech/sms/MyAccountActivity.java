package com.suyogcomputech.sms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Pintu on 7/27/2016.
 */
public class MyAccountActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        toolbar = (Toolbar) findViewById(R.id.toolbarMyAccount);
        toolbar.setTitle("My Account");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

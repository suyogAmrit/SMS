package com.suyogcomputech.sms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Pintu on 8/11/2016.
 */
public class FirstPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }

    public void benSms(View view) {
        Toast.makeText(FirstPageActivity.this, "Go To Website", Toast.LENGTH_SHORT).show();
    }

    public void login(View view) {
        Intent intent=new Intent(FirstPageActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.suyogcomputech.sms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.suyogcomputech.helper.Constants;

/**
 * Created by Pintu on 8/19/2016.
 */
public class EventDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lstEvent;
    String eventName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        defineComponents();
    }

    private void defineComponents() {
        eventName=getIntent().getExtras().getString(Constants.EVENT_NAME);
        Toast.makeText(EventDetailsActivity.this, eventName, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbarEventDetails);
        toolbar.setTitle("Event Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstEvent=(ListView)findViewById(R.id.lstEventDetails);
    }

    public void btnNextEvent(View view) {
        Intent intent=new Intent(EventDetailsActivity.this,EventConformActivity.class);
        startActivity(intent);
    }
}

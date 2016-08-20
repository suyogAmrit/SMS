package com.suyogcomputech.sms;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pintu on 8/19/2016.
 */
public class EventConformActivity extends AppCompatActivity{
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_conform);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventConform);
        toolbar.setTitle("Event");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void btnConformEvent(View view) {
        final String uniqueConformId=getUniqueId();
        Log.i("UniqueId",uniqueConformId);
        final Dialog dialog=new Dialog(EventConformActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_conform_event);
        TextView txtConformId=(TextView)dialog.findViewById(R.id.txtEventId);
        txtConformId.setText("Your Conformation Id is : "+uniqueConformId);

        Button btnConform=(Button)dialog.findViewById(R.id.btnConform);
        btnConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(EventConformActivity.this, "nsdjn", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private String getUniqueId() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timeStamp;
    }
}

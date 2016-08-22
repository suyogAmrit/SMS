package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lstEvent;
    String eventName;
    ArrayList<String> listEvent;
    ArrayAdapter<String> adapterInsurance;
    ConnectionClass connectionClass;
    ConnectionDetector detector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        detector=new ConnectionDetector(EventDetailsActivity.this);
        defineComponents();
    }

    private void defineComponents() {
        connectionClass=new ConnectionClass();
//        eventName=getIntent().getExtras().getString(AppConstants.EVENT_NAME);
//        Toast.makeText(EventDetailsActivity.this, eventName, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbarEventDetails);
        toolbar.setTitle("Event Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstEvent=(ListView)findViewById(R.id.lstEventDetails);
        if (detector.isConnectingToInternet()) {
            new GetEventList().execute();
        } else
            Toast.makeText(EventDetailsActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();

    }

    public void btnNextEvent(View view) {
        Intent intent=new Intent(EventDetailsActivity.this,EventConformActivity.class);
        startActivity(intent);
    }
    private class GetEventList extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EventDetailsActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            adapterInsurance = new ArrayAdapter<String>(EventDetailsActivity.this, android.R.layout.simple_list_item_multiple_choice, listEvent);
            lstEvent.setChoiceMode(lstEvent.CHOICE_MODE_MULTIPLE);
            lstEvent.setAdapter(adapterInsurance);

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select user_id from flat_user_Details";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                listEvent = new ArrayList<String>();
                while (rs.next()) {
                    String name = rs.getString("user_id");
                    listEvent.add(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

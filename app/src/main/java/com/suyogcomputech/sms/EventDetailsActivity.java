package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lstEvent;
    String eventId,eventName;
    ArrayList<String> listEvent;
    List<Event> eventList;
    ArrayAdapter<String> adapterInsurance;
    ConnectionClass connectionClass;
    ConnectionDetector detector;
    TextView txtEventName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        detector=new ConnectionDetector(EventDetailsActivity.this);
        defineComponents();
    }

    private void defineComponents() {
        txtEventName=(TextView)findViewById(R.id.txtEventName);
        connectionClass=new ConnectionClass();
        eventId=getIntent().getExtras().getString(AppConstants.EVENT_ID);
        eventName=getIntent().getExtras().getString(AppConstants.EVENT_TYPE);
        txtEventName.setText(eventName);
        Toast.makeText(EventDetailsActivity.this, eventId, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbarEventDetails);
        toolbar.setTitle("Event Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstEvent=(ListView)findViewById(R.id.lstEventDetails);
        if (detector.isConnectingToInternet()) {
            new SailorList().execute();
        } else
            Toast.makeText(EventDetailsActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();

    }


    private class SailorList extends AsyncTask<String, Void, ResultSet> {
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
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);
            try {
                dialog.dismiss();
                eventList=new ArrayList<>();
                listEvent = new ArrayList<String>();
                while (rs.next()) {
                    Event event=new Event();
                    event.setEventId(rs.getString("sl_no"));
                    event.setEventName(rs.getString("Event_mng_name"));
                    eventList.add(event);
                    listEvent.add(rs.getString("Event_mng_name"));
                }
                adapterInsurance = new ArrayAdapter<String>(EventDetailsActivity.this, android.R.layout.simple_list_item_1, listEvent);
                lstEvent.setAdapter(adapterInsurance);

                lstEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Event event2=eventList.get(i);
                        String id=event2.getEventId();
                        Intent intent=new Intent(EventDetailsActivity.this,SailorDetailsActivity.class);
                        intent.putExtra(AppConstants.EVENT_ID,eventId);
                        intent.putExtra(AppConstants.EVENT_MANAGER_USER_ID,id);
                        startActivity(intent);
                       // Toast.makeText(EventDetailsActivity.this,id,Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select em.sl_no,em.Event_mng_name from Event_Desc_tb as ed\n" +
                        "inner join Event_Type_Table as et on(et.slno=ed.Event_Type_id) \n" +
                        "inner join Event_Manager_tb as em on(em.sl_no=ed.Event_Mng_user_id) \n" +
                        "where et.slno="+eventId+"";
                Log.i(AppConstants.QUERY, query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                return rs;


            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}

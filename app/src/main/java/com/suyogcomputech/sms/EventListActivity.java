package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.suyogcomputech.adapter.EventAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/12/2016.
 */
public class EventListActivity extends AppCompatActivity {
    ConnectionDetector detector;
    ConnectionClass connectionClass;
    RecyclerView rcvEvent;
    EventAdapter adapter;
    ArrayList<Event> list;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventList);
        toolbar.setTitle("Event");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detector = new ConnectionDetector(EventListActivity.this);
        connectionClass = new ConnectionClass();
        rcvEvent=(RecyclerView)findViewById(R.id.rcvEvent);
        list = new ArrayList<>();
        getEventList();
    }



    private void getEventList() {
        if (detector.isConnectingToInternet()) {
            new EventList().execute();
        } else
            Toast.makeText(EventListActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();

    }

    private class EventList extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EventListActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);
            try {
                dialog.dismiss();
                while (rs.next()) {
                    Event event = new Event();
                    event.setEventName(rs.getString(AppConstants.EVENT_TYPE));
                    event.setEventImage(rs.getString(AppConstants.EVENT_TYPE_IMAGE).replace("~",""));
                    event.setEventId(rs.getString(AppConstants.EVENT_SERIAL_NO));
                    list.add(event);
                }
                adapter = new EventAdapter(list, EventListActivity.this);

                rcvEvent.setAdapter(adapter);
                rcvEvent.setHasFixedSize(true);
                GridLayoutManager glm = new GridLayoutManager(EventListActivity.this, 2, GridLayoutManager.VERTICAL, false);
                rcvEvent.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select slno,Event_Type,Event_Type_image from Event_Type_Table";
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

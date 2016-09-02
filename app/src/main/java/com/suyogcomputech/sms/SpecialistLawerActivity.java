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

import com.suyogcomputech.adapter.LawyerSpecialistAdapter;
import com.suyogcomputech.adapter.SpecialistAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Lawyer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Suyog on 9/2/2016.
 */
public class SpecialistLawerActivity extends AppCompatActivity {
    RecyclerView rcDoctor;
    LawyerSpecialistAdapter adapter;
    ArrayList<Lawyer> list;
    Toolbar toolbar;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventList);
        toolbar.setTitle("Specialist Lawyer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connectionClass = new ConnectionClass();
        rcDoctor=(RecyclerView)findViewById(R.id.rcvEvent);
        list = new ArrayList<>();
        getSpecialistList();
    }

    private void getSpecialistList() {
        if (AppHelper.isConnectingToInternet(SpecialistLawerActivity.this)) {
            new Specialist().execute();
        } else
            Toast.makeText(SpecialistLawerActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();

    }
    private class Specialist extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SpecialistLawerActivity.this);
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
                    Lawyer doctor = new Lawyer();
                    doctor.setSpecialistId(rs.getString("SpecialityID"));
                    doctor.setSpecialistImage(rs.getString("SpecialyImage").replace("~",""));
                    doctor.setLawyerSpecialist(rs.getString("SpecialityName"));
                    list.add(doctor);
                }
                adapter = new LawyerSpecialistAdapter(list, SpecialistLawerActivity.this);

                rcDoctor.setAdapter(adapter);
                rcDoctor.setHasFixedSize(true);
                GridLayoutManager glm = new GridLayoutManager(SpecialistLawerActivity.this, 2, GridLayoutManager.VERTICAL, false);
                rcDoctor.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select SpecialityID,SpecialityName,SpecialyImage from ELawer_SpecialityMaster";
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

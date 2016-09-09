package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.suyogcomputech.adapter.DoctorAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Doctor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/26/2016.
 */
public class DoctorActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    RecyclerView rcvDoctor;
    DoctorAdapter adapter;
    ArrayList<Doctor> list;
    Toolbar toolbar;
    String specialistId,designation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        specialistId=getIntent().getExtras().getString(AppConstants.SPECIALIST_ID);
        designation=getIntent().getExtras().getString(AppConstants.SPECIALIST_NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventList);
        toolbar.setTitle("Doctor");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        connectionClass = new ConnectionClass();
        rcvDoctor=(RecyclerView)findViewById(R.id.rcvEvent);
        list = new ArrayList<>();
        getSpecialistList();
    }
    private void getSpecialistList() {
        if (AppHelper.isConnectingToInternet(DoctorActivity.this)) {
            new Specialist().execute();
        } else
            Toast.makeText(DoctorActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();

    }
    private class Specialist extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DoctorActivity.this);
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
                    Doctor doctor = new Doctor();
                    doctor.setSpecialistType(rs.getString("Name"));
                    doctor.setSpecialistImageUrl(rs.getString("ImagePath").replace("~",""));
                    doctor.setId(rs.getString(AppConstants.DOCTOR_ID));
                    doctor.setQualification(rs.getString("Qualification"));
                    doctor.setExperience(rs.getString("Experience"));
                    doctor.setAddress(rs.getString("Address2"));
                    doctor.setDesignation(designation);
                    list.add(doctor);
                }
                adapter = new DoctorAdapter(list, DoctorActivity.this);
                rcvDoctor.setAdapter(adapter);
                rcvDoctor.setHasFixedSize(true);
                LinearLayoutManager glm = new LinearLayoutManager(DoctorActivity.this);
                rcvDoctor.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select d.DoctorID,d.Name,d.Address2,d.Qualification,d.Experience,DI.ImagePath from DoctorDetails as d \n" +
                        "  inner join DoctorsImages as DI on(DI.DoctorID=d.DoctorID)\n" +
                        "  inner join SpecialityMaster as sp on(sp.SpecialityID=d.SpecialityID)\n" +
                        "  where d.SpecialityID="+specialistId+"";
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

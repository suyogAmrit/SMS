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
import com.suyogcomputech.adapter.LawyerAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Lawyer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LawyerActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    RecyclerView rcvLawyer;
    LawyerAdapter adapter;
    ArrayList<Lawyer> list;
    Toolbar toolbar;
    String specialistId,designation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        specialistId=getIntent().getExtras().getString(AppConstants.SPECIALIST_ID);
        designation=getIntent().getExtras().getString(AppConstants.SPECIALIST_NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventList);
        toolbar.setTitle("Lawyer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        connectionClass = new ConnectionClass();
        rcvLawyer=(RecyclerView)findViewById(R.id.rcvEvent);
        list = new ArrayList<>();
        getSpecialistList();
    }
    private void getSpecialistList() {
        if (AppHelper.isConnectingToInternet(LawyerActivity.this)) {
            new Specialist().execute();
        } else
            Toast.makeText(LawyerActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }
    private class Specialist extends AsyncTask<Lawyer, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LawyerActivity.this);
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
                    Lawyer lawyer = new Lawyer();
                    lawyer.setLawyerSpecialist(rs.getString("Name"));
                    lawyer.setLawyerImageUrl(rs.getString("ImagePath").replace("~",""));
                    lawyer.setSpecialistId(rs.getString("lawID"));
                    lawyer.setQualification(rs.getString("Qualification"));
                    lawyer.setExperience(rs.getString("Experience"));
                    lawyer.setAddress(rs.getString("Address2"));
                    lawyer.setDesignation(designation);
                    list.add(lawyer);
                }
                adapter = new LawyerAdapter(list, LawyerActivity.this);
                rcvLawyer.setAdapter(adapter);
                rcvLawyer.setHasFixedSize(true);
                LinearLayoutManager glm = new LinearLayoutManager(LawyerActivity.this);
                rcvLawyer.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("Exception",e.getMessage());
            }
        }

        @Override
        protected ResultSet doInBackground(Lawyer... lawyers) {
            try {
                Connection con = connectionClass.connect();
                String query = "select l.Name,l.lawID,l.Qualification,l.Experience,l.Address2,i.ImagePath from Elawer_Details as l \n" +
                        "  inner join Lawer_images as i on(l.lawID=i.lawID)\n" +
                        "  inner join ELawer_SpecialityMaster as s on(l.SpecialityID=s.SpecialityID)\n" +
                        "  where s.SpecialityID="+specialistId+"";
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

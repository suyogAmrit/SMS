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

import com.suyogcomputech.adapter.InsuranceTypeAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Insurance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/2/2016.
 */
public class InsuranceTypeActivity extends AppCompatActivity{
    RecyclerView rcvInsurance;
    ConnectionClass connectionClass;
    ArrayList<Insurance> list;
    InsuranceTypeAdapter adapter;
    Toolbar toolbar;
    String insuranceId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        insuranceId=getIntent().getExtras().getString(AppConstants.INSURANCE_ID);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventList);
        toolbar.setTitle("Insurance Type");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connectionClass = new ConnectionClass();
        rcvInsurance = (RecyclerView)findViewById(R.id.rcvEvent);
        getInsuranceDetails();
    }
    private void getInsuranceDetails() {
        if (AppHelper.isConnectingToInternet(InsuranceTypeActivity.this)) {
            new GetInsuranceDetails().execute();
        } else
            Toast.makeText(InsuranceTypeActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }
    private class GetInsuranceDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsuranceTypeActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);
            try {
                dialog.dismiss();
                list = new ArrayList<>();
                while (rs.next()) {
                    Insurance insurance = new Insurance();
                    insurance.setInsuranceId(rs.getString("insurance_id"));
                    insurance.setInsuranceType(rs.getString("insurance_type"));
                    insurance.setInsuranceFeature(rs.getString("feature"));
                    insurance.setInsuranceTypeSlNo(rs.getString("sl_no"));
                    list.add(insurance);
                }
                adapter = new InsuranceTypeAdapter(list, InsuranceTypeActivity.this);
                rcvInsurance.setAdapter(adapter);
                rcvInsurance.setHasFixedSize(true);
                LinearLayoutManager glm = new LinearLayoutManager(InsuranceTypeActivity.this);
                rcvInsurance.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select sl_no,insurance_id,insurance_type,feature from Insurance_Type\n" +
                        "where insurance_id="+Integer.parseInt(insuranceId)+"";
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

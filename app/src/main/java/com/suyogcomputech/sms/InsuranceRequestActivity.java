package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.suyogcomputech.adapter.InsuranceTypeAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Insurance;
import com.suyogcomputech.helper.StringWithTag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pintu on 9/2/2016.
 */
public class InsuranceRequestActivity extends AppCompatActivity {
    Toolbar toolbar;
    ConnectionClass connectionClass;
    List<StringWithTag> list;
    String apptId, flatNo, uniqueUserId,insuranceId,insuranceTermId,insuranceTypeId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_insurance);
        insuranceId=getIntent().getExtras().getString(AppConstants.INSURANCE_ID);
        insuranceTypeId=getIntent().getExtras().getString(AppConstants.INSURANCE_TYPE_ID);
        toolbar = (Toolbar) findViewById(R.id.toolbarInsuranceRequest);
        toolbar.setTitle("Insurance Request");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        connectionClass = new ConnectionClass();
        getInsuranceTerms();
        getFlatDetails();
    }
    private void getInsuranceTerms() {
        if (AppHelper.isConnectingToInternet(InsuranceRequestActivity.this)) {
            new GetInsuranceDetails().execute();
        } else
            Toast.makeText(InsuranceRequestActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }
    private class GetInsuranceDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsuranceRequestActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);
            try {
                dialog.dismiss();
                list = new ArrayList<StringWithTag>();
                while (rs.next()) {
                    list.add(new StringWithTag(rs.getString("term"),rs.getString("sl_no")));

                }
                ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (InsuranceRequestActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
                Spinner spnInsurance=(Spinner)findViewById(R.id.spinnerInsuranceTerm);
                spnInsurance.setAdapter(adap);
                spnInsurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        StringWithTag s = (StringWithTag) adapterView.getItemAtPosition(i);
                        Object tag = s.tag;
                        insuranceTermId=tag.toString();
                        Log.i("bjhasd",tag.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

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
                String query = "select sl_no,insurance_id,term from Insurance_Terms where insurance_id=1";
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
    private void getFlatDetails() {
        if (AppHelper.isConnectingToInternet(InsuranceRequestActivity.this)) {
            new GetFlatDetails().execute();
        } else
            Toast.makeText(InsuranceRequestActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }

    private class GetFlatDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsuranceRequestActivity.this);
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
                    apptId = rs.getString(AppConstants.APARTMENT_ID);
                    flatNo = rs.getString(AppConstants.FLAT_NO);
                    Log.i("Apartment", apptId);
                    Log.i("Flat", flatNo);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "SELECT appt_id,flat_no FROM flat_user_Details WHERE user_id='" + findUserId() + "'";
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
    //code for find Find Flat User Details
    private String findUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }

    public void btnBookInsurance(View view) {
        if (AppHelper.isConnectingToInternet(InsuranceRequestActivity.this)) {
            new InsertInsurance().execute();
        } else
            Toast.makeText(InsuranceRequestActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }
    public class InsertInsurance extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsuranceRequestActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if (aBoolean) {
                Toast.makeText(InsuranceRequestActivity.this, "Booking Completed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InsuranceRequestActivity.this, SpecialistLawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }else {
                Toast.makeText(InsuranceRequestActivity.this, "Something Went Wrong Please Try Again...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection con = connectionClass.connect();
                String query = "Insert into Insurance_Req_tab(appt_id,flat_no,user_id,insurance_id,insurance_type_id,insurance_term_id,status) values('"+apptId+"','"+flatNo+"','"+findUserId()+"',"+Integer.parseInt(insuranceId)+","+Integer.parseInt(insuranceTypeId)+","+Integer.parseInt(insuranceTermId)+",0)" ;
                Log.i(AppConstants.QUERY, query);
                Statement stmt = con.createStatement();
                int result = stmt.executeUpdate(query);
                if (result == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                Log.i("Exception",ex.getMessage());
            }
            return false;
        }
    }

}

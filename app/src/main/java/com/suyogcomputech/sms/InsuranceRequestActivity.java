package com.suyogcomputech.sms;

import android.app.ProgressDialog;
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
    ArrayList<Insurance> listInsurance;
    List<StringWithTag> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_insurance);
        toolbar = (Toolbar) findViewById(R.id.toolbarInsuranceRequest);
        toolbar.setTitle("Insurance Request");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        connectionClass = new ConnectionClass();
        getInsuranceDetails();
    }
    private void getInsuranceDetails() {
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
                listInsurance = new ArrayList<>();
                list = new ArrayList<StringWithTag>();
                while (rs.next()) {
                    Insurance insurance = new Insurance();
                    list.add(new StringWithTag(rs.getString("term"),rs.getString("sl_no")));
                    insurance.setInsuranceId(rs.getString("insurance_id"));
                    insurance.setInsuranceTerm(rs.getString("term"));
                    insurance.setInsuranceTermSlno(rs.getString("sl_no"));
                    listInsurance.add(insurance);
                }
                ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (InsuranceRequestActivity.this, android.R.layout.simple_spinner_item, list);
                Spinner spnInsurance=(Spinner)findViewById(R.id.spinnerInsuranceTerm);
                spnInsurance.setAdapter(adap);
                spnInsurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        StringWithTag s = (StringWithTag) adapterView.getItemAtPosition(i);
                        Object tag = s.tag;
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

}

package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.suyogcomputech.adapter.InsuranceAdapter;
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
public class InsuranceActivity extends AppCompatActivity{
    RecyclerView rcvInsurance;
    ConnectionClass connectionClass;
    ArrayList<Insurance> list;
    InsuranceAdapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbarEventList);
        toolbar.setTitle("Insurance");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connectionClass = new ConnectionClass();
        rcvInsurance = (RecyclerView)findViewById(R.id.rcvEvent);
        getInsuranceDetails();
    }
    private void getInsuranceDetails() {
        if (AppHelper.isConnectingToInternet(InsuranceActivity.this)) {
            new GetInsuranceDetails().execute();
        } else
            Toast.makeText(InsuranceActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }

    private class GetInsuranceDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsuranceActivity.this);
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
                    insurance.setInsuranceName(rs.getString("insurance_name"));
                    list.add(insurance);
                }
                adapter = new InsuranceAdapter(list, InsuranceActivity.this);
                rcvInsurance.setAdapter(adapter);
                rcvInsurance.setHasFixedSize(true);
//                LinearLayoutManager glm = new LinearLayoutManager(InsuranceActivity.this);
                GridLayoutManager glm=new GridLayoutManager(InsuranceActivity.this,null,2,2);
                rcvInsurance.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select insurance_id,insurance_name from Insurance_master";
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

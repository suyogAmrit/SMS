package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.DatePickerFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pintu on 8/19/2016.
 */
public class EventConformActivity extends AppCompatActivity implements DatePickerFragment.GetDate {
    Toolbar toolbar;
    ConnectionClass connectionClass;
    EditText edEventDate, edEstimateQuantity, edUnit, edDescription, edEstimatedBudged;
    String apptId, flatNo, eventId,uniqueUserId, eventReqDate, eventProposeDate, unit, proposeDescription;
    int estimateQnty;
    float estimateBudget;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_conform);
        getFlatDetails();
        defineComponents();

    }


    private void defineComponents() {
        connectionClass=new ConnectionClass();
        toolbar = (Toolbar) findViewById(R.id.toolbarEventConform);
        toolbar.setTitle("Confirm Event");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edEstimateQuantity = (EditText) findViewById(R.id.edEstimate);
        edUnit = (EditText) findViewById(R.id.edUnit);
        edDescription = (EditText) findViewById(R.id.edDescription);
        edEstimatedBudged = (EditText) findViewById(R.id.edEstimateBudget);
        edEventDate = (EditText) findViewById(R.id.edEventDate);

        edEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AppConstants.getDatePicker(getSupportFragmentManager());
                }
            }
        });
        edEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.getDatePicker(getSupportFragmentManager());
            }
        });
    }

    public void btnConformEvent(View view) {
        final String uniqueConformId = getUniqueId();
        eventReqDate = currentDate();


        unit = edUnit.getText().toString();
        proposeDescription = edDescription.getText().toString();

        eventProposeDate = edEventDate.getText().toString();
//        if (edEstimateQuantity.getText().toString().equals("")){
//            edEstimateQuantity.setError("Please Enter Estimate Quantity");
//            edEstimateQuantity.requestFocus();
//        }
//        else if(unit.equals("")){
//            edUnit.setError("Enter Unit");
//            edUnit.requestFocus();
//            edEstimateQuantity.setError(null);
//        }else if (eventProposeDate.equals("")){
//            edEventDate.setError("Enter Propose Date");
//            edEventDate.requestFocus();
//            edUnit.setError(null);
//        }else if (proposeDescription.equals("")){
//            edEventDate.setError(null);
//            edDescription.setError("Enter Description");
//            edDescription.requestFocus();
//        }else if(edEstimatedBudged.getText().toString().equals("")){
//            edDescription.setError(null);
//            edEstimatedBudged.setError("Enter Estimate Budget");
//            edEstimatedBudged.requestFocus();
//        }else {
            estimateBudget =Float.parseFloat(edEstimatedBudged.getText().toString()) ;
            estimateQnty =Integer.parseInt(edEstimateQuantity.getText().toString()) ;
            edEstimatedBudged.setError(null);
            if (AppHelper.isConnectingToInternet(EventConformActivity.this)) {
                new InsertEventDetails().execute();
            } else
                Toast.makeText(EventConformActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
//        }
//        final Dialog dialog=new Dialog(EventConformActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_conform_event);
//        TextView txtConformId=(TextView)dialog.findViewById(R.id.txtEventId);
//        txtConformId.setText("Your Conformation Id is : "+uniqueConformId);
//
//        Button btnConform=(Button)dialog.findViewById(R.id.btnConform);
//        btnConform.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Toast.makeText(EventConformActivity.this, "nsdjn", Toast.LENGTH_SHORT).show();
//            }
//        });
//        dialog.show();
    }

    private String getUniqueId() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timeStamp;
    }

    private String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onGetDate(String dateNow) {
        edEventDate.setText(dateNow);
    }

    public class InsertEventDetails extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EventConformActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            Toast.makeText(EventConformActivity.this, String.valueOf(aBoolean), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection con = connectionClass.connect();
                String query = "INSERT INTO Event_Request_tb(appt_id,flat_no,Event_id,Event_Request_date,Event_Propose_date,Estmated_Qty,Unit,Proposed_Desc,Estimated_Budget,Event_Request_status) VALUES('"+apptId+"','"+flatNo+"',2,'" + eventReqDate + "','" + eventProposeDate + "'," + estimateQnty + ",'" + unit + "','" + proposeDescription + "'," + estimateBudget + ",0)";
                Statement stmt = con.createStatement();
                int result = stmt.executeUpdate(query);
                if (result == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {

            }
            return false;
        }
    }

        private String findUserId() {
            SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
            uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
            return uniqueUserId;
        }

    private void getFlatDetails() {
        if (AppHelper.isConnectingToInternet(EventConformActivity.this)) {
            new GetFlatDetails().execute();
        } else
            Toast.makeText(EventConformActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }

    private class GetFlatDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EventConformActivity.this);
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
                    apptId=rs.getString(AppConstants.APARTMENT_ID);
                    flatNo=rs.getString(AppConstants.FLAT_NO);
                    Log.i("Apartment",apptId);
                    Log.i("Flat",flatNo);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "SELECT appt_id,flat_no FROM flat_user_Details WHERE user_id='"+findUserId()+"'";
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

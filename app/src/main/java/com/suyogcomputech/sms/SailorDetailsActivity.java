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
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/24/2016.
 */
public class SailorDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ConnectionClass connectionClass;
    String eventId,managerId;
    TextView txtOrgName,txtorgAddress,txtManagerPhone,txtManagerMobile,txtManagerEmail,txtEventDescription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sailor_details);
        connectionClass=new ConnectionClass();
        toolbar = (Toolbar) findViewById(R.id.toolbarSailorDetails);
        toolbar.setTitle("Sailor Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        defineComponts();
        getSailorDetails();
    }

    private void defineComponts() {
        eventId=getIntent().getExtras().getString(AppConstants.EVENT_ID);
        managerId=getIntent().getExtras().getString(AppConstants.EVENT_MANAGER_USER_ID);
        txtEventDescription=(TextView)findViewById(R.id.txtEventDescription);
        txtOrgName=(TextView)findViewById(R.id.txtOrgName);
        txtorgAddress=(TextView)findViewById(R.id.txtOrgAddress);
        txtManagerPhone=(TextView)findViewById(R.id.txtManagerPhone);
        txtManagerMobile=(TextView)findViewById(R.id.txtManagerMobile);
        txtManagerEmail=(TextView)findViewById(R.id.txtManagerEmail);
    }

    public void btnConformSailor(View view) {
        Intent intent=new Intent(SailorDetailsActivity.this,EventConformActivity.class);
        startActivity(intent);
    }

    private void getSailorDetails() {
        if (AppHelper.isConnectingToInternet(SailorDetailsActivity.this)) {
            new SailorDetails().execute();
        } else
            Toast.makeText(SailorDetailsActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }

    private class SailorDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SailorDetailsActivity.this);
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
                    String mngName=rs.getString(AppConstants.EVENT_MANAGER_NAME);
                    Log.i("Name",mngName);
                    txtOrgName.setText(rs.getString(AppConstants.EVENT_ORGANIZATION_NAME));
                    txtorgAddress.setText("Address : "+rs.getString(AppConstants.EVENT_ORGANIZATION_ADDRESS));
                    txtManagerPhone.setText("Phone : "+rs.getString(AppConstants.EVENT_MANAGER_PHONE));
                    txtManagerMobile.setText("Mobile : "+rs.getString(AppConstants.EVENT_MANAGER_MOBILE));
                    txtManagerEmail.setText("Email : "+rs.getString(AppConstants.EVENT_MANAGER_EMAIL));
                    txtEventDescription.setText("Description : "+rs.getString(AppConstants.EVENT_DESCRIPTION));

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select em.Event_mng_name,em.Event_Mng_Org_Name,em.Event_Mng_org_address,em.Event_Mng_phno,em.Event_Mng_cell_phno,em.Event_Mng_email,ES.Event_Desc from dbo.Event_Manager_tb as em\n" +
                        "inner join Event_Desc_tb as ES on (ES.Event_Mng_user_id=em.sl_no)\n" +
                        "where em.sl_no='"+managerId+"' and ES.Event_Type_id='"+eventId+"'";
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

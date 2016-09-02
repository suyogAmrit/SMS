package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pintu on 8/26/2016.
 */
public class DoctorDetailsActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    Toolbar toolbar;
    TextView txtDoctorName, txtQualification, txtExperience, txtDesignation, txtAge, txtAddress;
    ImageView imgDoctor;
    String doctorId, designation;
    private int screenWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        designation = getIntent().getExtras().getString(AppConstants.DESIGNATION);
        doctorId = getIntent().getExtras().getString(AppConstants.DOCTOR_ID);
        toolbar = (Toolbar) findViewById(R.id.toolbarDoctorDetails);
        toolbar.setTitle("Doctor Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        connectionClass = new ConnectionClass();
        txtDoctorName = (TextView) findViewById(R.id.txtName);
        imgDoctor = (ImageView) findViewById(R.id.imgDoctor);
        txtQualification = (TextView) findViewById(R.id.txtQualification);
        txtExperience = (TextView) findViewById(R.id.txtExperienceDoctor);
        txtDesignation = (TextView) findViewById(R.id.txtDesignationDoctor);
        txtAge = (TextView) findViewById(R.id.txtAge);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        getDoctorDetails();
    }

    private void getDoctorDetails() {
        if (AppHelper.isConnectingToInternet(DoctorDetailsActivity.this)) {
            new DoctorDetails().execute();
        } else
            Toast.makeText(DoctorDetailsActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }



    private class DoctorDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DoctorDetailsActivity.this);
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
                    String imagepath = rs.getString("ImagePath").replace("~", "");
                    txtDoctorName.setText(rs.getString("Name"));
                    txtExperience.setText(rs.getString("Experience")+" Experience");
                    txtQualification.setText(rs.getString("Qualification"));
                    txtAge.setText("Age : "+rs.getString("Age"));
                    txtAddress.setText("Address : "+rs.getString("Address2"));
                    txtDesignation.setText(designation);
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imagepath, opts);
                    opts.inJustDecodeBounds = false;

                    Picasso.with(DoctorDetailsActivity.this)
                            .load("http://192.168.12.100/APMS" + imagepath)
                            .error(R.drawable.ic_empty)
                            .placeholder(R.drawable.backgroundd)
                            .resize(screenWidth / 2, 300)
                            .centerCrop()
                            .into((imgDoctor));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select d.DoctorID,d.Name,d.Qualification,d.Age,d.Experience,d.Address2,DI.ImagePath from DoctorDetails as d \n" +
                        "inner join DoctorsImages as DI on(DI.DoctorID=d.DoctorID)\n" +
                        " where d.DoctorID=" + doctorId + "";
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

    public void btnBookAppointment(View view) {
        Intent intent=new Intent(DoctorDetailsActivity.this,BookDoctorActivity.class);
        intent.putExtra(AppConstants.DOCTOR_ID,doctorId);
        startActivity(intent);
    }
}

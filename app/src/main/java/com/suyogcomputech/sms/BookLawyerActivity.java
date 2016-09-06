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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.Toast;

import com.suyogcomputech.adapter.BookDoctorAdapter;
import com.suyogcomputech.adapter.BookLawyerAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.Appointment;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.DatePickerFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pintu on 8/29/2016.
 */
public class BookLawyerActivity extends AppCompatActivity implements DatePickerFragment.GetDate {
    Toolbar toolbar;
    ConnectionClass connectionClass;
    EditText edReqDate;
    int dayId;
    String apptId, flatNo, lawyerId, uniqueUserId, appointmentRequestDate,scheduleID;
    BookLawyerAdapter adapter;
    ArrayList<Appointment> list;
    ListView lstAppointment;
    HorizontalScrollView hrsv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_doctor);
        dayId = 0;
        connectionClass = new ConnectionClass();
        getFlatDetails();
        defineComponents();

    }

    private void defineComponents() {
        hrsv=(HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        lawyerId = getIntent().getExtras().getString(AppConstants.LAWYER_ID);
        toolbar = (Toolbar) findViewById(R.id.toolbarBook);
        toolbar.setTitle("Book lawyer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        edReqDate = (EditText) findViewById(R.id.edAppointmentDate);
//        edReqDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    AppHelper.getDatePicker(getSupportFragmentManager());
//                }
//            }
//        });
        String sss="sss";
        edReqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppHelper.getDatePicker(getSupportFragmentManager());
            }
        });
    }

    public void btnAppointmentTime(View view) {
        appointmentRequestDate = edReqDate.getText().toString();
        String day2 = getDayFromDateString(edReqDate.getText().toString(), "yyyy-MM-dd");
        dayId = getDayId(day2);
        Log.i("Date", day2);
        Log.i("Day", String.valueOf(dayId));
        getAppointmentDetails();

    }

    private void getAppointmentDetails() {
        if (AppHelper.isConnectingToInternet(BookLawyerActivity.this)) {
            new AppointmentDoctor().execute();
        } else
            Toast.makeText(BookLawyerActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }


    private class AppointmentDoctor extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BookLawyerActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);
            try {

                dialog.dismiss();//Log.i("ssss",rs.getString("Name"));
                list = new ArrayList<>();
                while (rs.next()) {
                hrsv.setVisibility(View.VISIBLE);
                    Appointment lawyer = new Appointment();
                    //lawyer.setDoctorName(rs.getString("Name"));
                    lawyer.setDoctorId(rs.getString("lawID"));
                    lawyer.setStartTime(rs.getString("StartTime"));
                    lawyer.setEndTime(rs.getString("EndTime"));
                    lawyer.setPlace(rs.getString("Place"));
                    lawyer.setPrice(rs.getString("LawerFee"));
                    lawyer.setScheduleID(rs.getString("ScheduleID"));
                    list.add(lawyer);
                }
                adapter = new BookLawyerAdapter(BookLawyerActivity.this, list);
                lstAppointment = (ListView) findViewById(R.id.lstBookDoctor);
                lstAppointment.setAdapter(adapter);

            } catch (SQLException e) {
                Log.i("jsbjb",e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select s.ScheduleID,s.lawID,s.StartTime,s.EndTime,s.Place,s.LawerFee from Elawer_ScheduleMaster as s\n" +
                        "inner join Elawer_Details as l on(s.lawID=l.lawID)\n" +
                        "where l.lawID=" + Integer.parseInt(lawyerId) + " and s.DayID=" + dayId + "";
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

    @Override
    public void onGetDate(String dateNow) {
        edReqDate.setText(dateNow);
    }

    public int getDayId(String day) {
        if (day.equals("saturday")) {
            dayId = 7;
        } else if (day.equals("sunday")) {
            dayId = 1;
        } else if (day.equals("monday")) {
            dayId = 2;
        } else if (day.equals("tuesday")) {
            dayId = 3;
        } else if (day.equals("wednesday")) {
            dayId = 4;
        } else if (day.equals("thursday")) {
            dayId = 5;
        } else if (day.equals("friday")) {
            dayId = 6;
        }
        return dayId;
    }


    public static String getDayFromDateString(String stringDate, String dateTimeFormat) {
        String[] daysArray = new String[]{"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
        String day = "";

        int dayOfWeek = 0;
        //dateTimeFormat = yyyy-MM-dd HH:mm:ss
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            day = daysArray[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return day;
    }


    //code for find Find Flat User Details
    private String findUserId() {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }

    private void getFlatDetails() {
        if (AppHelper.isConnectingToInternet(BookLawyerActivity.this)) {
            new GetFlatDetails().execute();
        } else
            Toast.makeText(BookLawyerActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }

    private class GetFlatDetails extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BookLawyerActivity.this);
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

    public void insertAppointmentData(String schId) {
        scheduleID=schId;
        if (AppHelper.isConnectingToInternet(BookLawyerActivity.this)) {
            new InsertAppointment().execute();
        } else
            Toast.makeText(BookLawyerActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();

    }

    public class InsertAppointment extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BookLawyerActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if (aBoolean) {
                Toast.makeText(BookLawyerActivity.this, "Booking Completed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookLawyerActivity.this, SpecialistLawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }else {
                Toast.makeText(BookLawyerActivity.this, "Something Went Wrong Please Try Again...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection con = connectionClass.connect();
                String query = "Insert into Elawer_ClientReqDetails(appt_id,Flat_No,lawID,ScheduleID,ReqDate,appointDate,Status,user_id)\n" +
                        "values('" + apptId + "','" + flatNo + "'," + Integer.parseInt(lawyerId) + ","+Integer.parseInt(scheduleID)+",'" + currentDate() + "','" + appointmentRequestDate + "',0,'" + findUserId() + "');";
                Log.i(AppConstants.QUERY, query);
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

    private String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


}

package com.suyogcomputech.app_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.suyogcomputech.adapter.MyDoctorAppointmentAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.sms.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/29/2016.
 */
public class MyAppointmentFragment extends Fragment {
    RecyclerView rcvEventBook;
    MyDoctorAppointmentAdapter adapter;
    ArrayList<Doctor> list;
    ConnectionClass connectionClass;
    String uniqueUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_booking, container, false);
        connectionClass = new ConnectionClass();
        rcvEventBook = (RecyclerView) view.findViewById(R.id.rcvMyBooking);
        if (AppHelper.isConnectingToInternet(getActivity())) {
            new MyEventList().execute();
        } else
            Toast.makeText(getActivity(), AppConstants.dialog_message, Toast.LENGTH_LONG).show();
        return view;
    }

    private class MyEventList extends AsyncTask<String, Void, ResultSet> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
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
                    Doctor event = new Doctor();
                    event.setName(rs.getString("Name"));
                    event.setRequestDate(rs.getString("appointDate"));
                    event.setStatus(rs.getString("Status"));
                    list.add(event);
                }
                adapter = new MyDoctorAppointmentAdapter(list, getActivity());
                rcvEventBook.setAdapter(adapter);
                rcvEventBook.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                rcvEventBook.setLayoutManager(llm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select d.Name,p.Docter_id,p.appointDate,p.Status from PatientReqDetails as p\n" +
                        "inner join DoctorDetails d on(p.Docter_id=d.DoctorID)";
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

    private String findUserId() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }

}

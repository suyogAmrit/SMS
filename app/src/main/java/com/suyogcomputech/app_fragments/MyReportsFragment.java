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
import android.widget.Button;
import android.widget.Toast;

import com.suyogcomputech.adapter.MyBookingAdapter;
import com.suyogcomputech.adapter.MyReportsAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.Event;
import com.suyogcomputech.sms.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/31/2016.
 */
public class MyReportsFragment extends Fragment {
    RecyclerView rcvEventBook;
    MyReportsAdapter adapter;
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
                    Doctor doctor = new Doctor();
                    doctor.setType(rs.getString(AppConstants.EVENT_TYPE));
                    doctor.setRequestDate(rs.getString(AppConstants.EVENT_REQ_PROPOSE_DATE));
                    doctor.setSpecialistImageUrl(rs.getString(AppConstants.EVENT_ORGANIZATION_NAME));
                    list.add(doctor);
                }
                adapter = new MyReportsAdapter(list, getActivity());
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
                String query = "select et.Event_Type, em.Event_Mng_Org_Name, er.Event_Propose_date " +
                        "from Event_Request_tb as er\n " +
                        "   inner join Event_Desc_tb as es on(es.Event_id=er.Event_id)\n" +
                        "  inner join  Event_Type_Table as et on(et.slno=es.Event_id)\n" +
                        "  inner join Event_Manager_tb as em on(em.sl_no=es.Event_Mng_user_id)\n" +
                        "  where er.user_id='" + findUserId() + "'";
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

package com.suyogcomputech.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import com.suyogcomputech.sms.EventDetailsActivity;
import com.suyogcomputech.sms.MainActivity;
import com.suyogcomputech.sms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/12/2016.
 */
public class ListEventFragment extends Fragment {
    ListView lstEvent;
    ConnectionDetector detector;
    ConnectionClass connectionClass;
    ArrayAdapter<String> adapterInsurance;
    ArrayList<String> listEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insurance_list, container, false);
        detector = new ConnectionDetector(getActivity());
        connectionClass = new ConnectionClass();
        lstEvent = (ListView) view.findViewById(R.id.lstInsurance);
        getEventList();
        return view;
    }

    private void getEventList() {
        if (detector.isConnectingToInternet()) {
            new GetEventList().execute();
        } else
            Toast.makeText(getActivity(), Constants.dialog_message, Toast.LENGTH_LONG).show();

    }

    private class GetEventList extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            adapterInsurance = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listEvent);
            lstEvent.setAdapter(adapterInsurance);
            lstEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String type = adapterView.getItemAtPosition(i).toString();

                Intent intent=new Intent(getActivity(), EventDetailsActivity.class);
                //intent.putExtra(Constants.EVENT_NAME,type);
                getActivity().startActivity(intent);
            }
        });
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select uname from user_registration";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                listEvent = new ArrayList<String>();
                while (rs.next()) {
                    String name = rs.getString("uname");
                    listEvent.add(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}

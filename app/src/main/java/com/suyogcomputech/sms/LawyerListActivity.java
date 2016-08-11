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

import com.suyogcomputech.adapter.LawyerAdapter;
import com.suyogcomputech.adapter.OnlineShoppingAdapter;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.OnlineShopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/11/2016.
 */
public class LawyerListActivity extends AppCompatActivity {
    Toolbar toolbar;
    ConnectionDetector detector;
    RecyclerView rcvLawyer;
    LawyerAdapter adapter;
    ArrayList<Doctor> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        toolbar = (Toolbar) findViewById(R.id.toolbarDoctor);
        toolbar.setTitle("Lawyer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detector=new ConnectionDetector(LawyerListActivity.this);
        rcvLawyer = (RecyclerView) findViewById(R.id.rcvDoctor);
        if (detector.isConnectingToInternet()) {
          new FetchLawyerDetails().execute("http://54.193.93.238/fortest/AnugulPol/fetch_impcontacts_data.php");
        } else
            Toast.makeText(LawyerListActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();

    }
    private class FetchLawyerDetails extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // urlConnection.setConnectTimeout(30000);
                urlConnection.setDoInput(true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LawyerListActivity.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.progress_dialog_message);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                dialog.dismiss();
                Log.i("response", s);
//                JSONObject objJson = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(s);
                list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Doctor lawyer = new Doctor();
                    lawyer.setName(jsonObject.getString("Name"));
                    lawyer.setType(jsonObject.getString("Designation"));
                    list.add(lawyer);
                }

                adapter = new LawyerAdapter(list, LawyerListActivity.this);
                rcvLawyer.setAdapter(adapter);
                rcvLawyer.setHasFixedSize(true);
                LinearLayoutManager glm = new LinearLayoutManager(LawyerListActivity.this);
                rcvLawyer.setLayoutManager(glm);

            } catch (NullPointerException e) {
                Toast.makeText(LawyerListActivity.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException ex) {
            }
        }
    }
}

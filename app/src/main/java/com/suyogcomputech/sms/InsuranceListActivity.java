package com.suyogcomputech.sms;

import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.Toast;

import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;

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

/**
 * Created by Pintu on 8/11/2016.
 */
public class InsuranceListActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lstInsurance;
    ConnectionDetector detector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_list);
        detector = new ConnectionDetector(InsuranceListActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbarInsurance);
        toolbar.setTitle("Insurance");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstInsurance = (ListView) findViewById(R.id.lstInsurance);
        ArrayAdapter<String> adapterInsurance = new ArrayAdapter<String>(InsuranceListActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.insurance));
        lstInsurance.setAdapter(adapterInsurance);
        lstInsurance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String type = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(InsuranceListActivity.this, type, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Constants.INSURANCE_TYPE, type);
                    if (detector.isConnectingToInternet()) {
                        new SendInsuranceType().execute(Constants.URL_FACILITIES);
                    } else
                        Toast.makeText(InsuranceListActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private class SendInsuranceType extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsuranceListActivity.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
//            try {
//                Log.i("Result", s);
//                dialog.dismiss();
//                JSONObject jsonObject1 = new JSONObject(s);
//                String sus = jsonObject1.getString("status");
//
//                if (sus.equals("1")) {
//                    uniqueId = jsonObject1.getString("uniqid");
//                    name = jsonObject1.getString("username");
//                    emailId = jsonObject1.getString("useremail");
//                    available = jsonObject1.getString("available_city");
//
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString(Constants.UNIQUE_ID, uniqueId);
//                    editor.putString(Constants.AVAILABLE_DETAILS, available);
//                    editor.putString(Constants.USER_NAME, name);
//                    editor.putString(Constants.USER_MAIL, emailId);
//                    editor.commit();
//
//                    finish();
//
//                } else
//                    Toast.makeText(InsuranceListActivity.this, "Enter valid Email id and Password", Toast.LENGTH_LONG).show();
//
//            } catch (NullPointerException e) {
//                Toast.makeText(InsuranceListActivity.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
//                dialog.dismiss();
//                finish();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                dialog.dismiss();
//            }
        }


        @Override
        protected String doInBackground(String... params) {
            URL myUrl = null;
            try {
                String url = "";
                myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(params[0].getBytes());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                //Log.i("Responce", response.toString());
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
    }
}

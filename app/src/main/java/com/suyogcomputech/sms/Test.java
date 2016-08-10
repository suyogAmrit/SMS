package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.suyogcomputech.adapter.OnlineShoppingAdapter;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import com.suyogcomputech.helper.OnlineShopping;

import org.json.JSONArray;
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
import java.util.ArrayList;

/**
 * Created by Pintu on 7/16/2016.
 */
public class Test extends AppCompatActivity {
    ConnectionDetector detector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new ConnectionDetector(Test.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "abc");
            jsonObject.put("gender", "def");
            jsonObject.put("salary", "123");
            Log.i("jsonObject", jsonObject.toString());
            if (detector.isConnectingToInternet()) {
                new SaveData().execute(jsonObject.toString());
            } else
                Toast.makeText(Test.this, Constants.dialog_message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//            if (detector.isConnectingToInternet()) {
//            new FetchFacilities().execute("http://192.168.12.100/DemoUrl/EmployeeService.asmx?op=FetchProductsbyZipCodeandCatagoryid");
//        } else
//            Toast.makeText(Test.this, Constants.dialog_message, Toast.LENGTH_LONG).show();
    }


    private class FetchFacilities extends AsyncTask<String, Void, String> {
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
            dialog = new ProgressDialog(Test.this);
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
            } catch (NullPointerException e) {
                Toast.makeText(Test.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
                finish();
            } catch (IllegalArgumentException ex) {
            }
        }
    }

    private class SaveData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Test.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("Result", s);
//            try {
//                Log.i("Result", s);
//                JSONObject jsonObject1 = new JSONObject(s);
//                String status = jsonObject1.getString("status");
//                String message = jsonObject1.getString("message");
//                dialog.dismiss();
//                if (status.equals("1")) {
//                    Toast.makeText(Test.this, "Registration Successful", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(Test.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else
//                    Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
//
//            } catch (NullPointerException e) {
//                Toast.makeText(RegistrationActivity.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
//                dialog.dismiss();
//                finish();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }

        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected String doInBackground(String... params) {
            URL myUrl = null;
            try {
                String url = "http://123.63.220.45/demo_project1/WebService1.asmx?op=FetchProductsbyZipCodeandCatagoryid";
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, params[0]);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();

//                myUrl = new URL(url);
//                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
//                connection.setDoOutput(true);
//                OutputStream os = connection.getOutputStream();
//                os.write(params[0].getBytes());
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(connection.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//
//                in.close();
//                return response.toString();
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

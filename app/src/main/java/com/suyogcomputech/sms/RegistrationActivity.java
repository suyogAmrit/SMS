package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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


public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    String name, email, mobile, password;
    EditText ed_name, ed_email, ed_mobile, ed_password;
    ConnectionDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registaration);
        initilizeUi();

    }

    private void initilizeUi() {
        detector = new ConnectionDetector(RegistrationActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbarReg);
        toolbar.setTitle("Registration");
        toolbar.setTitleTextColor(Color.parseColor(Constants.COLOR_WHITE));
//
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ed_name = (EditText) findViewById(R.id.edName);
        ed_email = (EditText) findViewById(R.id.edEmail);
        ed_mobile = (EditText) findViewById(R.id.edMobile);
        ed_password = (EditText) findViewById(R.id.edPassword);
    }

    public void btnSubmitRegistration(View view) {
        name = ed_name.getText().toString();
        email = ed_email.getText().toString();
        mobile = ed_mobile.getText().toString();
        password = ed_password.getText().toString();
        if (name.equals("")) {
            ed_name.setError("Please Enter Your Name");
            ed_name.requestFocus();
        } else if (!isValidEmail(email)) {
            ed_email.setError("Enter valid Email Id");
            ed_name.setError(null);
            ed_email.requestFocus();
        } else if (!isValidMobile(mobile)) {
            ed_mobile.setError("Enter Mobile No.");
            ed_mobile.requestFocus();
            ed_email.setError(null);
        } else if (password.equals("")) {
            ed_password.setError("Enter Your Password");
            ed_password.requestFocus();
            ed_mobile.setError(null);
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.USER_NAME, name);
                jsonObject.put(Constants.USER_MAIL, email);
                jsonObject.put(Constants.USER_CONTACT, mobile);
                jsonObject.put(Constants.USER_PASSWORD, password);
                jsonObject.put(Constants.TYPE, "Email");
                Log.i("jsonObject", jsonObject.toString());
                if (detector.isConnectingToInternet()) {
                    //new SaveData().execute(jsonObject.toString());
                } else
                    Toast.makeText(RegistrationActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    private class SaveData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegistrationActivity.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                Log.i("Result", s);
                JSONObject jsonObject1 = new JSONObject(s);
                String status = jsonObject1.getString("status");
                String message = jsonObject1.getString("message");
                dialog.dismiss();
                if (status.equals("1")) {
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();

            } catch (NullPointerException e) {
                Toast.makeText(RegistrationActivity.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(String... params) {
            URL myUrl = null;
            try {
                String url="";
                Log.i("Responce", url);
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

    public void btnReset(View view) {
        ed_name.requestFocus();
        clear();
    }

    public void clear() {
        ed_name.setText("");
        ed_email.setText("");
        ed_mobile.setText("");
        ed_password.setText("");
    }

}



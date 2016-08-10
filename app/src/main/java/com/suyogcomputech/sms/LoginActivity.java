package com.suyogcomputech.sms;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

/**
 * Created by Pintu on 7/15/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    EditText edPassword;
    AutoCompleteTextView edUserName;
    ConnectionDetector detector;
    String uId, psw, name, emailId, uniqueId, type, available;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnLogin(View view) {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
//        uId = edUserName.getText().toString();
//        psw = edPassword.getText().toString();
//        if (uId.equals("")) {
//            edUserName.setError("Enter Your Email");
//            edUserName.requestFocus();
//        } else if (psw.equals("")) {
//            edPassword.setError("Enter Your Password");
//            edUserName.setError(null);
//            edPassword.requestFocus();
//        } else {
//            edPassword.setError(null);
//            edUserName.setError(null);
//
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put(Constants.USER_MAIL, uId);
//                jsonObject.put(Constants.USER_PASSWORD, psw);
//                Log.i("jsonObject", jsonObject.toString());
//                if (detector.isConnectingToInternet()) {
//                   // new LoginDetails().execute(jsonObject.toString());
//                } else
//                    Toast.makeText(LoginActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
    }
    private class LoginDetails extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.i("Result", s);
                dialog.dismiss();
                JSONObject jsonObject1 = new JSONObject(s);
                String sus = jsonObject1.getString("status");

                if (sus.equals("1")) {
                    uniqueId = jsonObject1.getString("uniqid");
                    name = jsonObject1.getString("username");
                    emailId = jsonObject1.getString("useremail");
                    available=jsonObject1.getString("available_city");

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constants.UNIQUE_ID, uniqueId);
                    editor.putString(Constants.AVAILABLE_DETAILS,available);
                    editor.putString(Constants.USER_NAME, name);
                    editor.putString(Constants.USER_MAIL, emailId);
                    editor.commit();

                    finish();

                } else
                    Toast.makeText(LoginActivity.this, "Enter valid Email id and Password", Toast.LENGTH_LONG).show();

            } catch (NullPointerException e) {
                Toast.makeText(LoginActivity.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            URL myUrl = null;
            try {
                String url="";
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
    public void btnSignIn(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void btnFgtPassword(View view) {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        Button btnGetPassword = (Button) dialog.findViewById(R.id.btnGetPassword);
        final EditText edMailID = (EditText) dialog.findViewById(R.id.edForgotPassword);
        btnGetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailId = edMailID.getText().toString();
                if (!isValidEmail(mailId)) {
                    edMailID.setError("Enter valid password");
                    edMailID.requestFocus();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Constants.USER_MAIL, mailId);
                        Log.i("jsonObject", jsonObject.toString());
                        if (detector.isConnectingToInternet()) {
                            //new GetForgotPassword().execute(jsonObject.toString());
                        } else
                            Toast.makeText(LoginActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.show();
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}

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

import com.suyogcomputech.helper.ConnectionClass;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pintu on 7/15/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    EditText edPassword, edUserName;
    ConnectionDetector detector;
    String uId, psw, name, emailId, uniqueId, type, available, z;
    SharedPreferences sharedpreferences;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectionClass = new ConnectionClass();
        edUserName = (EditText) findViewById(R.id.edUserId);
        edPassword = (EditText) findViewById(R.id.edPsw);
        detector=new ConnectionDetector(LoginActivity.this);

    }

    public void btnLogin(View view) {

        uId = edUserName.getText().toString();
        psw = edPassword.getText().toString();
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
        if (detector.isConnectingToInternet()) {
            new LoginDetails().execute();
        } else
            Toast.makeText(LoginActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();
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
            Log.i(Constants.Response,s);
            dialog.dismiss();
            if (s.equals(Constants.SUCCESSFUL)){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                String query = "select uname from user_registration where email='bb' and password='12345';";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if(rs.next()) {
                    uniqueId=rs.getString("uname");
                    Log.i(Constants.Response,uniqueId);
                    z = Constants.SUCCESSFUL;
                    return z;
                } else {
                    z = "Invalid Credentials";
                }
            } catch (SQLException e) {
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

package com.suyogcomputech.sms;

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
import android.widget.EditText;
import android.widget.Toast;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Pintu on 7/15/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    EditText edPassword, edUserName;
    String uId, psw, name, emailId, uniqueId, type, available, result;
    ConnectionClass connectionClass;
    Login taskLogin;

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectionClass = new ConnectionClass();
        edUserName = (EditText) findViewById(R.id.edUserId);
        edPassword = (EditText) findViewById(R.id.edPsw);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (taskLogin != null && taskLogin.getStatus() != AsyncTask.Status.FINISHED) {
            taskLogin.cancel(true);
        }
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

        if (AppHelper.isConnectingToInternet(LoginActivity.this)) {
            taskLogin = new Login();
            taskLogin.execute();
        } else
            Toast.makeText(LoginActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
        //}
    }

    private class Login extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                Log.i(AppConstants.Response, String.valueOf(s));
                if (s) {
                    SharedPreferences shr = getSharedPreferences(AppConstants.USERPREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = shr.edit();
                    editor.putString(AppConstants.USERID, uniqueId);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (NullPointerException e) {
                Toast.makeText(LoginActivity.this, AppConstants.TRYAGAIN, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Connection con = connectionClass.connect();
                //select user_id from flat_user_Details where user_id='b' and password='b'
                String query = "select user_id from flat_user_Details where user_id='" + uId + "' and password='" + psw + "';";
                Log.i("query", query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    uniqueId = rs.getString(AppConstants.USERID);
                    Log.i(AppConstants.Response, uniqueId);
                    result = AppConstants.SUCCESSFUL;
                    con.close();
                    return true;
                } else {
                    con.close();
                    result = "Invalid Credentials";
                    return false;
                }
            } catch (Exception e) {
                return null;
            }

        }
    }
}

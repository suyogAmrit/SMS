package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import java.sql.Connection;
import java.sql.Statement;


public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    String name, email, mobile, password;
    EditText ed_name, ed_email, ed_mobile, ed_password;
    ConnectionDetector detector;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registaration);
        connectionClass=new ConnectionClass();
        initilizeUi();
    }
    private void initilizeUi() {
        detector = new ConnectionDetector(RegistrationActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbarReg);
        toolbar.setTitle("Registration");
        toolbar.setTitleTextColor(Color.parseColor(Constants.COLOR_WHITE));

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
//        if (name.equals("")) {
//            ed_name.setError("Please Enter Your Name");
//            ed_name.requestFocus();
//        } else if (!isValidEmail(email)) {
//            ed_email.setError("Enter valid Email Id");
//            ed_name.setError(null);
//            ed_email.requestFocus();
//        } else if (!isValidMobile(mobile)) {
//            ed_mobile.setError("Enter Mobile No.");
//            ed_mobile.requestFocus();
//            ed_email.setError(null);
//        } else if (password.equals("")) {
//            ed_password.setError("Enter Your Password");
//            ed_password.requestFocus();
//            ed_mobile.setError(null);
//        } else {
            if (detector.isConnectingToInternet()) {
                new LoginData().execute();
            } else
                Toast.makeText(RegistrationActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();
//        }
    }


    public class LoginData extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(RegistrationActivity.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(RegistrationActivity.this, r, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            if (r.equals(Constants.SUCCESSFUL)) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                String query = "insert into user_registration(uname,email,password,temp_cart) values('aa','aaa','12345','t')";
                Statement stmt = con.createStatement();
                int result=stmt.executeUpdate(query);
                if (result==1){
                    return Constants.SUCCESSFUL;
                }else {
                    return "Something went wrong";
                }

            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }

            return z;
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
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}



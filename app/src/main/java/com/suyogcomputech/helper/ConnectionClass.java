package com.suyogcomputech.helper;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Pintu on 8/17/2016.
 */
public class ConnectionClass {

//    IP – The IP address or server name of your PC or server where your database is stored.
//    CLASS_NAME – Class name.
//    DB – Database name.
//    UN – Username of the database (yours will be different).
//    PASSWORD – Password of the database.

    public Connection connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(AppConstants.CLASS_NAME);
            ConnURL = "jdbc:jtds:sqlserver://" + AppConstants.IP + ";"
                    + "databaseName=" + AppConstants.DB + ";user=" + AppConstants.UN + ";PASSWORD="
                    + AppConstants.PASSWORD + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
            return null;
        }
        return conn;
    }
}

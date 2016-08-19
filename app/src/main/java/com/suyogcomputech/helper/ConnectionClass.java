package com.suyogcomputech.helper;

import android.database.SQLException;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Pintu on 8/17/2016.
 */
public class ConnectionClass {
    public static final String ip = "192.168.12.100";
    public static final String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static final String db = "APMS";
    public static final String un = "sa";
    public static final String password = "sql2012";
//    ip – The IP address or server name of your PC or server where your database is stored.
//    classs – Class name.
//    db – Database name.
//    un – Username of the database (yours will be different).
//    password – Password of the database.

    public Connection connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}

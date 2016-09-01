package com.suyogcomputech.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by suyogcomputech on 22/08/16.
 */
public class AppHelper {
    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String updateDate(int day, int month, int year) {

        String date = new StringBuilder()
                .append(year).append("-").append(month + 1).append("-")
                .append(day).append(" ").toString();
        return date;
    }
    public static void getDatePicker(FragmentManager fragmentManager) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "date");
    }
}

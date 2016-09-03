package com.suyogcomputech.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public static Date getDateFromString(String string) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
            d = sdf.parse(string);
            return d;
        } catch (ParseException ex) {
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            ex.printStackTrace();
        }
        return d;
    }

    public static boolean compareDate(String fromDate, String toDate) {
        try {
            long currentTime = System.currentTimeMillis();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date fdt = dateFormat.parse(fromDate);
            Date tDt = dateFormat.parse(toDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime);
            Date curreDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
            int comparefrom = curreDate.compareTo(fdt);
            int compareTo = curreDate.compareTo(tDt);
            return (comparefrom == 0 || comparefrom == 1) && (compareTo == 0 || compareTo == -1);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}

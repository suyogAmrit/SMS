package com.suyogcomputech.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Amrit Ratha on 12/28/2015.
 */
public final class AppConstants {
    public static final String COLOR_WHITE = "#FFFFFF";
    public static final String TEST = "#FFFFFF";
    public static final String Request = "request";
    public static final String Response = "response";
    public static final String SUCCESSFUL= "Successful";
    public static final String EVENT_NAME= "event_name";


    public static final String INSURANCE_TYPE = "insurance_type";

    public static final String IMAGE = "image";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String FACILITIES = "facilities";

    public static final String TAG_TIME = "time";
    public static final String TAG_DATE = "date";

    public static final String dialog_title = "No Network Connection.";
    public static final String dialog_message = "Please connect to Internet to visit this page";
    public static final String progress_dialog_title = "Please Wait";
    public static final String progress_dialog_message = "Fetching Contact details ";
    public static final String null_pointer_message = "You have a slow network connectivity,please try again...";
    public static final String processed_report = "Your report is being processed..";

    public static final String NOT_AVAILABLE = "NotAvailable";


    public static final String USERID = "user_id";
    public static final String USERPREFS = "user_prefs";

    public static final String UNIQUE_ID = "uniqueId";
    public static final String AVAILABLE_DETAILS = "available_city";
    public static final String TYPE = "type";


    public static final String BASE_URL = "http://192.168.10.200/ananyaResort/android_response/";
    public static final String URL_GALLERY =BASE_URL+"gallery_image.php";
    public static final String URL_TARIFFS =BASE_URL+ "tariff_plan.php";
    public static final String URL_SHOW_TARIFFS = BASE_URL+"tariff_details.php";
    public static final String URL_REGISTRATION = BASE_URL+"registration.php";
    public static final String URL_LOGIN = BASE_URL+"login.php";
    public static final String URL_FACILITIES = BASE_URL+"facilities.php";
    public static final String URL_BOOK= BASE_URL+"booking.php";


    public static final String TRYAGAIN = "Please Try Again";
}

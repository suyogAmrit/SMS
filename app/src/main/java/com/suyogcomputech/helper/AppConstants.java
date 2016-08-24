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
    public static final String SUCCESSFUL = "Successful";
    public static final String QUERY = "query";
    public static final String STATUS = "status";


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
    public static final String URL_GALLERY = BASE_URL + "gallery_image.php";
    public static final String URL_TARIFFS = BASE_URL + "tariff_plan.php";
    public static final String URL_SHOW_TARIFFS = BASE_URL + "tariff_details.php";
    public static final String URL_REGISTRATION = BASE_URL + "registration.php";
    public static final String URL_LOGIN = BASE_URL + "login.php";
    public static final String URL_FACILITIES = BASE_URL + "facilities.php";
    public static final String URL_BOOK = BASE_URL + "booking.php";


    public static final String TRYAGAIN = "Please Try Again";

    //Event Description Table
    public static final String EVENT_ID = "Event_id";
    public static final String EVENT_TYPE_ID = "Event_Type_id";
    public static final String EVENT_MANAGER_USER_ID = "Event_Mng_user_id";
    public static final String EVENT_DESCRIPTION  = "Event_Desc";

    //Event Type Table
    public static final String EVENT_SERIAL_NO = "slno";
    public static final String EVENT_TYPE = "Event_Type";
    public static final String EVENT_TYPE_IMAGE = "Event_Type_image";


    //Event Request Table
    public static final String EVENT_REQ_SERIAL_NO = "sl_no";
    public static final String EVENT_REQ_APPT_ID = "appt_id";
    public static final String EVENT_REQ_FLAT_NO = "flat_no";
    public static final String EVENT_REQ_DATE = "Event_Request_date";
    public static final String EVENT_REQ_PROPOSE_DATE= "Event_Propose_date";
    public static final String EVENT_REQ_ESTIMATE_QUANTITY= "Estmated_Qty";
    public static final String EVENT_REQ_UNIT = "Unti";
    public static final String EVENT_REQ_PROPOSE_DESC = "Proposed_Desc";
    public static final String EVENT_REQ_ESTIMATED_BUDGET= "Estimated_Budget";
    public static final String EVENT_REQ_STATUS= "Event_Request_status";

//    em.Event_mng_name,em.Event_Mng_Org_Name,em.Event_Mng_org_address,em.Event_Mng_phno
//    ,em.Event_Mng_cell_phno,em.Event_Mng_email,ES.Event_Desc from dbo.Event_Manager_tb as em
public static final String EVENT_MANAGER_NAME = "Event_mng_name";
    public static final String EVENT_ORGANIZATION_NAME= "Event_Mng_Org_Name";
    //public static final String EVENT_DESCRIPTION = "Event_Desc";
    public static final String EVENT_ORGANIZATION_ADDRESS = "Event_Mng_org_address";
    public static final String EVENT_MANAGER_PHONE= "Event_Mng_phno";
    public static final String EVENT_MANAGER_MOBILE= "Event_Mng_cell_phno";
    public static final String EVENT_MANAGER_EMAIL = "Event_Mng_email";


    public static final String APARTMENT_ID= "appt_id";
    public static final String FLAT_NO= "flat_no";

    public static final String updateDate(int day, int month, int year) {

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

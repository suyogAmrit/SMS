package com.suyogcomputech.helper;

/**
 * Created by Amrit Ratha on 12/28/2015.
 */
public final class AppConstants {

    public static final String IP = "192.168.12.100";
    public static final String CLASS_NAME = "net.sourceforge.jtds.jdbc.Driver";
    public static final String DB = "APMS";
    public static final String UN = "sa";
    public static final String PASSWORD = "sql2012";

    public static final String COLOR_WHITE = "#FFFFFF";
    public static final String TEST = "#FFFFFF";
    public static final String Request = "request";
    public static final String Response = "response";
    public static final String SUCCESSFUL = "Successful";
    public static final String EVENT_NAME = "event_name";


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
    public static final String CATMSG = "Getting Category List";
    public static final String CATID = "cat_id";
    public static final String CATDESC = "cat_desc";
    public static final String SUBCATID = "sub_cat_id";
    public static final String SUBCATDESC = "sub_cat_desc";
    public static final String PRDMSG = "Fetching Products";
    public static final String PRDID = "prod_id";
    public static final String PRDTITLE = "prod_title";
    public static final String PRDDESC = "prod_description";
    public static final String SHRTDESC = "prod_short_description";
    public static final String SELLERID = "prod_Sel_id";
    public static final String STATUS = "prod_status";
    public static final String PRDBRAND = "prod_brand";
    public static final String PRDFRMDATE = "product_from_date";
    public static final String PRDTODATE = "product_to_date";
    public static final String PRDPRICE = "price";
    public static final String PRDALIASNAME = "alias_name";
    public static final String PRDMAINIMAGE = "images";
    public static final String PRDQUNATITY = "quantity";
    public static final String PRDMAXCARTQTY = "max_quantity_in_cart";
    public static final String PRDSHIPPINGFEE = "shipping_fee";
    public static final String PRDSERVICES = "prod_services";
    public static final String PRDFEATURES = "prod_features";
    public static final String PRDOTHERDETAILS = "other_details";
    public static final String RUPEESYM = "\u20B9";
    public static final String PRDDTLMSG = "Getting Product Details";
    public static final String OFRFROMDATE = "from_date";
    public static final String OFFERDESC = "offer_Desc";
    public static final String OFFRPER = "offer_per";
    public static final String IMAGE1 = "image1";
    public static final String IMAGE2 = "image2";
    public static final String IMAGE3 = "image3";
    public static final String SELLOR_NAME = "sellor_name";
    public static final String PRDIMAGESTATUS = "image_status";
    public static final String SIZE1 = "size1";
    public static final String SIZE2 = "size2";
    public static final String SIZE3 = "size3";
    public static final String SIZE4 = "size4";
    public static final String SIZE5 = "size5";
    public static final String SIZE6 = "size6";
    public static final String SIZEAVAILABLE1 = "size1_available";
    public static final String SIZEAVAILABLE2 = "size2_available";
    public static final String SIZEAVAILABLE3 = "size3_available";
    public static final String SIZEAVAILABLE4 = "size4_available";
    public static final String SIZEAVAILABLE5 = "size5_available";
    public static final String SIZEAVAILABLE6 = "size6_available";
    public static final String AVGRATING = "avg_rating";


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

    //Event Description Table
    public static final String EVENT_ID = "Event_id";
    public static final String EVENT_TYPE_ID = "Event_Type_id";
    public static final String EVENT_MANAGER_USER_ID = "Event_Mng_user_id";
    public static final String EVENT_DESCRIPTION  = "Event_Desc";

    //Event Type Table
    public static final String EVENT_SERIAL_NO = "slno";
    public static final String EVENT_TYPE = "Event_Type";
    public static final String EVENT_TYPE_IMAGE = "Event_Type_image";
    public static final String QUERY = "query";

    public static final String APARTMENT_ID= "appt_id";
    public static final String FLAT_NO= "flat_no";
    public static final String SPECIALIST_ID = "SpecialityID";
    public static final String SPECIALIST_NAME = "SpecialityName";
    public static final String SPECIALIST_IMAGE = "SpecialyImage";
    public static final String DOCTOR_ID = "DoctorID";
    public static final String LAWYER_ID = "LawyerID";
    public static final String DESIGNATION = "designation";


    public static final String INSURANCE_ID = "insurance_id";
    public static final String OFFERTODATE = "to_date";
    public static final String EXTRA_PROCUCT_DETAILS = "EXTRA_PROCUCT_DETAILS";


    public static final String INSURANCE_TYPE_ID = "insurance_type_Id";


    public static final String SORTPREFS = "product_sort_prefs";
    public static final String SORTDESC = "sort_desc";
    public static final String addingToCart="Adding to cart...";
    public static final String COUNTROW = "count_row";
    public static final String PROD_ID = "prod_id";
    public static final String SIZE = "size" ;
    public static final String QUANTITY = "Quantity";
}

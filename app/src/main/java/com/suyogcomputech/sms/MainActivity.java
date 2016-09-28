package com.suyogcomputech.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.ExpandableListAdapter;
import com.suyogcomputech.app_fragments.EShopCategoryFragment;
import com.suyogcomputech.app_fragments.GroceryFragment;
import com.suyogcomputech.app_fragments.MyAppointmentFragment;
import com.suyogcomputech.app_fragments.MyGroceryOrderFragment;
import com.suyogcomputech.app_fragments.MyOrderFragment;
import com.suyogcomputech.app_fragments.MyEventBookingFragment;
import com.suyogcomputech.app_fragments.MyReportsFragment;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ItemCounter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener{

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    ExpandableListView expListView;
    HashMap<String, List<String>> listDataChild;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;


    Fragment fragment;
    FragmentManager fragmentManager;
    String uniqueUserId;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    int badgeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkUserData()) {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.i("UserId",uniqueUserId);
            setContentView(R.layout.activity_main);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            toolbar = (Toolbar) findViewById(R.id.toolbarHome);
            toolbar.setTitle("e-Shopping");
            toolbar.setTitleTextColor(Color.WHITE);
            fragmentManager = getSupportFragmentManager();
            setSupportActionBar(toolbar);
            fragment = new EShopCategoryFragment();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

            expListView = (ExpandableListView) findViewById(R.id.lvExp);
            prepareListData();
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
            mDrawerLayout.addDrawerListener(drawerToggle);
            expListView.setOnChildClickListener(this);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (AppHelper.isConnectingToInternet(this)) {
//            new FetchbadgeNumber().execute();
//        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.item_samplebadge) {
//            if (badgeCount>0) {
//                Intent intent = new Intent(MainActivity.this, ShoppingCartItemActivity.class);
//                startActivity(intent);
//            }else {
//                AppHelper.showAlertDilog(MainActivity.this,"","You don't have any item in cart","Ok");
//            }
//           return true;
//        }
        if (item.getItemId()==R.id.item_search){
                Intent intent = new Intent(MainActivity.this, SearchProductActivity.class);
                startActivity(intent);
            }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("e-Shopping");
        listDataHeader.add("event management");
        listDataHeader.add("grocery");
        listDataHeader.add("e-Doctor");

        listDataHeader.add("e-Layer");
        listDataHeader.add("Insurance");

        // Adding child data
        List<String> e_shopping = new ArrayList<String>();
        e_shopping.add("Product List");
        e_shopping.add("My Order");

        List<String> event = new ArrayList<String>();
        event.add("List Of Event");
        event.add("My Booking");

        List<String> grocery = new ArrayList<String>();
        grocery.add("grocery list");
        grocery.add("my order");

        List<String> doctor = new ArrayList<String>();
        doctor.add("Doctor List");
        doctor.add("My appointment");
        doctor.add("My Reports");

        List<String> lawyer = new ArrayList<String>();
        lawyer.add("Lawyer List");
        lawyer.add("My appointment");

        List<String> insurance = new ArrayList<String>();
        insurance.add("Insurance List");
        insurance.add("Status");

        listDataChild.put(listDataHeader.get(0), e_shopping); // Header, Child data
        listDataChild.put(listDataHeader.get(1), event);
        listDataChild.put(listDataHeader.get(2), grocery);
        listDataChild.put(listDataHeader.get(3), doctor);
        listDataChild.put(listDataHeader.get(4), lawyer);
        listDataChild.put(listDataHeader.get(5), insurance);
    }




    private boolean checkUserData() {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        if (uniqueUserId.equals(AppConstants.NOT_AVAILABLE)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        switch (groupPosition) {
            case 0:
                switch (childPosition) {
                    case 0:
                        fragment = new EShopCategoryFragment();
                        toolbar.setTitle("e-Shopping");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        fragment = new MyOrderFragment();
                        toolbar.setTitle("My Order");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                break;

            case 1:
                switch (childPosition) {
                    case 0:
                        Intent intent=new Intent(MainActivity.this,EventListActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        fragment = new MyEventBookingFragment();
                        toolbar.setTitle("My Booking");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                break;

            case 2:
                switch (childPosition) {
                    case 0:
                        fragment = new GroceryFragment();
                        toolbar.setTitle("Grocery");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        fragment = new MyGroceryOrderFragment();
                        toolbar.setTitle("My Order");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                break;
            case 3:
                switch (childPosition) {
                    case 0:
                        Intent intent=new Intent(MainActivity.this,SpecialistActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        fragment = new MyAppointmentFragment();
                        toolbar.setTitle("My Appointment");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        fragment = new MyReportsFragment();
                        toolbar.setTitle("My Reports");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                break;
            case 4:
                switch (childPosition) {
                    case 0:
                        Intent intent=new Intent(MainActivity.this,SpecialistLawerActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "My Appointment", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                break;
            case 5:
                switch (childPosition) {
                    case 0:
                        Intent intent=new Intent(MainActivity.this,InsuranceActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Status", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
        try {
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerLayout.closeDrawer(expListView);
        return false;
    }
    private class FetchbadgeNumber extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "SELECT COUNT(*) as count_row FROM Eshop_cart_tb where " + AppConstants.USERID + "='" + findUserId() + "'";
                Log.i("Query", query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                String count = resultSet.getString("count_row");
                Log.v("count", count);
                badgeCount = Integer.valueOf(count);
            } catch (SQLException e) {
                Log.i("Except", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            invalidateOptionsMenu();
        }
    }
    private String findUserId(){
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }
}



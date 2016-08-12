package com.suyogcomputech.sms;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.ExpandableListAdapter;
import com.suyogcomputech.fragment.DoctorListFragment;
import com.suyogcomputech.fragment.GroceryFragment;
import com.suyogcomputech.fragment.InsuranceListFragment;
import com.suyogcomputech.fragment.LawyerListFragment;
import com.suyogcomputech.fragment.ListEventFragment;
import com.suyogcomputech.fragment.MyOrderFragment;
import com.suyogcomputech.fragment.ShoppingFragment;
import com.suyogcomputech.helper.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    ExpandableListView expListView;
    HashMap<String, List<String>> listDataChild;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;

    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;
    ConnectionDetector detector;

    Fragment fragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        toolbar.setTitle("e-Shopping");
        toolbar.setTitleTextColor(Color.WHITE);
        fragmentManager = getSupportFragmentManager();
        setSupportActionBar(toolbar);
        fragment = new ShoppingFragment();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                fragment = new ShoppingFragment();
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
                                fragment = new ListEventFragment();
                                toolbar.setTitle("Event Management");
                                toolbar.setTitleTextColor(Color.WHITE);
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case 1:
                                Toast.makeText(MainActivity.this, "My Booking", Toast.LENGTH_SHORT).show();
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
                                fragment = new MyOrderFragment();
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
                                fragment = new DoctorListFragment();
                                toolbar.setTitle("Doctor");
                                toolbar.setTitleTextColor(Color.WHITE);
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
                    case 4:
                        switch (childPosition) {
                            case 0:
                                fragment = new LawyerListFragment();
                                toolbar.setTitle("Lawyer");
                                toolbar.setTitleTextColor(Color.WHITE);
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
                                fragment = new InsuranceListFragment();
                                toolbar.setTitle("Insurance");
                                toolbar.setTitleTextColor(Color.WHITE);
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
        });

        detector = new ConnectionDetector(MainActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_samplebadge) {
                Intent i = new Intent(MainActivity.this, ShoppingCartItem.class);
                startActivity(i);
        }
        return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);

        return true;
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


    public void addItemToCart(String name) {
        badgeCount++;
        invalidateOptionsMenu();
    }

}



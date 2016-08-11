package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.ExpandableListAdapter;
import com.suyogcomputech.adapter.OnlineShoppingAdapter;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import com.suyogcomputech.helper.OnlineShopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
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
    RecyclerView rcvFacilities;
    OnlineShoppingAdapter adapter;
    ArrayList<OnlineShopping> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        toolbar.setTitle("e-Shopping");
        setSupportActionBar(toolbar);
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
                                Toast.makeText(MainActivity.this, "Product List", Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case 1:
                                Intent intentMyOrder=new Intent(MainActivity.this,MyOrderActivity.class);
                                startActivity(intentMyOrder);
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;

                            default:
                                break;
                        }
                        break;

                    case 1:
                        switch (childPosition) {
                            case 0:
                                Toast.makeText(MainActivity.this, "List Of Event", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Grocery List", Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case 1:
                                Toast.makeText(MainActivity.this, "My Order", Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;

                            default:
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition) {
                            case 0:
                                Intent intentDoc=new Intent(MainActivity.this,DoctorListActivity.class);
                                startActivity(intentDoc);
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
                                Intent intentDoc=new Intent(MainActivity.this,LawyerListActivity.class);
                                startActivity(intentDoc);
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
                                Intent intentDoc=new Intent(MainActivity.this,InsuranceListActivity.class);
                                startActivity(intentDoc);
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
                mDrawerLayout.closeDrawer(expListView);
                return false;
            }
        });

        detector=new ConnectionDetector(MainActivity.this);
        rcvFacilities = (RecyclerView) findViewById(R.id.rcvShopping);
        if (detector.isConnectingToInternet()) {
            new FetchFacilities().execute(Constants.URL_FACILITIES);
        } else
            Toast.makeText(MainActivity.this, Constants.dialog_message, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    private class FetchFacilities extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // urlConnection.setConnectTimeout(30000);
                urlConnection.setDoInput(true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.progress_dialog_message);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                dialog.dismiss();
                Log.i("response", s);
                JSONObject objJson = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(objJson.getString(Constants.FACILITIES));
                list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    OnlineShopping facilities = new OnlineShopping();
                    facilities.setTitle(jsonObject.getString(Constants.TITLE));
                    facilities.setImageUrl(jsonObject.getString(Constants.IMAGE));
                    list.add(facilities);
                }

                adapter = new OnlineShoppingAdapter(list, MainActivity.this);
                rcvFacilities.setAdapter(adapter);
                rcvFacilities.setHasFixedSize(true);
                GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 3, GridLayoutManager.VERTICAL, false);
                rcvFacilities.setLayoutManager(glm);

            } catch (NullPointerException e) {
                Toast.makeText(MainActivity.this, Constants.null_pointer_message, Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException ex) {
            }
        }
    }
    public void addItemToCart(String name) {
        badgeCount++;
        invalidateOptionsMenu();
    }

}



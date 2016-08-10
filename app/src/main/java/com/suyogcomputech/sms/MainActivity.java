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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    ArrayList<String> dataList;
    private ListView mDrawerList;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = new ArrayList<String>();
        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawerHome);
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        toolbar.setTitle("Online Shopping");
        dataList.add("Online Shopping");
        dataList.add("Insurance");
        dataList.add("Doctor");
        dataList.add("Legal Service");
        dataList.add("Grocery");
        dataList.add("Event Management");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dataList);
        mDrawerList.setAdapter(adapter);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, OnlineShoppingActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        Intent intentImpContact = new Intent(MainActivity.this, Test.class);
                        startActivity(intentImpContact);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 2:
//                        Intent intentPoliceStation = new Intent(MainActivity.this, PoliceStationActivity.class);
//                        startActivity(intentPoliceStation);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
//                        Intent intentSubmitTips = new Intent(MainActivity.this, SubmitTipsActivity.class);
//                        startActivity(intentSubmitTips);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        toolbar.setTitle("");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });

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
}



package com.suyogcomputech.sms;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.fragment.AutomotiveFragment;
import com.suyogcomputech.fragment.BooksFragment;
import com.suyogcomputech.fragment.ElectronicsFragment;
import com.suyogcomputech.fragment.HomeFragment;
import com.suyogcomputech.fragment.HomeFurnitureFragment;
import com.suyogcomputech.fragment.LifeStyleFragment;

import java.util.ArrayList;

public class OnlineShoppingActivity extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    ArrayList<String> dataList;
    private ListView mDrawerList;

    Fragment fragment;
    Class fragmentClass;
    FragmentManager fragmentManager;

    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_shopping);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawerHome);
        toolbar = (Toolbar) findViewById(R.id.toolbarOnlineShopping);
        toolbar.setTitle("Online Shopping");
        fragmentManager = getSupportFragmentManager();
        fragment = null;
        fragmentClass = null;
        fragmentClass = HomeFragment.class;
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataList = new ArrayList<>();
        dataList.add("Home");
        dataList.add("Electronics");
        dataList.add("Lifestyle");
        dataList.add("Home And Furniture");
        dataList.add("Books And More");
        dataList.add("Automotive");

        dataList.add("My Cart");
        dataList.add("My Order");
        dataList.add("My Account");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OnlineShoppingActivity.this, android.R.layout.simple_list_item_1, dataList);


        mDrawerList.setAdapter(adapter);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        toolbar.setTitle("Home");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        fragment = new ElectronicsFragment();
                        toolbar.setTitle("Electronic");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 2:
                        fragment = new LifeStyleFragment();
                        toolbar.setTitle("LifeStyle");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 3:
                        fragment = new HomeFurnitureFragment();
                        toolbar.setTitle("Home And Furniture");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        fragment = new BooksFragment();
                        toolbar.setTitle("Books And More");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        fragment = new AutomotiveFragment();
                        toolbar.setTitle("Automotive");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 6:
                        Intent intentMyCart = new Intent(OnlineShoppingActivity.this, MyCartActivity.class);
                        startActivity(intentMyCart);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 7:
                        Intent intentMyOrder = new Intent(OnlineShoppingActivity.this, MyOrderActivity.class);
                        startActivity(intentMyOrder);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 8:
                        Intent intentAccount = new Intent(OnlineShoppingActivity.this, MyAccountActivity.class);
                        startActivity(intentAccount);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    default:
                        fragment = new HomeFragment();
                        toolbar.setTitle("Home Service");
                        toolbar.setTitleTextColor(Color.WHITE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);

        return true;
    }
    public void addItemToCart(String name) {
        badgeCount++;
        invalidateOptionsMenu();
    }

}

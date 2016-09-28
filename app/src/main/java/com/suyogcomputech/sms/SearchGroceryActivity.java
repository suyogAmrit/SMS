package com.suyogcomputech.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.SearchGroceryAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.SearchGroceryItems;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/28/2016.
 */
public class SearchGroceryActivity  extends AppCompatActivity{
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;
    ArrayList<SearchGroceryItems> arrayGrocery;
    SearchGroceryAdapter searchGroceryAdapter;
    RecyclerView rv_search;
    String uniqueUserId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_grocery);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_product);
        rv_search=(RecyclerView)findViewById(R.id.rv_search);
        setSupportActionBar(toolbar);
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        new CartCount().execute();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_menu_search,menu);
        ActionItemBadge.update(SearchGroceryActivity.this, menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.item_search));
        searchView.setQueryHint("Search...");
        EditText editText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setHintTextColor(getResources().getColor(R.color.mygrey));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (AppHelper.isConnectingToInternet(SearchGroceryActivity.this)) {
                    if (newText.length()>=3) {
                        new SearchGrocery().execute(newText);
//                        Toast.makeText(SearchGroceryActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SearchGroceryActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.addcart) {
            if(badgeCount!=0)
            {
                Intent intent = new Intent(SearchGroceryActivity.this,GroceryCartActivity.class);
                //intent.putExtra(AppConstants.PROD_ID,productDetails.getId());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(SearchGroceryActivity.this, "Cart empty please add item to cart", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
//        if(item.getItemId()==R.id.item_search)
//        {
//            Intent intent = new Intent(SearchGroceryActivity.this,SearchGroceryActivity.class);
//            startActivity(intent);
//        }
        return false;
    }



    private class SearchGrocery extends AsyncTask<String,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);
            try {
                arrayGrocery=new ArrayList<>();
                while(resultSet.next())
                {
                    SearchGroceryItems searchGroceryItems= new SearchGroceryItems();

                    searchGroceryItems.setCatId(resultSet.getString("prod_id"));
                    searchGroceryItems.setProdTitle(resultSet.getString("prod_title"));
                    searchGroceryItems.setProdBrand(resultSet.getString("prod_brand"));

                    arrayGrocery.add(searchGroceryItems);
                }
                searchGroceryAdapter =new SearchGroceryAdapter(SearchGroceryActivity.this,arrayGrocery);
                rv_search.setAdapter(searchGroceryAdapter);

                GridLayoutManager glm=new GridLayoutManager(SearchGroceryActivity.this,1,GridLayoutManager.VERTICAL,false);
                rv_search.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... strings) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "select prod_id,prod_title,prod_brand from Grocery_Prod_table where prod_title like '%"+strings[0]+"%' or prod_brand like '%"+strings[0]+"%'";
                Statement statement = null;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CartCount extends AsyncTask<Void,Void,Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            badgeCount=integer;
            invalidateOptionsMenu();

        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String cartQuery="select count(*) as cart_count from Grocery_cart_table where user_id='"+uniqueUserId+"' and status=1";
                Statement statement = null;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(cartQuery);

                resultSet.next();
//                Log.i("ssss",cartQuery);
//                Log.i("ssss",String.valueOf(resultSet.getInt("cart_count")));
                return(resultSet.getInt("cart_count"));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

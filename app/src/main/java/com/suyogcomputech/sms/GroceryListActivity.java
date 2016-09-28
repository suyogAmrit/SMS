package com.suyogcomputech.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.GroceryListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.GroceryProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Suyog on 9/7/2016.
 */
public class GroceryListActivity extends AppCompatActivity {
    String category,subCategory,subDesc;
    Toolbar tbProduct;
    RecyclerView rvPdroducts;
    GroceryListAdapter adapter;
    String uniqueUserId;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;

    private ArrayList<GroceryProductDetails> productDetailList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grocery_list);

        category = getIntent().getExtras().getString(AppConstants.CATID);
        subCategory = getIntent().getExtras().getString(AppConstants.SUBCATID);
        subDesc = getIntent().getExtras().getString(AppConstants.SUBCATDESC);
        tbProduct = (Toolbar) findViewById(R.id.tb_product);

        TextView tvProdct = (TextView) findViewById(R.id.toolbar_title);
        tvProdct.setText(subDesc);
        setSupportActionBar(tbProduct);
        tbProduct.setTitle(subDesc);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productDetailList = new ArrayList<>();
        rvPdroducts = (RecyclerView) findViewById(R.id.rv_product);

        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);


        new GroceryList().execute();
        new CartCount().execute();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.grocery_menu_cart, menu);
        ActionItemBadge.update(GroceryListActivity.this, menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
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
                Intent intent = new Intent(GroceryListActivity.this,GroceryCartActivity.class);
                //intent.putExtra(AppConstants.PROD_ID,productDetails.getId());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(GroceryListActivity.this, "Cart empty please add item to cart", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }





    private class GroceryList extends AsyncTask<String,Void,ResultSet> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try {
                while(resultSet.next())
                {
                    GroceryProductDetails groceryProductDetails=new GroceryProductDetails();
                    groceryProductDetails.setProduct_id(resultSet.getString("prod_id"));
                    groceryProductDetails.setProd_title(resultSet.getString("prod_title"));
                    groceryProductDetails.setProd_short_description(resultSet.getString("prod_short_description"));
                    groceryProductDetails.setPrice(resultSet.getString("price"));
                    groceryProductDetails.setImages_path(resultSet.getString("images_path"));

                    productDetailList.add(groceryProductDetails);
                }
                if(productDetailList.size()>0)
                {
                    adapter=new GroceryListAdapter(GroceryListActivity.this);
                    adapter.setAdpData(productDetailList);
                    rvPdroducts.setAdapter(adapter);

                    GridLayoutManager gglm=new GridLayoutManager(GroceryListActivity.this,2,GridLayoutManager.VERTICAL,false);
                    rvPdroducts.setLayoutManager(gglm);


                }
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("errore",e.getMessage());
            }
        }

        @Override
        protected ResultSet doInBackground(String... params) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "select prod_id,prod_title,prod_short_description, images_path, price from Grocery_Prod_table where sub_cat_id='" + subCategory + "' and cat_id='" + category + "'";
                Statement statement = null;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
//                connection.close();
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
                Log.i("ssss",cartQuery);
                Log.i("ssss",String.valueOf(resultSet.getInt("cart_count")));
                return(resultSet.getInt("cart_count"));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

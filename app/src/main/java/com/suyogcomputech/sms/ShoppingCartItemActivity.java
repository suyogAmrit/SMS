package com.suyogcomputech.sms;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.suyogcomputech.adapter.CartItemAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 7/27/2016.
 */
public class ShoppingCartItemActivity extends AppCompatActivity{
    Toolbar toolbar;
    private CartItemAdapter cartItemAdapter;
    private RecyclerView rcvMyOrderRecycler;
    String productId;
    ProductDetails productDetails;
    ArrayList<ProductDetails> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        toolbar.setTitle("My Cart");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //productId=getIntent().getExtras().getString(AppConstants.PROD_ID);
            new FetchCartItems().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return false;
    }

    private class FetchCartItems extends AsyncTask<Void,Void,ResultSet>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "select p.prod_title,p.images,p.prod_brand,p.price,e.size,e.Quantity,o.from_date,\n" +
                        "o.to_date,o.offer_per from Eshop_Prod_table as p\n" +
                        "inner join Eshop_cart_tb as e on(p.prod_id=e.prod_id)\n" +
                        "inner join Eshop_Offer_tb as o on(p.prod_id=o.prod_id)\n" +
                        " and e.Status=1 and e.user_id='"+findUserId()+"'";
                Log.v("Query",query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);
            try {
                list=new ArrayList<>();
                while (rs.next()){
                    ProductDetails details=new ProductDetails();
                    details.setTitle(rs.getString("prod_title"));
                    details.setBrand(rs.getString("prod_brand"));
                    details.setPrice(rs.getString("price"));
                    details.setSizeProduct(rs.getString("size"));
                    details.setOfferPer(rs.getString("offer_per"));
                    details.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + rs.getString(AppConstants.PRDMAINIMAGE).replace("~", "").replace(" ", "%20"));
                    details.setQuantity(rs.getString("Quantity"));
                    list.add(details);
                }
                rcvMyOrderRecycler = (RecyclerView)findViewById(R.id.rcvMyOrder);
                cartItemAdapter = new CartItemAdapter(ShoppingCartItemActivity.this,list);
                rcvMyOrderRecycler.setHasFixedSize(true);
                LinearLayoutManager glm = new LinearLayoutManager(ShoppingCartItemActivity.this);
                rcvMyOrderRecycler.setLayoutManager(glm);
                rcvMyOrderRecycler.setAdapter(cartItemAdapter);

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
    public String findUserId()
    {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }
}

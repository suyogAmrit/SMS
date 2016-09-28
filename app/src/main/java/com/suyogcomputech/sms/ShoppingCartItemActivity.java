package com.suyogcomputech.sms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.adapter.CartItemAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pintu on 7/27/2016.
 */
public class ShoppingCartItemActivity extends AppCompatActivity{
    Toolbar toolbar;
    private CartItemAdapter cartItemAdapter;
    private RecyclerView rcvMyOrderRecycler;
    ArrayList<ProductDetails> list;
    CardView toatlBillCardLayout;
    public static TextView txtCartTotal,txtDiscountTotal,txtTotalPayble;
    ProgressDialog progressDialog;
    int slNo;
    String orderId;
    public static final  int REQUEST_CODE = 325;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Cart");
        toatlBillCardLayout = (CardView) findViewById(R.id.toatlBillCardLayout);
        txtCartTotal = (TextView)findViewById(R.id.txtCartTotal);
        txtDiscountTotal = (TextView)findViewById(R.id.txtDiscountTotal);
        txtTotalPayble = (TextView)findViewById(R.id.txtTotalPayble);
        progressDialog = new ProgressDialog(this);
        //productId=getIntent().getExtras().getString(AppConstants.PROD_ID);
        if (AppHelper.isConnectingToInternet(ShoppingCartItemActivity.this)) {
            new FetchCartItems().execute();
        }else {
            Toast.makeText(ShoppingCartItemActivity.this,"No internet Connection",Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        new FetchCartItems().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    public void deleteItem(final String serielNo) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Removing...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ConnectionClass connectionClass = new ConnectionClass();
                    Connection connection = connectionClass.connect();
                    Statement statement = connection.createStatement();
                    String query = "Delete from Eshop_cart_tb where slno="+ Integer.parseInt(serielNo);
                    Boolean resultSet = statement.execute(query);
                    Log.i("Result",String.valueOf(resultSet));
                }catch (SQLException e){
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                new FetchCartItems().execute();

            }
        }.execute();

    }

    public void placeOrder(View view) {
        Intent intent=new Intent(ShoppingCartItemActivity.this,OrderItemActivity.class);
        startActivity(intent);
    }

    private class FetchCartItems extends AsyncTask<Void,Void,ResultSet>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Fetching cart items");
            progressDialog.show();
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                /*String query = "select p.prod_id,e.slno,p.prod_title,p.images,p.prod_brand,p.price,e.size,e.Quantity,o.from_date,\n" +
                        "o.to_date,o.offer_per from Eshop_Prod_table as p\n" +
                        "inner join Eshop_cart_tb as e on(p.prod_id=e.prod_id)\n" +
                        "inner join Eshop_Offer_tb as o on(p.prod_id=o.prod_id)\n" +
                        " and e.Status=1 and e.user_id='"+findUserId()+"'";*/
                String query = "select p.prod_id,e.slno,p.prod_title,p.images,p.prod_brand,p.price,e.size,e.Quantity from Eshop_Prod_table as p\n" +
                        "inner join Eshop_cart_tb as e on(p.prod_id=e.prod_id)\n" +
                        "and e.Status=1 and e.user_id='"+findUserId()+"'";

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
            progressDialog.dismiss();
            try {
                list=new ArrayList<>();
                while (rs.next()){
                    ProductDetails details=new ProductDetails();
                    details.setTitle(rs.getString("prod_title"));
                    details.setBrand(rs.getString("prod_brand"));
                    details.setPrice(rs.getString("price"));
                    details.setSizeProduct(rs.getString("size"));
                    details.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + rs.getString(AppConstants.PRDMAINIMAGE).replace("~", "").replace(" ", "%20"));
                    details.setQuantity(rs.getString("Quantity"));
                    details.setSerielNo(rs.getString("slno"));
                    details.setId(rs.getString("prod_id"));
                    list.add(details);
                }
                rcvMyOrderRecycler = (RecyclerView)findViewById(R.id.rcvMyOrder);
                cartItemAdapter = new CartItemAdapter(ShoppingCartItemActivity.this,list);
                rcvMyOrderRecycler.setHasFixedSize(true);
                rcvMyOrderRecycler.setAdapter(cartItemAdapter);
                LinearLayoutManager glm = new LinearLayoutManager(ShoppingCartItemActivity.this);
                rcvMyOrderRecycler.setLayoutManager(glm);
                cartItemAdapter.totalPrice();
                cartItemAdapter.totalDis();
                if (list==null && list.size()==0){
                    Toast.makeText(ShoppingCartItemActivity.this,"No Orders",Toast.LENGTH_SHORT).show();
                }
                //cartItemAdapter.totalDis();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("ResultCode", "123");
                new FetchCartItems().execute();
                cartItemAdapter.notifyDataSetChanged();
            }
        }
    }


}

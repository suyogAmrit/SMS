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
    public static TextView txtCartTotal;
    public TextView txtDiscountTotal,txtSubTotal,txtTotalPayble;
    ProgressDialog progressDialog;
    int slNo;
    String orderId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
       setUpToolBar();
        toatlBillCardLayout = (CardView) findViewById(R.id.toatlBillCardLayout);
        txtCartTotal = (TextView)findViewById(R.id.txtCartTotal);
        txtDiscountTotal = (TextView)findViewById(R.id.txtDiscountTotal);
        txtSubTotal = (TextView)findViewById(R.id.txtSubTotal);
        txtTotalPayble = (TextView)findViewById(R.id.txtTotalPayble);
        progressDialog = new ProgressDialog(this);
        //productId=getIntent().getExtras().getString(AppConstants.PROD_ID);
            new FetchCartItems().execute();
    }
    private void setUpToolBar() {

        toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        toolbar.setTitle("My Cart");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void placeOrder() {
        if (AppHelper.isConnectingToInternet(ShoppingCartItemActivity.this)) {

        } else
            Toast.makeText(ShoppingCartItemActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
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
                String query = "select p.prod_id,e.slno,p.prod_title,p.images,p.prod_brand,p.price,e.size,e.Quantity,o.from_date,\n" +
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
            progressDialog.dismiss();
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
                    details.setFromDate(rs.getString("from_date"));
                    details.setToDate(rs.getString("to_date"));
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
                totalPriceCount();
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
    public void totalPriceCount(){
        toatlBillCardLayout.setVisibility(View.GONE);
        txtTotalPayble.setText(txtCartTotal.getText().toString());
        //txtSubTotal.setText(AppConstants.RUPEESYM+(Double.valueOf(txtCartTotal.getText().toString())-Double.valueOf(txtDiscountTotal.getText().toString())));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("ResultCode", "123");
                new FetchCartItems().execute();
            }
        }
    }


}

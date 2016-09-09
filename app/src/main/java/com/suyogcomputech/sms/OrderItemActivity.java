package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.adapter.OrderItemAdapter;
import com.suyogcomputech.adapter.SpecialistAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Suyog on 9/9/2016.
 */
public class OrderItemActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcvOrder;
    private OrderItemAdapter orderAdapter;
    ArrayList<ProductDetails> list;
    ConnectionClass connectionClass;
    String ownerName,address,phoneNo,appertment_id;
    ProgressDialog progressDialog;
    private TextView txtName,txtPhone,txtAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        connectionClass=new ConnectionClass();
        defineComponents();
        findUserDetails();
    }
    private void defineComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbarOrder);
        toolbar.setTitle("Order");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtName = (TextView)findViewById(R.id.txtName);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtPhone = (TextView)findViewById(R.id.txtPhone);
    }

    private void findUserDetails() {
        if (AppHelper.isConnectingToInternet(OrderItemActivity.this)) {
            new Specialist().execute();

        } else
            Toast.makeText(OrderItemActivity.this, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
    }

    public void confirmOrder(View view) {
            new ConfirmOrderTask().execute();
    }
    private class ConfirmOrderTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OrderItemActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.placingOrder);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "insert into Eshop_Order_tb (s_fname,s_address1,s_ph1,uniqueId,) Values ('"+ownerName+"','"+address+"','+"+phoneNo+"'"+createUniqueUserId()+"')";
                PreparedStatement statement = connection.prepareStatement(query);
                long resSet = statement.executeUpdate();
                Log.i("Result", String.valueOf(resSet));
                if (resSet>0){
                    connection.close();
                    return true;
                }else {
                    connection.close();
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isExecuted) {
            super.onPostExecute(isExecuted);
            if (isExecuted) {
                Toast.makeText(OrderItemActivity.this, "Item Placed successfully", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            new  DeleteCarttask().execute();
        }
    }
        private class DeleteCarttask extends AsyncTask<Void,Void,Void>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ConnectionClass connectionClass = new ConnectionClass();
                    Connection connection = connectionClass.connect();
                    Statement statement = connection.createStatement();
                    String query = "Delete Eshop_cart_tb where user_id='"+findUserId()+"'";
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

    private class Specialist extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OrderItemActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.processed_report);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String rs) {
            super.onPostExecute(rs);
            dialog.dismiss();
            Log.i("Owner Name",ownerName);
            txtName.setText(ownerName);
            txtAddress.setText(address);
            txtPhone.setText(phoneNo);
            new FetchCartItems().execute();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.connect();
                String query = "select a.appt_add,u.appt_id,u.flat_owner,u.extension_no,u.user_id from flat_user_Details u\n" +
                        "inner join appartment a on(u.appt_id=a.appt_id)\n" +
                        "where u.user_id='"+findUserId()+"'";
                Log.i(AppConstants.QUERY, query);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    ownerName = rs.getString("flat_owner");
                    phoneNo = rs.getString("extension_no");
                    address = rs.getString("appt_add");
                    appertment_id = rs.getString("appt_id");

                }
                return ownerName;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    public String findUserId()
    {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }
    public String createUniqueUserId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime+findUserId();
    }
    private class FetchCartItems extends AsyncTask<Void,Void,ResultSet>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrderItemActivity.this);
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
                rcvOrder = (RecyclerView)findViewById(R.id.rcvOrder);
                orderAdapter = new OrderItemAdapter(OrderItemActivity.this,list);
                rcvOrder.setHasFixedSize(true);
                rcvOrder.setAdapter(orderAdapter);
                LinearLayoutManager glm = new LinearLayoutManager(OrderItemActivity.this);
                rcvOrder.setLayoutManager(glm);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}
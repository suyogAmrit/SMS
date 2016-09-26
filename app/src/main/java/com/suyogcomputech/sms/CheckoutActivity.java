package com.suyogcomputech.sms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suyogcomputech.adapter.CheckOutGroceryAdapter;
import com.suyogcomputech.adapter.GroceryCartAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.GroceryCartList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/26/2016.
 */
public class CheckoutActivity extends AppCompatActivity {
    String uniqueUserId;
    Toolbar toolbar;
    CheckOutGroceryAdapter checkoutAdapter;
    ArrayList<GroceryCartList> cartArrayList;
    RecyclerView rvCartItems;
    TextView tv_grand_total,tv_shipping_fee,tv_grand_total_price,tv_name,tv_appt_name,tv_phone_number,tv_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grocery_place_order);
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);


        tv_grand_total=(TextView)findViewById(R.id.total_price);
        tv_shipping_fee=(TextView)findViewById(R.id.shipping_fee);
        tv_grand_total_price=(TextView)findViewById(R.id.grand_total_price);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_appt_name=(TextView)findViewById(R.id.tv_appt_name);
        tv_phone_number=(TextView)findViewById(R.id.tv_phone_number);
        tv_address=(TextView)findViewById(R.id.tv_address);


        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        rvCartItems=(RecyclerView)findViewById(R.id.rv_cart_item);
        toolbar.setTitle("Checkout");
        setSupportActionBar(toolbar);
        new GetCartItemCheckout().execute();
        new GetAddress().execute();

    }

    public void placeOrder(View view) {

    }


    private class GetCartItemCheckout extends AsyncTask<Void,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try {
                cartArrayList=new ArrayList<>();
                float total,shipping_fee;
                total=0;
                shipping_fee=0;
                while(resultSet.next())
                {
                    GroceryCartList groceryCartList=new GroceryCartList();
                    groceryCartList.setProdId(resultSet.getString("prod_id"));
                    groceryCartList.setQuantity(resultSet.getString("quantity"));
                    groceryCartList.setProdTitle(resultSet.getString("prod_title"));
                    groceryCartList.setImageUrl(resultSet.getString("images_path"));
                    groceryCartList.setPrice(resultSet.getString("price"));
                    groceryCartList.setShippingFee(resultSet.getString("shipping_fee"));
                    groceryCartList.setMaxQuantity(resultSet.getString("quantityM"));

                    cartArrayList.add(groceryCartList);
                    Log.i("image_url",resultSet.getString("images_path"));

                    total=total+(Float.valueOf(resultSet.getString("price"))*Float.valueOf(resultSet.getString("quantity")));
                    shipping_fee=shipping_fee+Float.valueOf(resultSet.getString("shipping_fee"));
                }
                tv_grand_total.setText(String.valueOf(total));
                tv_shipping_fee.setText(String.valueOf(shipping_fee));
                tv_grand_total_price.setText(String.valueOf(total+shipping_fee));
                Log.i("ssss",String.valueOf(total));

                checkoutAdapter= new CheckOutGroceryAdapter(CheckoutActivity.this,cartArrayList);
                rvCartItems.setAdapter(checkoutAdapter);

                GridLayoutManager gglm=new GridLayoutManager(CheckoutActivity.this,1,GridLayoutManager.VERTICAL,false);
                rvCartItems.setLayoutManager(gglm);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("error_msg",e.getMessage());
            }
        }

        @Override
        protected ResultSet doInBackground(Void... voids) {
            try {
                ConnectionClass connectionClass=new ConnectionClass();
                Connection connection=connectionClass.connect();
                String select_ids_query="select gct.quantity,gpt.prod_id,gpt.prod_title,gpt.price,gpt.shipping_fee,gpt.images_path,gpt.quantity as quantityM from Grocery_cart_table as gct inner join Grocery_Prod_table as gpt on gct.prod_id=gpt.prod_id where gct.user_id='"+uniqueUserId+"' and status='1'";
                Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(select_ids_query);
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }




    private class GetAddress extends AsyncTask<Void,Void,ResultSet> {

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
                    tv_name.setText(resultSet.getString("flat_owner"));
                    tv_appt_name.setText(resultSet.getString("appt_name"));
                    tv_phone_number.setText(resultSet.getString("extension_no"));
                    tv_address.setText(resultSet.getString("appt_add"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(Void... voids) {

            try {
                ConnectionClass connectionClass=new ConnectionClass();
                Connection connection=connectionClass.connect();
                String select_ids_query="select f.flat_no,f.flat_owner,f.extension_no,a.appt_name,a.appt_add from flat_user_Details as f inner join appartment as a on f.appt_id=a.appt_id where f.user_id='"+uniqueUserId+"'";
                Statement statement= null;
                statement = connection.createStatement();
                ResultSet resultSet=statement.executeQuery(select_ids_query);
                return resultSet;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

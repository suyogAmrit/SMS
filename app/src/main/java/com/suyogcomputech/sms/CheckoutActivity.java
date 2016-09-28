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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.adapter.CheckOutGroceryAdapter;
import com.suyogcomputech.adapter.GroceryCartAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.GroceryCartList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.id;

/**
 * Created by Pintu on 9/26/2016.
 */
public class CheckoutActivity extends AppCompatActivity {
    String uniqueUserId;
    Toolbar toolbar;
    CheckOutGroceryAdapter checkoutAdapter;
    ArrayList<GroceryCartList> cartArrayList;
    RecyclerView rvCartItems;
    TextView tv_grand_total,tv_shipping_fee,tv_grand_total_price,tv_name,tv_appt_name,tv_phone_number,tv_address,tv_flat_no;

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
        tv_flat_no=(TextView)findViewById(R.id.tv_flat_no);


        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        rvCartItems=(RecyclerView)findViewById(R.id.rv_cart_item);
        toolbar.setTitle("Checkout");
        setSupportActionBar(toolbar);
        new GetCartItemCheckout().execute();
        new GetAddress().execute();

    }

    public void placeOrder(View view) {
        new PlaceOrder().execute();
        new RemoveFromCart().execute();
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
                    tv_flat_no.setText(resultSet.getString("flat_no"));
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

    private class RemoveFromCart extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Toast.makeText(CheckoutActivity.this, "Cart Cleared", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                ConnectionClass connectionClass=new ConnectionClass();
                Connection connection=connectionClass.connect();
                String select_ids_query="delete from Grocery_cart_table where user_id='"+uniqueUserId+"' and status=1";
                Statement statement= null;
                statement = connection.createStatement();
                statement.executeQuery(select_ids_query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public String createUniqueUserId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        return (currentDateandTime+uniqueUserId).toUpperCase();
    }

    private class PlaceOrder extends AsyncTask<Void,Void,Long>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if(aLong!=0)
            {
                Toast.makeText(CheckoutActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(CheckoutActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected Long doInBackground(Void... voids) {
//            try {
//                ConnectionClass connectionClass = new ConnectionClass();
//                Connection connection = connectionClass.connect();
//                String insertQuery="insert into Grocery_cart_table (user_id,prod_id,quantity,status) values ('"+uniqueUserId+"','"+id+"','"+id+"',1)";
//                PreparedStatement statementInsert = connection.prepareStatement(insertQuery);
//                long resSetInsert = statementInsert.executeUpdate();
//                return resSetInsert;
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            try {
                long resSetInsert = 0;
                String id = createUniqueUserId();
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String currentDateandTime = sdf.format(new Date());

                for (int i = 0; i < cartArrayList.size(); i++) {
                    String insertOrder = "insert into Grocery_Order_table (prod_id,quantity,user_id,order_date,order_id,status) values ('" + cartArrayList.get(i).getProdId() + "','" + cartArrayList.get(i).getQuantity() + "','" + uniqueUserId + "','"+currentDateandTime+"','" + id + "','1')";
                    PreparedStatement statementInsert = connection.prepareStatement(insertOrder);
                    resSetInsert = statementInsert.executeUpdate();
                }
                return resSetInsert;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

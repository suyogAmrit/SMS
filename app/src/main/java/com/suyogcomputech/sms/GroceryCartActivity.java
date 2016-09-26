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
import android.widget.Button;
import android.widget.LinearLayout;

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
 * Created by Suyog on 9/9/2016.
 */
public class GroceryCartActivity extends AppCompatActivity {
    String uniqueUserId;
    ArrayList<GroceryCartList> cartArrayList;
    GroceryCartAdapter groceryCartAdapter;
    RecyclerView rvCartItems;
    Toolbar toolbar;
    Button placeOrder,checkout;
    LinearLayout new_address_layout,exist_address,exist_layout,new_address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);

        setContentView(R.layout.grocery_cart_items);
//        new_address_layout=(LinearLayout)findViewById(R.id.new_address);
//        exist_address=(LinearLayout)findViewById(R.id.exist_address);
//        exist_layout=(LinearLayout)findViewById(R.id.exist_address);
//        new_address=(LinearLayout)findViewById(R.id.use_exist_address);
        checkout=(Button)findViewById(R.id.checkout);
        rvCartItems=(RecyclerView)findViewById(R.id.rv_cart_item);
        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
//        exist_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                exist_layout.setVisibility(View.GONE);
//                new_address_layout.setVisibility(View.VISIBLE);
//            }
//        });
//        new_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new_address_layout.setVisibility(View.GONE);
//                exist_layout.setVisibility(View.VISIBLE);
//            }
//        });
        new GetCartItem().execute();

    }

    public void checkout(View view) {
        Intent intent=new Intent(GroceryCartActivity.this,CheckoutActivity.class);
        startActivity(intent);
    }


    private class GetCartItem extends AsyncTask<Void,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try {
                cartArrayList=new ArrayList<>();
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
                }
                groceryCartAdapter=new GroceryCartAdapter(GroceryCartActivity.this,cartArrayList);
                rvCartItems.setAdapter(groceryCartAdapter);

                GridLayoutManager gglm=new GridLayoutManager(GroceryCartActivity.this,1,GridLayoutManager.VERTICAL,false);
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
}

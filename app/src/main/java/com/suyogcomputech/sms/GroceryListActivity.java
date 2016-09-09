package com.suyogcomputech.sms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.adapter.GroceryListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.GroceryProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Suyog on 9/7/2016.
 */
public class GroceryListActivity extends AppCompatActivity {
    String category,subCategory,subDesc;
    Toolbar tbProduct;
    RecyclerView rvPdroducts;
    GroceryListAdapter adapter;
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

        new GroceryList().execute();
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
}

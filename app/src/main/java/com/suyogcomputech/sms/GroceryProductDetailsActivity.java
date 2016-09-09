package com.suyogcomputech.sms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Suyog on 9/8/2016.
 */
public class GroceryProductDetailsActivity extends AppCompatActivity {
    int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_product_details_content);

//        Toast.makeText(GroceryProductDetailsActivity.this, getIntent().getExtras().getString("product_id"), Toast.LENGTH_SHORT).show();
        id= Integer.parseInt(getIntent().getExtras().getString("product_id"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("fff");


        new GroceryDetails().execute();

    }

    private class GroceryDetails extends AsyncTask<Void,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);

            try {
                while(rs.next())
                {
                    Log.i("ssss",rs.getString("prod_title"));
                }
//                if(rs.getRow()>0)

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query="select * from Grocery_Prod_table where prod_id="+id;
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
}

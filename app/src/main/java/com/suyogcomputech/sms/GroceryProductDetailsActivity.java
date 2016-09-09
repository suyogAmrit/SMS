package com.suyogcomputech.sms;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Suyog on 9/8/2016.
 */
public class GroceryProductDetailsActivity extends AppCompatActivity {
    int id;
    TextView tv_title, tv_desc, tv_brand, tv_others, tv_shipping, tv_price, tv_total_price;
    ImageView iv_image;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_product_details_content);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_others = (TextView) findViewById(R.id.tv_others);
        tv_shipping = (TextView) findViewById(R.id.tv_shipping);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        iv_image = (ImageView) findViewById(R.id.iv_image);

//        Toast.makeText(GroceryProductDetailsActivity.this, getIntent().getExtras().getString("product_id"), Toast.LENGTH_SHORT).show();
        id = Integer.parseInt(getIntent().getExtras().getString("product_id"));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        iv_image.getLayoutParams(). height =  metrics.heightPixels / 5 * 2;

        new GroceryDetails().execute();

    }

    private class GroceryDetails extends AsyncTask<Void, Void, ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            super.onPostExecute(rs);

            try {
                while (rs.next()) {
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(rs.getString("prod_title"));
                    tv_title.setText(rs.getString("prod_title"));
                    tv_desc.setText(rs.getString("prod_description"));
                    tv_brand.setText(rs.getString("prod_brand"));
                    tv_others.setText(rs.getString("other_details"));
                    tv_shipping.setText(rs.getString("shipping_fee"));
                    tv_price.setText(rs.getString("price"));
                    tv_total_price.setText(String.valueOf(rs.getInt("shipping_fee") + rs.getInt("price")));


//                    iv_image.setImageURI(Uri.parse(rs.getString("images_path")));
                    WindowManager wm = (WindowManager) GroceryProductDetailsActivity.this.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int screenWidth = size.x;

                    Picasso.with(GroceryProductDetailsActivity.this)
                            .load(rs.getString("images_path"))
                            .error(R.drawable.ic_empty)
                            .placeholder(R.drawable.backgroundd)
                            .fit()
                            .into((iv_image));


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
                String query = "select * from Grocery_Prod_table where prod_id=" + id;
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

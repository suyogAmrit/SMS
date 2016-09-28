package com.suyogcomputech.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Suyog on 9/8/2016.
 */
public class GroceryProductDetailsActivity extends AppCompatActivity {
    int id;
    TextView tv_title, tv_desc, tv_brand, tv_others, tv_shipping, tv_price, tv_total_price,tv_qty;
    ImageView iv_image,iv_plus,iv_minus;
    Toolbar toolbar;
    int total,max_quantity,product_price;
    String uniqueUserId,qty;
    EditText et_qty;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;

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
//        et_qty=(EditText)findViewById(R.id.et_qty);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_plus=(ImageView)findViewById(R.id.iv_plus);
        iv_minus=(ImageView)findViewById(R.id.iv_minus);
        tv_qty=(TextView)findViewById(R.id.tv_qty);

        id = Integer.parseInt(getIntent().getExtras().getString("product_id"));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        iv_image.getLayoutParams(). height =  metrics.heightPixels / 5 * 2;

        SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);

        new GroceryDetails().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CartCount().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.grocery_menu_cart, menu);
        ActionItemBadge.update(GroceryProductDetailsActivity.this, menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.addcart) {
            if(badgeCount!=0)
            {
                Intent intent = new Intent(GroceryProductDetailsActivity.this,GroceryCartActivity.class);
                //intent.putExtra(AppConstants.PROD_ID,productDetails.getId());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(GroceryProductDetailsActivity.this, "Cart empty please add item to cart", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if(item.getItemId()==R.id.item_search)
        {
            Intent intent = new Intent(GroceryProductDetailsActivity.this,SearchGroceryActivity.class);
            startActivity(intent);
        }
        return false;
    }


    public void addToCart(View view) {
        qty=String.valueOf(tv_qty.getText());
        new InsertCart().execute();
    }

    public void plus(View view) {
        int current_qty;
        current_qty=Integer.parseInt(String.valueOf(tv_qty.getText()));
        if(current_qty<max_quantity)
        {
            tv_qty.setText(String.valueOf(++current_qty));
            tv_total_price.setText(String.valueOf(product_price*current_qty+Integer.parseInt(tv_shipping.getText().toString())));
        }
    }

    public void minus(View view) {
        int current_qty;
        current_qty=Integer.parseInt(String.valueOf(tv_qty.getText()));

        if(current_qty>1)
        {
            tv_qty.setText(String.valueOf(--current_qty));
            tv_total_price.setText(String.valueOf(product_price*current_qty+Integer.parseInt(tv_shipping.getText().toString())));
        }
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
//                    product_price=Integer.parseInt(tv_price.getText().toString());
                    product_price=rs.getInt("price");

                    total=rs.getInt("shipping_fee") + rs.getInt("price");
                    tv_total_price.setText(String.valueOf(total));

                    max_quantity=Integer.parseInt(rs.getString("quantity"));


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

    private class InsertCart extends AsyncTask<Void,Void,Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long i) {
            super.onPostExecute(i);

            if(i>0 && i!=1111111)
            {
                ++badgeCount;
                invalidateOptionsMenu();
                Toast.makeText(GroceryProductDetailsActivity.this, "Add to cart Successfully.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(GroceryProductDetailsActivity.this, "Quantity updated.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Long doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String selectQuery="select * from Grocery_cart_table where user_id='"+uniqueUserId+"' and prod_id='"+id+"' and status='1'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectQuery);
                if (!resultSet.next()) {
                    String insertQuery="insert into Grocery_cart_table (user_id,prod_id,quantity,status) values ('"+uniqueUserId+"','"+id+"','"+qty+"',1)";
                    PreparedStatement statementInsert = connection.prepareStatement(insertQuery);
                    long resSetInsert = statementInsert.executeUpdate();
                    return resSetInsert;
                }
                else
                {
                    String updateQuery="update Grocery_cart_table set quantity='"+qty+"' where user_id='"+uniqueUserId+"' and prod_id='"+id+"' and status='1'";
                    PreparedStatement statementInsert = connection.prepareStatement(updateQuery);
                    long resSet = statementInsert.executeUpdate();

                    return Long.valueOf(1111111);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class CartCount extends AsyncTask<Void,Void,Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            badgeCount=integer;
            invalidateOptionsMenu();

        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String cartQuery="select count(*) as cart_count from Grocery_cart_table where user_id='"+uniqueUserId+"' and status=1";
                Statement statement = null;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(cartQuery);

                resultSet.next();
                Log.i("ssss",cartQuery);
                Log.i("ssss",String.valueOf(resultSet.getInt("cart_count")));
                return(resultSet.getInt("cart_count"));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

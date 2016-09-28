package com.suyogcomputech.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.HistoryAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.GroceryHistory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/27/2016.
 */
public class GroceryHistoryActivity extends AppCompatActivity {
String orderId,uniqueUserId;
    Toolbar toolbar;
    RecyclerView rvCartItems;
    TextView orderNo,tv_Hitems,tv_Hprice,tv_Hshipping,tv_HgrandTotal,tv_Hname,tv_Haddress,tv_Hphone,order_status;
    ArrayList<GroceryHistory> arrayHistory;
    HistoryAdapter historyAdapter;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grocery_history);

        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        rvCartItems=(RecyclerView)findViewById(R.id.rv_history);
        orderNo=(TextView)findViewById(R.id.order_no);
        tv_Hitems=(TextView)findViewById(R.id.tv_Hitems);
        tv_Hprice=(TextView)findViewById(R.id.tv_Hprice);
        tv_Hshipping=(TextView)findViewById(R.id.tv_Hshipping);
        tv_HgrandTotal=(TextView)findViewById(R.id.tv_HgrandTotal);
        tv_Hname=(TextView)findViewById(R.id.tv_Hname);
        tv_Haddress=(TextView)findViewById(R.id.tv_Haddress);
        tv_Hphone=(TextView)findViewById(R.id.tv_Hphone);
        order_status=(TextView)findViewById(R.id.order_status);
        toolbar.setTitle("Order History");
        setSupportActionBar(toolbar);

        orderId = getIntent().getStringExtra("order_id");
        orderNo.setText(orderId);
        SharedPreferences sharedpreferences = GroceryHistoryActivity.this.getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        new GetOrderDetails().execute(orderId);
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
        ActionItemBadge.update(GroceryHistoryActivity.this, menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
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
                Intent intent = new Intent(GroceryHistoryActivity.this,GroceryCartActivity.class);
                //intent.putExtra(AppConstants.PROD_ID,productDetails.getId());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(GroceryHistoryActivity.this, "Cart empty please add item to cart", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    private class GetOrderDetails extends AsyncTask<String,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try {
                arrayHistory=new ArrayList<>();
                int noItems=0;
                float total_price=0,shipping_fee=0,grand_total=0;
                while(resultSet.next())
                {
                    GroceryHistory gh=new GroceryHistory();
                    gh.setImageUrl(resultSet.getString("images_path"));
                    gh.setTitle(resultSet.getString("prod_title"));
                    gh.setPrice(resultSet.getString("price"));
                    gh.setQuantity(resultSet.getString("cart_quantity"));
                    gh.setStatus(resultSet.getString("status"));

                    arrayHistory.add(gh);

                    shipping_fee=shipping_fee+Float.valueOf(resultSet.getString("shipping_fee"));
                    noItems=noItems+Integer.parseInt(resultSet.getString("cart_quantity"));
                    total_price=total_price+(Float.valueOf(resultSet.getString("price"))*Float.valueOf(resultSet.getString("cart_quantity")));
                    tv_Hname.setText(resultSet.getString("flat_owner"));
                    tv_Haddress.setText(resultSet.getString("appt_name")+", "+resultSet.getString("appt_add"));
                    tv_Hphone.setText(resultSet.getString("extension_no"));

                    if(resultSet.getString("status").equals("1"))
                    {
                        order_status.setText("Order Pending");
                    }
                    else if(resultSet.getString("status").equals("2"))
                    {
                        order_status.setText("Order Shipped");
                    }
                    else if(resultSet.getString("status").equals("3"))
                    {
                        order_status.setText("Order Delevered");
                    }
                }

                tv_HgrandTotal.setText(String.valueOf(total_price+shipping_fee));
                tv_Hshipping.setText(String.valueOf(shipping_fee));
                tv_Hitems.setText(String.valueOf(noItems));
                tv_Hprice.setText(String.valueOf(total_price));

                historyAdapter=new HistoryAdapter(GroceryHistoryActivity.this,arrayHistory);
                rvCartItems.setAdapter(historyAdapter);

                GridLayoutManager glm= new GridLayoutManager(GroceryHistoryActivity.this,1,GridLayoutManager.VERTICAL,false);
                rvCartItems.setLayoutManager(glm);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResultSet doInBackground(String... strings) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String selectQuery="SELECT gpt.images_path,gpt.prod_title,gpt.price,got.status,gpt.shipping_fee,gpt.price,fud.flat_owner,fud.flat_no,fud.extension_no,apt.appt_name,apt.appt_add,got.quantity as cart_quantity from Grocery_Order_table as got inner join Grocery_Prod_table as gpt on got.prod_id=gpt.prod_id inner join flat_user_Details as fud on  got.user_id=fud.user_id inner join appartment as apt on fud.appt_id=apt.appt_id where got.user_id='"+uniqueUserId+"' and got.order_id='"+strings[0]+"'";
//                String selectQuery="SELECT *,got.quantity as cart_quantity from Grocery_Order_table as got inner join Grocery_Prod_table as gpt on got.prod_id=gpt.prod_id  where got.user_id='"+uniqueUserId+"' and got.order_id='"+strings[0]+"'";
                Statement statement = null;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectQuery);
                Log.i("sssss",selectQuery);
                return resultSet;
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
                return(resultSet.getInt("cart_count"));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

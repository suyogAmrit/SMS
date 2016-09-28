package com.suyogcomputech.app_fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.AllOrderListAdapter;
import com.suyogcomputech.adapter.GroceryListAdapter;
import com.suyogcomputech.adapter.OrderListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.OrderList;
import com.suyogcomputech.sms.GroceryCartActivity;
import com.suyogcomputech.sms.GroceryHistoryActivity;
import com.suyogcomputech.sms.GroceryListActivity;
import com.suyogcomputech.sms.GroceryProductDetailsActivity;
import com.suyogcomputech.sms.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/26/2016.
 */
public class MyGroceryOrderFragment extends Fragment {
    ListView lv_oredr;
//    ArrayList<String> orderList;
    String uniqueUserId;
    int badgeCount=0;
    AllOrderListAdapter allOrderListAdapter;
    ArrayList<OrderList> arrayOrder;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    RecyclerView rvPdroducts;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_order, container, false);

//        lv_oredr=(ListView)view.findViewById(R.id.lv_order_item);
        rvPdroducts=(RecyclerView)view.findViewById(R.id.orderList);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);

        new GetOrderDetails().execute();
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        new CartCount().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
//            finish();
            return true;
        }
        if (item.getItemId() == R.id.addcart) {
            if(badgeCount!=0)
            {
                Intent intent = new Intent(getActivity(),GroceryCartActivity.class);
                //intent.putExtra(AppConstants.PROD_ID,productDetails.getId());
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getActivity(), "Cart empty please add item to cart", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.grocery_menu_cart, menu);
        ActionItemBadge.update(getActivity(), menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
    }


    private class GetOrderDetails extends AsyncTask<Void,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);
            try {

                arrayOrder=new ArrayList<>();
                while(resultSet.next())
                {
                    OrderList orderList=new OrderList();
                    orderList.setOrder_id(resultSet.getString("order_id"));
                    orderList.setQuantity(resultSet.getString("quantity"));
                    orderList.setOrder_date(resultSet.getString("order_date"));
                    orderList.setStatus(resultSet.getString("status"));

                    arrayOrder.add(orderList);
                }
                allOrderListAdapter=new AllOrderListAdapter(getActivity(),arrayOrder);
                rvPdroducts.setAdapter(allOrderListAdapter);

                GridLayoutManager gglm=new GridLayoutManager(getActivity(),1,GridLayoutManager.VERTICAL,false);
                rvPdroducts.setLayoutManager(gglm);



//                orderList=new ArrayList<>();
//                while(resultSet.next())
//                {
//                    orderList.add(resultSet.getString("order_id"));
//                }
//
//                ArrayAdapter<String> arr_str=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,orderList);
//                lv_oredr.setAdapter(arr_str);
//                lv_oredr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Intent intent= new Intent(getActivity(),GroceryHistoryActivity.class);
//                        intent.putExtra("order_id",adapterView.getItemAtPosition(i).toString());
//                        startActivity(intent);
//                    }
//                });
//
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected ResultSet doInBackground(Void... voids) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String selectQuery="SELECT  SUM(quantity) as quantity,order_id,order_date,status FROM Grocery_Order_table where user_id='"+uniqueUserId+"' GROUP BY order_id,order_date,status";
                Statement statement = null;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectQuery);
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
            getActivity().invalidateOptionsMenu();
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

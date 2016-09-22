package com.suyogcomputech.app_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.suyogcomputech.adapter.CartItemAdapter;
import com.suyogcomputech.adapter.MyOrderAdapter;
import com.suyogcomputech.adapter.OrderAdapter;
import com.suyogcomputech.adapter.OrderListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.OnlineShopping;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 7/27/2016.
 */
public class MyOrderFragment extends Fragment {
    ConnectionDetector detector;
    RecyclerView rcvMyOrder;
    ArrayList<ProductDetails> list;
    ProgressDialog progressDialog;
    private OrderListAdapter orderListAdapter;
    String ordername;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        detector = new ConnectionDetector(getActivity());
        rcvMyOrder = (RecyclerView) view.findViewById(R.id.rcvMyOrder);
        if (AppHelper.isConnectingToInternet(getActivity())) {
            new FetchOrderItem().execute();
        } else {
            Toast.makeText(getActivity(), AppConstants.dialog_message, Toast.LENGTH_LONG).show();
        }
        return view;
    }
    private class FetchOrderItem extends AsyncTask<Void,Void,ResultSet> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Fetching cart items");
            progressDialog.show();
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                //String query = "select * from Eshop_Order_tb where uid='"+findUserId()+"'";
//                String query = "select p.prod_id,e.slno,p.prod_title,p.images,p.prod_brand,p.price,e.size,e.Quantity,o.from_date,\n" +
//                        "o.to_date,o.offer_per from Eshop_Prod_table as p\n" +
//                        "inner join Eshop_cart_tb as e on(p.prod_id=e.prod_id)\n" +
//                        "inner join Eshop_Offer_tb as o on(p.prod_id=o.prod_id)\n" +
//                        " and e.Status=1 and e.user_id='"+findUserId()+"'";
                String query = "select eot.b_fname,eot.b_email,eot.b_ph1,eot.b_address1,eot.quantity,eot.size,ept.prod_brand,ept.images,ept.prod_title,ept.price from Eshop_Order_tb as eot inner join Eshop_Prod_table as ept on eot.prod_id=ept.prod_id ;";
                Log.v("Query",query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Log.v("resulttttt",resultSet.toString());
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
//                    details.setTitle(rs.getString("prod_title"));
//                    details.setBrand(rs.getString("prod_brand"));
//                    details.setPrice(rs.getString("price"));
//                    details.setSizeProduct(rs.getString("size"));
//                    details.setOfferPer(rs.getString("offer_per"));
//                    details.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + rs.getString(AppConstants.PRDMAINIMAGE).replace("~", "").replace(" ", "%20"));
//                    details.setQuantity(rs.getString("Quantity"));
//                    details.setFromDate(rs.getString("from_date"));
//                    details.setToDate(rs.getString("to_date"));
//                    details.setSerielNo(rs.getString("slno"));
//                    details.setId(rs.getString("prod_id"));
                    details.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + rs.getString("images").replace("~", "").replace(" ", "%20"));
                    details.setTitle(rs.getString("prod_title"));
                    details.setBrand(rs.getString("prod_brand"));
                    details.setPrice(rs.getString("price"));
                    details.setQuantity(rs.getString("Quantity"));
                    details.setSizeProduct(rs.getString("size"));
                    details.setOrderplaceHolderName(rs.getString("b_fname"));
                    details.setOrderPlaceHolderEmail(rs.getString("b_email"));
                    details.setOrderPlaceHolderAddr(rs.getString("b_address1"));
                    details.setOrderPlaceHolderPhone(rs.getString("b_ph1"));
                    list.add(details);
                }
                orderListAdapter = new OrderListAdapter(list,getActivity());
                rcvMyOrder.setHasFixedSize(true);
                rcvMyOrder.setAdapter(orderListAdapter);
                LinearLayoutManager glm = new LinearLayoutManager(getActivity());
                rcvMyOrder.setLayoutManager(glm);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    public String findUserId()
    {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }

}

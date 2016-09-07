package com.suyogcomputech.app_fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.suyogcomputech.adapter.GroceryAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.EGroceryCategory;
import com.suyogcomputech.helper.EGrocerySubCategory;
import com.suyogcomputech.helper.EShopCategory;
import com.suyogcomputech.helper.Grocery;
import com.suyogcomputech.sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/11/2016.
 */
public class GroceryFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    ConnectionDetector detector;
    RecyclerView rcvGrocery;
    GroceryAdapter adapter;
    ArrayList<EGroceryCategory> list;
    ExpandableListView expLvCatgory;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eshop_category, container, false);
        detector = new ConnectionDetector(getActivity());
        rcvGrocery = (RecyclerView) view.findViewById(R.id.rcvGrocery);
        expLvCatgory = (ExpandableListView) view.findViewById(R.id.exLvCategory);
        if (detector.isConnectingToInternet()) {
            new FetchFacilities().execute();
        } else
            Toast.makeText(getActivity(), AppConstants.dialog_message, Toast.LENGTH_LONG).show();


        return view;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    private class FetchFacilities extends AsyncTask<Void,Void,ArrayList<EGroceryCategory>> {
        ProgressDialog dialog;

        @Override
        protected ArrayList<EGroceryCategory> doInBackground(Void... voids) {

            try {
                ConnectionClass connectionClass=new ConnectionClass();
                Connection connection=connectionClass.connect();
                String query="select * from Grocery_Cat_table";
                Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(query);
                ArrayList<EGroceryCategory> list = new ArrayList<>();
                while(resultSet.next())
                {
                    String catId = resultSet.getString(AppConstants.CATID);
                    String catDesc = resultSet.getString(AppConstants.CATDESC);

                    String query_sub_cat="select * from Grocery_Sub_Cat_table where cat_id="+catId;
                    Statement statement1=connection.createStatement();
                    ResultSet resultSetSubcat=statement1.executeQuery(query_sub_cat);
                    ArrayList<EGrocerySubCategory> shopSubCategories = new ArrayList<>();

                    while(resultSetSubcat.next())
                    {
                        Log.i("iddd",resultSetSubcat.getString("sub_cat_desc"));
                        EGrocerySubCategory shopSubCategory=new EGrocerySubCategory();

                        shopSubCategory.setCatId(catId);
                        shopSubCategory.setSubCatDesc(resultSetSubcat.getString(AppConstants.SUBCATDESC));
                        shopSubCategory.setSubcatId(resultSetSubcat.getString(AppConstants.SUBCATID));

                        shopSubCategories.add(shopSubCategory);
                    }

                    EGroceryCategory category = new EGroceryCategory();
                    category.setCatId(catId);
                    category.setCaDesc(catDesc);
                    category.setSubCategories(shopSubCategories);

                    list.add(category);


                }
                return list;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<EGroceryCategory> eGroceryCategories) {
            super.onPostExecute(eGroceryCategories);
            dialog.dismiss();
            Log.i("Size", String.valueOf(eGroceryCategories.get(0).getSubCategories().size()));
            try {
                list = eGroceryCategories;
                GroceryAdapter adapter = new GroceryAdapter(getActivity(), list);
                expLvCatgory.setAdapter(adapter);
                expLvCatgory.setOnChildClickListener(GroceryFragment.this);
            }
            catch (NullPointerException e){
                Toast.makeText(getActivity(), AppConstants.TRYAGAIN, Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPreExecute(){
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(AppConstants.dialog_title);
            dialog.setMessage(AppConstants.CATMSG);
            dialog.show();
        }

    }
}


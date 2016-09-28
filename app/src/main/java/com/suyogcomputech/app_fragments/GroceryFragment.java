package com.suyogcomputech.app_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.GroceryAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.EGroceryCategory;
import com.suyogcomputech.helper.EGrocerySubCategory;
import com.suyogcomputech.helper.EShopCategory;
import com.suyogcomputech.helper.Grocery;
import com.suyogcomputech.helper.StringWithTag;
import com.suyogcomputech.sms.GroceryCartActivity;
import com.suyogcomputech.sms.GroceryListActivity;
import com.suyogcomputech.sms.GroceryProductDetailsActivity;
import com.suyogcomputech.sms.R;
import com.suyogcomputech.sms.SearchGroceryActivity;

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
    ArrayList<EGroceryCategory> list;
    ExpandableListView expLvCatgory;
    int badgeCount=0;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    String uniqueUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eshop_category, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);

        detector = new ConnectionDetector(getActivity());
        expLvCatgory = (ExpandableListView) view.findViewById(R.id.exLvCategory);
        if (detector.isConnectingToInternet()) {
            new FetchFacilities().execute();
        } else
            Toast.makeText(getActivity(), AppConstants.dialog_message, Toast.LENGTH_LONG).show();
//        getActivity().invalidateOptionsMenu();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.grocery_menu_cart, menu);
        ActionItemBadge.update(getActivity(), menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
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
        if(item.getItemId()==R.id.item_search)
        {
            Intent intent = new Intent(getActivity(),SearchGroceryActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String catId=list.get(groupPosition).getCatId();
        String subCatId=list.get(groupPosition).getSubCategories().get(childPosition).getSubcatId();
        String subcatdesc=list.get(groupPosition).getSubCategories().get(childPosition).getSubCatDesc();

        Intent intent=new Intent(getActivity(),GroceryListActivity.class);
        intent.putExtra(AppConstants.CATID,catId);
        intent.putExtra(AppConstants.SUBCATID,subCatId);
        intent.putExtra(AppConstants.SUBCATDESC,subcatdesc);
        startActivity(intent);

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
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(AppConstants.dialog_title);
            dialog.setMessage(AppConstants.CATMSG);
            dialog.show();
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


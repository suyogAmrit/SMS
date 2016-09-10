package com.suyogcomputech.app_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.suyogcomputech.adapter.EShopCatAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.EShopCategory;
import com.suyogcomputech.helper.EShopSubCategory;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.ProductListActivity;
import com.suyogcomputech.sms.R;
import com.suyogcomputech.sms.ShoppingCartItemActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by suyogcomputech on 23/08/16.
 */
public class EShopCategoryFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    ExpandableListView expLvCatgory;
    GetCategoryList taskGetCategoryList;
    ArrayList<EShopCategory> list;


    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (AppHelper.isConnectingToInternet(getActivity())) {
            new FetchbadgeNumber().execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        ActionItemBadge.update(getActivity(), menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
        menu.findItem(R.id.item_sort).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_samplebadge) {
            Intent intent = new Intent(getActivity(),ShoppingCartItemActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eshop_category, container, false);
        expLvCatgory = (ExpandableListView) view.findViewById(R.id.exLvCategory);
        if (AppHelper.isConnectingToInternet(getActivity())) {
            taskGetCategoryList = new GetCategoryList();
            taskGetCategoryList.execute();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (taskGetCategoryList != null && taskGetCategoryList.getStatus() != AsyncTask.Status.FINISHED) {
            taskGetCategoryList.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AppHelper.isConnectingToInternet(getActivity())) {
            new FetchbadgeNumber().execute();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        String catId = list.get(i).getCatId();
        String subCatId = list.get(i).getSubCategories().get(i1).getSubcatId();

        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(AppConstants.CATID, catId);
        intent.putExtra(AppConstants.SUBCATID, subCatId);
        intent.putExtra(AppConstants.SUBCATDESC, list.get(i).getSubCategories().get(i1).getSubCatDesc());
        startActivity(intent);

        return true;
    }

    private class GetCategoryList extends AsyncTask<Void, Void, ArrayList<EShopCategory>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(AppConstants.dialog_title);
            dialog.setMessage(AppConstants.CATMSG);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<EShopCategory> eShopCategories) {
            super.onPostExecute(eShopCategories);
            dialog.dismiss();
            try {
                list = eShopCategories;
                Log.i("Size", String.valueOf(eShopCategories.get(0).getSubCategories().size()));
                EShopCatAdapter adapter = new EShopCatAdapter(getActivity(), eShopCategories);
                expLvCatgory.setAdapter(adapter);
                expLvCatgory.setOnChildClickListener(EShopCategoryFragment.this);
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), AppConstants.TRYAGAIN, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ArrayList<EShopCategory> doInBackground(Void... voids) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "select * from Eshop_cat_tb";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<EShopCategory> list = new ArrayList<>();
                while (resultSet.next()) {

                    String catId = resultSet.getString(AppConstants.CATID);
                    String catDesc = resultSet.getString(AppConstants.CATDESC);

                    String subcatQuery = "SELECT * from Eshop_sub_cat_tb where cat_id = '" + catId + "'";
                    Statement subCatStatement = connection.createStatement();
                    Log.i("subcatqury", subcatQuery);
                    ResultSet subcatResultSet = subCatStatement.executeQuery(subcatQuery);

                    ArrayList<EShopSubCategory> shopSubCategories = new ArrayList<>();

                    while (subcatResultSet.next()) {
                        String subCatId = subcatResultSet.getString(AppConstants.SUBCATID);
                        String subCatDesc = subcatResultSet.getString(AppConstants.SUBCATDESC);
                        EShopSubCategory shopSubCategory = new EShopSubCategory();

                        shopSubCategory.setCatId(catId);
                        shopSubCategory.setSubCatDesc(subCatDesc);
                        shopSubCategory.setSubcatId(subCatId);

                        shopSubCategories.add(shopSubCategory);
                    }

                    EShopCategory category = new EShopCategory();
                    category.setCatId(catId);
                    category.setCaDesc(catDesc);
                    category.setSubCategories(shopSubCategories);

                    list.add(category);

                }
                return list;
            } catch (Exception e) {
                return null;
            }

        }
    }
    private class FetchbadgeNumber extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "SELECT COUNT(*) as count_row FROM Eshop_cart_tb where " + AppConstants.USERID + "='" + findUserId() + "'";
                Log.i("Query", query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                String count = resultSet.getString("count_row");
                Log.v("count", count);
                badgeCount = Integer.valueOf(count);
            } catch (SQLException e) {
                Log.i("Except", e.getMessage());   
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            getActivity().invalidateOptionsMenu();
        }
    }
    private String findUserId(){
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return uniqueUserId;
    }
}

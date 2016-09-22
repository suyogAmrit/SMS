package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.ProductListAdapter;
import com.suyogcomputech.adapter.SearchAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ItemCounter;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by suyogcomputech on 23/08/16.
 */
public class ProductListActivity extends AppCompatActivity {
    Toolbar tbProduct;
    RecyclerView rvPdroducts;
    GetProducts taskGetProducts;
    String subCategory, category, subDesc;
    ProductListAdapter adapter;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;
    private ArrayList<ProductDetails> productDetailList, productDetailsArrayList, productDetailsesnew, productDetailsesListOfData;
    String query;
    RadioGroup radioGroup;
    ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        category = b.getString(AppConstants.CATID);
        subCategory = b.getString(AppConstants.SUBCATID);
        subDesc = b.getString(AppConstants.SUBCATDESC);
        setContentView(R.layout.activity_product_list);
        tbProduct = (Toolbar) findViewById(R.id.tb_product);
        TextView tvProdct = (TextView) findViewById(R.id.toolbar_title);
        tvProdct.setText(subDesc);
        setSupportActionBar(tbProduct);
        tbProduct.setTitle(subDesc);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productDetailList = new ArrayList<>();
        rvPdroducts = (RecyclerView) findViewById(R.id.rv_product);
        adapter = new ProductListAdapter(ProductListActivity.this);
        GridLayoutManager glm = new GridLayoutManager(ProductListActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rvPdroducts.setLayoutManager(glm);
        rvPdroducts.setAdapter(adapter);
        dialog = new ProgressDialog(ProductListActivity.this);
        if (AppHelper.isConnectingToInternet(ProductListActivity.this)) {
            taskGetProducts = new GetProducts();
            taskGetProducts.execute();
        }
        productDetailsesListOfData = new ArrayList<>();
        if (AppHelper.isConnectingToInternet(ProductListActivity.this)) {
            new FetchbadgeNumber().execute();
        }else {
            Toast.makeText(ProductListActivity.this,"No internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        ActionItemBadge.update(ProductListActivity.this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.item_search));
        //EditText editText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setQueryHint("Search...");
        EditText editText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter(newText);
                AppCompatDialog dialog = new AppCompatDialog(ProductListActivity.this);
                dialog.setTitle("gggg");
                dialog.getWindow().getAttributes().verticalMargin = -0.4F;
                dialog.setContentView(R.layout.serch_dilog);
//                RecyclerView rcvSearchResult = (RecyclerView) dialog.findViewById(R.id.rcvSearchResult);
//                rcvSearchResult.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
//                SearchAdapter searchAdapter = new SearchAdapter(productDetailList);
//                rcvSearchResult.setAdapter(searchAdapter);
                dialog.show();
                return false;
            }
        });
        searchView.setIconified(true);
        return true;
    }

    private void filter(String newText) {
        query = newText.toLowerCase();
        productDetailsArrayList.clear();
        adapter.clear();
        //adapter.notifyDataSetChanged();
        if (query.length() != 0) {
            for (ProductDetails productDetails1 : productDetailList) {
                String text = productDetails1.getBrand().toLowerCase();
                if (text.contains(query)) {
                    productDetailsArrayList.add(productDetails1);
                }
            }
        } else {
            productDetailsArrayList.addAll(productDetailList);
        }
        adapter.addProducts(productDetailsArrayList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.item_sort) {
            showProductSortDialog();
            return true;
        }
        if (item.getItemId()==R.id.item_samplebadge){
            Intent intent = new Intent(ProductListActivity.this,ShoppingCartItemActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void showProductSortDialog() {
        SharedPreferences shr = getSharedPreferences(AppConstants.SORTPREFS, MODE_PRIVATE);
        int sortDesc = shr.getInt(AppConstants.SORTDESC, 0);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductListActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.low_high_layout, null);
        dialogBuilder.setView(dialogView);
        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroup1);
        final RadioButton lowHigh = (RadioButton) dialogView.findViewById(R.id.rb_low_high);
        final RadioButton highLow = (RadioButton) dialogView.findViewById(R.id.rb_high_low);
        switch (sortDesc) {
            case 1:
                lowHigh.setChecked(true);
                break;
            case 2:
                highLow.setChecked(true);
                break;
        }
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (lowHigh.isChecked() || highLow.isChecked()) {
                    adapter.clear();
                    new GetProducts().execute();
                }
            }
        });
        final AlertDialog alertDialog = dialogBuilder.create();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_high_low:
                        saveSorting(2);
                        break;
                    case R.id.rb_low_high:
                        saveSorting(1);
                        break;
                }
            }
        });
        alertDialog.show();
    }

    private void saveSorting(int i) {
        SharedPreferences shr = getSharedPreferences(AppConstants.SORTPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shr.edit();
        editor.putInt(AppConstants.SORTDESC, i);
        editor.apply();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences shr = getSharedPreferences(AppConstants.SORTPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shr.edit();
        editor.clear();
        editor.apply();
        if ( dialog!=null && dialog.isShowing() ){
            dialog.cancel();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if ( dialog!=null && dialog.isShowing() ){
            dialog.cancel();
        }
    }

    private class GetProducts extends AsyncTask<Void, Void, ArrayList<ProductDetails>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(AppConstants.dialog_title);
            dialog.setMessage(AppConstants.PRDMSG);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ProductDetails> productDetailses) {
            super.onPostExecute(productDetailses);
            dialog.dismiss();
           // if (productDetailses!=null) {
                if (productDetailses.size() > 0) {
                    adapter.addProducts(productDetailses);
                    productDetailList.addAll(productDetailses);
                    productDetailsArrayList = new ArrayList<>(productDetailList);
                    productDetailsesnew = new ArrayList<>();
                    productDetailsesnew.addAll(productDetailList);
                    //productDetailsArrayList.addAll(productDetailList);
                    Log.v("", "" + productDetailsArrayList.size());
                }
        //    }
        else {
                Toast.makeText(ProductListActivity.this,"No Product",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ArrayList<ProductDetails> doInBackground(Void... voids) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "SELECT p.prod_id,p.cat_id,p.sub_cat_id,p.prod_title,p.prod_description,p.prod_short_description,p.prod_status,p.prod_brand,\n" +
                        "p.price,p.images,p.quantity,p.max_quantity_in_cart,p.prod_services,p.prod_features,p.other_details,p.images, \n" +
                        "o.offer_Desc,o.offer_per,o.from_date,o.to_date,o.status as offer_status,\n" +
                        "i.image1,i.image2,i.image3,image4, i.status as image_status,\n" +
                        "s.sellor_name,\n" +
                        "sz.size1,sz.size1_available,sz.size2,size2_available,sz.size3,sz.size3_available,sz.size4,sz.size4_available,sz.size5,sz.size5_available,sz.size6,sz.size6_available\n" +
                        " from Eshop_Prod_table as p\n" +
                        " left outer join Eshop_Offer_tb as o on p.prod_id=o.prod_id \n" +
                        " left outer join Eshop_Prod_image_tb as i on p.prod_id = i.prod_id\n" +
                        " left outer join Eshop_Sellor_tb as s on s.sellor_user_id = p.prod_Sel_id\n" +
                        " left outer join Eshop_Prod_Size_tb as sz on sz.prod_id = p.prod_id\n" +
                        " where sub_cat_id='" + subCategory + "' and cat_id='" + category + "'";
                SharedPreferences shr = getSharedPreferences(AppConstants.SORTPREFS, MODE_PRIVATE);
                int sortDesc = shr.getInt(AppConstants.SORTDESC, 0);
                switch (sortDesc) {
                    case 1:
                        query = query + " ORDER BY p.price ASC";
                        break;
                    case 2:
                        query = query + " ORDER BY p.price DESC";
                        break;
                }
                Log.i("PrdQry", query);

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<ProductDetails> list = new ArrayList<>();
                while (resultSet.next()) {
                    ProductDetails p = new ProductDetails();
                    Log.i("name", resultSet.getString("prod_title"));
                    p.setTitle(resultSet.getString(AppConstants.PRDTITLE));
                    p.setBrand(resultSet.getString(AppConstants.PRDBRAND));
                    p.setDesc(resultSet.getString(AppConstants.PRDDESC));
                    p.setId(resultSet.getString(AppConstants.PRDID));
                    p.setSubCatId(AppConstants.SUBCATID);
                    p.setFromDate(resultSet.getString(AppConstants.OFRFROMDATE));
                    p.setSubCatId(resultSet.getString(AppConstants.SUBCATID));
                    p.setShortDesc(resultSet.getString(AppConstants.SHRTDESC));
                    p.setStatus(resultSet.getString(AppConstants.STATUS));
                    p.setPrice(resultSet.getString(AppConstants.PRDPRICE));
                    p.setMainImage(resultSet.getString(AppConstants.PRDMAINIMAGE));
                    p.setMaxQtyInCart(resultSet.getString(AppConstants.PRDMAXCARTQTY));
                    p.setServices(resultSet.getString(AppConstants.PRDSERVICES));
                    p.setFeatures(resultSet.getString(AppConstants.PRDFEATURES));
                    p.setOtherDetails(resultSet.getString(AppConstants.PRDOTHERDETAILS));
                    p.setOfferDesc(resultSet.getString(AppConstants.OFFERDESC));
                    p.setOfferPer(resultSet.getString(AppConstants.OFFRPER));
                    p.setToDate(resultSet.getString(AppConstants.OFFERTODATE));
                    p.setImageStatus(resultSet.getString(AppConstants.PRDIMAGESTATUS));
                    p.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + resultSet.getString(AppConstants.PRDMAINIMAGE).replace("~", "").replace(" ", "%20"));
                    p.setSellerName(resultSet.getString(AppConstants.SELLOR_NAME));
                    ArrayList<String> imageList = new ArrayList<>();
                    if (p.getStatus().equals("1")) {
                        imageList.add("http://" + AppConstants.IP + "/" + AppConstants.DB + resultSet.getString(AppConstants.IMAGE1).replace("~", "").replace(" ", "%20"));
                        imageList.add("http://" + AppConstants.IP + "/" + AppConstants.DB + resultSet.getString(AppConstants.IMAGE2).replace("~", "").replace(" ", "%20"));
                        imageList.add("http://" + AppConstants.IP + "/" + AppConstants.DB + resultSet.getString(AppConstants.IMAGE3).replace("~", "").replace(" ", "%20"));
                        p.setImages(imageList);
                    }
                    ArrayList<String> listSize = new ArrayList<>();
                    listSize.add(resultSet.getString(AppConstants.SIZE1));
                    listSize.add(resultSet.getString(AppConstants.SIZE2));
                    listSize.add(resultSet.getString(AppConstants.SIZE3));
                    listSize.add(resultSet.getString(AppConstants.SIZE4));
                    listSize.add(resultSet.getString(AppConstants.SIZE5));
                    listSize.add(resultSet.getString(AppConstants.SIZE6));

                    p.setSizes(listSize);

                    ArrayList<Integer> listSizeAvail = new ArrayList<>();
                    listSizeAvail.add(Integer.valueOf(resultSet.getString(AppConstants.SIZEAVAILABLE1)));
                    listSizeAvail.add(Integer.valueOf(resultSet.getString(AppConstants.SIZEAVAILABLE2)));
                    listSizeAvail.add(Integer.valueOf(resultSet.getString(AppConstants.SIZEAVAILABLE3)));
                    listSizeAvail.add(Integer.valueOf(resultSet.getString(AppConstants.SIZEAVAILABLE4)));
                    listSizeAvail.add(Integer.valueOf(resultSet.getString(AppConstants.SIZEAVAILABLE5)));
                    listSizeAvail.add(Integer.valueOf(resultSet.getString(AppConstants.SIZEAVAILABLE6)));

                    p.setSizeAvailable(listSizeAvail);
                    String ratingQuery = "Select AVG(ratting) as avg_rating from Eshop_Prod_rating_tb where prod_id=" + p.getId();
                    Statement ratingStatement = connection.createStatement();
                    ResultSet ratingSet = ratingStatement.executeQuery(ratingQuery);
                    while (ratingSet.next()) {
                        String ratingStirng = ratingSet.getString(AppConstants.AVGRATING);
                        if (ratingStirng != null)
                            p.setRating(Integer.parseInt(ratingStirng));
                    }
                    list.add(p);
                }
                connection.close();
                return list;
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
                return null;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AppHelper.isConnectingToInternet(ProductListActivity.this)) {
            new FetchbadgeNumber().execute();
        }else {
            Toast.makeText(ProductListActivity.this,"No internet Connection",Toast.LENGTH_SHORT).show();
        }
        //badgeCount = ItemCounter.getInstance().getItemCount();
        //invalidateOptionsMenu();
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
                SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
                String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
                String query = "SELECT COUNT(*) as count_row FROM Eshop_cart_tb where " + AppConstants.USERID + "='" + uniqueUserId + "'";
                Log.i("Query", query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
//                    badgeCount = resultSet.getRow();
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
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        invalidateOptionsMenu();
    }
}


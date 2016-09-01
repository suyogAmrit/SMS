package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.ProductListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Product;

import java.sql.Connection;
import java.sql.ResultSet;
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
        rvPdroducts = (RecyclerView) findViewById(R.id.rv_product);
        adapter = new ProductListAdapter(ProductListActivity.this);
        GridLayoutManager glm = new GridLayoutManager(ProductListActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rvPdroducts.setLayoutManager(glm);
        rvPdroducts.setAdapter(adapter);
        if (AppHelper.isConnectingToInternet(ProductListActivity.this)) {
            taskGetProducts = new GetProducts();
            taskGetProducts.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        ActionItemBadge.update(ProductListActivity.this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class GetProducts extends AsyncTask<Void, Void, ArrayList<Product>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductListActivity.this);
            dialog.setMessage(AppConstants.dialog_title);
            dialog.setMessage(AppConstants.PRDMSG);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productDetailses) {
            super.onPostExecute(productDetailses);
            dialog.dismiss();
            if (productDetailses.size() > 0) {
                adapter.addProducts(productDetailses);
            }
        }

        @Override
        protected ArrayList<Product> doInBackground(Void... voids) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "select * from Eshop_Prod_table where sub_cat_id='" + subCategory + "' and cat_id='" + category + "'";
                Log.i("PrdQry", query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<Product> list = new ArrayList<>();
                while (resultSet.next()) {
                    Product details = new Product();
                    details.setId(resultSet.getString(AppConstants.PRDID));
                    details.setSubCatId(resultSet.getString(AppConstants.SUBCATID));
                    details.setTitle(resultSet.getString(AppConstants.PRDTITLE));
                    details.setDesc(resultSet.getString(AppConstants.PRDDESC));
                    details.setShortDesc(resultSet.getString(AppConstants.SHRTDESC));
                    details.setSelId(resultSet.getString(AppConstants.SELLERID));
                    details.setStatus(resultSet.getString(AppConstants.STATUS));
                    details.setBrand(resultSet.getString(AppConstants.PRDBRAND));
                    details.setFromDate(resultSet.getString(AppConstants.PRDFRMDATE));
                    details.setToDate(resultSet.getString(AppConstants.PRDTODATE));
                    details.setPrice(resultSet.getString(AppConstants.PRDPRICE));
                    details.setAliasName(resultSet.getString(AppConstants.PRDALIASNAME));
                    details.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + resultSet.getString(AppConstants.PRDMAINIMAGE).replace("~", "").replace(" ", "%20"));
                    details.setQuantity(resultSet.getString(AppConstants.PRDQUNATITY));
                    details.setMaxQtyInCart(resultSet.getString(AppConstants.PRDMAXCARTQTY));
                    details.setShippingFee(resultSet.getString(AppConstants.PRDSHIPPINGFEE));
                    details.setServices(resultSet.getString(AppConstants.PRDSERVICES));
                    details.setFeatures(resultSet.getString(AppConstants.PRDFEATURES));
                    details.setOtherDetails(resultSet.getString(AppConstants.PRDOTHERDETAILS));
                    list.add(details);
                }
                return list;
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
                return null;
            }
        }
    }
}

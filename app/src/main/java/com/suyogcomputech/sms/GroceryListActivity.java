package com.suyogcomputech.sms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.adapter.GroceryListAdapter;
import com.suyogcomputech.adapter.ProductListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ProductDetails;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/7/2016.
 */
public class GroceryListActivity extends AppCompatActivity {
    String category,subCategory,subDesc;
    Toolbar tbProduct;
    RecyclerView rvPdroducts;
    GroceryListAdapter adapter;
    private ArrayList<ProductDetails> productDetailList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        category = getIntent().getExtras().getString(AppConstants.CATID);
        subCategory = getIntent().getExtras().getString(AppConstants.SUBCATID);
        subDesc = getIntent().getExtras().getString(AppConstants.SUBCATDESC);
        tbProduct = (Toolbar) findViewById(R.id.tb_product);

        TextView tvProdct = (TextView) findViewById(R.id.toolbar_title);
        tvProdct.setText(subDesc);
        setSupportActionBar(tbProduct);
        tbProduct.setTitle(subDesc);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productDetailList = new ArrayList<>();
        rvPdroducts = (RecyclerView) findViewById(R.id.rv_product);
        adapter = new GroceryListAdapter(GroceryListActivity.this);
//        GridLayoutManager glm = new GridLayoutManager(GroceryListActivity.this, 2, GridLayoutManager.VERTICAL, false);
//        rvPdroducts.setLayoutManager(glm);
        rvPdroducts.setAdapter(adapter);
    }
}

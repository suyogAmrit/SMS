package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.suyogcomputech.adapter.SearchAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.DividerItemDecoration;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity {
   private RecyclerView rcvSearchResultList;
    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter serachAdapter;
    private ProgressDialog dialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String CatId = "CatId";
    public static final String SubCatId = "SubCatId";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcvSearchResultList = (RecyclerView)findViewById(R.id.rcvSearchResultList);
        rcvSearchResultList.addItemDecoration(new DividerItemDecoration(this));
        layoutManager = new LinearLayoutManager(this);
        rcvSearchResultList.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.searc_menu,menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.item_search));
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
                if (AppHelper.isConnectingToInternet(SearchProductActivity.this)) {
                    if (newText.length()>=3) {
                        new SearchProductTask().execute(newText);
                    }
                }else {
                    Toast.makeText(SearchProductActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
    private class SearchProductTask extends AsyncTask<String,Void,ArrayList<ProductDetails>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SearchProductActivity.this);
            dialog.setTitle("Please wait.");
            dialog.setMessage("Searching for Products...");
            dialog.show();
        }

        @Override
        protected ArrayList<ProductDetails> doInBackground(String... params) {
            try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.connect();
            String query = "select * from Eshop_Prod_table where prod_title like '%"+params[0]+"%' or prod_brand like '%"+params[0]+"%'";
                Log.v("Qury",query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<ProductDetails> searchresults = new ArrayList<>();
                while (resultSet.next()){
                    ProductDetails productDetails = new ProductDetails();
                    Log.i("name", resultSet.getString("prod_title"));
                    productDetails.setTitle(resultSet.getString(AppConstants.PRDTITLE));
                    productDetails.setBrand(resultSet.getString(AppConstants.PRDBRAND));
                    searchresults.add(productDetails);
                }
                connection.close();
                return searchresults;
            } catch (SQLException e) {
                Log.i("Exception", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ProductDetails> productDetailses) {
            super.onPostExecute(productDetailses);
            dialog.dismiss();
            if (productDetailses.size()>0){
                Log.v("Size",""+productDetailses.size());
                serachAdapter  = new SearchAdapter(productDetailses,SearchProductActivity.this);
                rcvSearchResultList.setAdapter(serachAdapter);
            }
        }
    }

}

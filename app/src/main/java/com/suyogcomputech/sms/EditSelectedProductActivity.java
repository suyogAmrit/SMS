package com.suyogcomputech.sms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.adapter.CartItemAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/8/2016.
 */
public class EditSelectedProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtItemBrand;
    Spinner spnQuantity;
    ProductDetails productDetails;
    private RadioGroup productSizeRadioGroup;
    private RadioButton small, medium, large, extraLarge, doubleExtraLarge, tripleExtraLarge;
    private Button saveButton;
    private Integer[] qtySpinnerArrayValues = {1,2,3,4,5,6,7,8,9,10};
    private ArrayList<Integer> qtySpinnerValue ;
    ProgressDialog dialog;
    int quantity;
    String checkedItemSize;
    private ArrayList<ProductDetails>detailsArrayList;
    String prodId;
    ArrayAdapter<Integer> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        toolbar = (Toolbar) findViewById(R.id.toolbarEditProduct);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        txtItemBrand=(TextView)findViewById(R.id.txtBrandName);
        spnQuantity=(Spinner)findViewById(R.id.spnQuantity);
        productSizeRadioGroup = (RadioGroup)findViewById(R.id.product_size_radio_group);
        small = (RadioButton)findViewById(R.id.small);
        medium = (RadioButton)findViewById(R.id.mideuim);
        large = (RadioButton)findViewById(R.id.large);
        extraLarge = (RadioButton)findViewById(R.id.extraLarge);
        doubleExtraLarge = (RadioButton)findViewById(R.id.doubleExtraLarge);
        tripleExtraLarge = (RadioButton)findViewById(R.id.tripleExtraLarge);
        saveButton = (Button)findViewById(R.id.saveButton);
        productDetails = getIntent().getParcelableExtra(AppConstants.EXTRA_PRODUCT_EDIT);
        prodId = productDetails.getId();
        Log.v("","");
        updateUi();
        detailsArrayList = new ArrayList<>();
//        if (AppHelper.isConnectingToInternet(EditSelectedProductActivity.this)){
//            new FetchSizeAvaliabilityTask().execute();
//        }
    }

    private void updateUi() {
        dialog = new ProgressDialog(this);
        txtItemBrand.setText(productDetails.getBrand()+" "+productDetails.getTitle());
        new FtechcartSizeAvailibletask().execute();
        qtySpinnerValue = new ArrayList<>();
        small.setText("S");
        medium.setText("M");
        large.setText("L");
        extraLarge.setText("XL");
        doubleExtraLarge.setText("XXL");
        tripleExtraLarge.setText("XXXL");
        if (productDetails.getSizeProduct().equalsIgnoreCase("S")){
            small.setChecked(true);
        }else if (productDetails.getSizeProduct().equalsIgnoreCase("M")){
            medium.setChecked(true);
        }else if (productDetails.getSizeProduct().equalsIgnoreCase("L")){
            large.setChecked(true);
        }else if (productDetails.getSizeProduct().equalsIgnoreCase("XL")){
            extraLarge.setChecked(true);
        }else if (productDetails.getSizeProduct().equalsIgnoreCase("XXL")){
            doubleExtraLarge.setChecked(true);
        }else if (productDetails.getSizeProduct().equalsIgnoreCase("XXXL")){
            tripleExtraLarge.setChecked(true);
        }
        productSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.small){
                    qtySpinnerValue.clear();
                    adapter.clear();
                    checkedItemSize = "S";
                    small.setChecked(true);
                    int sizeS = detailsArrayList.get(0).getSizeAvailable().get(0);
                    for (int i=1;i<=sizeS;i++){
                        qtySpinnerValue.add(i);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (checkedId==R.id.mideuim){
                    qtySpinnerValue.clear();
                    adapter.clear();
                    checkedItemSize = "M";
                    medium.setChecked(true);
                    int sizeM = detailsArrayList.get(0).getSizeAvailable().get(1);
                    for (int i=1;i<=sizeM;i++){
                        qtySpinnerValue.add(i);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (checkedId==R.id.large){
                    qtySpinnerValue.clear();
                    adapter.clear();
                    checkedItemSize = "L";
                    large.setChecked(true);
                    int sizeL = detailsArrayList.get(0).getSizeAvailable().get(2);
                    for (int i=1;i<=sizeL;i++){
                        qtySpinnerValue.add(i);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (checkedId==R.id.extraLarge){
                    qtySpinnerValue.clear();
                    adapter.clear();
                    checkedItemSize = "XL";
                    extraLarge.setChecked(true);
                    int sizeXL = detailsArrayList.get(0).getSizeAvailable().get(3);
                    for (int i=1;i<=sizeXL;i++){
                        qtySpinnerValue.add(i);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (checkedId==R.id.doubleExtraLarge){
                    qtySpinnerValue.clear();
                    adapter.clear();
                    checkedItemSize = "XXL";
                    doubleExtraLarge.setChecked(true);
                    int sizeXXL = detailsArrayList.get(0).getSizeAvailable().get(4);
                    for (int i=1;i<=sizeXXL;i++){
                        qtySpinnerValue.add(i);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (checkedId==R.id.tripleExtraLarge){
                    qtySpinnerValue.clear();
                    adapter.clear();
                    checkedItemSize = "XXXL";
                    tripleExtraLarge.setChecked(true);
                    int sizeXXXL = detailsArrayList.get(0).getSizeAvailable().get(5);
                    for (int i=1;i<=sizeXXXL;i++){
                        qtySpinnerValue.add(i);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter = new ArrayAdapter<Integer>(EditSelectedProductActivity.this,android.R.layout.simple_spinner_item,android.R.id.text1,qtySpinnerValue);
        spnQuantity.setAdapter(adapter);
        spnQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quantity=Integer.parseInt(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //quantity = Integer.parseInt(spnQuantity.getSelectedItem().toString());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Quantity",String.valueOf(quantity));
                Log.i("Item Size",checkedItemSize);
                   new EditCartTask().execute();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null && dialog.isShowing()){
            dialog.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
    private class FetchSizeAvaliabilityTask extends AsyncTask<String,Void,String>{
        String resultstring="";
        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "" ;
                if (productDetails.getSizeProduct().equalsIgnoreCase("S")){
                    query = query+ "select size1_available from Eshop_Prod_Size_tb where size1='s' AND prod_id = "+productDetails.getId()+";";
                    resultstring = "size1_available";
                }else if (productDetails.getSizeProduct().equalsIgnoreCase("M")){
                    query = query+"select size2_available from Eshop_Prod_Size_tb where size2='m' AND prod_id = "+productDetails.getId()+";";
                    resultstring = "size2_available";
                }else if (productDetails.getSizeProduct().equalsIgnoreCase("L")){
                    query = query+ "select size3_available from Eshop_Prod_Size_tb where size3='L' AND prod_id = "+productDetails.getId()+";";
                    resultstring = "size3_available";
                }else if (productDetails.getSizeProduct().equalsIgnoreCase("XL")){
                    query = query+"select size4_available from Eshop_Prod_Size_tb where size4='XL' AND prod_id = "+productDetails.getId()+";";
                    resultstring = "size4_available";
                }else if (productDetails.getSizeProduct().equalsIgnoreCase("XXL")){
                    query = query+"select size5_available from Eshop_Prod_Size_tb where size5='XXL' AND prod_id = "+productDetails.getId()+";";
                    resultstring = "size5_available";
                }else if (productDetails.getSizeProduct().equalsIgnoreCase("XXXL")){
                    query = query+"select size6_available from Eshop_Prod_Size_tb where size6='XXXL' AND prod_id = "+productDetails.getId()+";";
                    resultstring = "size6_available";
                }
                Log.v("Query", query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Log.v("ResSet",resultSet.toString());
                String value = resultSet.getString(resultstring);
                connection.close();
                return value;
            }catch (SQLException s){
                s.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    private class FtechcartSizeAvailibletask extends AsyncTask<Void,Void,ArrayList<ProductDetails>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ProductDetails> doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String query = "select * from Eshop_Prod_Size_tb where prod_id="+prodId;
//                String query = "select size1,size1_available,size2,size2_available,size3,size3_available,size4,size4_available,size5,size5_available,size6,size6_available\n" +
//                        " from Eshop_Prod_Size_tb where prod_id="+prodId;
                Log.v("Query",query);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<ProductDetails> list = new ArrayList<>();
                while (resultSet.next()){
                    ProductDetails p = new ProductDetails();
                    ArrayList<String> listSize = new ArrayList<>();
                    p.setId(resultSet.getString(AppConstants.PRDID));
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
                    list.add(p);
                }
                connection.close();
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ProductDetails> productDetailses) {
            super.onPostExecute(productDetailses);
            if (productDetailses.size() > 0) {
                Log.v("", "" + productDetailses.size());
                detailsArrayList.addAll(productDetailses);
            }
        }
    }
    private class  EditCartTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait");
            dialog.setMessage("Saving...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                //Statement statement = connection.createStatement();
                String query = "Update Eshop_cart_tb set Quantity="+quantity+",size='"+checkedItemSize+"' where slno="+productDetails.getSerielNo();
                Log.v("Query",query);
                PreparedStatement statement = connection.prepareStatement(query);
                long resSet = statement.executeUpdate();
                Log.i("Result", String.valueOf(resSet));
                if (resSet==1){
                    return true;
                }
            }catch (SQLException e){
                e.printStackTrace();
                return null;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                dialog.dismiss();
                if (aBoolean){
                    Intent intent = new Intent(EditSelectedProductActivity.this, ShoppingCartItemActivity.class);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    Toast.makeText(EditSelectedProductActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){

            }

            //finish();
        }
    }
}

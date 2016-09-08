package com.suyogcomputech.sms;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.suyogcomputech.adapter.CartItemAdapter;
import com.suyogcomputech.helper.AppConstants;
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
                    checkedItemSize = "S";
                    small.setChecked(true);
                    int sizeS = detailsArrayList.get(0).getSizeAvailable().get(0);
                    for (int i=1;i<=sizeS;i++){
                        qtySpinnerValue.add(i);
                    }
                    Log.i("","");
                }
                if (checkedId==R.id.mideuim){
                    qtySpinnerValue.clear();
                    checkedItemSize = "M";
                    medium.setChecked(true);
                    int sizeM = detailsArrayList.get(0).getSizeAvailable().get(1);
                    for (int i=1;i<=sizeM;i++){
                        qtySpinnerValue.add(i);
                    }
                    Log.i("","");
                }
                if (checkedId==R.id.large){
                    qtySpinnerValue.clear();
                    checkedItemSize = "L";
                    large.setChecked(true);
                    int sizeL = detailsArrayList.get(0).getSizeAvailable().get(2);
                    for (int i=1;i<=sizeL;i++){
                        qtySpinnerValue.add(i);
                    }
                    Log.i("","");
                }
                if (checkedId==R.id.extraLarge){
                    qtySpinnerValue.clear();
                    checkedItemSize = "XL";
                    extraLarge.setChecked(true);
                    int sizeXL = detailsArrayList.get(0).getSizeAvailable().get(3);
                    for (int i=1;i<=sizeXL;i++){
                        qtySpinnerValue.add(i);
                    }
                    Log.i("","");
                }
                if (checkedId==R.id.doubleExtraLarge){
                    qtySpinnerValue.clear();
                    checkedItemSize = "XXL";
                    doubleExtraLarge.setChecked(true);
                    int sizeXXL = detailsArrayList.get(0).getSizeAvailable().get(4);
                    for (int i=1;i<=sizeXXL;i++){
                        qtySpinnerValue.add(i);
                    }
                    Log.i("","");
                }
                if (checkedId==R.id.tripleExtraLarge){
                    qtySpinnerValue.clear();
                    checkedItemSize = "XXXL";
                    tripleExtraLarge.setChecked(true);
                    int sizeXXXL = detailsArrayList.get(0).getSizeAvailable().get(5);
                    for (int i=1;i<=sizeXXXL;i++){
                        qtySpinnerValue.add(i);
                    }
                    Log.i("","");
                }
            }
        });
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,qtySpinnerValue);
        spnQuantity.setAdapter(adapter);
        //quantity = Integer.parseInt(spnQuantity.getSelectedItem().toString());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private class EditCartTask extends AsyncTask<Void,Void,Boolean>{
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
                String query = "Update Eshop_cart_tb set"+quantity+"size='"+checkedItemSize+"' where slno="+productDetails.getSerielNo();
                Log.v("Query",query);
                PreparedStatement statement = connection.prepareStatement(query);
                long resSet = statement.executeUpdate();
                Log.i("Result", String.valueOf(resSet));
            }catch (SQLException e){
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            //finish();
        }
    }
}

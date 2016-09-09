package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.suyogcomputech.adapter.ImagePagerAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ProductDetails;
import com.viewpagerindicator.CirclePageIndicator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by suyogcomputech on 31/08/16.
 */
public class ProductDetailsActivity extends AppCompatActivity {
    String productSize;
    //GetProductDetails taskGetProductDetails;
    ViewPager imgPager;
    private TextView product_title, product_price, product_cutting_price, productDiscount, product_offer_desc, sizeWithText;
    private Button infoButton, addToCartButton;
    private RadioGroup productSizeRadioGroup;
    private RadioButton small, medium, large, extraLarge, doubleExtraLarge, tripleExtraLarge;
    FrameLayout product_frame;
    ProductDetails productDetails;
    private BadgeStyle style = ActionItemBadge.BadgeStyles.RED.getStyle();
    private int badgeCount = 0;
    int selectedId=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productSize="";
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        product_title = (TextView ) findViewById(R.id.product_title);
        product_price = (TextView) findViewById(R.id.product_price);
        product_cutting_price = (TextView) findViewById(R.id.product_cutting_price);
        productDiscount = (TextView) findViewById(R.id.productDiscount);
        infoButton = (Button) findViewById(R.id.infoButton);
        product_offer_desc = (TextView) findViewById(R.id.product_offer_desc);
        productSizeRadioGroup = (RadioGroup) findViewById(R.id.product_size_radio_group);
        small = (RadioButton) findViewById(R.id.small);
        medium = (RadioButton) findViewById(R.id.mideuim);
        large = (RadioButton) findViewById(R.id.large);
        extraLarge = (RadioButton) findViewById(R.id.extraLarge);
        doubleExtraLarge = (RadioButton) findViewById(R.id.doubleExtraLarge);
        tripleExtraLarge = (RadioButton) findViewById(R.id.tripleExtraLarge);
        sizeWithText = (TextView) findViewById(R.id.sizeWithText);
        product_frame = (FrameLayout) findViewById(R.id.product_frame);
        imgPager = (ViewPager) findViewById(R.id.image_pager);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        /*productId = getIntent().getExtras().getString(AppConstants.PRDID);
        if (AppHelper.isConnectingToInternet(ProductDetailsActivity.this)) {
            taskGetProductDetails = new GetProductDetails();
            taskGetProductDetails.execute();
        }*/
//        if (AppHelper.isConnectingToInternet(ProductDetailsActivity.this)) {
//            new FetchbadgeNumber().execute();
//        }
        productDetails = getIntent().getParcelableExtra(AppConstants.EXTRA_PROCUCT_DETAILS);
        Log.v("", "");
        updateUi();
    }

    private void updateUi() {
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(ProductDetailsActivity.this, productDetails.getImages());
        imgPager.setAdapter(imagePagerAdapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(imgPager);
        Log.d("", productDetails.getImages().get(0));
        product_title.setText(productDetails.getBrand() + " " + productDetails.getTitle());
        product_cutting_price.setText(AppConstants.RUPEESYM + productDetails.getPrice());
        productDiscount.setText(Integer.valueOf(productDetails.getOfferPer()) + "% off");
        product_offer_desc.setText(productDetails.getOfferDesc());
        small.setText(productDetails.getSizes().get(0));
        medium.setText(productDetails.getSizes().get(1));
        large.setText(productDetails.getSizes().get(2));
        extraLarge.setText(productDetails.getSizes().get(3));
        doubleExtraLarge.setText(productDetails.getSizes().get(4));
        tripleExtraLarge.setText(productDetails.getSizes().get(5));
        if (productDetails.getFromDate() != null && productDetails.getToDate() != null) {
            if (AppHelper.compareDate(productDetails.getFromDate(), productDetails.getToDate())) {
                double actualPrice = Double.valueOf(productDetails.getPrice()) - (Double.valueOf(productDetails.getPrice()) * Double.valueOf(productDetails.getOfferPer())) / 100;
                product_price.setText(AppConstants.RUPEESYM + actualPrice);
                product_frame.setVisibility(View.VISIBLE);
                product_cutting_price.setText(AppConstants.RUPEESYM + productDetails.getPrice());
                productDiscount.setVisibility(View.VISIBLE);
            } else {
                product_price.setText(AppConstants.RUPEESYM + Double.valueOf(productDetails.getPrice()));
                product_frame.setVisibility(View.GONE);
                productDiscount.setVisibility(View.GONE);
            }
        } else {
            product_price.setText(AppConstants.RUPEESYM + Double.valueOf(productDetails.getPrice()));
            product_frame.setVisibility(View.GONE);
            productDiscount.setVisibility(View.GONE);
        }
        productSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.small) {
                    if (productDetails.getSizeAvailable().get(0) == 0) {
                        small.setEnabled(false);
                        small.setSelected(false);
                        sizeWithText.setText("Size " + productDetails.getSizes().get(0));
                    } else {
                        productSize="S";
                        sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(0) + " left");
                    }
                }
                if (checkedId == R.id.mideuim) {
                    if (productDetails.getSizeAvailable().get(1) == 0) {

                        small.setEnabled(false);
                        small.setSelected(false);
                        sizeWithText.setText("Size " + productDetails.getSizes().get(1));
                    } else {
                        productSize="M";
                        sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(1) + " left");
                    }
                }
                if (checkedId == R.id.large) {
                    if (productDetails.getSizeAvailable().get(2) == 0) {

                        small.setEnabled(false);
                        small.setSelected(false);
                        sizeWithText.setText("Size " + productDetails.getSizes().get(2));
                    } else {
                        productSize="L";
                        sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(2) + " left");
                    }
                }
                if (checkedId == R.id.extraLarge) {
                    if (productDetails.getSizeAvailable().get(3) == 0) {

                        small.setEnabled(false);
                        small.setSelected(false);
                        sizeWithText.setText("Size " + productDetails.getSizes().get(3));
                    } else {
                        productSize="XL";
                        sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(3) + " left");
                    }
                }
                if (checkedId == R.id.doubleExtraLarge) {
                    if (productDetails.getSizeAvailable().get(4) == 0) {
                        small.setEnabled(false);
                        small.setSelected(false);
                        sizeWithText.setText("Size " + productDetails.getSizes().get(4));
                    } else {
                        productSize="XXL";

                        sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(4) + " left");
                    }
                }
                if (checkedId == R.id.tripleExtraLarge) {
                    if (productDetails.getSizeAvailable().get(5) == 0) {

                        small.setEnabled(false);
                        small.setSelected(false);
                        sizeWithText.setText("Size " + productDetails.getSizes().get(5));
                    } else {
                        productSize="XXXL";
                        sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(5) + " left");
                    }
                }
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dilog = new AlertDialog.Builder(ProductDetailsActivity.this);
                dilog.setMessage(productDetails.getOtherDetails());
                dilog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dilog.create().show();
            }
        });
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = productSizeRadioGroup.getCheckedRadioButtonId();
                if (productSizeRadioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(ProductDetailsActivity.this, "Please select Size", Toast.LENGTH_SHORT).show();
                }else {
                    new AddCartTask().execute();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_product_cart, menu);
        ActionItemBadge.update(ProductDetailsActivity.this, menu.findItem(R.id.addcart), FontAwesome.Icon.faw_shopping_cart, style, badgeCount);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.addcart) {
            Intent intent = new Intent(ProductDetailsActivity.this,ShoppingCartItemActivity.class);
            //intent.putExtra(AppConstants.PROD_ID,productDetails.getId());
            startActivity(intent);
            return true;
        }
        return false;
    }
    private class AddCartTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailsActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.addingToCart);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
                String uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
                String query="update Eshop_cart_tb set Quantity=Quantity+1\n" +
                        "where user_id='"+uniqueUserId+"' and prod_id='"+productDetails.getId()+"' and size='"+productSize+"'";

                Log.i("query", query);
                PreparedStatement statement = connection.prepareStatement(query);
                long resSet = statement.executeUpdate();
                Log.i("Result", String.valueOf(resSet));
                if (resSet <= 0) {
                    String insertQuery = "insert into Eshop_cart_tb (user_id,prod_id,Quantity,Status,size) values ('" + uniqueUserId + "','" + productDetails.getId() + "','" + 1 + "','" + productDetails.getStatus()+ "','" +productSize +"'" + ");";
                    PreparedStatement statementInsert = connection.prepareStatement(insertQuery);
                    long resSetInsert = statementInsert.executeUpdate();
                    Log.i("Result", String.valueOf(resSetInsert));
                    if (resSetInsert>0){
                        connection.close();
                        return true;
                    }
                }else {
                    connection.close();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isExecuted) {
            super.onPostExecute(isExecuted);
            if (isExecuted) {
                Toast.makeText(ProductDetailsActivity.this, "Item Added successfully", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            new FetchbadgeNumber().execute();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AppHelper.isConnectingToInternet(ProductDetailsActivity.this)) {
            new FetchbadgeNumber().execute();
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
    /*private class GetProductDetails extends AsyncTask<Void, Void, ProductDetails>  {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailsActivity.this);
            dialog.setTitle(AppConstants.progress_dialog_title);
            dialog.setMessage(AppConstants.PRDDTLMSG);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final ProductDetails productDetails) {
            super.onPostExecute(productDetails);
            dialog.dismiss();
            try {
                Log.i("name", String.valueOf(productDetails.getImages().get(0)));



                if (productDetails.getFromDate()!=null && productDetails.getToDate()!=null) {
                    Date lastRange = AppHelper.getDateFromString(productDetails.getFromDate());
                    Date startrange = AppHelper.getDateFromString(productDetails.getToDate());
                    Log.d("llllllll", lastRange.toString());
                    Log.d("sssssss", startrange.toString());
                    Date currentdate = new Date();
                    Log.d("todaye", currentdate.toString());
                    if (lastRange.compareTo(currentdate)<0 && currentdate.compareTo(startrange)<0){
                        product_frame.setVisibility(View.VISIBLE);
                        int actualPrice = 0;
                        actualPrice = Integer.valueOf(productDetails.getPrice())-(Integer.valueOf(productDetails.getPrice())* Integer.valueOf(productDetails.getOfferPer()))/100;
                        product_price.setText(AppConstants.RUPEESYM +actualPrice);
                    }
                }else {
                    product_price.setText(AppConstants.RUPEESYM +productDetails.getPrice());
                    product_frame.setVisibility(View.GONE);
                }
                product_title.setText(productDetails.getBrand()+" "+productDetails.getTitle());
                product_cutting_price.setText(AppConstants.RUPEESYM +productDetails.getPrice());
                productDiscount.setText(Integer.valueOf(productDetails.getOfferPer())+"% off");
                product_offer_desc.setText(productDetails.getOfferDesc());
                small.setText(productDetails.getSizes().get(0));
                medium.setText(productDetails.getSizes().get(1));
                large.setText(productDetails.getSizes().get(2));
                extraLarge.setText(productDetails.getSizes().get(3));
                doubleExtraLarge.setText(productDetails.getSizes().get(4));
                tripleExtraLarge.setText(productDetails.getSizes().get(5));
                productSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId==R.id.small){
                            Toast.makeText(ProductDetailsActivity.this,""+productDetails.getSizes().get(0),Toast.LENGTH_SHORT).show();
                            if (productDetails.getSizeAvailable().get(0) == 0){
                                small.setEnabled(false);
                                small.setSelected(false);
                                sizeWithText.setText("Size "+productDetails.getSizes().get(0) );
                            }else {
                                sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(0) + " left");
                            }
                        }
                        if (checkedId==R.id.mideuim){
                            Toast.makeText(ProductDetailsActivity.this,""+productDetails.getSizes().get(1),Toast.LENGTH_SHORT).show();
                            if (productDetails.getSizeAvailable().get(1) == 0){
                                small.setEnabled(false);
                                small.setSelected(false);
                                sizeWithText.setText("Size "+productDetails.getSizes().get(1) );
                            }else {
                                sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(1) + " left");
                            }
                        }
                        if (checkedId==R.id.large){
                            Toast.makeText(ProductDetailsActivity.this,""+productDetails.getSizes().get(2),Toast.LENGTH_SHORT).show();
                            if (productDetails.getSizeAvailable().get(2) == 0){
                                small.setEnabled(false);
                                small.setSelected(false);
                                sizeWithText.setText("Size "+productDetails.getSizes().get(2) );
                            }else {
                                sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(2) + " left");
                            }
                        }
                        if (checkedId==R.id.extraLarge){
                            Toast.makeText(ProductDetailsActivity.this,""+productDetails.getSizes().get(3),Toast.LENGTH_SHORT).show();
                            if (productDetails.getSizeAvailable().get(3) == 0){
                                small.setEnabled(false);
                                small.setSelected(false);
                                sizeWithText.setText("Size "+productDetails.getSizes().get(3) );
                            }else {
                                sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(3) + " left");
                            }
                        }
                        if (checkedId==R.id.doubleExtraLarge){
                            Toast.makeText(ProductDetailsActivity.this,""+productDetails.getSizes().get(4),Toast.LENGTH_SHORT).show();
                            if (productDetails.getSizeAvailable().get(4) == 0){
                                small.setEnabled(false);
                                small.setSelected(false);
                                sizeWithText.setText("Size "+productDetails.getSizes().get(4) );
                            }else {
                                sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(4) + " left");
                            }
                        }
                        if (checkedId==R.id.tripleExtraLarge){
                            Toast.makeText(ProductDetailsActivity.this,""+productDetails.getSizes().get(5),Toast.LENGTH_SHORT).show();
                            if (productDetails.getSizeAvailable().get(5) == 0){
                                small.setEnabled(false);
                                small.setSelected(false);
                                sizeWithText.setText("Size "+productDetails.getSizes().get(5) );
                            }else {
                                sizeWithText.setText("Size    Only " + productDetails.getSizeAvailable().get(5) + " left");
                            }
                        }
                    }
                });
            } catch (NullPointerException e) {
                Toast.makeText(ProductDetailsActivity.this, "Please Try Again.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        protected ProductDetails doInBackground(Void... voids) {
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                ArrayList<String> imageList = new ArrayList<>();
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
                        " where p.prod_id=" + productId;

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Log.d("Product", resultSet + "");
                while (resultSet.next()) {

                    ProductDetails p = new ProductDetails();
                    Log.i("name", resultSet.getString("prod_title"));
                    p.setTitle(resultSet.getString(AppConstants.PRDTITLE));
                    p.setBrand(resultSet.getString(AppConstants.PRDBRAND));
                    p.setDesc(resultSet.getString(AppConstants.PRDDESC));
                    p.setId(productId);
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
                    p.setFromDate(resultSet.getString(AppConstants.OFRFROMDATE));
                    p.setImageStatus(resultSet.getString(AppConstants.PRDIMAGESTATUS));
                    p.setMainImage("http://" + AppConstants.IP + "/" + AppConstants.DB + resultSet.getString(AppConstants.PRDMAINIMAGE).replace("~", "").replace(" ", "%20"));
                    p.setSellerName(resultSet.getString(AppConstants.SELLOR_NAME));
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

                    String ratingQuery = "Select AVG(ratting) as avg_rating from Eshop_Prod_rating_tb where prod_id=" + productId;
                    ResultSet ratingSet = statement.executeQuery(ratingQuery);
                    while (ratingSet.next()) {
                        String ratingStirng = ratingSet.getString(AppConstants.AVGRATING);
                        if (ratingStirng != null)
                            p.setRating(Integer.parseInt(ratingStirng));
                    }
                    connection.close();
                    return p;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }


    }*/
}

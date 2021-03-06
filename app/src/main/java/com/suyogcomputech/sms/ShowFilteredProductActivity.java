package com.suyogcomputech.sms;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.suyogcomputech.adapter.ProductListAdapter;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.ProductDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Suyog on 9/23/2016.
 */
public class ShowFilteredProductActivity extends AppCompatActivity {
    RecyclerView rvPdroducts;
    ProductListAdapter adapter;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfilterd);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvPdroducts = (RecyclerView) findViewById(R.id.rv_product);
        adapter = new ProductListAdapter(ShowFilteredProductActivity.this);
        GridLayoutManager glm = new GridLayoutManager(ShowFilteredProductActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rvPdroducts.setLayoutManager(glm);
        rvPdroducts.setAdapter(adapter);
        dialog = new ProgressDialog(ShowFilteredProductActivity.this);
        Bundle b = getIntent().getExtras();
        String brand = b.getString(AppConstants.PRDBRAND);
        String title = b.getString(AppConstants.PRDTITLE);
        if (AppHelper.isConnectingToInternet(ShowFilteredProductActivity.this)) {
            new ShowFilteredProducts().execute(brand, title);
        }else {
            Toast.makeText(ShowFilteredProductActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
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

    private class ShowFilteredProducts extends AsyncTask<String, Void, ArrayList<ProductDetails>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Please wait.");
            dialog.setMessage("Fetching Products...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ProductDetails> productDetailses) {
            super.onPostExecute(productDetailses);
            dialog.dismiss();
            if (productDetailses != null && productDetailses.size() > 0) {
                adapter.addProducts(productDetailses);
            }
        }

        @Override
        protected ArrayList<ProductDetails> doInBackground(String... params) {
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
                        " where p.prod_brand='" + params[0].replace("'", "''") + "' AND p.prod_title='" + params[1].replace("'", "''") + "'";
                /*SharedPreferences shr = getSharedPreferences(AppConstants.SORTPREFS, MODE_PRIVATE);
                int sortDesc = shr.getInt(AppConstants.SORTDESC, 0);
                switch (sortDesc) {
                    case 1:
                        query = query + " ORDER BY p.price ASC";
                        break;
                    case 2:
                        query = query + " ORDER BY p.price DESC";
                        break;
                }*/
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
}

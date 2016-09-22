package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.EditSelectedProductActivity;
import com.suyogcomputech.sms.R;
import com.suyogcomputech.sms.ShoppingCartItemActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Suyog on 9/7/2016.
 */
public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    ArrayList<ProductDetails> myItems;
    private Context context;
    int focusedItem = 0;
    public  static final int TYPE_ITEMS = 0;
    public  static final int TYPE_CALCULATE = 1;

    public CartItemAdapter(Context context,ArrayList<ProductDetails> list) {
        this.context = context;
        this.myItems=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.item_product_details, parent, false);
//        return new CartViewHolder(view);
        View view;
        if (viewType == TYPE_ITEMS){
            view = LayoutInflater.from(context).inflate(R.layout.item_product_details,parent,false);
            return new CartViewHolder(view);
        }if (viewType == TYPE_CALCULATE){
            view = LayoutInflater.from(context).inflate(R.layout.total_calculation_layout,parent,false);
            return new TotalViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof CartViewHolder){
                    CartViewHolder cartViewHolder = (CartViewHolder) holder;
                final ProductDetails myItem = myItems.get(position);
                holder.itemView.setSelected(focusedItem == position);
                holder.getLayoutPosition();
                    cartViewHolder.txtBrandName.setText(myItem.getBrand()+" "+myItem.getTitle());
                    cartViewHolder.txtBrandSize.setText("Size "+myItem.getSizeProduct());
                    cartViewHolder.txtDisPer.setText("("+myItem.getOfferPer()+"% off)");
                    cartViewHolder.txtNo.setText(myItem.getQuantity());
                Picasso.with(context).load(myItem.getMainImage()).into(cartViewHolder.imgProduct);
                if (myItem.getFromDate() != null && myItem.getToDate() != null && AppHelper.compareDate(myItem.getFromDate(), myItem.getToDate())) {
                    double actualPrice = Double.valueOf(myItem.getPrice()) - (Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer())) / 100;
                    double finalPrice = actualPrice * Integer.valueOf(myItem.getQuantity());
                    cartViewHolder.txtProdPrice.setText(AppConstants.RUPEESYM + finalPrice);
                    cartViewHolder.frameItem.setVisibility(View.VISIBLE);
                    cartViewHolder.txtDisPer.setVisibility(View.VISIBLE);
                }else {
                    double finalRupee = Double.valueOf(myItem.getPrice()) * Integer.valueOf(myItem.getQuantity());
                    //holder.txtProdPrice.setText(AppConstants.RUPEESYM + Double.valueOf(myItem.getPrice()));
                    cartViewHolder.txtProdPrice.setText(AppConstants.RUPEESYM + finalRupee);
                    cartViewHolder.frameItem.setVisibility(View.GONE);
                    cartViewHolder.txtDisPer.setVisibility(View.GONE);
                }
                //holder.txtProdPrice.setText(myItem.getPrice());
                double finalAmount = Double.valueOf(myItem.getPrice()) * Integer.valueOf(myItem.getQuantity());
               // holder.txtOffer.setText(myItem.getPrice());
                    cartViewHolder.txtOffer.setText(""+finalAmount);
                    cartViewHolder.editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetails productDetail = myItems.get(holder.getAdapterPosition());
                        Intent intent = new Intent(context, EditSelectedProductActivity.class);
                        intent.putExtra(AppConstants.EXTRA_PRODUCT_EDIT,productDetail);
                        Log.i("ProdId",productDetail.getId());
                        ((ShoppingCartItemActivity) context).startActivityForResult(intent, 1);
                    }
                });
                    cartViewHolder.removeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppHelper.isConnectingToInternet(context)) {
                            ((ShoppingCartItemActivity) context).deleteItem(myItem.getSerielNo());
                        }else {
                            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        }
        if (holder instanceof  TotalViewHolder){
            final ProductDetails myItem = myItems.get(position-1);
            TotalViewHolder totalViewHolder = (TotalViewHolder) holder;
            double amount = 0;
                for (int j = 0; j < myItems.size(); j++) {
                    amount = amount + Double.valueOf(myItems.get(j).getPrice());
                }
            //double amountlast = amount+ Double.valueOf(myItems.get(myItems.size()-1).getPrice());
            totalViewHolder.txtCartTotal.setText(AppConstants.RUPEESYM+amount);
            double actualPrice = Double.valueOf(myItem.getPrice()) - (Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer())) / 100;
            double finalPrice = actualPrice * Integer.valueOf(myItem.getQuantity());
//            double disprice = 0;
//            for (int k = 0;k<myItems.size();k++){
//                disprice = disprice + (Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer()));
//            }
            totalViewHolder.txtDiscountTotal.setText(AppConstants.RUPEESYM+"0.00");
            totalViewHolder.txtTotalPayble.setText(AppConstants.RUPEESYM+amount);
//            double finalPayble = amount - disprice;
//            totalViewHolder.txtTotalPayble.setText(AppConstants.RUPEESYM+finalPayble);
        }
     }

    @Override
    public int getItemCount() {
        return myItems.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position==myItems.size() ? TYPE_CALCULATE:TYPE_ITEMS;
    }

    public void totalPrice(){
        double price = 0;
        for (int k=0; k<myItems.size();k++){
            price = price+ Double.valueOf(myItems.get(k).getPrice());
        }
        ShoppingCartItemActivity.txtCartTotal.setText(AppConstants.RUPEESYM+price);
    }
    public double getSum(double sum){
        for(ProductDetails data: myItems){
            sum = sum+ Double.valueOf(data.getPrice());
        }
        return sum;
    }
    public class CartViewHolder extends ViewHolder{
        ImageView imgProduct;
        TextView txtBrandName,txtBrandSize,txtProdPrice,txtDisPer,txtNo,txtOffer,removeText,editText;
        FrameLayout frameItem;
        public CartViewHolder(View itemView) {
            super(itemView);
            imgProduct = (ImageView)itemView.findViewById(R.id.imgProduct);
            txtBrandName = (TextView)itemView.findViewById(R.id.txtBrandName);
            txtBrandSize = (TextView)itemView.findViewById(R.id.txtBrandSize);
            txtProdPrice = (TextView)itemView.findViewById(R.id.txtProdPrice);
            txtDisPer = (TextView)itemView.findViewById(R.id.txtDisPer);
            txtNo = (TextView)itemView.findViewById(R.id.txtNo);
            txtOffer = (TextView)itemView.findViewById(R.id.txtOffer);
            frameItem = (FrameLayout)itemView.findViewById(R.id.frameItem);
            removeText = (TextView)itemView.findViewById(R.id.removeText);
            editText = (TextView)itemView.findViewById(R.id.editText);
        }
    }
    public class TotalViewHolder extends ViewHolder{
       TextView txtCartTotal,txtDiscountTotal,txtSubTotal,txtTotalPayble;
        public TotalViewHolder(View itemView) {
            super(itemView);
            txtCartTotal = (TextView)itemView.findViewById(R.id.txtCartTotal);
            txtDiscountTotal = (TextView)itemView.findViewById(R.id.txtDiscountTotal);
            txtSubTotal = (TextView)itemView.findViewById(R.id.txtSubTotal);
            txtTotalPayble = (TextView)itemView.findViewById(R.id.txtTotalPayble);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}

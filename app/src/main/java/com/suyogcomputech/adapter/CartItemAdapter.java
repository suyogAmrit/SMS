package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder>{
    ArrayList<ProductDetails> myItems;
    private Context context;
    int focusedItem = 0;

    public CartItemAdapter(Context context,ArrayList<ProductDetails> list) {
        this.context = context;
        this.myItems=list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product_details, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, int position) {
        final ProductDetails myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();
        holder.txtBrandName.setText(myItem.getBrand()+" "+myItem.getTitle());
        holder.txtBrandSize.setText("Size "+myItem.getSizeProduct());
        holder.txtDisPer.setText("("+myItem.getOfferPer()+"% off)");
        holder.txtNo.setText(myItem.getQuantity());
        Picasso.with(context).load(myItem.getMainImage()).into(holder.imgProduct);
        if (myItem.getFromDate() != null && myItem.getToDate() != null && AppHelper.compareDate(myItem.getFromDate(), myItem.getToDate())) {
            double actualPrice = Double.valueOf(myItem.getPrice()) - (Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer())) / 100;
            double finalPrice = actualPrice * Integer.valueOf(myItem.getQuantity());
            holder.txtProdPrice.setText(AppConstants.RUPEESYM + finalPrice);
            holder.frameItem.setVisibility(View.VISIBLE);
            holder.txtDisPer.setVisibility(View.VISIBLE);
        }else {
            double finalRupee = Double.valueOf(myItem.getPrice()) * Integer.valueOf(myItem.getQuantity());
            //holder.txtProdPrice.setText(AppConstants.RUPEESYM + Double.valueOf(myItem.getPrice()));
            holder.txtProdPrice.setText(AppConstants.RUPEESYM + finalRupee);
            holder.frameItem.setVisibility(View.GONE);
            holder.txtDisPer.setVisibility(View.GONE);
        }
        //holder.txtProdPrice.setText(myItem.getPrice());
        double finalAmount = Double.valueOf(myItem.getPrice()) * Integer.valueOf(myItem.getQuantity());
       // holder.txtOffer.setText(myItem.getPrice());
        holder.txtOffer.setText(""+finalAmount);
        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetails productDetail = myItems.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, EditSelectedProductActivity.class);
                intent.putExtra(AppConstants.EXTRA_PRODUCT_EDIT,productDetail);
                Log.i("ProdId",productDetail.getId());
                context.startActivity(intent);
            }
        });
        holder.removeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShoppingCartItemActivity)context).deleteItem(myItem.getSerielNo());

            }
        });
     }

    @Override
    public int getItemCount() {
        return myItems.size();
    }
    public void totalPrice(){
        double price = 0;
        for (int k=0; k<myItems.size();k++){
            price = price+ Double.valueOf(myItems.get(k).getPrice());
        }
        ShoppingCartItemActivity.txtCartTotal.setText(AppConstants.RUPEESYM+price);
    }
    public class CartViewHolder extends RecyclerView.ViewHolder{
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

}

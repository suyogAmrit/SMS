package com.suyogcomputech.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.R;

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
    public void onBindViewHolder(CartViewHolder holder, int position) {
        final ProductDetails myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();

        holder.txtBrandName.setText(myItem.getBrand()+" "+myItem.getTitle());
        holder.txtBrandSize.setText("Size "+myItem.getSizeProduct());
        holder.txtDisPer.setText(myItem.getOfferPer()+"% off");
        holder.txtNo.setText(myItem.getQuantity());
        Picasso.with(context).load(myItem.getMainImage()).into(holder.imgProduct);
        holder.txtProdPrice.setText(myItem.getPrice());
     }

    @Override
    public int getItemCount() {
        return myItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView txtBrandName,txtBrandSize,txtProdPrice,txtDisPer,txtNo;
        public CartViewHolder(View itemView) {
            super(itemView);
            imgProduct = (ImageView)itemView.findViewById(R.id.imgProduct);
            txtBrandName = (TextView)itemView.findViewById(R.id.txtBrandName);
            txtBrandSize = (TextView)itemView.findViewById(R.id.txtBrandSize);
            txtProdPrice = (TextView)itemView.findViewById(R.id.txtProdPrice);
            txtDisPer = (TextView)itemView.findViewById(R.id.txtDisPer);
            txtNo = (TextView)itemView.findViewById(R.id.txtNo);
        }
    }
}

package com.suyogcomputech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/22/2016.
 */

public class OrderListAdapter  extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{
    private ArrayList<ProductDetails> detailses;
    private Context context;
    public OrderListAdapter(ArrayList<ProductDetails> detailses,Context context1) {
        this.detailses = detailses;
        this.context = context1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtProdTitleBrand.setText(detailses.get(position).getTitle() +" " + detailses.get(position).getBrand());
        holder.txtProdPrice.setText(AppConstants.RUPEESYM+detailses.get(position).getPrice());
        holder.txtProdQty.setText("Quantity: "+detailses.get(position).getQuantity());
        holder.txtProdSize.setText("Size: "+detailses.get(position).getSizeProduct());
        Picasso.with(context).load(detailses.get(position).getMainImage().toString()).resize(400,400).placeholder(android.R.drawable.ic_menu_gallery).into(holder.imgOrderedProduct);
        holder.txtOrderName.setText("Name: " +detailses.get(position).getOrderplaceHolderName());
        holder.txtOrderEmail.setText("Email: "+detailses.get(position).getOrderPlaceHolderEmail());
        holder.txtOrderAddr.setText("Address: " +detailses.get(position).getOrderPlaceHolderAddr());
        holder.txtOrderPhone.setText("Phone: " + detailses.get(position).getOrderPlaceHolderPhone());
        holder.txtOrderId.setText("ORDERED ID: "+detailses.get(position).getUniqueId());
        if (!TextUtils.isEmpty(detailses.get(position).getOrderedDate())) {
            holder.txtOrderedDate.setText("Date: " + AppHelper.getDateFromString(detailses.get(position).getOrderedDate()));
        }
    }

    @Override
    public int getItemCount() {
        return detailses.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView txtProdTitleBrand,txtProdPrice,txtProdQty,txtProdSize,txtOrderName,txtOrderEmail,txtOrderAddr,txtOrderPhone,txtOrderId,txtOrderedDate;
        private ImageView imgOrderedProduct;
       public ViewHolder(View itemView) {
           super(itemView);
           txtProdTitleBrand = (TextView)itemView.findViewById(R.id.txtProdTitleBrand);
           txtProdPrice = (TextView)itemView.findViewById(R.id.txtProdPrice);
           txtProdQty = (TextView)itemView.findViewById(R.id.txtProdQty);
           txtProdSize = (TextView)itemView.findViewById(R.id.txtProdSize);
           imgOrderedProduct = (ImageView)itemView.findViewById(R.id.imgOrderedProduct);
           txtOrderName = (TextView)itemView.findViewById(R.id.txtOrderName);
           txtOrderEmail = (TextView)itemView.findViewById(R.id.txtOrderEmail);
           txtOrderAddr = (TextView)itemView.findViewById(R.id.txtOrderAddr);
           txtOrderPhone = (TextView)itemView.findViewById(R.id.txtOrderPhone);
           txtOrderId = (TextView)itemView.findViewById(R.id.txtOrderId);
           txtOrderedDate = (TextView)itemView.findViewById(R.id.txtOrderedDate);
       }
   }
}

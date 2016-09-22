package com.suyogcomputech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/10/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder>{
    private Context context;
    private ArrayList<ProductDetails>detailses;

    public OrderAdapter(Context context, ArrayList<ProductDetails> detailses) {
        this.context = context;
        this.detailses = detailses;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product_details, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        Picasso.with(context).load(detailses.get(position).getMainImage()).resize(400,400).placeholder(R.drawable.ic_empty).into(holder.imgOrder);
        holder.txtOrderName.setText(detailses.get(position).getTitle()+ " " +detailses.get(position).getBrand());
        holder.txtOrderQuantity.setText(detailses.get(position).getQuantity());
        holder.txtOrderSize.setText(detailses.get(position).getSizeProduct());
    }

    @Override
    public int getItemCount() {
        return detailses.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        TextView txtOrderName,txtOrderQuantity,txtOrderSize,txtOrderPrice;
        ImageView imgOrder;
        public OrderHolder(View itemView) {
            super(itemView);
            txtOrderName = (TextView)itemView.findViewById(R.id.txtOrderName);
            txtOrderQuantity = (TextView)itemView.findViewById(R.id.txtOrderQuantity);
            txtOrderSize = (TextView)itemView.findViewById(R.id.txtOrderSize);
            txtOrderPrice = (TextView)itemView.findViewById(R.id.txtOrderPrice);
            imgOrder = (ImageView)itemView.findViewById(R.id.imgOrder);
        }
    }
}

package com.suyogcomputech.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suyogcomputech.sms.R;

/**
 * Created by Suyog on 9/10/2016.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder>{
    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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

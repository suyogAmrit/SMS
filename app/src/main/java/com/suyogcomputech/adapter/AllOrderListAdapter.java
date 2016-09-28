package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.suyogcomputech.helper.OrderList;
import com.suyogcomputech.sms.GroceryHistoryActivity;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Pintu on 9/27/2016.
 */
public class AllOrderListAdapter extends RecyclerView.Adapter<AllOrderListAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderList> arrayOrder;
    public AllOrderListAdapter(Context c, ArrayList<OrderList> arrayOrder1) {
        context=c;
        arrayOrder=arrayOrder1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.all_order_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderList olist=new OrderList();
        olist=arrayOrder.get(position);
        holder.order_id.setText(olist.getOrder_id());
        holder.order_date.setText(olist.getOrder_date());
        holder.order_qty.setText(olist.getQuantity());
        if(olist.getStatus().equals("1"))
            holder.order_statuss.setText("Order Pending");
        else if(olist.getOrder_date().equals("2"))
            holder.order_statuss.setText("Order Complete");

        final OrderList finalOlist = olist;
        holder.btn_view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,GroceryHistoryActivity.class);
                intent.putExtra("order_id", finalOlist.getOrder_id());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id,order_date,order_qty,order_statuss;
        Button btn_view_order;
        public ViewHolder(View itemView) {
            super(itemView);
            order_id=(TextView)itemView.findViewById(R.id.order_id);
            order_date=(TextView)itemView.findViewById(R.id.order_date);
            order_qty=(TextView)itemView.findViewById(R.id.order_qty);
            order_statuss=(TextView)itemView.findViewById(R.id.order_statuss);
            btn_view_order=(Button)itemView.findViewById(R.id.btn_view_order);
        }
    }
}

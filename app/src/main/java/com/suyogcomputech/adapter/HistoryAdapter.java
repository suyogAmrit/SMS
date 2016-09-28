package com.suyogcomputech.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suyogcomputech.helper.GroceryHistory;
import com.suyogcomputech.sms.GroceryHistoryActivity;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Pintu on 9/27/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VieWholder> {
    Context context;
    ArrayList<GroceryHistory> arrayHistory;

    public HistoryAdapter(Context c, ArrayList<GroceryHistory> aHistory) {
        context=c;
        arrayHistory=aHistory;
    }

    @Override
    public VieWholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Fresco.initialize(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grocery_history_adapter, parent, false);
        return new VieWholder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.VieWholder holder, int position) {
        GroceryHistory groceryHistory=arrayHistory.get(position);
        holder.image.setImageURI(Uri.parse(groceryHistory.getImageUrl()));
        holder.prod_title.setText(groceryHistory.getTitle());
        holder.prod_quantity.setText(groceryHistory.getQuantity());
//        holder.prod_price.setText(groceryHistory.getPrice());
        float prices=Float.valueOf(groceryHistory.getQuantity())*Float.valueOf(groceryHistory.getPrice());
        holder.prod_price.setText(String.valueOf(prices));

    }


    @Override
    public int getItemCount() {
        return arrayHistory.size();
    }

    public class VieWholder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView prod_title,prod_quantity,prod_price;
        public VieWholder(View itemView) {
            super(itemView);
            image=(SimpleDraweeView)itemView.findViewById(R.id.iv_history);
            prod_title=(TextView)itemView.findViewById(R.id.prod_title);
            prod_quantity=(TextView)itemView.findViewById(R.id.prod_quantity);
            prod_price=(TextView)itemView.findViewById(R.id.prod_price);
//            prod_status=(TextView)itemView.findViewById(R.id.prod_status);
        }
    }
}

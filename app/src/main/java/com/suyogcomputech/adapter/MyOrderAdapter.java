package com.suyogcomputech.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by suyogcomputech on 12/08/16.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ShowTariffsViewHolder> {
    List<Doctor> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public MyOrderAdapter(List<Doctor> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public MyOrderAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_my_order, null);
        MyOrderAdapter.ShowTariffsViewHolder holder = new MyOrderAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyOrderAdapter.ShowTariffsViewHolder holder, int position) {
        final Doctor myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        holder.tvName.setText(myItem.getName());
        holder.tvDesignation.setText(myItem.getType());



    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDesignation;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.txtName);
            this.tvDesignation=(TextView)itemView.findViewById(R.id.txtDesignation);

        }
    }
}


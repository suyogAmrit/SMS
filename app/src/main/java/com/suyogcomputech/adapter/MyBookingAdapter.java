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
import com.suyogcomputech.helper.Event;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by Pintu on 8/25/2016.
 */
public class MyBookingAdapter  extends RecyclerView.Adapter<MyBookingAdapter.ShowTariffsViewHolder> {
    List<Event> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public MyBookingAdapter(List<Event> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public MyBookingAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_my_event_booking, null);
        MyBookingAdapter.ShowTariffsViewHolder holder = new MyBookingAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyBookingAdapter.ShowTariffsViewHolder holder, int position) {
        final Event myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();
        holder.txtEventName.setText(myItem.getEventName());
        holder.txtDate.setText("Date : "+myItem.getEventDate());
        holder.txtCompanyName.setText(myItem.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView txtEventName,txtDate,txtCompanyName;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.txtEventName=(TextView)itemView.findViewById(R.id.txtEventType);
            this.txtDate=(TextView)itemView.findViewById(R.id.txtDate);
            this.txtCompanyName=(TextView)itemView.findViewById(R.id.txtCompanyName);

        }
    }
}


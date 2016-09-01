package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.Event;
import com.suyogcomputech.helper.Grocery;
import com.suyogcomputech.helper.MySingleton;
import com.suyogcomputech.sms.EventDetailsActivity;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by Pintu on 8/23/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ShowTariffsViewHolder> {
    List<Event> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public EventAdapter(List<Event> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public EventAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_event, null);
        EventAdapter.ShowTariffsViewHolder holder = new EventAdapter.ShowTariffsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ShowTariffsViewHolder holder, int position) {
        final Event myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        myImageLoader = MySingleton.getInstance(myContext).getImageLoader();
        holder.tvEventName.setText(myItem.getEventName());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(myItem.getEventImage(), opts);
        opts.inJustDecodeBounds = false;

        Picasso.with(myContext)
                .load("http://192.168.12.100/APMS"+myItem.getEventImage())
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.backgroundd)
                .resize(screenWidth / 2, 300)
                .centerCrop()
                .into((holder.imgEvent));
        holder.imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(myContext, EventDetailsActivity.class);
                intent.putExtra(AppConstants.EVENT_ID,myItem.getEventId());
                intent.putExtra(AppConstants.EVENT_TYPE,myItem.getEventName());
                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName;
        ImageView imgEvent;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.tvEventName = (TextView) itemView.findViewById(R.id.txtEventName);
            this.imgEvent = (ImageView) itemView.findViewById(R.id.imgEvent);
        }
    }
}


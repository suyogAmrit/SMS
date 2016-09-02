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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.Lawyer;
import com.suyogcomputech.helper.MySingleton;
import com.suyogcomputech.sms.DoctorActivity;
import com.suyogcomputech.sms.LawyerActivity;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by Suyog on 9/2/2016.
 */
public class LawyerSpecialistAdapter extends RecyclerView.Adapter<LawyerSpecialistAdapter.DoctorHolder> {
    List<Lawyer> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public LawyerSpecialistAdapter(List<Lawyer> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public DoctorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_event, null);
        DoctorHolder holder = new DoctorHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DoctorHolder holder, int position) {
        final Lawyer myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        myImageLoader = MySingleton.getInstance(myContext).getImageLoader();
        holder.tvSpecialistType.setText(myItem.getLawyerSpecialist());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(myItem.getSpecialistImageUrl(), opts);
        opts.inJustDecodeBounds = false;

        Picasso.with(myContext)
                .load("http://192.168.12.100/APMS" + myItem.getSpecialistImageUrl())
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.backgroundd)
                .resize(screenWidth / 2, 300)
                .centerCrop()
                .into((holder.imgSpecialist));
        holder.imgSpecialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SpecialistId", myItem.getSpecialistId());
                Intent intent = new Intent(myContext, LawyerActivity.class);
                intent.putExtra(AppConstants.SPECIALIST_ID, myItem.getSpecialistId());
                intent.putExtra(AppConstants.SPECIALIST_NAME, myItem.getLawyerSpecialist());
                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class DoctorHolder extends RecyclerView.ViewHolder {
        TextView tvSpecialistType;
        ImageView imgSpecialist;

        public DoctorHolder(View itemView) {
            super(itemView);
            this.tvSpecialistType = (TextView) itemView.findViewById(R.id.txtEventName);
            this.imgSpecialist = (ImageView) itemView.findViewById(R.id.imgEvent);
        }
    }
}
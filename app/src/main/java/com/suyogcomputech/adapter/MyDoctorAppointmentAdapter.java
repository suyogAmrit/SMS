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
 * Created by Pintu on 8/29/2016.
 */
public class MyDoctorAppointmentAdapter extends RecyclerView.Adapter<MyDoctorAppointmentAdapter.ShowTariffsViewHolder> {
    List<Doctor> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public MyDoctorAppointmentAdapter(List<Doctor> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public MyDoctorAppointmentAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_booking_doctor, null);
        MyDoctorAppointmentAdapter.ShowTariffsViewHolder holder = new MyDoctorAppointmentAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyDoctorAppointmentAdapter.ShowTariffsViewHolder holder, int position) {
        final Doctor myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();
        holder.txtDoctorName.setText(myItem.getName());
        holder.txtDate.setText("Date : "+myItem.getRequestDate());
        if (myItem.getStatus().equals("0")){
            holder.txtStatus.setText("");
        }else {
            holder.txtStatus.setText("Conform");
        }
    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView txtDoctorName,txtDate,txtStatus;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.txtDoctorName=(TextView)itemView.findViewById(R.id.txtDoctorName);
            this.txtDate=(TextView)itemView.findViewById(R.id.txtRequestDate);
            this.txtStatus=(TextView)itemView.findViewById(R.id.txtStatus);

        }
    }
}

package com.suyogcomputech.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.suyogcomputech.helper.Appointment;
import com.suyogcomputech.sms.BookDoctorActivity;
import com.suyogcomputech.sms.BookLawyerActivity;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;


public class BookLawyerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Appointment> list;
    String startTime,endTime,place,price;
//
    public BookLawyerAdapter(Context context, ArrayList<Appointment> appointmentsList)  {
        super();
        this.context = context;
        this.list = appointmentsList;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
        TextView txtStartTime, txtEndTime, txtPlace, txtPrice;
        Button btnBookNow;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_book_doctor, null);
            holder = new ViewHolder();
            holder.txtStartTime = (TextView) convertView.findViewById(R.id.txtStartTime);
            holder.txtEndTime = (TextView) convertView.findViewById(R.id.txtEndTime);
            holder.txtPlace = (TextView) convertView.findViewById(R.id.txtPlace);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.btnBookNow=(Button)convertView.findViewById(R.id.btnBookNow);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        startTime=list.get(position).getStartTime();
        endTime=list.get(position).getEndTime();
        place=list.get(position).getPlace();
        price=list.get(position).getPrice();
        holder.txtStartTime.setText(startTime);
        holder.txtEndTime.setText(endTime);
        holder.txtPlace.setText(place);
        holder.txtPrice.setText(price);
        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("BookId",list.get(position).getScheduleID());
                ((BookLawyerActivity) context).insertAppointmentData(list.get(position).getScheduleID());
            }
        });

        return convertView;
    }

}
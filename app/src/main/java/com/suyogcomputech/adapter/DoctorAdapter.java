package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.MySingleton;
import com.suyogcomputech.sms.BookDoctorActivity;
import com.suyogcomputech.sms.DoctorDetailsActivity;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by Pintu on 8/11/2016.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorHolder> {
    List<Doctor> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public DoctorAdapter(List<Doctor> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public DoctorAdapter.DoctorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_doctor, null);
        DoctorAdapter.DoctorHolder holder = new DoctorAdapter.DoctorHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DoctorAdapter.DoctorHolder holder, int position) {
        final Doctor myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        myImageLoader = MySingleton.getInstance(myContext).getImageLoader();
        holder.txtDoctorName.setText(myItem.getSpecialistType());
        holder.txtExperience.setText(myItem.getExperience());
        holder.txtQualification.setText(myItem.getQualification());
        holder.txtAddress.setText(myItem.getAddress());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(myItem.getSpecialistImageUrl(), opts);
        opts.inJustDecodeBounds = false;

        Picasso.with(myContext)
                .load("http://192.168.12.100/APMS"+myItem.getSpecialistImageUrl())
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.backgroundd)
                .resize(screenWidth / 2, 300)
                .centerCrop()
                .into((holder.imgDoctor));
        holder.btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(myContext,DoctorDetailsActivity.class);
                intent.putExtra(AppConstants.DOCTOR_ID,myItem.getId());
                intent.putExtra(AppConstants.DESIGNATION,myItem.getDesignation());
                myContext.startActivity(intent);
            }
        });
        holder.btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(myContext,BookDoctorActivity.class);
                intent.putExtra(AppConstants.DOCTOR_ID,myItem.getId());
                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class DoctorHolder extends RecyclerView.ViewHolder {
        TextView txtDoctorName,txtQualification,txtExperience,txtAddress;
        ImageView imgDoctor;
        Button btnViewProfile,btnBookAppointment;

        public DoctorHolder(View itemView) {
            super(itemView);
            this.txtDoctorName = (TextView) itemView.findViewById(R.id.txtName);
            this.imgDoctor = (ImageView) itemView.findViewById(R.id.imgDoctor);
            this.txtQualification = (TextView) itemView.findViewById(R.id.txtQualification);
            this.txtExperience = (TextView) itemView.findViewById(R.id.txtExperience);
            this.txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            this.btnBookAppointment=(Button)itemView.findViewById(R.id.btnBookAppointment);
            this.btnViewProfile=(Button)itemView.findViewById(R.id.btnViewProfile);
        }
    }
}


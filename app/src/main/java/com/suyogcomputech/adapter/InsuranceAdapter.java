package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.suyogcomputech.helper.Insurance;
import com.suyogcomputech.sms.InsuranceTypeActivity;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by Pintu on 9/1/2016.
 */
public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.ShowTariffsViewHolder> {
    List<Insurance> myItems;
    Context myContext;
    int focusedItem = 0;

    public InsuranceAdapter(List<Insurance> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;

    }

    @Override
    public InsuranceAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_insurance, null);
        InsuranceAdapter.ShowTariffsViewHolder holder = new InsuranceAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(InsuranceAdapter.ShowTariffsViewHolder holder, int position) {
        final Insurance myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();
        holder.txtInsuranceType.setText(myItem.getInsuranceName());
        holder.rlInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Insurance Id",myItem.getInsuranceId());
                Intent intent=new Intent(myContext,InsuranceTypeActivity.class);
                myContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView txtInsuranceType;
        RelativeLayout rlInsurance;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.txtInsuranceType=(TextView)itemView.findViewById(R.id.txtInsuranceType);
            this.rlInsurance=(RelativeLayout)itemView.findViewById(R.id.rlInsurance);
        }
    }
}

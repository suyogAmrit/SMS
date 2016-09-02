package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suyogcomputech.helper.Insurance;
import com.suyogcomputech.sms.InsuranceRequestActivity;
import com.suyogcomputech.sms.InsuranceTypeActivity;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by Pintu on 9/2/2016.
 */
public class InsuranceTypeAdapter extends RecyclerView.Adapter<InsuranceTypeAdapter.ShowTariffsViewHolder> {
    List<Insurance> myItems;
    Context myContext;
    int focusedItem = 0;

    public InsuranceTypeAdapter(List<Insurance> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;

    }

    @Override
    public InsuranceTypeAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_insurance_type, null);
        InsuranceTypeAdapter.ShowTariffsViewHolder holder = new InsuranceTypeAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(InsuranceTypeAdapter.ShowTariffsViewHolder holder, int position) {
        final Insurance myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();
        holder.txtInsuranceType.setText(myItem.getInsuranceType());
        holder.txtInsuranceFeature.setText(myItem.getInsuranceFeature());
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("InsuranceTypeIs",myItem.getInsuranceTypeSlNo());
                Intent intent=new Intent(myContext,InsuranceRequestActivity.class);
                myContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView txtInsuranceType,txtInsuranceFeature;
        RelativeLayout rlInsurance;
        Button btnBook;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.txtInsuranceType=(TextView)itemView.findViewById(R.id.txtInsuranceType);
            this.rlInsurance=(RelativeLayout)itemView.findViewById(R.id.rlInsurance);
            this.txtInsuranceFeature=(TextView)itemView.findViewById(R.id.txtFeature);
            this.btnBook=(Button)itemView.findViewById(R.id.btnBookInsurance);

        }
    }
}

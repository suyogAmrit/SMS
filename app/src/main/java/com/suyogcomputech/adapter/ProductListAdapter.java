package com.suyogcomputech.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.Product;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.ProductDetailsActivity;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by suyogcomputech on 24/08/16.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private final int width, height;
    List<ProductDetails> list;
    Context myContext;

    public ProductListAdapter(Context myContext) {
        this.myContext = myContext;
        list = new ArrayList<>();
        Fresco.initialize(myContext);
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels / 2;
        height = metrics.heightPixels / 5 * 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProductDetails p = list.get(position);
        Log.i("images", p.getMainImage());
        holder.tvShortDesc.setText(p.getShortDesc().toUpperCase());
        holder.tvOffer.setText(p.getOfferPer() + "% off");
        holder.tvBrand.setText(p.getBrand());
        //if (list.get(position).getFromDate() != null && list.get(position).getToDate() != null) {
        // Log.i(String.valueOf(list.get(position)), "offer");
        if (list.get(position).getFromDate() != null && list.get(position).getToDate() != null && AppHelper.compareDate(list.get(position).getFromDate(), list.get(position).getToDate())) {
            holder.tvOffer.setVisibility(View.VISIBLE);
            double actualPrice = Double.valueOf(list.get(position).getPrice()) - (Double.valueOf(list.get(position).getPrice()) * Double.valueOf(list.get(position).getOfferPer())) / 100;
            holder.tvPrice.setText(AppConstants.RUPEESYM + actualPrice);
            holder.offer_frame.setVisibility(View.VISIBLE);
            holder.productCutPrice.setText(AppConstants.RUPEESYM + list.get(position).getPrice());
        }else {
            holder.tvPrice.setText(AppConstants.RUPEESYM + Double.valueOf(list.get(position).getPrice()));
            holder.tvOffer.setVisibility(View.GONE);
            holder.offer_frame.setVisibility(View.GONE);
        }
        Uri uri = Uri.parse(p.getMainImage());
        holder.ivImage.setImageURI(uri);

        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetails p1 = list.get(holder.getAdapterPosition());
                Intent iProduct = new Intent(myContext, ProductDetailsActivity.class);
                iProduct.putExtra(AppConstants.EXTRA_PROCUCT_DETAILS,p1);
                myContext.startActivity(iProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addProducts(ArrayList<ProductDetails> productDetailses) {
        list.addAll(productDetailses);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvParent;
        TextView tvBrand, tvPrice, tvShortDesc, tvOffer, productCutPrice;
        SimpleDraweeView ivImage;
        FrameLayout offer_frame;

        public ViewHolder(View itemView) {
            super(itemView);
            cvParent = (CardView) itemView.findViewById(R.id.item_prod_parent);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_prod_brand);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_prod_price);
            tvShortDesc = (TextView) itemView.findViewById(R.id.tv_prod_sort_desc);
            tvOffer = (TextView) itemView.findViewById(R.id.tv_prod_offer);
            productCutPrice = (TextView) itemView.findViewById(R.id.productCutPrice);
            offer_frame = (FrameLayout) itemView.findViewById(R.id.offer_frame);
            ivImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_product);
            ivImage.getLayoutParams().width = width;
            ivImage.getLayoutParams().height = height;
        }
    }
}

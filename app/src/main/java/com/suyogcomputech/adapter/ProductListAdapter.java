package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.Product;
import com.suyogcomputech.sms.ProductDetailsActivity;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suyogcomputech on 24/08/16.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private final int width, height;
    List<Product> list;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Product p = list.get(position);
        Log.i("images", p.getMainImage());
        holder.tvShortDesc.setText(p.getShortDesc().toUpperCase());
        holder.tvPrice.setText(AppConstants.RUPEESYM + p.getPrice());
        holder.tvBrand.setText(p.getBrand());

        Uri uri = Uri.parse(p.getMainImage());
        holder.ivImage.setImageURI(uri);

        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product p1 = list.get(holder.getAdapterPosition());
                Intent iProduct = new Intent(myContext, ProductDetailsActivity.class);
                iProduct.putExtra(AppConstants.PRDID, p1.getId());
                myContext.startActivity(iProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addProducts(ArrayList<Product> productDetailses) {
        list.addAll(productDetailses);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvParent;
        TextView tvBrand, tvPrice, tvShortDesc, tvOffer;
        SimpleDraweeView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            cvParent = (CardView) itemView.findViewById(R.id.item_prod_parent);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_prod_brand);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_prod_price);
            tvShortDesc = (TextView) itemView.findViewById(R.id.tv_prod_sort_desc);
//            tvOffer = (TextView) itemView.findViewById(R.id.tv_prod_offer);
            ivImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_product);
            ivImage.getLayoutParams().width = width;
            ivImage.getLayoutParams().height = height;
        }

    }
}

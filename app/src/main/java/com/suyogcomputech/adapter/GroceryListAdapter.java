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
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.GroceryProductDetails;
import com.suyogcomputech.sms.GroceryListActivity;
import com.suyogcomputech.sms.GroceryProductDetailsActivity;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/7/2016.
 */
public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.ViewHolder> {
    int width, height;
    ArrayList<GroceryProductDetails> productIdList;
    Context myContext;
    public GroceryListAdapter(Context myContext) {
        this.myContext = myContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Fresco.initialize(myContext);
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.grocery_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GroceryProductDetails dtls=productIdList.get(position);
        holder.image.setImageURI(Uri.parse(dtls.getImages_path()));
        holder.title.setText(dtls.getProd_title());
        holder.price.setText(AppConstants.RUPEESYM +dtls.getPrice());
        holder.description.setText(dtls.getProd_short_description());

        holder.cvProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(myContext,GroceryProductDetailsActivity.class);
                intent.putExtra("product_id",dtls.getProduct_id());
                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productIdList.size();
    }

    public void setAdpData(ArrayList<GroceryProductDetails> productDetailList) {
        productIdList=productDetailList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price,description;
        SimpleDraweeView image;
        CardView cvProduct;
        public ViewHolder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.tv_prod_brand);
            price=(TextView)itemView.findViewById(R.id.productCutPrice);
            description=(TextView)itemView.findViewById(R.id.tv_prod_sort_desc);
            image=(SimpleDraweeView)itemView.findViewById(R.id.iv_product);
            cvProduct=(CardView)itemView.findViewById(R.id.item_prod_parent);





            WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels / 2;
            height = metrics.heightPixels / 5 * 2;
            image.getLayoutParams().width = width;
            image.getLayoutParams().height = height;
        }
    }
}

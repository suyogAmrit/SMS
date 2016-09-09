package com.suyogcomputech.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by office on 9/1/2016.
 */
public class ImagePagerAdapter extends PagerAdapter {
    private final int width, height;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> arrayList;

    public ImagePagerAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    @Override
    public int getCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.image_viewpager_layout, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.viewpgaerImage);
        imageView.getLayoutParams().width = width;
        imageView.getLayoutParams().height = height;
        Picasso.with(context).load(arrayList.get(position)).placeholder(R.drawable.ic_empty).error(R.drawable.ic_empty).into(imageView);
//        SimpleDraweeView imageView = (SimpleDraweeView) itemView.findViewById(R.id.iv_product);
//        imageView.getLayoutParams().width = width;
//        imageView.getLayoutParams().height = height;
        //Picasso.with(context).load(arrayList.get(position)).resize(400,400).placeholder(R.drawable.ic_empty).error(R.drawable.ic_empty).into(imageView);
//        Uri uri = Uri.parse(arrayList.get(position));
//        imageView.setImageURI(uri);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
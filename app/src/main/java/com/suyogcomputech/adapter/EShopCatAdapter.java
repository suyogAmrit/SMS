package com.suyogcomputech.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.suyogcomputech.helper.EShopCategory;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by suyogcomputech on 23/08/16.
 */
public class EShopCatAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<EShopCategory> categoryArrayList;

    public EShopCatAdapter(Context context, ArrayList<EShopCategory> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @Override
    public int getGroupCount() {
        return categoryArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return categoryArrayList.get(i).getSubCategories().size();
    }

    @Override
    public Object getGroup(int i) {
        return categoryArrayList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return categoryArrayList.get(i).getSubCategories().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_header_category, viewGroup, false);
            holder = new ViewHolder();
            holder.tvDesc = (TextView) view.findViewById(R.id.tv_category_header);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String desc = categoryArrayList.get(i).getCaDesc();
        holder.tvDesc.setText(desc);
        holder.tvDesc.setTypeface(null, Typeface.BOLD);
        holder.tvDesc.setGravity(Gravity.LEFT | Gravity.START);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_header_category, viewGroup, false);
            holder = new ViewHolder();
            holder.tvDesc = (TextView) view.findViewById(R.id.tv_category_header);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String desc = categoryArrayList.get(i).getSubCategories().get(i1).getSubCatDesc();
        holder.tvDesc.setText(desc);
        LayoutParams params = new LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        holder.tvDesc.setLayoutParams(params);
        holder.tvDesc.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class ViewHolder {
        TextView tvDesc;
    }
}

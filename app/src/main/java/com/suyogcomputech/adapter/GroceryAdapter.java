package com.suyogcomputech.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.suyogcomputech.helper.EGroceryCategory;
import com.suyogcomputech.sms.R;
import java.util.ArrayList;
import android.widget.LinearLayout.LayoutParams;

/**
 * Created by suyogcomputech on 12/08/16.
 */
public class GroceryAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<EGroceryCategory> categoryArrayList;
    public GroceryAdapter(Context c, ArrayList<EGroceryCategory> list) {
        context=c;
        categoryArrayList=list;
    }

    @Override
    public int getGroupCount() {
        return categoryArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryArrayList.get(groupPosition).getSubCategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryArrayList.get(groupPosition).getSubCategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_header_category, parent, false);
            holder = new ViewHolder();
            holder.tvDesc = (TextView) view.findViewById(R.id.tv_category_header);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String desc = categoryArrayList.get(groupPosition).getCaDesc();
        holder.tvDesc.setText(desc);
        holder.tvDesc.setTypeface(null, Typeface.BOLD);
        holder.tvDesc.setGravity(Gravity.LEFT | Gravity.START);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_header_category, parent, false);
            holder = new ViewHolder();
            holder.tvDesc = (TextView) view.findViewById(R.id.tv_category_header);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String desc = categoryArrayList.get(groupPosition).getSubCategories().get(childPosition).getSubCatDesc();
        holder.tvDesc.setText(desc);
        LayoutParams params = new LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        holder.tvDesc.setLayoutParams(params);
        holder.tvDesc.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class ViewHolder {
        TextView tvDesc;
    }
}


package com.suyogcomputech.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.EGroceryCategory;
import com.suyogcomputech.helper.Grocery;
import com.suyogcomputech.helper.MySingleton;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;
import java.util.List;

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
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));

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
//    List<Grocery> myItems;
//    ArrayList<EGroceryCategory> myContext;
//    ImageLoader myImageLoader;
//    int focusedItem = 0;
//    private int screenWidth;
//
//    public GroceryAdapter(FragmentActivity myItems, ArrayList<EGroceryCategory> myContext) {
//        this.myItems = myItems;
//        this.myContext = myContext;
//        WindowManager wm = (WindowManager) myItems.getSystemService();
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        screenWidth = size.x;
//    }
//
//    @Override
//    public GroceryAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_grocery, null);
//        GroceryAdapter.ShowTariffsViewHolder holder = new GroceryAdapter.ShowTariffsViewHolder(v);
//
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(GroceryAdapter.ShowTariffsViewHolder holder, int position) {
//        final Grocery myItem = myItems.get(position);
//        holder.itemView.setSelected(focusedItem == position);
//
//        holder.getLayoutPosition();
//
//        myImageLoader = MySingleton.getInstance(myContext).getImageLoader();
//        holder.tvName.setText(myItem.getName());
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(myItem.getImage_url(), opts);
//        opts.inJustDecodeBounds = false;
//
//        Picasso.with(myContext)
//                .load(myItem.getImage_url())
//                .error(R.drawable.ic_empty)
//                .placeholder(R.drawable.backgroundd)
//                .resize(screenWidth / 2, 300)
//                .centerCrop()
//                .into((holder.imgGrocery));
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return (null != myItems ? myItems.size() : 0);
//    }
//
//    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName, tvPrice;
//        ImageView imgGrocery;
//        Button btnAddItem;
//
//        public ShowTariffsViewHolder(View itemView) {
//            super(itemView);
//            this.tvName = (TextView) itemView.findViewById(R.id.txtItemName);
//            this.imgGrocery = (ImageView) itemView.findViewById(R.id.img_Grocery);
//            this.tvPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);
//            this.btnAddItem = (Button) itemView.findViewById(R.id.btnAdditems);
//
//        }
//    }
}


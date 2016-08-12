package com.suyogcomputech.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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
import com.suyogcomputech.helper.Grocery;
import com.suyogcomputech.helper.MySingleton;
import com.suyogcomputech.sms.R;

import java.util.List;

/**
 * Created by suyogcomputech on 12/08/16.
 */
public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ShowTariffsViewHolder> {
    List<Grocery> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public GroceryAdapter(List<Grocery> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public GroceryAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_grocery, null);
        GroceryAdapter.ShowTariffsViewHolder holder = new GroceryAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GroceryAdapter.ShowTariffsViewHolder holder, int position) {
        final Grocery myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        myImageLoader = MySingleton.getInstance(myContext).getImageLoader();
        holder.tvName.setText(myItem.getName());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(myItem.getImage_url(), opts);
        opts.inJustDecodeBounds = false;

        Picasso.with(myContext)
                .load(myItem.getImage_url())
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.backgroundd)
                .resize(screenWidth / 2, 300)
                .centerCrop()
                .into((holder.imgGrocery));


    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgGrocery;
        Button btnAddItem;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.txtItemName);
            this.imgGrocery = (ImageView) itemView.findViewById(R.id.img_Grocery);
            this.tvPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);
            this.btnAddItem = (Button) itemView.findViewById(R.id.btnAdditems);

        }
    }
}


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
import com.suyogcomputech.helper.OnlineShopping;
import com.suyogcomputech.helper.MySingleton;
import com.suyogcomputech.sms.MainActivity;
import com.suyogcomputech.sms.R;

import java.util.List;


/**
 * Created by Pintu on 6/10/2016.
 */
public class OnlineShoppingAdapter extends RecyclerView.Adapter<OnlineShoppingAdapter.ShowTariffsViewHolder> {
    List<OnlineShopping> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public OnlineShoppingAdapter(List<OnlineShopping> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public OnlineShoppingAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_show_shopping, null);
        OnlineShoppingAdapter.ShowTariffsViewHolder holder = new OnlineShoppingAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(OnlineShoppingAdapter.ShowTariffsViewHolder holder, int position) {
        final OnlineShopping myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);

        holder.getLayoutPosition();

        myImageLoader = MySingleton.getInstance(myContext).getImageLoader();
        holder.tvName.setText(myItem.getTitle());
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(myItem.getImageUrl(), opts);
        opts.inJustDecodeBounds = false;

        Picasso.with(myContext)
                .load(myItem.getImageUrl())
                .error(R.drawable.ic_empty)
                .placeholder(R.drawable.backgroundd)
                .resize(screenWidth / 2, 300)
                .centerCrop()
                .into((holder.img_tariffs));

        holder.tvOffPrice.setPaintFlags(holder.tvOffPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)myContext).addItemToCart(myItem.getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvOffPrice,tvPrice;
        ImageView img_tariffs;
        Button btnAddItem;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.txtItemName);
            this.img_tariffs = (ImageView) itemView.findViewById(R.id.img_tariffs);
            this.tvPrice=(TextView)itemView.findViewById(R.id.txtItemPrice);
            this.tvOffPrice=(TextView)itemView.findViewById(R.id.txtOffPrice);
            this.btnAddItem=(Button)itemView.findViewById(R.id.btnAdditems);

        }
    }
}


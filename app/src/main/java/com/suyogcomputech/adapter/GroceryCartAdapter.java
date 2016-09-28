package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ConnectionClass;
import com.suyogcomputech.helper.GroceryCartList;
import com.suyogcomputech.sms.GroceryCartActivity;
import com.suyogcomputech.sms.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Pintu on 9/23/2016.
 */
public class GroceryCartAdapter extends RecyclerView.Adapter<GroceryCartAdapter.ViewHolder> {
    int width, height;
    String uniqueUserId;
    Context context;
    ArrayList<GroceryCartList> cartArrayLists;

    public GroceryCartAdapter(Context c, ArrayList<GroceryCartList> cartArrayList) {
        context = c;
        cartArrayLists = cartArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Fresco.initialize(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grocery_cart_adapter, parent, false);

        SharedPreferences sharedpreferences = context.getSharedPreferences(AppConstants.USERPREFS, Context.MODE_PRIVATE);
        uniqueUserId = sharedpreferences.getString(AppConstants.USERID, AppConstants.NOT_AVAILABLE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GroceryCartList cartlists = cartArrayLists.get(position);
        holder.title.setText(cartlists.getProdTitle());
        holder.price.setText(cartlists.getPrice());
        holder.quantity.setText(cartlists.getQuantity());
        holder.image.setImageURI(Uri.parse(cartlists.getImageUrl()));

        final String id = cartlists.getProdId();
        holder.remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteCartItem().execute(id, String.valueOf(holder.getAdapterPosition()));
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qty_str = holder.quantity.getText().toString();
                int qty = Integer.parseInt(qty_str);
                int maxQty = Integer.parseInt(cartlists.getMaxQuantity());
                if (qty < maxQty) {
                    qty++;
                    holder.quantity.setText(String.valueOf(qty));
                    new UpdateQuantity().execute(qty,Integer.parseInt(cartlists.getProdId()));
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qty_str = holder.quantity.getText().toString();
                int qty = Integer.parseInt(qty_str);
                if(qty>1)
                {
                    holder.quantity.setText(String.valueOf(--qty));
                    new UpdateQuantity().execute(qty,Integer.parseInt(cartlists.getProdId()));
                }
            }
        });
    }

    private class DeleteCartItem extends AsyncTask<String, Void, Integer> {
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            if (i > 0) {
                cartArrayLists.remove(pos);
                Toast.makeText(context, "Item Removed from cart", Toast.LENGTH_SHORT).show();

                int cart_count=cartArrayLists.size();
                if(cart_count==0)
                {
//                    checkout.setVisibility(View.GONE);
//                    tv_no_items.setVisibility(View.VISIBLE);

                    ((GroceryCartActivity)context).finish();
                }
                notifyDataSetChanged();
            }
        }

        @Override
        protected Integer doInBackground(String... strings) {
            pos = Integer.parseInt(strings[1]);
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connect();
                String deleteQuery = "delete from Grocery_cart_table  where user_id='" + uniqueUserId + "' and prod_id='" + strings[0] + "' and status='1'";
                PreparedStatement statementInsert = null;
                statementInsert = connection.prepareStatement(deleteQuery);
                long resSet = statementInsert.executeUpdate();
                return Integer.parseInt(String.valueOf(resSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class UpdateQuantity extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                ConnectionClass connectionClass=new ConnectionClass();
                Connection connection=connectionClass.connect();
                String updateQty="update Grocery_cart_table set quantity='"+integers[0]+"' where user_id='"+uniqueUserId+"' and prod_id='"+integers[1]+"' and status=1";
                PreparedStatement statementInsert = null;
                statementInsert = connection.prepareStatement(updateQty);
                statementInsert.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return cartArrayLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, quantity;
        ImageView product_image, plus, minus, remove_cart;
        SimpleDraweeView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
//            product_image=(ImageView)itemView.findViewById(R.id.prod_image);
            plus = (ImageView) itemView.findViewById(R.id.plus);
            minus = (ImageView) itemView.findViewById(R.id.minus);
            remove_cart = (ImageView) itemView.findViewById(R.id.remove_cart);
            image = (SimpleDraweeView) itemView.findViewById(R.id.iv_product);


            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
//            width = metrics.widthPixels / 2;
//            height = metrics.heightPixels / 5 * 2;
//            image.getLayoutParams().width = width;
//            image.getLayoutParams().height = height;
        }
    }
}

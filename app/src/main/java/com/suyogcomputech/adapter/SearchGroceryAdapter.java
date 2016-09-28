package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suyogcomputech.helper.SearchGroceryItems;
import com.suyogcomputech.sms.GroceryListActivity;
import com.suyogcomputech.sms.R;
import com.suyogcomputech.sms.SearchGroceryActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Pintu on 9/28/2016.
 */
public class SearchGroceryAdapter extends RecyclerView.Adapter<SearchGroceryAdapter.Holder> {
    Context context;
    ArrayList<SearchGroceryItems> arrayGrocery;
    public SearchGroceryAdapter(Context c, ArrayList<SearchGroceryItems> arrayGroceryy) {
        context=c;
        arrayGrocery=arrayGroceryy;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grocery_search_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final SearchGroceryAdapter.Holder holder, int position) {
        final SearchGroceryItems searchGroceryItems=arrayGrocery.get(position);
        holder.search_title.setText(searchGroceryItems.getProdTitle());
        holder.search_brand.setText(searchGroceryItems.getProdBrand());
        holder.card_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, GroceryListActivity.class);
                intent.putExtra("searchList","1");
                intent.putExtra("searchTitle",searchGroceryItems.getProdTitle());
                intent.putExtra("searchBrand",searchGroceryItems.getProdBrand());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayGrocery.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout card_select;
        TextView search_title,search_brand;
        public Holder(View itemView) {
            super(itemView);
            card_select=(LinearLayout)itemView.findViewById(R.id.card_select);
            search_title=(TextView) itemView.findViewById(R.id.search_title);
            search_brand=(TextView)itemView.findViewById(R.id.search_brand);
        }
    }
}

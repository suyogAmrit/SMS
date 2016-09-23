package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.R;
import com.suyogcomputech.sms.SearchProductActivity;
import com.suyogcomputech.sms.ShowFilteredProductActivity;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/23/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<ProductDetails> detailses;
    private Context context;
    public static final String MyPREFERENCES = "MyPrefs";
    public static String CatId = "CatId";
    public static String SubCatId = "SubCatId";
    public static String SubCatDesc = "SubCatDesc";
    SharedPreferences sharedpreferences;

    public SearchAdapter(ArrayList<ProductDetails> detailses, Context context1) {
        this.detailses = detailses;
        this.context = context1;
        sharedpreferences = context1.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        CatId = sharedpreferences.getString(CatId, "0");
        SubCatId = sharedpreferences.getString(SubCatId, "0");
        SubCatDesc = sharedpreferences.getString(SubCatDesc, "0");
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_items, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        final ProductDetails details = detailses.get(position);
        holder.txtSearchResult.setText(details.getTitle() + details.getBrand());
        holder.txtSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowFilteredProductActivity.class);
                intent.putExtra(AppConstants.PRDBRAND, details.getBrand());
                intent.putExtra(AppConstants.PRDTITLE, details.getTitle());
                context.startActivity(intent);
                ((SearchProductActivity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailses.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView txtSearchResult;

        public SearchViewHolder(View itemView) {
            super(itemView);
            txtSearchResult = (TextView) itemView.findViewById(R.id.txtSearchResult);
        }
    }
}

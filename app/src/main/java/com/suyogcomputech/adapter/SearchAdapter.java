package com.suyogcomputech.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/12/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    private ArrayList<ProductDetails> arrayList;
    public SearchAdapter(ArrayList<ProductDetails> arrayList) {
        this.arrayList = arrayList;
    }
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_layout,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.txtSearch.setText(arrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView txtSearch;
        public SearchViewHolder(View itemView) {
            super(itemView);
            txtSearch = (TextView)itemView.findViewById(R.id.txtSearch);
        }
    }
}

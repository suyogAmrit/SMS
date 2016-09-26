package com.suyogcomputech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.ProductDetails;
import com.suyogcomputech.sms.EditSelectedProductActivity;
import com.suyogcomputech.sms.R;
import com.suyogcomputech.sms.ShoppingCartItemActivity;

import java.util.ArrayList;

/**
 * Created by Suyog on 9/7/2016.
 */
public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    ArrayList<ProductDetails> myItems;
    private Context context;
    int focusedItem = 0;
    public  static final int TYPE_ITEMS = 0;
    public  static final int TYPE_CALCULATE = 1;
    public static final  int REQUEST_CODE = 325;

    public CartItemAdapter(Context context,ArrayList<ProductDetails> list) {
        this.context = context;
        this.myItems=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.item_product_details, parent, false);
//        return new CartViewHolder(view);
        View view;
        if (viewType == TYPE_ITEMS){
            view = LayoutInflater.from(context).inflate(R.layout.item_product_details,parent,false);
            return new CartViewHolder(view);
        }if (viewType == TYPE_CALCULATE){
            view = LayoutInflater.from(context).inflate(R.layout.total_calculation_layout,parent,false);
            return new TotalViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof CartViewHolder){
                    CartViewHolder cartViewHolder = (CartViewHolder) holder;
                final ProductDetails myItem = myItems.get(position);
                holder.itemView.setSelected(focusedItem == position);
                holder.getLayoutPosition();
                    cartViewHolder.txtBrandName.setText(myItem.getBrand()+" "+myItem.getTitle());
                    cartViewHolder.txtBrandSize.setText("Size "+myItem.getSizeProduct());
                    cartViewHolder.txtDisPer.setText("("+myItem.getOfferPer()+"% off)");
                    cartViewHolder.txtNo.setText(myItem.getQuantity());
                Picasso.with(context).load(myItem.getMainImage()).into(cartViewHolder.imgProduct);
                if (myItem.getFromDate() != null && myItem.getToDate() != null && AppHelper.compareDate(myItem.getFromDate(), myItem.getToDate())) {
                    double actualPrice = Double.valueOf(myItem.getPrice()) - (Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer())) / 100;
                    double finalPrice = actualPrice * Integer.valueOf(myItem.getQuantity());
                    cartViewHolder.txtProdPrice.setText(AppConstants.RUPEESYM + finalPrice);
                    cartViewHolder.frameItem.setVisibility(View.VISIBLE);
                    cartViewHolder.txtDisPer.setVisibility(View.VISIBLE);
                }else {
                    double finalRupee = Double.valueOf(myItem.getPrice()) * Integer.valueOf(myItem.getQuantity());
                    //holder.txtProdPrice.setText(AppConstants.RUPEESYM + Double.valueOf(myItem.getPrice()));
                    cartViewHolder.txtProdPrice.setText(AppConstants.RUPEESYM + finalRupee);
                    cartViewHolder.frameItem.setVisibility(View.GONE);
                    cartViewHolder.txtDisPer.setVisibility(View.GONE);
                }
                //holder.txtProdPrice.setText(myItem.getPrice());
                double finalAmount = Double.valueOf(myItem.getPrice()) * Integer.valueOf(myItem.getQuantity());
               // holder.txtOffer.setText(myItem.getPrice());
                    cartViewHolder.txtOffer.setText(""+finalAmount);
                    cartViewHolder.editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetails productDetail = myItems.get(holder.getAdapterPosition());
                        Intent intent = new Intent(context, EditSelectedProductActivity.class);
                        intent.putExtra(AppConstants.EXTRA_PRODUCT_EDIT,productDetail);
                        Log.i("ProdId",productDetail.getId());
                        ((ShoppingCartItemActivity) context).startActivityForResult(intent, REQUEST_CODE);
                        notifyDataSetChanged();
                    }
                });
                    cartViewHolder.removeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppHelper.isConnectingToInternet(context)) {
                            ((ShoppingCartItemActivity) context).deleteItem(myItem.getSerielNo());
                        }else {
                            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        }
        if (holder instanceof  TotalViewHolder){
            final ProductDetails myItem = myItems.get(position);
            TotalViewHolder totalViewHolder = (TotalViewHolder) holder;
            double amount = 0;
                for (int j = 0; j < myItems.size(); j++) {
                    amount = amount + Double.valueOf(myItems.get(j).getPrice());
                }
            totalViewHolder.txtCartTotal.setText(AppConstants.RUPEESYM+amount);
            double actualPrice = Double.valueOf(myItem.getPrice()) - (Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer())) / 100;
            double finalPrice = actualPrice * Integer.valueOf(myItem.getQuantity());
            double disprice = 0;
            for (int k = 0;k<myItems.size();k++){
                disprice = disprice + ((Double.valueOf(myItem.getPrice()) * Double.valueOf(myItem.getOfferPer()))/100);
            }
            totalViewHolder.txtDiscountTotal.setText(AppConstants.RUPEESYM+disprice);
            totalViewHolder.txtTotalPayble.setText(AppConstants.RUPEESYM+(amount-disprice));
            ShoppingCartItemActivity.txtCartTotal.setText(AppConstants.RUPEESYM+amount);
            ShoppingCartItemActivity.txtDiscountTotal.setText(AppConstants.RUPEESYM+disprice);
            ShoppingCartItemActivity.txtTotalPayble.setText(AppConstants.RUPEESYM+(amount-disprice));
        }
     }
    public void totalPrice(){
        double price = 0;
        for (int k=0; k<myItems.size();k++){
            price = price+ Double.valueOf(myItems.get(k).getPrice());
        }
        ShoppingCartItemActivity.txtCartTotal.setText(AppConstants.RUPEESYM+price);
        ShoppingCartItemActivity.txtTotalPayble.setText(AppConstants.RUPEESYM+price);
    }
    public void totalDis(){
        double dispr = 0;
        for (int l = 0;l<myItems.size();l++){
            Log.v("qunnn", ""+Double.parseDouble(myItems.get(l).getPrice()));
            dispr = dispr + ((Double.valueOf(myItems.get(l).getPrice()) * Double.valueOf(myItems.get(l).getOfferPer()))/100);
        }
        ShoppingCartItemActivity.txtDiscountTotal.setText(AppConstants.RUPEESYM+""+dispr);
    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position==myItems.size() ? TYPE_CALCULATE:TYPE_ITEMS;
    }
    public class CartViewHolder extends ViewHolder{
        ImageView imgProduct;
        TextView txtBrandName,txtBrandSize,txtProdPrice,txtDisPer,txtNo,txtOffer;
        LinearLayout removeText,editText;
        FrameLayout frameItem;
        public CartViewHolder(View itemView) {
            super(itemView);
            imgProduct = (ImageView)itemView.findViewById(R.id.imgProduct);
            txtBrandName = (TextView)itemView.findViewById(R.id.txtBrandName);
            txtBrandSize = (TextView)itemView.findViewById(R.id.txtBrandSize);
            txtProdPrice = (TextView)itemView.findViewById(R.id.txtProdPrice);
            txtDisPer = (TextView)itemView.findViewById(R.id.txtDisPer);
            txtNo = (TextView)itemView.findViewById(R.id.txtNo);
            txtOffer = (TextView)itemView.findViewById(R.id.txtOffer);
            frameItem = (FrameLayout)itemView.findViewById(R.id.frameItem);
            removeText = (LinearLayout) itemView.findViewById(R.id.removeText);
            editText = (LinearLayout) itemView.findViewById(R.id.editText);
        }
    }
    public class TotalViewHolder extends ViewHolder{
       TextView txtCartTotal,txtDiscountTotal,txtTotalPayble;
        public TotalViewHolder(View itemView) {
            super(itemView);
            txtCartTotal = (TextView)itemView.findViewById(R.id.txtCartTotal);
            txtDiscountTotal = (TextView)itemView.findViewById(R.id.txtDiscountTotal);
            txtTotalPayble = (TextView)itemView.findViewById(R.id.txtTotalPayble);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}

package com.ortusolis.water_distribution.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ortusolis.water_distribution.Activity.CardListActivity;
import com.ortusolis.water_distribution.R;
import com.ortusolis.water_distribution.Utility.SquareImageView;
import com.ortusolis.water_distribution.pojo.ProductRequestCart;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ModuleViewHolder> {
    private List<ProductRequestCart> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    public CartListAdapter(Context context, List<ProductRequestCart> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView customeName;
        TextView orderId;
        TextView orderDate;
        TextView orderCost;
        TextView noOfProducts;
        LinearLayout customerLay;
        SquareImageView picture;
        ImageView delete;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            customeName = (TextView) itemView.findViewById(R.id.customeName);
            orderId = (TextView) itemView.findViewById(R.id.orderId);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            orderCost = (TextView) itemView.findViewById(R.id.orderCost);
            noOfProducts = (TextView) itemView.findViewById(R.id.noOfProducts);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
            picture = (SquareImageView) itemView.findViewById(R.id.picture);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_cart, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModuleViewHolder holder, final int position) {
        ProductRequestCart item = dataList.get(position);

        holder.customeName.setText(Html.fromHtml("<b>Customer Name: </b>"+item.getCustomerName()));
        holder.orderId.setText(Html.fromHtml("<b>Delivery Date: </b>"+item.getDelivaryDate()));
        holder.orderDate.setText(Html.fromHtml("<b>Order Date: </b>"+item.getOrderDate()));
        holder.orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+item.getOrderCost()+context.getString(R.string.Rs)));
        holder.noOfProducts.setText(Html.fromHtml("<b>No. of Products: </b>"+item.getOrderedQty()));

        if (position%2==0){
            holder.picture.setImageResource(R.drawable.nut);

        }
        else {
            holder.picture.setImageResource(R.drawable.nut);
        }

        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                notifyDataSetChanged();
                ((CardListActivity)context ).deleteFormCart();
            }
        });

    }

    public List<ProductRequestCart> getDataList() {
        return dataList;
    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return 0;
    }
}

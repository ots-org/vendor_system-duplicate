package com.ortusolis.rotarytaranaadmin.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortusolis.rotarytaranaadmin.R;
import com.ortusolis.rotarytaranaadmin.pojo.OrderResponse;

import java.util.ArrayList;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ModuleViewHolder> {
    private ArrayList<OrderResponse.RequestS.ProductOrder> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    public ProductDetailAdapter(Context context, ArrayList<OrderResponse.RequestS.ProductOrder> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView openBalance;
        TextView stockAddition;
        TextView orderDelivered;
        TextView closingBalance;
        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
            openBalance = (TextView) itemView.findViewById(R.id.openBalance);
            stockAddition = (TextView) itemView.findViewById(R.id.stockAddition);
            orderDelivered = (TextView) itemView.findViewById(R.id.orderDelivered);
            closingBalance = (TextView) itemView.findViewById(R.id.closingBalance);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);

        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_product_stock, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ModuleViewHolder holder, int position) {
        OrderResponse.RequestS.ProductOrder item = dataList.get(position);

        holder.name.setText(item.getProductName());
        holder.openBalance.setText(item.getOtsOrderedQty());
        holder.stockAddition.setText(item.getOtsDeliveredQty());
        holder.orderDelivered.setText(item.getOtsOrderProductCost());
        holder.closingBalance.setVisibility(View.GONE);

        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return 0;
    }


    public void clearAll(){
        dataList.clear();
        notifyDataSetChanged();
    }

}

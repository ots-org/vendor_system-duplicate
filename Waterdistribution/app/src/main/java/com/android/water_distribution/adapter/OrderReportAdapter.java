package com.android.water_distribution.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.water_distribution.Activity.ProductDetailReportActivity;
import com.android.water_distribution.R;
import com.android.water_distribution.pojo.CompleteOrderDetails;

import java.util.ArrayList;
import java.util.List;

public class OrderReportAdapter extends RecyclerView.Adapter<OrderReportAdapter.ModuleViewHolder> {
    private List<CompleteOrderDetails> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    public OrderReportAdapter(Context context, List<CompleteOrderDetails> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView orderNo;
        TextView customeName;
        TextView orderDate;
        TextView orderCost;
        TextView deliveryDate;
        TextView deliveredBy;
        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            orderNo = (TextView) itemView.findViewById(R.id.orderNo);
            customeName = (TextView) itemView.findViewById(R.id.customerName);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            orderCost = (TextView) itemView.findViewById(R.id.orderCost);
            deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
            deliveredBy = (TextView) itemView.findViewById(R.id.deliveredBy);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_order_report, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModuleViewHolder holder, final int position) {
        CompleteOrderDetails item = dataList.get(position);

        holder.orderNo.setText(Html.fromHtml("<b>Order No: </b>"+item.getOrderId()));
        holder.customeName.setText(Html.fromHtml("<b>Customer Name: </b>"+item.getCustomerDetails().getFirstName()));
        holder.orderDate.setText(Html.fromHtml("<b>Order Date: </b>"+item.getOrderDate()));
        holder.orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+item.getOrderCost()+context.getString(R.string.Rs)));
        holder.deliveryDate.setText(Html.fromHtml("<b>Delivery Date: </b>"+item.getOrderDeliveryDate()));
        holder.deliveredBy.setText(Html.fromHtml("<b>Delivered By: </b>"+item.getEmployeeDetails().getFirstName()));

        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailReportActivity.class);
                intent.putParcelableArrayListExtra("prods", (ArrayList<? extends Parcelable>) dataList.get(position).getOrderProductDetails());
                context.startActivity(intent);
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

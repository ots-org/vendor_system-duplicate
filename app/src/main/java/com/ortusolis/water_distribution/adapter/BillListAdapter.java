package com.ortusolis.water_distribution.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortusolis.water_distribution.Activity.BillListActivity;
import com.ortusolis.water_distribution.R;
import com.ortusolis.water_distribution.Utility.CommonFunctions;
import com.ortusolis.water_distribution.pojo.CompleteOrderDetails;

import java.util.ArrayList;
import java.util.List;

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.ModuleViewHolder> {
    private List<CompleteOrderDetails> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    public BillListAdapter(Context context, ArrayList<CompleteOrderDetails> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView customeName;
        TextView orderId;
        TextView orderDate;
        TextView orderCost;
        TextView noOfProducts;
        ImageView selectableImage;
        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            customeName = (TextView) itemView.findViewById(R.id.customeName);
            orderId = (TextView) itemView.findViewById(R.id.orderId);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            orderCost = (TextView) itemView.findViewById(R.id.orderCost);
            noOfProducts = (TextView) itemView.findViewById(R.id.noOfProducts);
            selectableImage = (ImageView) itemView.findViewById(R.id.selectableImage);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_bill, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModuleViewHolder holder, final int position) {
        CompleteOrderDetails item = dataList.get(position);

        holder.customeName.setText(Html.fromHtml("<b>Customer Name: </b>"+item.getCustomerDetails().getFirstName()));
        holder.orderId.setText(Html.fromHtml("<b>Order Id: </b>"+item.getOrderId()));
        holder.orderDate.setText(Html.fromHtml("<b>Order Date: </b>"+ CommonFunctions.converDateStr(item.getOrderDate())));
        holder.orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+item.getOrderCost()+context.getString(R.string.Rs)));
        holder.noOfProducts.setText(Html.fromHtml("<b>No. of Products: </b>"+item.getOrderProductDetails().size()));

        if (((BillListActivity)context).checkIfSameOrder(item)){
            holder.selectableImage.setImageResource(android.R.drawable.checkbox_on_background);
        }
        else {
            holder.selectableImage.setImageResource(android.R.drawable.checkbox_off_background);
        }

        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isAdded = ((BillListActivity)context).addBills(dataList.get(position));

                if (isAdded){
                    holder.selectableImage.setImageResource(android.R.drawable.checkbox_on_background);
                }
                else {
                    holder.selectableImage.setImageResource(android.R.drawable.checkbox_off_background);
                }

                /*Intent intent = new Intent(context, BillDescription.class);

                intent.putExtra("bill",dataList.get(position));

                context.startActivity(intent);*/
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

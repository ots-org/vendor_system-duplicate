package com.ortusolis.subhaksha.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortusolis.subhaksha.Interfaces.IClickInterfaceAssigned;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.Utility.CommonFunctions;
import com.ortusolis.subhaksha.pojo.AssignedOrderModel;

import java.util.List;

public class AssignedOrderReportAdapter extends RecyclerView.Adapter<AssignedOrderReportAdapter.ModuleViewHolder> {
    private List<AssignedOrderModel> dataList;
    Context context;
    IClickInterfaceAssigned iClickInterface;
    private static final String TAG = "ContentItemAdapter";

    public AssignedOrderReportAdapter(Context context, List<AssignedOrderModel> dataList, IClickInterfaceAssigned iClickInterface) {
        this.dataList = dataList;
        this.context = context;
        this.iClickInterface = iClickInterface;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView orderNo;
        TextView customeName;
        TextView orderDate;
        TextView orderCost;
        TextView deliveryDate;
        TextView deliveredBy;
        TextView outstandingAmt;
        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            orderNo = (TextView) itemView.findViewById(R.id.orderNo);
            customeName = (TextView) itemView.findViewById(R.id.customerName);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            orderCost = (TextView) itemView.findViewById(R.id.orderCost);
            deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
            deliveredBy = (TextView) itemView.findViewById(R.id.deliveredBy);
            outstandingAmt = (TextView) itemView.findViewById(R.id.outstandingAmt);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_order_assigned, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModuleViewHolder holder, final int position) {
        final AssignedOrderModel item = dataList.get(position);

        holder.orderNo.setText(Html.fromHtml("<b>Order Number: </b>"+item.getOrderId()));
        holder.customeName.setText(Html.fromHtml("<b>Ordered Date: </b>"+CommonFunctions.converDateStr(item.getOrderDate())));
        holder.orderDate.setText(Html.fromHtml("<b>Customer Name: </b>"+item.getCustomerDetails().getFirstName()));
        holder.orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+item.getOrderCost()));
        holder.deliveryDate.setText(Html.fromHtml("<b>Delivery Date: </b>"+ CommonFunctions.converDateStr(item.getDelivaryDate())));
        holder.deliveredBy.setText(Html.fromHtml("<b>Distributor Name: </b>"+item.getDistributorDetails().getFirstName()));

        holder.outstandingAmt.setText(Html.fromHtml("<b>Outstanding Amount: </b>"+item.getOutStandingAmount()+context.getString(R.string.Rs)));

        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickInterface.click(item);
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
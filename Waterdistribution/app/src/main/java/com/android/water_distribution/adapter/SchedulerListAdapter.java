package com.android.water_distribution.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.water_distribution.R;
import com.android.water_distribution.Utility.CommonFunctions;
import com.android.water_distribution.pojo.SchedulerModel;

import java.util.List;

public class SchedulerListAdapter extends RecyclerView.Adapter<SchedulerListAdapter.ModuleViewHolder> {
    private List<SchedulerModel.SchedulerModelInternal> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    public SchedulerListAdapter(Context context, List<SchedulerModel.SchedulerModelInternal> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        TextView productPrice;
        TextView qtyRequest;
        TextView customer;
        TextView phoneNumber;
        TextView scheduledOn;
        TextView nextScheduledOn;
        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            qtyRequest = (TextView) itemView.findViewById(R.id.qtyRequest);
            customer = (TextView) itemView.findViewById(R.id.customer);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNumber);
            scheduledOn = (TextView) itemView.findViewById(R.id.scheduledOn);
            nextScheduledOn = (TextView) itemView.findViewById(R.id.nextScheduledOn);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_schedule, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModuleViewHolder holder, final int position) {
        final SchedulerModel.SchedulerModelInternal item = dataList.get(position);

        holder.productName.setText(Html.fromHtml("<b>Product Name: </b>"+item.getProductDetails().getProductName()));
        holder.productPrice.setText(Html.fromHtml("<b>Product Price: </b>"+item.getProductDetails().getProductPrice()+context.getString(R.string.Rs)));
        holder.qtyRequest.setText(Html.fromHtml("<b>Qty requested: </b>"+item.getRequestedQty()));
        holder.customer.setText(Html.fromHtml("<b>Customer: </b>"+item.getUserDetails().getFirstName()));
        holder.phoneNumber.setText(Html.fromHtml("<b>Phone Number: </b>"+ item.getUserDetails().getPhonenumber()));
        holder.scheduledOn.setText(Html.fromHtml("<b>Scheduled On: </b>"+CommonFunctions.converDateStr(item.getScheduledDate())));

        holder.nextScheduledOn.setText(Html.fromHtml("<b>Next Scheduled On: </b>"+CommonFunctions.converDateStr(item.getNxtScheduledDate())));

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

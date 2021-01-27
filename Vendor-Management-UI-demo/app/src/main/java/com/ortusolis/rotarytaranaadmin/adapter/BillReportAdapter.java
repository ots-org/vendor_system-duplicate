package com.ortusolis.rotarytaranaadmin.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortusolis.rotarytaranaadmin.R;
import com.ortusolis.rotarytaranaadmin.pojo.BillRequest;

import java.util.List;

public class BillReportAdapter extends RecyclerView.Adapter<BillReportAdapter.ModuleViewHolder> {
    private List<BillRequest.RequestS> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    public BillReportAdapter(Context context, List<BillRequest.RequestS> dataList) {
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
        BillRequest.RequestS item = dataList.get(position);

        holder.orderNo.setText(Html.fromHtml("<b>Bill No: </b>"+item.getBillId()));
        holder.customeName.setText(Html.fromHtml("<b>Bill Cost: </b>"+item.getBillAmount()+context.getString(R.string.Rs)));
        holder.orderDate.setText(Html.fromHtml("<b>Bill Generated : </b>"+item.getBillGenerated()));
        holder.orderCost.setText(Html.fromHtml("<b>Received Amount: </b>"+item.getBillAmountReceived()+context.getString(R.string.Rs)));

        int outStandingAmt = Integer.valueOf(item.getBillAmount())-Integer.valueOf(item.getBillAmountReceived());
        holder.deliveryDate.setText(Html.fromHtml("<b>Outstanding Amount: </b>"+outStandingAmt+context.getString(R.string.Rs)));
        holder.deliveredBy.setVisibility(View.GONE);

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

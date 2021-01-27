package com.ortusolis.etaaranavkf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.pojo.OrderResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AssignedOrderGridAdapter extends BaseAdapter {

    public List<OrderResponse.RequestS.ProductOrder> getmData() {
        return mData;
    }

    private List<OrderResponse.RequestS.ProductOrder> mData;
    private List<OrderResponse.RequestS.ProductOrder> mOriginalData;
    private LayoutInflater mInflater;
    Context context;

    // data is passed into the constructor
    public AssignedOrderGridAdapter(Context context, List<OrderResponse.RequestS.ProductOrder> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOriginalData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public String getTotalCost(){
        float totalCost = 0;
       for (OrderResponse.RequestS.ProductOrder productOrder: mData){
           totalCost = totalCost + (Float.valueOf(productOrder.getOtsDeliveredQty()) * Float.valueOf(productOrder.getOtsOrderProductCost()));
       }
       /* //
        Intent intent = new Intent(this.context, RazorPayActivity.class);
         intent.putExtra("amount", totalCost);
        context.startActivity(intent);

        //*/
       return (String.format("%.02f", totalCost));
    }


    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ImageView picture;
        final TextView name;
        final TextView orderDateText;
        final TextView orderCostText;
        final TextView orderQtyText;
        final TextView deliveredQuantityText;

        if (v == null) {
            v = mInflater.inflate(R.layout.order_grid_item, parent, false);
        }

        picture = (ImageView) v.findViewById(R.id.picture);
        name = (TextView) v.findViewById(R.id.prodNameText);
        orderDateText = (TextView) v.findViewById(R.id.orderDateText);
        orderCostText = (TextView) v.findViewById(R.id.orderCostText);
        orderQtyText = (TextView) v.findViewById(R.id.orderQtyText);
        deliveredQuantityText = (TextView) v.findViewById(R.id.deliveredQuantityText);

        OrderResponse.RequestS.ProductOrder item = mData.get(position);

        name.setText(!item.getProductName().isEmpty()?item.getProductName():"");
        orderDateText.setText("Ordered Quantity: "+(!item.getOtsOrderedQty().isEmpty()?item.getOtsOrderedQty():""));

        orderCostText.setText("Product Price: "+item.getOtsOrderProductCost()+context.getString(R.string.Rs));
        deliveredQuantityText.setVisibility(View.VISIBLE);
        deliveredQuantityText.setText("Delivery Quantity: "+item.getOtsDeliveredQty());
        if (item.getOutSbalanceCan()!=null || item.getBalanceCan() != null) {
        }
        else {
            orderQtyText.setVisibility(View.GONE);
        }
        if (item.getProductImage()!=null) {
            Picasso.get().load(item.getProductImage()).into(picture);
        } else {
            picture.setImageResource(R.drawable.no_image);
        }
        return v;
    }

}

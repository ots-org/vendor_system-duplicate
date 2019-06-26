package com.android.water_distribution.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.water_distribution.R;
import com.android.water_distribution.pojo.OrderResponse;

import java.util.ArrayList;
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
        int totalCost = 0;
       for (OrderResponse.RequestS.ProductOrder productOrder: mData){
           totalCost = totalCost + (Integer.valueOf(productOrder.getOtsDeliveredQty()) * Integer.valueOf(productOrder.getOtsOrderProductCost()));
       }
       return String.valueOf(totalCost);
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

        orderCostText.setText("Product Cost: "+item.getOtsOrderProductCost()+context.getString(R.string.Rs));
        deliveredQuantityText.setVisibility(View.VISIBLE);
        deliveredQuantityText.setText("Delivery Quantity: "+item.getOtsDeliveredQty());
        if (item.getOutSbalanceCan()!=null || item.getBalanceCan() != null) {
            orderQtyText.setText("Outstanding Can: " + (item.getOutSbalanceCan() != null ? item.getOutSbalanceCan() : item.getBalanceCan()));
        }
        else {
            orderQtyText.setVisibility(View.GONE);
        }
        /*description.setText(item.getDescription()!=null?item.getDescription():"");*/

        /*Bitmap decodedByte = null;

        if (item.getProductImage()!=null) {
            byte[] decodedString = Base64.decode(item.getProductImage(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        if (decodedByte != null) {
            picture.setImageBitmap(decodedByte);
        } else {*/
            picture.setImageResource(R.drawable.no_image);
        //}

        return v;
    }

}

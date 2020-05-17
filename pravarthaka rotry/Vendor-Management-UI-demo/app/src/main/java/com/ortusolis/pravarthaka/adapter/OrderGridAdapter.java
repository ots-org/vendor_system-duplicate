package com.ortusolis.pravarthaka.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.pojo.OrderResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderGridAdapter extends BaseAdapter {

    private List<OrderResponse.RequestS> mData;
    private List<OrderResponse.RequestS> mOriginalData;
    private LayoutInflater mInflater;
    Context context;
    private PlantFilter mFilter = new PlantFilter();

    // data is passed into the constructor
    public OrderGridAdapter(Context context, List<OrderResponse.RequestS> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOriginalData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
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

        if (v == null) {
            v = mInflater.inflate(R.layout.order_grid_item, parent, false);
        }

        picture = (ImageView) v.findViewById(R.id.picture);
        name = (TextView) v.findViewById(R.id.prodNameText);
        orderDateText = (TextView) v.findViewById(R.id.orderDateText);
        orderCostText = (TextView) v.findViewById(R.id.orderCostText);
        orderQtyText = (TextView) v.findViewById(R.id.orderQtyText);

        OrderResponse.RequestS item = mData.get(position);

        final String regex = "\\d+";

        /*if (item.getPath().matches(regex)) {
            picture.setImageResource(Integer.valueOf(item.getPath()));
        }
        else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(item.getPath(), options);
            picture.setImageBitmap(bitmap);
        }*/

        if (position%2==0){
            picture.setImageResource(R.drawable.water1);

        }
        else {
            picture.setImageResource(R.drawable.water2);
        }

        name.setText(!item.getOrderdProducts().isEmpty()?item.getOrderdProducts().get(0).getProductName():"");
        if (item.getDelivaryDate()!=null)
        orderDateText.setText(item.getDelivaryDate());
        else
            orderDateText.setVisibility(View.GONE);

        orderCostText.setText("Order Cost: "+item.getOrderCost()+context.getString(R.string.Rs));
        orderQtyText.setText("Order Quantity: "+(!item.getOrderdProducts().isEmpty()?item.getOrderdProducts().get(0).getOtsOrderedQty():""));
        /*description.setText(item.getDescription()!=null?item.getDescription():"");*/

        return v;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class PlantFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<OrderResponse.RequestS> list = mOriginalData;

            int count = list.size();
            final ArrayList<OrderResponse.RequestS> nlist = new ArrayList<OrderResponse.RequestS>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getOrderId();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<OrderResponse.RequestS>) results.values;
            notifyDataSetChanged();
        }

    }

}

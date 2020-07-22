package com.ortusolis.rotarytarana.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ortusolis.rotarytarana.R;

public class DonationAdapter  extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] productName ;
    private final String[] productPrice;
    private final String[] addedStock;
    private final String[] stock;

    public DonationAdapter(Activity context, String[] productName, String[] productPrice, String[] addedStock,String[] stock) {
        super(context, R.layout.donation_list, productName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.productName=productName;
        this.productPrice=productPrice;
        this.addedStock=addedStock;
        this.stock=stock;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.donation_list, null,true);

        TextView productNameText = (TextView) rowView.findViewById(R.id.productName);
        TextView productPriceText = (TextView) rowView.findViewById(R.id.productPrice);
        TextView addedStockText = (TextView) rowView.findViewById(R.id.addedStock);
        TextView stockText = (TextView) rowView.findViewById(R.id.stock);
        addedStockText.setVisibility(View.GONE);
        productNameText.append(productName[position]);
        productPriceText.append(productPrice[position]);
        addedStockText.append(addedStock[position]);
        stockText.append((Integer.parseInt(stock[position])-Integer.parseInt(addedStock[position].toString())+""));

        return rowView;

    };
}

package com.ortusolis.rotarytarana.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ortusolis.rotarytarana.R;

public class PendingRequestAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] pendingProductName ;
    private final String[] pendingProductPrice;
    private final String[] pendingDescripetion;

    public PendingRequestAdapter(Activity context, String[] pendingProductName, String[] pendingProductPrice, String[] pendingDescripetion) {
        super(context, R.layout.donation_list, pendingProductName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.pendingProductName=pendingProductName;
        this.pendingProductPrice=pendingProductPrice;
        this.pendingDescripetion=pendingDescripetion;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.pending_request_list, null,true);

        TextView pendingProductNameText = (TextView) rowView.findViewById(R.id.pendingProductName);
        TextView pendingProductPriceText = (TextView) rowView.findViewById(R.id.pendingProductPrice);
        TextView pendingDescriptionText = (TextView) rowView.findViewById(R.id.pendingDescription);

        pendingProductNameText.setText( " Product Name: "+pendingProductName[position]);
//        pendingProductNameText.append(pendingProductName[position]);
        pendingProductPriceText.setText("Product Price: "+pendingProductPrice[position]);
//        pendingProductPriceText.append(pendingProductPrice[position]);
        pendingDescriptionText.setText("Description: "+pendingDescripetion[position]);
//        pendingDescriptionText.append(pendingDescripetion[position]);

        return rowView;

    };
}

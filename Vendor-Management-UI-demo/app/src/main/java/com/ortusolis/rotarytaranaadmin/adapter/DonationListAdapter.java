package com.ortusolis.rotarytaranaadmin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ortusolis.rotarytaranaadmin.R;

public class DonationListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] productName ;
    private final String[] quantity;
    private final String[] donarName;

    public DonationListAdapter(Activity context, String[] productName, String[] quantity, String[] donarName) {
        super(context, R.layout.donation_status_list, productName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.productName=productName;
        this.quantity=quantity;
        this.donarName=donarName;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.donation_status_list, null,true);

        TextView productNameText = (TextView) rowView.findViewById(R.id.productName);
        TextView quantitylist = (TextView) rowView.findViewById(R.id.quantityDonation);
        TextView donarNameList = (TextView) rowView.findViewById(R.id.donarNameList);
        productNameText.append(productName[position]);
        quantitylist.append(quantity[position]);
        donarNameList.append(donarName[position]);
        return rowView;

    };
}

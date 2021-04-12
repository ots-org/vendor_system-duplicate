package com.ortusolis.subhaksha.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ortusolis.subhaksha.R;

public class DonorDonationListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] donarName ;
    private final String[] contactNumber;
    private final String[] address;
    private final String[] quantity ;
    private final String[] amount;
    private final String[] date;
    private final String[] DonarDiscription;

    public DonorDonationListAdapter(Activity context, String[] donarName, String[] contactNumber, String[] address, String[] quantity, String[] amount, String[] date, String[] DonarDiscription) {
        super(context, R.layout.donar_donation_status_list, donarName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.donarName=donarName;
        this.contactNumber=contactNumber;
        this.address=address;
        this.quantity=quantity;
        this.amount=amount;
        this.date=date;
        this.DonarDiscription=DonarDiscription;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.donar_donation_status_list, null,true);

        TextView donarNameList = (TextView) rowView.findViewById(R.id.donarNameList);
        TextView donarContactNumber = (TextView) rowView.findViewById(R.id.donarContactNumber);
        TextView donarAddress = (TextView) rowView.findViewById(R.id.donarAddress);
        TextView quantityDonation = (TextView) rowView.findViewById(R.id.quantityDonation);
        TextView donarAmount = (TextView) rowView.findViewById(R.id.donarAmount);
        TextView donationDate = (TextView) rowView.findViewById(R.id.donationDate);
        TextView donationDescription = (TextView) rowView.findViewById(R.id.donationDescription);
        donarNameList.append(donarName[position]);
        donarContactNumber.append(contactNumber[position]);
        donarAddress.append(address[position]);
        quantityDonation.append(quantity[position]);
        donarAmount.append(amount[position]);
        donationDate.append(date[position]);
        donationDescription.append(DonarDiscription[position]);
        return rowView;

    };
}

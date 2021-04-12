package com.ortusolis.subhaksha.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ortusolis.subhaksha.R;

public class NotificationAdminAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] firstName ;
    private final String[] lastName;
    private final String[] emailId;
    private final String[] contactNo ;
    private final String[] address1;
    private final String[] address2;
    private final String[] pincode;
    private final String[] userRoleId;
    private final String[] userAdminFlag;

    public NotificationAdminAdapter(Activity context, String[] firstName, String[] lastName, String[] emailId, String[] contactNo, String[] address1, String[] address2, String[] pincode, String[] userRoleId,String[] userAdminFlag) {
        super(context, R.layout.notification_admin, firstName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.firstName=firstName;
        this.lastName=lastName;
        this.emailId=emailId;
        this.contactNo=contactNo;
        this.address1=address1;
        this.address2=address2;
        this.pincode=pincode;
        this.userRoleId=userRoleId;
        this.userAdminFlag=userAdminFlag;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.notification_admin, null,true);

        TextView firstName1 = (TextView) rowView.findViewById(R.id.firstName);
        TextView lastName1 = (TextView) rowView.findViewById(R.id.lastName);
        TextView emailId1 = (TextView) rowView.findViewById(R.id.emailId);
        TextView contactNo1 = (TextView) rowView.findViewById(R.id.contactNo);
        TextView address11 = (TextView) rowView.findViewById(R.id.address1);
        TextView address21 = (TextView) rowView.findViewById(R.id.address2);
        TextView pincode1 = (TextView) rowView.findViewById(R.id.pincode);
        TextView userRoleId1 = (TextView) rowView.findViewById(R.id.userRoleId);
        TextView userAdminFlag1 = (TextView) rowView.findViewById(R.id.userAdminFlag);

        firstName1.append(firstName[position]);
        lastName1.append(lastName[position]);
        emailId1.append(emailId[position]);
        contactNo1.append(contactNo[position]);
        address11.append(address1[position]);
        address21.append(address2[position]);
        pincode1.append(pincode[position]);

        if(userRoleId[position].equals("1")){
            userRoleId1.append("admin");
        }else if(userRoleId[position].equals("2")){
            userRoleId1.append("Facilitator");
        }else if(userRoleId[position].equals("3")){
            userRoleId1.append("Partner");
        }else if(userRoleId[position].equals("4")){
            userRoleId1.append("Donor");
        }else {
            userRoleId1.append("Beneficiary");
        }
        if(userAdminFlag[position].equals("1")){
            userAdminFlag1.append("Yes");
        }else {
            userAdminFlag1.append("No");
        }

        return rowView;

    };
}

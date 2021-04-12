package com.ortusolis.subhaksha.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ortusolis.subhaksha.R;
import com.squareup.picasso.Picasso;

public class AdapterCatagoryLIst extends ArrayAdapter<String> {

    private final Activity  context;
    private final String[] imgid;

    public AdapterCatagoryLIst(Activity context, String[] imgid) {
        super(context, R.layout.mylist);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.imgid=imgid;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        Picasso.get().load(imgid[position]).into(imageView);
//        imageView.setImageResource(imgid[position]);

        return rowView;

    };
}
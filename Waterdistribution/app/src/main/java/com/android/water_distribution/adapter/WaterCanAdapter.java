package com.android.water_distribution.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.water_distribution.Activity.ProductDescription;
import com.android.water_distribution.R;
import com.android.water_distribution.pojo.ProductDetails;

import java.util.ArrayList;
import java.util.List;

public class WaterCanAdapter extends BaseAdapter {

    private ArrayList<ProductDetails> mData;
    private ArrayList<ProductDetails> mOriginalData;
    private LayoutInflater mInflater;
    Context context;
    private PlantFilter mFilter = new PlantFilter();

    // data is passed into the constructor
    public WaterCanAdapter(Context context, ArrayList<ProductDetails> data) {
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
        final TextView description;
        final Button buyNow;
        final Button scheduleButton;

        if (v == null) {
            v = mInflater.inflate(R.layout.water_can_grid_item, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.description, v.findViewById(R.id.description));
            v.setTag(R.id.buyButton, v.findViewById(R.id.buyButton));
            v.setTag(R.id.scheduleButton, v.findViewById(R.id.scheduleButton));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        description = (TextView) v.getTag(R.id.description);
        buyNow = (Button) v.getTag(R.id.buyButton);
        scheduleButton = (Button) v.getTag(R.id.scheduleButton);

        //final PlantModel item = (PlantModel) getItem(position);

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

        Bitmap decodedByte = null;

        if (mData.get(position).getProductImage()!=null) {
            byte[] decodedString = Base64.decode(mData.get(position).getProductImage(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        if (decodedByte != null) {
            picture.setImageBitmap(decodedByte);
        } else {
            picture.setImageResource(R.drawable.no_image);
        }

        description.setText("Price " + context.getString(R.string.Rs) + mData.get(position).getProductPrice());

        name.setText(mData.get(position).getProductName());
        /*description.setText(item.getDescription()!=null?item.getDescription():"");*/

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDescription.class);
                Pair[] views = new Pair[]{
                        new Pair<View, String>(picture, "object_image"),
                        new Pair<View, String>(name, "object_name"),
                        new Pair<View, String>(description, "object_description")
                };

                if (position % 2 == 0) {
                    intent.putExtra("price", 30);
                } else {
                    intent.putExtra("price", 20);
                }
                intent.putExtra("product", mData.get(position));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, views);

                context.startActivity(intent, options.toBundle());

            }
        };

        v.setOnClickListener(onClickListener);
        buyNow.setOnClickListener(onClickListener);

        if (context.getSharedPreferences("water_management", 0).getString("userRoleId", "").equalsIgnoreCase("2")) {
            scheduleButton.setVisibility(View.VISIBLE);
        } else {
            scheduleButton.setVisibility(View.GONE);
        }

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDescription.class);
                Pair[] views = new Pair[]{
                        new Pair<View, String>(picture, "object_image"),
                        new Pair<View, String>(name, "object_name"),
                        new Pair<View, String>(description, "object_description")
                };

                if (position % 2 == 0) {
                    intent.putExtra("price", 30);
                } else {
                    intent.putExtra("price", 20);
                }
                intent.putExtra("product", mData.get(position));
                intent.putExtra("schedule", true);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, views);

                context.startActivity(intent, options.toBundle());
            }
        });

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

            final List<ProductDetails> list = mOriginalData;

            int count = list.size();
            final ArrayList<ProductDetails> nlist = new ArrayList<ProductDetails>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getProductName();
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
            mData = (ArrayList<ProductDetails>) results.values;
            notifyDataSetChanged();
        }

    }

}

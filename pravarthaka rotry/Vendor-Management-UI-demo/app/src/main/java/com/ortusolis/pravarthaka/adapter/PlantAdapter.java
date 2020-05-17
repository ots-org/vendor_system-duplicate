package com.ortusolis.pravarthaka.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.Activity.ViewPlant;
import com.ortusolis.pravarthaka.pojo.PlantModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter  extends BaseAdapter {

    private ArrayList<PlantModel> mData;
    private ArrayList<PlantModel> mOriginalData;
    private LayoutInflater mInflater;
    Context context;
    private PlantFilter mFilter = new PlantFilter();

    // data is passed into the constructor
    public PlantAdapter(Context context, ArrayList<PlantModel> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ImageView picture;
        final TextView name;
        final TextView description;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.description, v.findViewById(R.id.description));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        description = (TextView) v.getTag(R.id.description);

        final PlantModel item = (PlantModel) getItem(position);

        final String regex = "\\d+";

        if (item.getPath().matches(regex)) {
            picture.setImageResource(Integer.valueOf(item.getPath()));
        }
        else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(item.getPath(), options);
            picture.setImageBitmap(bitmap);
        }
        name.setText(item.getName());
        description.setText(item.getDescription()!=null?item.getDescription():"");

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewPlant.class);
                intent.putExtra("plant",new Gson().toJson(item));
                Pair[] views = new Pair[]{
                        new Pair<View, String>(picture, "object_image"),
                        new Pair<View, String>(name, "object_name"),
                        new Pair<View, String>(description, "object_description")
                };


                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity)context, views);

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

            final List<PlantModel> list = mOriginalData;

            int count = list.size();
            final ArrayList<PlantModel> nlist = new ArrayList<PlantModel>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
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
            mData = (ArrayList<PlantModel>) results.values;
            notifyDataSetChanged();
        }

    }

}

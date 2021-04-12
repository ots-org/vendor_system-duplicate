package com.ortusolis.subhaksha.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ortusolis.subhaksha.Activity.MainActivity;
import com.ortusolis.subhaksha.R;
import com.squareup.picasso.Picasso;

import java.util.List;

// The adapter class which
// extends RecyclerView Adapter
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<String> imageUrlList;
    private List<String> categoryId;
    Context context;
    SharedPreferences sharedPreferences;



    class MyViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;
        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.picture);

        }
    }
    public CategoryAdapter(Context context,List<String> moviesList,List<String>categoryId) {
        this.imageUrlList = moviesList;
        this.context = context;
        this.categoryId=categoryId;
        sharedPreferences = context.getSharedPreferences("water_management",0);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String imageLoad = imageUrlList.get(position);
//        holder.imageView.setImageURI(Uri.parse(imageLoad.getUrl()));
        Picasso.get().load(imageLoad).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("categoryIdClicked",categoryId.get(position)).commit();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("a","A");
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }


}
















     /*   extends RecyclerView.Adapter<Adapter.MyView> {

    // List with String type
    private List<String> list;
    Context context;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView textView; //
        ImageView imageView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
//            textView = (TextView)view
//                    .findViewById(R.id.textview);

            imageView = (ImageView)view
                    .findViewById(R.id.picture);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public Adapter(List<String> horizontalList)
    {
        this.list = horizontalList;
    }


//    private final View.OnClickListener mOnClickListener = new MyOnClickListener();


    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item,
                        parent,
                        false);

//        itemView.setOnClickListener(mOnClickListener);
        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
//        holder.textView.setText(list.get(position));
        holder.imageView.setImageResource(R.drawable.vkf_logo);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a=0;
                int b=a;
//                Toast.makeText(getApplication(),"clicked on " +position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }





}*/


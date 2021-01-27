package com.ortusolis.etaaranavkf.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.etaaranavkf.Activity.AddProductActivity;
import com.ortusolis.etaaranavkf.Activity.DonationActivityDiscription;
import com.ortusolis.etaaranavkf.Activity.LoginActivity;
import com.ortusolis.etaaranavkf.Activity.ProductDescription;
import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.NetworkUtility.IResult;
import com.ortusolis.etaaranavkf.NetworkUtility.WebserviceController;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.fragment.ProductsFragment;
import com.ortusolis.etaaranavkf.pojo.ProductDetails;
import com.ortusolis.etaaranavkf.pojo.UpdateProductStatusResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WaterCanAdapter extends BaseAdapter {

    private ArrayList<ProductDetails> mData;
    private ArrayList<ProductDetails> mOriginalData;
    private LayoutInflater mInflater;
    Context context;
    private PlantFilter mFilter = new PlantFilter();
    ProductsFragment productsFragment;
    EditText searchContent;
    String paymentMethod="";

    // data is passed into the constructor
    public WaterCanAdapter(Context context, ArrayList<ProductDetails> data, ProductsFragment productsFragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mOriginalData = data;
        this.context = context;
        this.productsFragment = productsFragment;
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
        final ImageButton editProduct;
        final LinearLayout activeLL;
        final ImageView checkBoxImg;
        final Button buyNow;
//        final Button scheduleButton;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("water_management",0);

        if (v == null) {
            v = mInflater.inflate(R.layout.water_can_grid_item, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.description, v.findViewById(R.id.description));
            v.setTag(R.id.activeLL, v.findViewById(R.id.activeLL));
            v.setTag(R.id.checkBoxImg, v.findViewById(R.id.checkBoxImg));
            v.setTag(R.id.editProduct, v.findViewById(R.id.editProduct));
            v.setTag(R.id.buyButton, v.findViewById(R.id.buyButton));
//            v.setTag(R.id.scheduleButton, v.findViewById(R.id.scheduleButton));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        description = (TextView) v.getTag(R.id.description);
        editProduct = (ImageButton) v.getTag(R.id.editProduct);
        activeLL = (LinearLayout) v.getTag(R.id.activeLL);
        checkBoxImg = (ImageView) v.getTag(R.id.checkBoxImg);
        buyNow = (Button) v.getTag(R.id.buyButton);
//        scheduleButton = (Button) v.getTag(R.id.scheduleButton);

        if (mData.get(position).getProductImage()!=null) {
            Picasso.get().load(mData.get(position).getProductImage()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(picture);
        }else {
            picture.setImageResource(R.drawable.no_image);
        }

        if(mData.get(position).getProductPrice()!=null) {
            description.setText("Price " + context.getString(R.string.Rs) + (String.format("%.02f", Float.valueOf(mData.get(position).getProductPrice()))));
        }

        name.setText(mData.get(position).getProductName());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("000")) {
                    loginPage();
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
                }else  if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){

                }else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){

                }else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){

                }
                else {
                    AlertDialog.Builder alertDialogCash = new AlertDialog.Builder(context);
                    alertDialogCash.setTitle("Choose Payment Option");
                    String[] itemsCash = {"Gift","Donate"};
                    int checkedItemCash = 0;
                    paymentMethod="Request";
                    alertDialogCash.setSingleChoiceItems(itemsCash, checkedItemCash, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    paymentMethod="Request";
                                    break;
                                case 1:
                                    paymentMethod="Donate";
                                    break;
                            }
                        }
                    });
                    alertDialogCash.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialogCash.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // navigation

//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    if (paymentMethod.equals("Cash")) {
//                        builder.setMessage("Your Order Cost is "+mData.get(position).getProductPrice()+", click Confirm to place request");
//                    }else {
//                        builder.setMessage("Your Order Cost is "+mData.get(position).getProductPrice()+", click Confirm to place Donate");
//                    }
//                    builder.setCancelable(true)
//                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
                                    if (paymentMethod.equals("Request")) {
                                        dialog.dismiss();
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
                                        intent.putExtra("customerId", productsFragment.getCustomerStr());
                                        intent.putExtra("paymentMethod",paymentMethod);
                                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                                makeSceneTransitionAnimation((Activity) context, views);

                                        try {
                                            context.startActivity(intent, options.toBundle());
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Intent intent = new Intent(context, DonationActivityDiscription.class);
                                        Pair[] views = new Pair[]{
                                                new Pair<View, String>(picture, "object_image"),
                                                new Pair<View, String>(name, "object_name"),
                                                new Pair<View, String>(description, "object_description")
                                        };

                                        intent.putExtra("productID", mData.get(position).getProductId());
                                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                                makeSceneTransitionAnimation((Activity) context, views);

                                        try {
                                            context.startActivity(intent, options.toBundle());
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
                }
            });
              AlertDialog alert = alertDialogCash.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                }
            }
        };

        v.setOnClickListener(onClickListener);
        buyNow.setOnClickListener(onClickListener);

        if (context.getSharedPreferences("water_management", 0).getString("userRoleId", "").equalsIgnoreCase("2")) {
//            scheduleButton.setVisibility(View.VISIBLE);
            buyNow.setVisibility(View.GONE);
        } else {
//            scheduleButton.setVisibility(View.GONE);
        }
        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
            buyNow.setVisibility(View.GONE);
        }
        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            buyNow.setVisibility(View.GONE);
            activeLL.setVisibility(View.VISIBLE);
        }
        else {
            activeLL.setVisibility(View.GONE);
        }

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            editProduct.setVisibility(View.VISIBLE);
            editProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddProductActivity.class);
                    intent.putExtra("product",  mData.get(position));
                    context.startActivity(intent);
                }
            });
        }
        else {
            editProduct.setVisibility(View.GONE);
        }

        if (mData.get(position).getProductStatus().equalsIgnoreCase("active")){
            checkBoxImg.setImageResource(android.R.drawable.checkbox_on_background);
        }
        else {
            checkBoxImg.setImageResource(android.R.drawable.checkbox_off_background);
        }

        activeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getProductStatus().equalsIgnoreCase("active")){
                    activeOrNot(context, "INACTIVE",mData.get(position).getProductId());
                    checkBoxImg.setImageResource(android.R.drawable.checkbox_off_background);
                    mData.get(position).setProductStatus("INACTIVE");
                }
                else {
                    activeOrNot(context, "ACTIVE",mData.get(position).getProductId());
                    checkBoxImg.setImageResource(android.R.drawable.checkbox_on_background);
                    mData.get(position).setProductStatus("ACTIVE");
                }

            }
        });

//        scheduleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    Intent intent = new Intent(context, ProductDescription.class);
//                    Pair[] views = new Pair[]{
//                            new Pair<View, String>(picture, "object_image"),
//                            new Pair<View, String>(name, "object_name"),
//                            new Pair<View, String>(description, "object_description")
//                    };
//
//                    if (position % 2 == 0) {
//                        intent.putExtra("price", 30);
//                    } else {
//                        intent.putExtra("price", 20);
//                    }
//                    intent.putExtra("product", mData.get(position));
//                    intent.putExtra("customerId", productsFragment.getCustomerStr());
//                    intent.putExtra("schedule", true);
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation((Activity) context, views);
//
//                    context.startActivity(intent, options.toBundle());
//            }
//        });

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

    void activeOrNot(final Context context, String activeOrNot,String prodId) {
        WebserviceController wss = new WebserviceController(context);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", activeOrNot);
            jsonObject.put("productId", prodId);
            requestObject.put("request", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.updateProductStatus, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("login response", response);

                    UpdateProductStatusResponse responseData = new Gson().fromJson(response, UpdateProductStatusResponse.class);

                    Toast.makeText(context, responseData.getResponseData(), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
            }
        });
    }
    public void loginPage(){
        AlertDialog.Builder alertDialogCash = new AlertDialog.Builder(context);
        alertDialogCash.setTitle("Please Login to  make Donation !!!");
        alertDialogCash.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogCash.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // navigation
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        AlertDialog alert = alertDialogCash.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}

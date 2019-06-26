package com.android.water_distribution.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.adapter.WaterCanAdapter;
import com.android.water_distribution.pojo.ProductDetails;
import com.android.water_distribution.pojo.ProductsResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    GridView gridview;
    SharedPreferences sharedPreferences;
    Gson gson;
    WaterCanAdapter waterCanAdapter;
    TextView noResult;

    public static ProductsFragment newInstance() {

        Bundle args = new Bundle();

        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product,null);
        gridview = view.findViewById(R.id.gridview);
        noResult = view.findViewById(R.id.noResult);
        gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences("water_management",0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
    }

    void getProducts(){

        WebserviceController wss = new WebserviceController(getActivity());


        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") ? sharedPreferences.getString("userid","") : sharedPreferences.getString("distId","")));

            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);

                    ProductsResponse responseData = gson.fromJson(response,ProductsResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        /*ArrayList<ProductDetails> productDetailsS = new ArrayList<>();
                        for (ProductDetails productDetails : responseData.getResponseData().getProductDetails()){
                            if (productDetails.getStock().equalsIgnoreCase("yes")){
                                productDetailsS.add(productDetails);
                            }
                        }*/

                        waterCanAdapter = new WaterCanAdapter(getActivity(),responseData.getResponseData().getProductDetails());
                        gridview.setAdapter(waterCanAdapter);

                        waterCanAdapter.notifyDataSetChanged();

                        if (responseData.getResponseData().getProductDetails().isEmpty()){
                            noResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            noResult.setVisibility(View.GONE);
                        }

                    }
                    else {
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));

                if (waterCanAdapter == null || waterCanAdapter.getCount()==0){
                    noResult.setVisibility(View.VISIBLE);
                }
                else {
                    noResult.setVisibility(View.GONE);
                }

                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    // Add this inside your class
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();
            if (b.containsKey("productId")) {
                getProducts();
            }
        }
    };

}

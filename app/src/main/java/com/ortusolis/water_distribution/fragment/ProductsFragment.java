package com.ortusolis.water_distribution.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.water_distribution.NetworkUtility.Constants;
import com.ortusolis.water_distribution.NetworkUtility.IResult;
import com.ortusolis.water_distribution.NetworkUtility.WebserviceController;
import com.ortusolis.water_distribution.R;
import com.ortusolis.water_distribution.adapter.WaterCanAdapter;
import com.ortusolis.water_distribution.pojo.DistributorResponse;
import com.ortusolis.water_distribution.pojo.ProductsResponse;
import com.ortusolis.water_distribution.pojo.UserInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductsFragment extends Fragment {

    GridView gridview;
    SharedPreferences sharedPreferences;
    Gson gson;
    WaterCanAdapter waterCanAdapter;
    TextView noResult;
    LinearLayout customeLinearLayout;
    String customerStr = "";
    String customerStrName = null;
    String strCustName = null;
    String strCust = null;
    TextView customerText;
    //
    ProgressDialog progressDialog;
    //
    List<String> userNames, userIdList;
    boolean custSelect = false;
    ProgressBar loading_indicator;

    public String getCustomerStr() {
        return customerStr;
    }

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
        customeLinearLayout = view.findViewById(R.id.customeLinearLayout);
        customerText = view.findViewById(R.id.customerStr);
        loading_indicator = view.findViewById(R.id.loading_indicator);
        gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences("water_management",0);

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")) {
        customerStr = sharedPreferences.getString("userid","");
        }

        if (sharedPreferences.contains("userRoleId") && (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3"))){
            customeLinearLayout.setVisibility(View.VISIBLE);
        }
        else {
            customeLinearLayout.setVisibility(View.GONE);
        }

        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //
                if (!custSelect) {
                    custSelect = true;
                    getMappedRegister();
                }
                //getAllRegister(true,true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(sharedPreferences.contains("userRoleId") && (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")))) {
            getProducts();
        }
    }

    void getProducts(){

        WebserviceController wss = new WebserviceController(getActivity());
        //loading_indicator.setVisibility(View.VISIBLE);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", ((sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")) ? sharedPreferences.getString("userid","") : sharedPreferences.getString("distId","")));

            if (customerStr!=null && !customerStr.isEmpty())
                jsonObject.put("customerId", customerStr);

            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Restricted Image size.
       // File file = new File(selectedPath);
       // int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        //

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                //loading_indicator.setVisibility(View.GONE);
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

                        waterCanAdapter = new WaterCanAdapter(getActivity(),responseData.getResponseData().getProductDetails(),ProductsFragment.this);
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
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                //loading_indicator.setVisibility(View.GONE);
                if (waterCanAdapter == null || waterCanAdapter.getCount()==0){
                    noResult.setVisibility(View.VISIBLE);
                }
                else {
                    noResult.setVisibility(View.GONE);
                }

                try {
                    Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
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

    void getMappedRegister(){
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
                jsonObject.put("mappedTo", sharedPreferences.getString("distId", ""));
            }
            else {
                jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
            }

            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        userNames.clear();
        userIdList.clear();

        wss.postLoginVolley(Constants.getUserDetailsByMapped, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);
                    custSelect = false;
                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        //
                        progressDialog.dismiss();
                        //

                        strCust = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){

                            if (userInfo1.getUserRoleId().equalsIgnoreCase("4")) {
                                userNames.add(userInfo1.getFirstName());
                                userIdList.add(userInfo1.getUserId());
                            }

                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(getActivity(), "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Customer</font>"));

                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = userNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        strCust = userIdList.get(checkedItem);
                        strCustName = userNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                strCust = userIdList.get(which);
                                strCustName = userNames.get(which);
                            }
                        });

                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               //
                                customerText.setText(strCustName );
                                //
                                customerStrName = strCustName;
                                customerStr = strCust;
                                getProducts();
                            }
                        });

                        builderSingle.show();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                custSelect = false;
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }

}

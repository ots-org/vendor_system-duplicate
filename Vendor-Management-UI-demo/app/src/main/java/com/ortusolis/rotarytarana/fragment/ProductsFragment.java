package com.ortusolis.rotarytarana.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
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
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.WaterCanAdapter;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.ProductsResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductsFragment extends Fragment {

    GridView gridview;
    SharedPreferences sharedPreferences;
    Gson gson;
    WaterCanAdapter waterCanAdapter;
    TextView noResult,customerProductStr,customerProductStrBase;
    LinearLayout customeLinearLayout,customerProductLinearLayout;
    String customerStr = "";
    String progressStatus = "";
    String customerStrName = null;
    String strCustName = null;
    String strCust = null;
    TextView customerText;
    ProgressDialog progressDialog;
    List<String> productCatagoryNames, productCatagoryIdList;
    String productName = null;
    String productCust = null;
    List<String> userNames, userIdList;
    boolean custSelect = false;
    boolean flag_loading=false;
    ProgressBar loading_indicator;
    Button searchButtonHome;
    EditText searchContent;
    ProductsResponse responseDatafinal;

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
        customerProductLinearLayout=view.findViewById(R.id.customerProductLinearLayout);
        customerText = view.findViewById(R.id.customerStr);
        loading_indicator = view.findViewById(R.id.loading_indicator);
        gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences("water_management",0);
        customerProductStr= view.findViewById(R.id.customerProductStr);
        customerProductStrBase=view.findViewById(R.id.customerProductStrBase);
        productCatagoryNames = new ArrayList<>();
        productCatagoryIdList = new ArrayList<>();
        searchButtonHome =  view.findViewById(R.id.searchButtonHome);
        searchContent=  view.findViewById(R.id.searchContent);
        progressStatus="yes";
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")) {
        customerStr = sharedPreferences.getString("userid","");
            customerProductLinearLayout.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.contains("userRoleId") && (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3"))){
            customeLinearLayout.setVisibility(View.GONE);
        }
        else {
            customeLinearLayout.setVisibility(View.GONE);
        }

        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (!custSelect) {
                    custSelect = true;
                    getMappedRegister();
                }
            }
        });
        customerProductStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductCatqgory();
            }
        });
        searchButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchContent.getText().length()>0){
                    ProductSearch();
                }
            }
        });
        if(sharedPreferences.getString("userProductSelect","").equalsIgnoreCase("yes")){
            productCust=sharedPreferences.getString("userProductSelectId","");
            productName=sharedPreferences.getString("userProductSelectProduct","");
            getProducts();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }else {
            getProductsMain();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                searchContent.setText("");
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        new loadMoreListView().execute();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(sharedPreferences.contains("userRoleId") && (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")))) {
            if(sharedPreferences.getString("userProductSelect","").equalsIgnoreCase("yes")){
                progressStatus="yes";
                getProducts();
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }else {
                progressStatus="yes";
                getProductsMain();
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }
    }

    void getProducts(){
        if(progressStatus.equals("yes")) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "product");
            jsonObject.put("searchvalue", productCust);
            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                searchContent.setText("");
                sharedPreferences.edit().putString("userProductSelect","yes").commit();
                sharedPreferences.edit().putString("userProductSelectId",productCust).commit();
                sharedPreferences.edit().putString("userProductSelectProduct",productName).commit();
                customerProductStr.setText(productName );
                customerProductStrBase.setText("Selected Sub-Category");
                try {
                    Log.e("login response",response);
                    ProductsResponse responseData = gson.fromJson(response,ProductsResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
//                        gridview.removeAllViews();
//                        waterCanAdapter.notifyDataSetInvalidated();
                        waterCanAdapter = new WaterCanAdapter(getActivity(),responseData.getResponseData().getProductDetails(),ProductsFragment.this);
                        waterCanAdapter.notifyDataSetChanged();
                        gridview.setAdapter(waterCanAdapter);
//                        list.remove(position);
//                        recycler.removeViewAt(position);
//                        mAdapter.notifyItemRemoved(position);
//                        mAdapter.notifyItemRangeChanged(position, list.size());

                        if(progressStatus.equals("yes")){
                            progressDialog.dismiss();
                            progressStatus="no";
                        }

                        if (responseData.getResponseData().getProductDetails().isEmpty()){
                            noResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            noResult.setVisibility(View.GONE);
                        }
                    }
                    else {
                        if(progressStatus.equals("yes")){
                            progressDialog.dismiss();
                            progressStatus="no";
                        }
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if(progressStatus.equals("yes")){
                    progressDialog.dismiss();
                    progressStatus="no";
                }
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                if (waterCanAdapter == null || waterCanAdapter.getCount()==0){
                    noResult.setVisibility(View.VISIBLE);
                }
                else {
                    noResult.setVisibility(View.GONE);
                }
            }
        });

    }


    void getProductsMain(){
        if(progressStatus.equals("yes")) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("status", "active");
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
                        waterCanAdapter = new WaterCanAdapter(getActivity(),responseData.getResponseData().getProductDetails(),ProductsFragment.this);

                        waterCanAdapter.notifyDataSetChanged();
                        gridview.setAdapter(waterCanAdapter);
                        if(progressStatus.equals("yes")){
                            progressDialog.dismiss();
                            progressStatus="no";
                        }

                        if (responseData.getResponseData().getProductDetails().isEmpty()){
                            noResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            noResult.setVisibility(View.GONE);
                        }
                    }
                    else {
                        if(progressStatus.equals("yes")){
                            progressDialog.dismiss();
                            progressStatus="no";
                        }
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if(progressStatus.equals("yes")){
                    progressDialog.dismiss();
                    progressStatus="no";
                }
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                if (waterCanAdapter == null || waterCanAdapter.getCount()==0){
                    noResult.setVisibility(View.VISIBLE);
                }
                else {
                    noResult.setVisibility(View.GONE);
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
//                getProducts();
            }
        }
    };

    void getMappedRegister(){
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){

                jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
            }
            else if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
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
                        progressDialog.dismiss();
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
                                customerText.setText(strCustName );
                                customerStrName = strCustName;
                                customerStr = strCust;
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
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }

    public void getProductCatqgory(){
        searchContent.setText("");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "category");
            jsonObject.put("searchvalue", "1");
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        productCatagoryNames.clear();
        productCatagoryIdList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                progressDialog.dismiss();
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productDetails = responseData.getJSONArray("productDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productCatagoryNames.clear();
                        productCatagoryIdList.clear();
                        for (int ProdCategory=0;ProdCategory<productDetails.length();ProdCategory++){
                            JSONObject productDetailsobject = productDetails.getJSONObject(ProdCategory);
                            productCatagoryNames.add(productDetailsobject.getString("productName"));
                            productCatagoryIdList.add(productDetailsobject.getString("productId"));
                        }
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product Category</font>"));
                        builderSingle.setCancelable(false);
                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productCatagoryNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        productCust = productCatagoryIdList.get(checkedItem);
                        productName = productCatagoryNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                productCust = productCatagoryIdList.get(which);
                                productName = productCatagoryNames.get(which);
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
                                getProductSubCatqgory(productCust);
                                Toast.makeText(getActivity(), "selected Product category is "+ productName, Toast.LENGTH_LONG).show();

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
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }

    public void getProductSubCatqgory(String prdCatId){

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "subcategory");
            jsonObject.put("searchvalue", prdCatId);
            jsonObject.put("distributorId", 1);
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        productCatagoryNames.clear();
        productCatagoryIdList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                progressDialog.dismiss();
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productDetails = responseData.getJSONArray("productDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productCatagoryNames.clear();
                        productCatagoryIdList.clear();
                        for (int ProdCategory=0;ProdCategory<productDetails.length();ProdCategory++){
                            JSONObject productDetailsobject = productDetails.getJSONObject(ProdCategory);
                            productCatagoryNames.add(productDetailsobject.getString("productName"));
                            productCatagoryIdList.add(productDetailsobject.getString("productId"));
                        }
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose ProductSub- Category</font>"));
                        builderSingle.setCancelable(false);
                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productCatagoryNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        productCust = productCatagoryIdList.get(checkedItem);
                        productName = productCatagoryNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                productCust = productCatagoryIdList.get(which);
                                productName = productCatagoryNames.get(which);
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

                                getProducts();
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getActivity(), "selected Product Sub-Category is "+ productName, Toast.LENGTH_LONG).show();
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
                progressDialog.dismiss();
                custSelect = false;
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }

    public void getProductfinalList(String prdSubCatId){
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "product");
            jsonObject.put("searchvalue", prdSubCatId);
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        productCatagoryNames.clear();
        productCatagoryIdList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productDetails = responseData.getJSONArray("productDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productCatagoryNames.clear();
                        productCatagoryIdList.clear();
                        for (int ProdCategory=0;ProdCategory<productDetails.length();ProdCategory++){
                            JSONObject productDetailsobject = productDetails.getJSONObject(ProdCategory);
                            productCatagoryNames.add(productDetailsobject.getString("productName"));
                            productCatagoryIdList.add(productDetailsobject.getString("productId"));
                        }
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product Category</font>"));
                        builderSingle.setCancelable(false);
                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productCatagoryNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        productCust = productCatagoryIdList.get(checkedItem);
                        productName = productCatagoryNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                productCust = productCatagoryIdList.get(which);
                                productName = productCatagoryNames.get(which);
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
                                sharedPreferences.edit().putString("productName",productName).commit();
                                customerProductStr.setText(productName );
                                customerProductStrBase.setText("Selected Product");
                                Toast.makeText(getActivity(), "selected id is "+ productName, Toast.LENGTH_LONG).show();
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
            }
        });
    }
    public  void  ProductSearch() {

        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "FirstLetter");
            jsonObject.put("searchvalue", searchContent.getText());
            jsonObject.put("distributorId", "string");
            jsonObject.put("customerId",null);
            jsonObject.put("status","3");
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
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                if (waterCanAdapter == null || waterCanAdapter.getCount()==0){
                    noResult.setVisibility(View.VISIBLE);
                }
                else {
                    noResult.setVisibility(View.GONE);
                }
            }
        });
    }
    private class loadMoreListView extends AsyncTask< Void, Void, String> {


        protected String doInBackground(Void... unused) {
            flag_loading = false;
            final String[] result = {null};
            WebserviceController wss = new WebserviceController(getActivity());

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("searchKey", "product");
                jsonObject.put("searchvalue", productCust);
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
                            responseDatafinal=responseData;
                            result[0] ="";
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
                        progressDialog.dismiss();
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
                }
            });

            return result[0];
        }

        protected void onPostExecute(String result) {
            if (result != null) {
            searchContent.setText("");
            waterCanAdapter = new WaterCanAdapter(getActivity(),responseDatafinal.getResponseData().getProductDetails(),ProductsFragment.this);
            gridview.setAdapter(waterCanAdapter);
            waterCanAdapter.notifyDataSetChanged();
            }
        }
    }

}

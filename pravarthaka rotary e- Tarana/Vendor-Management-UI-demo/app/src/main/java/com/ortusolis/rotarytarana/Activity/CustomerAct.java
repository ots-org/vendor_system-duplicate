package com.ortusolis.rotarytarana.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.GeneralResponse;
import com.ortusolis.rotarytarana.pojo.NotificationResponse;
import com.ortusolis.rotarytarana.pojo.ProductDetails;
import com.ortusolis.rotarytarana.pojo.ProductsResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerAct extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    TextView distributorText;
    TextView productText;
    Button rejectButton,approveButton;
    TextView nameText,phoneText,addressText,emailText;
    UserInfo userInfo;
    RelativeLayout distributorCodeLayout;
    LinearLayout productLayout;
    EditText productEdit;

    SharedPreferences sharedPreferences;

    String selEmpl = "";
    String strName = "";
    Gson gson;
    List<String> userNames, userIdList;
    String strDistName = null;
    String strDist = null;
    String distributorStr = "1";
    List<String> productNames, productIdList, productPriceList;
    String productDistName = null;
    String productDist = null;
    String productPrice = null;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_screen);

        mToolbar = findViewById(R.id.toolbar);
        distributorText = findViewById(R.id.distributorText);
        productText = findViewById(R.id.productText);
        productEdit = findViewById(R.id.productEdit);
        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);
        addressText = findViewById(R.id.addressText);
        emailText = findViewById(R.id.emailText);
        distributorCodeLayout = findViewById(R.id.distributorCodeLayout);
        rejectButton = findViewById(R.id.rejectButton);
        approveButton = findViewById(R.id.approveButton);
        //productPriceEdit = findViewById(R.id.productPriceEdit);
        productLayout = findViewById(R.id.productLayout);
        sharedPreferences = getSharedPreferences("water_management",0);

        setSupportActionBar(mToolbar);

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        productNames = new ArrayList<>();
        productIdList = new ArrayList<>();
        productPriceList = new ArrayList<>();

        gson = new Gson();

        userInfo = getIntent().getExtras().getParcelable("user");

        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);

            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            if (userInfo.getUserRoleId().equalsIgnoreCase("2")){
                toolbarTitle.setText("New Distributor");
                distributorCodeLayout.setVisibility(View.GONE);
                distributorText.setText(sharedPreferences.getString("username","")+" ("+sharedPreferences.getString("userid","")+" )");
                productLayout.setVisibility(View.GONE);
            }
            else if (userInfo.getUserRoleId().equalsIgnoreCase("3")){
                toolbarTitle.setText("New Partner");
                if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                    distributorCodeLayout.setVisibility(View.VISIBLE);
                    distributorText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getAllRegister(true);
                        }
                    });
                }
                else {
                    distributorCodeLayout.setVisibility(View.VISIBLE);
                }

                productLayout.setVisibility(View.GONE);
            }
            else if (userInfo.getUserRoleId().equalsIgnoreCase("4")){
                toolbarTitle.setText("New Customer");
                distributorCodeLayout.setVisibility(View.VISIBLE);
                productLayout.setVisibility(View.VISIBLE);

                if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                    distributorText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getAllRegister(true);
                        }
                    });
                }

            }
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        nameText.setText(userInfo.getFirstName()+" "+userInfo.getLastName());
        phoneText.setText(userInfo.getPhonenumber());
        addressText.setText(userInfo.getAddress1());
        emailText.setText(userInfo.getEmailId());

        if (userInfo.getMappedTo()!=null){
            getAllRegister(false);
        }

        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProducts(true);
            }
        });

        if (userInfo!=null && userInfo.getUserRoleId().equalsIgnoreCase("4")){
            getProducts(false);
        }

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveNewUserRegistration();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectUser();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void approveNewUserRegistration(){

        WebserviceController wss = new WebserviceController(CustomerAct.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("registrationId", userInfo.getRegistrationId());
            jsonObject.put("productId", productDist==null ? JSONObject.NULL : productDist);
            jsonObject.put("productPrice", !TextUtils.isEmpty(productEdit.getText().toString()) ? productEdit.getText().toString():"0");

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.approveRegistration, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    GeneralResponse responseData = gson.fromJson(response, GeneralResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            setResult(RESULT_OK);
                            Toast.makeText(CustomerAct.this, responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                            finish();
                    }

                    Toast.makeText(CustomerAct.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Approval Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }

    void rejectUser(){

        WebserviceController wss = new WebserviceController(CustomerAct.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("registrationId", userInfo.getRegistrationId());
            jsonObject.put("status", "reject");

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.rejectUser, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    GeneralResponse responseData = gson.fromJson(response, GeneralResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            setResult(RESULT_OK);
                            finish();
                    }

                    Toast.makeText(CustomerAct.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "No Description from API" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }

    void mappUser(){

        WebserviceController wss = new WebserviceController(CustomerAct.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
            jsonObject.put("userId", userInfo.getRegistrationId());

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.mappUser, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    GeneralResponse responseData = gson.fromJson(response, GeneralResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            setResult(RESULT_OK);
                            Toast.makeText(CustomerAct.this, responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                            finish();
                        if (userInfo.getUserRoleId().equalsIgnoreCase("4")){
                            MapUserProduct();
                        }
                    }

                    Toast.makeText(CustomerAct.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Login Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }

    void MapUserProduct(){

        WebserviceController wss = new WebserviceController(CustomerAct.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
            jsonObject.put("productId", productDist==null ? JSONObject.NULL : productDist);
            jsonObject.put("productPrice", !TextUtils.isEmpty(productEdit.getText().toString()) ? productEdit.getText().toString():"0");
            jsonObject.put("productname", productDistName==null?JSONObject.NULL:productDistName);
            jsonObject.put("customerProductId", productDist==null?JSONObject.NULL:productDist);

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.MapUserProduct, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    NotificationResponse responseData = gson.fromJson(response, NotificationResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        setResult(RESULT_OK);
                        Toast.makeText(CustomerAct.this, "Approved successfully", Toast.LENGTH_LONG).show();
                        finish();

                    }

                    Toast.makeText(CustomerAct.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Login Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }


    void getAllRegister(final boolean showPop){
        WebserviceController wss = new WebserviceController(CustomerAct.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "UserRoleId");
            jsonObject.put("searchvalue", "2");

            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        userNames.clear();
        userIdList.clear();

        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        strDist = "";

                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
                            userNames.add(userInfo1.getFirstName());
                            userIdList.add(userInfo1.getUserId());

                            if (!showPop && userInfo.getMappedTo()!=null && userInfo.getMappedTo().equalsIgnoreCase(userInfo1.getUserId())){
                                strDist = userInfo1.getUserId();
                                strDistName = userInfo1.getFirstName();
                                distributorText.setText(strDistName + " (" + strDist + ")");
                                distributorStr = strDist;
                            }

                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(CustomerAct.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        if (showPop){

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerAct.this);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose distributor</font>"));

                            //First Step: convert ArrayList to an Object array.
                            Object[] objNames = userNames.toArray();

                            //Second Step: convert Object array to String array

                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                            int checkedItem = 0;
                            strDist = userIdList.get(checkedItem);
                            strDistName = userNames.get(checkedItem);
                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // user checked an item
                                    strDist = userIdList.get(which);
                                    strDistName = userNames.get(which);
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
                                    distributorText.setText(strDistName + " (" + strDist + ")");
                                    distributorStr = strDist;
                                }
                            });

                            builderSingle.show();
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }

    void getProducts(final boolean showPop){

        WebserviceController wss = new WebserviceController(CustomerAct.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", ((sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")) ? sharedPreferences.getString("userid","") : sharedPreferences.getString("distId","")));

            jsonObject.put("customerId", "");

            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        productNames.clear();
        productIdList.clear();
        productPriceList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);

                    ProductsResponse responseData = gson.fromJson(response,ProductsResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        productDist = null;
                        productDistName = null;
                        productPrice = null;
                        productNames.clear();
                        productIdList.clear();
                        productPriceList.clear();

                        for (ProductDetails productDetails: responseData.getResponseData().getProductDetails()){
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                            productPriceList.add(productDetails.getProductPrice());
                        }

                        if (!showPop && !productIdList.isEmpty()){

                            for (int i=0;i<productIdList.size();i++) {
                                if (userInfo.getProductId() != null && userInfo.getProductId().equalsIgnoreCase(productIdList.get(i))){
                                    productDist = productIdList.get(i);
                                    productDistName = productNames.get(i);
                                    productPrice = productPriceList.get(i);
                                    break;
                                }

                            }
                            productText.setText(productDistName + "");
                            productEdit.setText(productPrice+"");

                        }

                        if (showPop) {

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerAct.this);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product</font>"));

                            //First Step: convert ArrayList to an Object array.
                            Object[] objNames = productNames.toArray();

                            //Second Step: convert Object array to String array

                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                            int checkedItem = 0;
                            productDist = productIdList.get(checkedItem);
                            productDistName = productNames.get(checkedItem);
                            productPrice = productPriceList.get(checkedItem);
                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // user checked an item
                                    productDist = productIdList.get(which);
                                    productDistName = productNames.get(which);
                                    productPrice = productPriceList.get(which);
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
                                    productText.setText(productDistName + "");
                                    productEdit.setText(productPrice);
                                }
                            });

                            builderSingle.show();
                        }

                    }
                    else {
                        Toast.makeText(CustomerAct.this, TextUtils.isEmpty(responseData.getResponseDescription())? "No data" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }

}

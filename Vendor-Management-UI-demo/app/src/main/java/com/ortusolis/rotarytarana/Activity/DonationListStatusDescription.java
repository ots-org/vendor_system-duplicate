package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.RegisterResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DonationListStatusDescription extends AppCompatActivity {
    Toolbar mToolbar;
    ActionBar action;
    TextView donarName,donarAddress,donarPhoneNumber,donarAmount,productName,productprice,donarQty,assignEmpLoyee;
    String donarNameText,donarAddressText,donarPhoneNumberText,donarAmountText,productNameText,productpriceText,donarQtyText,donationId,productId,userRoleId,Payment;
    ArrayList<String> finalList;
    SharedPreferences sharedPreferences;
    List<String> userNames, userIdList;
    String strDistName = null;
    String strDist = null;
    String distributorStr = null;
    Gson gson;
    Button assign,cancel,close;
    ProgressDialog progressDialog;
    LinearLayout empLayout,buttonLayout,closeButtonLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list_status_description);
        mToolbar = findViewById(R.id.toolbar);
        donarName= findViewById(R.id.donarName);
        donarAddress= findViewById(R.id.donarAddress);
        donarPhoneNumber= findViewById(R.id.donarPhoneNumber);
        donarAmount= findViewById(R.id.donarAmount);
        productName= findViewById(R.id.productName);
        productprice= findViewById(R.id.productprice);
        donarQty=findViewById(R.id.donarQty);
        assignEmpLoyee = findViewById(R.id.assignEmpLoyee);
        assign= findViewById(R.id.assign);
        cancel= findViewById(R.id.cancel);
        close= findViewById(R.id.close);
        empLayout= findViewById(R.id.empLayout);
        buttonLayout= findViewById(R.id.buttonLayout);
        closeButtonLayout= findViewById(R.id.closeButtonLayout);
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        finalList = new ArrayList<>();
        finalList = (ArrayList<String>)getIntent().getSerializableExtra("finalList");
        productNameText=finalList.get(0);
        productpriceText=finalList.get(1);
        donarNameText=finalList.get(2);
        donarAddressText=finalList.get(3);
        donarQtyText=finalList.get(4);
        donarAmountText=finalList.get(5);
        donarPhoneNumberText=finalList.get(6);
        donationId=finalList.get(7);
        productId=finalList.get(8);
        userRoleId=finalList.get(9);
        Payment=finalList.get(10);
        if(userRoleId.equals("3")){
            empLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
        }else {
            closeButtonLayout.setVisibility(View.GONE);
        }

        if(Payment.equals("Payment")){
            empLayout.setVisibility(View.GONE);
            assign.setVisibility(View.GONE);
        }

        donarName.setText(donarNameText);
        donarAddress.setText(donarAddressText);
        donarPhoneNumber.setText(donarPhoneNumberText);
        donarAmount.setText(donarAmountText);
        productName.setText(productNameText);
        productprice.setText(productpriceText);
        donarQty.setText(donarQtyText);

        assignEmpLoyee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(DonationListStatusDescription.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegister();
            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignOrder();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReciveDonation();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReciveDonation();
            }
        });

        setSupportActionBar(mToolbar);

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
            if (getIntent().hasExtra("AssignedRequest")){
                toolbarTitle.setText("Assigned Request");
                close.setText("Receive");
            }else {
                toolbarTitle.setText("Donation Status");
            }
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
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

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(DonationListStatusDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mappedTo", "1");

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

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        progressDialog.dismiss();
                        strDist = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo: distributorResponse.getResponseData().getUserDetails()){
                            if (userInfo.getUserRoleId().equalsIgnoreCase("3")) {
                                userNames.add(userInfo.getFirstName());
                                userIdList.add(userInfo.getUserId());
                            }
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(DonationListStatusDescription.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(DonationListStatusDescription.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose employee</font>"));

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
                                assignEmpLoyee.setText(strDistName);
                                distributorStr = strDist;

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
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }

    public  void assignOrder(){

        if (distributorStr!=null) {

            WebserviceController wss = new WebserviceController(DonationListStatusDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                jsonObject.put("donationId", donationId);
                jsonObject.put("donationAmount", donarAmountText);
                jsonObject.put("paymentId", "string");
                jsonObject.put("donatedQty", donarQtyText);
                jsonObject.put("donationDate", fDate);
                jsonObject.put("donationStatus", "assigneeRequest");
                jsonObject.put("donationMethod", "abc");
                jsonObject.put("assignedId", distributorStr);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("productId", productId);
                jsonObject.put("productDetails", jsonObject1);
                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.updateDonation, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            setResult(RESULT_OK);

                            finish();
                            Toast.makeText(DonationListStatusDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(DonationListStatusDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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
        else {
            Toast.makeText(this, "Select Partner", Toast.LENGTH_LONG).show();
        }
    }

    public  void ReciveDonation(){

            WebserviceController wss = new WebserviceController(DonationListStatusDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                jsonObject.put("donationId", donationId);
                jsonObject.put("donationAmount", donarAmountText);
                jsonObject.put("paymentId", "string");
                jsonObject.put("donatedQty", donarQtyText);
                jsonObject.put("donationDate", fDate);
                jsonObject.put("donationStatus", "receiveDonation");
                jsonObject.put("donationMethod", "abc");
                if(distributorStr!=null || !userRoleId.equals("3")){
                    jsonObject.put("assignedId", distributorStr);
                }
                else {
                    jsonObject.put("assignedId", sharedPreferences.getString("userid", ""));
                }
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("productId", productId);
                jsonObject.put("productDetails", jsonObject1);
                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.updateDonation, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            setResult(RESULT_OK);
                            finish();
                            Toast.makeText(DonationListStatusDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(DonationListStatusDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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


    }

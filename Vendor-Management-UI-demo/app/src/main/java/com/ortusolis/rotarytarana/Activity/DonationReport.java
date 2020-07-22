package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ortusolis.rotarytarana.pojo.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DonationReport extends AppCompatActivity {
    Button buttonDatePick,buttonDatePickEnd,getReport;
    Calendar newCalendar;
    Calendar newCalendarEnd;
    DatePickerDialog startTime,endTime;
    TextView noResult ,customerText;
    LinearLayout addLL;
    RelativeLayout customerLayoutDonation;
    Gson gson;
    String employeeStr = "";
    String strEmpName = null;
    String strEmp = null;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    List<String> empNames, empIdList;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_report);

        buttonDatePick = findViewById(R.id.buttonDatePickDonationReport);
        buttonDatePickEnd = findViewById(R.id.buttonDateEndPickDonationReport);
        getReport = findViewById(R.id.getReport);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        addLL = findViewById(R.id.addLL);
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();
        empNames = new ArrayList<>();
        empIdList = new ArrayList<>();
        customerText= findViewById(R.id.customerText);
        customerLayoutDonation= findViewById(R.id.customerLayoutDonation);
        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(DonationReport.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegisterEmployee();
            }
        });
        setSupportActionBar(mToolbar);

        gson = new Gson();

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(DonationReport.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendarEnd = Calendar.getInstance();
        endTime = new DatePickerDialog(DonationReport.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendarEnd.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendarEnd.getTime());

                buttonDatePickEnd.setText(dateStr);
            }
        }, newCalendarEnd.get(Calendar.YEAR), newCalendarEnd.get(Calendar.MONTH), newCalendarEnd.get(Calendar.DAY_OF_MONTH));

        buttonDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime.show();
            }
        });

        buttonDatePickEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime.show();
            }
        });

        getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonationReport();
            }
        });

        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            customerLayoutDonation.setVisibility(View.VISIBLE);
        }

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
            toolbarTitle.setText("Donation Report");
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

    public void DonationReport(){
        addLL.removeAllViews();
        WebserviceController wss = new WebserviceController(DonationReport.this);

        JSONObject requestObject = new JSONObject();

        String usserId = "";
        String status = "";

        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            status="all";
        }
        else if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            status="all";
        }
        else {
            usserId = sharedPreferences.getString("userid","");
            status=usserId;
        }


        JSONObject jsonObject = new JSONObject();
        try {
            if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                if (!employeeStr.isEmpty()){
                    jsonObject.put("userId", employeeStr);
                    jsonObject.put("status", "donorId");
                }else {
                    jsonObject.put("userId", "1");
                    jsonObject.put("status", status);
                }
            }else {
                jsonObject.put("userId", sharedPreferences.getString("userid",""));
                jsonObject.put("status", "donorId");
            }
            jsonObject.put("startDate", buttonDatePick.getText().toString());
            jsonObject.put("endDate", buttonDatePickEnd.getText().toString());
            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getDonationReportByDate, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("DonatnList response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray donationList = responseData.getJSONArray("donationList");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {

                        if (responseData!=null &&donationList!=null) {
                            noResult.setVisibility(View.GONE);
                            loadData(responseData);
                        }
                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        addLL.removeAllViews();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    addLL.removeAllViews();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                addLL.removeAllViews();
            }
        });
    }



    public void loadData(JSONObject responseData){
        addLL.removeAllViews();

        View view = getLayoutInflater().inflate(R.layout.view_donation_transaction,null);
        addLL.addView(view);

        String donationAmount,donatedQty,customerName,address1,productName,productPrice;
        try {
            JSONArray donationList = responseData.getJSONArray("donationList");
            for (int donationListob=0;donationListob<donationList.length();donationListob++){

                JSONObject donationListsobject = donationList.getJSONObject(donationListob);
                donationAmount=donationListsobject.getString("donationAmount");
                donatedQty= donationListsobject.getString("donatedQty");
                JSONObject productDetails =donationListsobject.getJSONObject("productDetails");
                productName=productDetails.getString("productName");
                productPrice=productDetails.getString("productPrice");
                JSONObject userDetails =donationListsobject.getJSONObject("userDetails");
                customerName=userDetails.getString("firstName")+" "+userDetails.getString("lastName");
                address1=userDetails.getString("address1");

                View viewDat = getLayoutInflater().inflate(R.layout.view_donation_transaction,null);

                TextView donationProductName = (TextView) viewDat.findViewById(R.id.donationProductName);
                TextView donationProductPrice = (TextView) viewDat.findViewById(R.id.donationProductPrice);
                TextView donationAmountProduct = (TextView) viewDat.findViewById(R.id.donationAmount);
                TextView donatedQtyProduct = (TextView) viewDat.findViewById(R.id.donatedQty);
                TextView donationFirstName = (TextView) viewDat.findViewById(R.id.donationFirstName);
                TextView donationAddress1 = (TextView) viewDat.findViewById(R.id.donationAddress1);
                TextView donationStatus = (TextView) viewDat.findViewById(R.id.donationStatus);
                TextView paymentMethod = (TextView) viewDat.findViewById(R.id.paymentMethod);


                donationProductName.setText(productName);
                donationProductPrice.setText(productPrice);
                donationAmountProduct.setText(donationAmount);
                donatedQtyProduct.setText(donatedQty);
                donationFirstName.setText(customerName);
                donationAddress1.setText(address1);
                donationStatus.setText(donationListsobject.getString("donationStatus"));
                paymentMethod.setText(donationListsobject.getString("donationMethod"));

                addLL.addView(viewDat);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getAllRegisterEmployee(){
        WebserviceController wss = new WebserviceController(DonationReport.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("mappedTo", "1");

            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        empNames.clear();
        empIdList.clear();

        wss.postLoginVolley(Constants.getUserDetailsByMapped, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = new Gson().fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        //
                        progressDialog.dismiss();
                        //

                        strEmp = "";

                        empNames.clear();
                        empIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
//                            if (userInfo1.getUserRoleId().equalsIgnoreCase("4")) {
                                empNames.add(userInfo1.getFirstName());
                                empIdList.add(userInfo1.getUserId());
//                            }
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(DonationReport.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(DonationReport.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Donor</font>"));

                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = empNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        strEmp = empIdList.get(checkedItem);
                        strEmpName = empNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                strEmp = empIdList.get(which);
                                strEmpName = empNames.get(which);
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
                                employeeStr = strEmp;
                                //
                                customerText.setText(strEmpName);
                                //
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
//                Toast.makeText(CustomerLedgerReportActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }
}
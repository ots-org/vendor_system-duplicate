package com.ortusolis.subhaksha.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.pojo.DistributorResponse;
import com.ortusolis.subhaksha.pojo.OutstandingResponse;
import com.ortusolis.subhaksha.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerOutstandingReportActivity extends AppCompatActivity {

    LinearLayout addLL;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    CheckBox checkBoxReport;
    LinearLayout distributorCodeLayout;
    String distributorStr = "";
    TextView distributorText;
    ProgressDialog progressDialog;
    String strDistName = null;
    Button getReport;
    String strDist = null;
    List<String> userNames, userIdList;
    String pdf="no";

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_outstanding_report);
        addLL = findViewById(R.id.addLL);
        getReport = findViewById(R.id.getReport);
        checkBoxReport = (CheckBox)findViewById(R.id.checkboxOutstandingReport);
        checkBoxReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    if (((CheckBox) v).isChecked()) {
                        pdf="yes";
                    }
                }

            }
        });
        getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getListOfOrderByDate();
            }
        });
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        distributorCodeLayout = findViewById(R.id.distributorCodeLayout);
        distributorText = findViewById(R.id.distributorText);
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();

        setSupportActionBar(mToolbar);

        gson = new Gson();

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

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
            toolbarTitle.setText("Customer Outstanding Report ");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            distributorCodeLayout.setVisibility(View.GONE);
        }
        else {
            distributorCodeLayout.setVisibility(View.GONE);
        }

        distributorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CustomerOutstandingReportActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegister();
            }
        });
    }

    void getListOfOrderByDate(){

        WebserviceController wss = new WebserviceController(CustomerOutstandingReportActivity.this);

        String usserId = "";
        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            usserId = sharedPreferences.getString("userid","");
        }
        else if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            usserId = sharedPreferences.getString("userid","");
        }
        else {
            usserId = sharedPreferences.getString("distId","");
        }

        if (usserId.isEmpty()){
            Toast.makeText(this, "Select Facilitator", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("distributorId", usserId);
            jsonObject.put("pdf", pdf);

        } catch (Exception e) {
            e.printStackTrace();
        }


        wss.postLoginVolley(Constants.getCustomerOutstandingData, jsonObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    OutstandingResponse responseData = gson.fromJson(response, OutstandingResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getCustomerOutstandingList()!=null) {
                            loadData(responseData.getResponseData().getCustomerOutstandingList());
                            if(pdf.equals("yes")){
                                String txt=responseData.getResponseData().getPdf();
                                final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "Customer outstanding Report" + ".pdf");
                                byte[] pdfAsBytes = Base64.decode(txt, 0);
                                FileOutputStream os;
                                try {
                                    os = new FileOutputStream(dwldsPath, false);
                                    os.write(pdfAsBytes);
                                    os.flush();
                                    os.close();
                                    Toast.makeText(getApplicationContext(), "Customer Outstanding Report Pdf Available", Toast.LENGTH_LONG).show();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        Toast.makeText(CustomerOutstandingReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
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

    void loadData(List<OutstandingResponse.RequestS> requestS){
        addLL.removeAllViews();
        if (!requestS.isEmpty()) {
            addRow(null, 0);
            int i = 0;
            for (OutstandingResponse.RequestS requestSdata : requestS) {
                i = i + 1;
                addRow(requestSdata, i);

            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(OutstandingResponse.RequestS requestSdata, int pos){

        if (requestSdata!=null){

            String customerStr = "";

            for (OutstandingResponse.RequestS.ProductBalanceCan productBalanceCan : requestSdata.getBalanceCan()){

                View view = getLayoutInflater().inflate(R.layout.view_customer_outstanding,null);

                TextView customerName = view.findViewById(R.id.customerName);
                TextView productName = view.findViewById(R.id.productName);
                TextView oustandingAmount = view.findViewById(R.id.oustandingAmount);
                TextView outstandingCan = view.findViewById(R.id.outstandingCan);

                productName.setText(productBalanceCan.getProductName());
                outstandingCan.setText(productBalanceCan.getBalanceCan());
                outstandingCan.setGravity(Gravity.RIGHT);
                if (requestSdata.getCustomerId()==null || requestSdata.getCustomerId().equalsIgnoreCase(customerStr)) {
                    customerName.setText("");
                    oustandingAmount.setText("");
                } else {
                    customerStr = requestSdata.getCustomerId();
                    customerName.setText(requestSdata.getCustomerName());
                    oustandingAmount.setText(requestSdata.getOutstandingAmount());
                    oustandingAmount.setGravity(Gravity.RIGHT);
                }
                addLL.addView(view);
            }

        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_customer_outstanding,null);
            addLL.addView(view);
        }

    }

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(CustomerOutstandingReportActivity.this);

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

                        progressDialog.dismiss();
                        strDist = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
                            userNames.add(userInfo1.getFirstName());
                            userIdList.add(userInfo1.getUserId());
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(CustomerOutstandingReportActivity.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerOutstandingReportActivity.this);
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
                                distributorText.setText(strDistName );
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

}

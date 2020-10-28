package com.ortusolis.rotarytarana.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.rotarytarana.Interfaces.IClickInterfaceAssigned;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.Utility.VenderConstants;
import com.ortusolis.rotarytarana.adapter.AssignedOrderReportAdapter;
import com.ortusolis.rotarytarana.pojo.AssignedOrderModel;
import com.ortusolis.rotarytarana.pojo.AssignedResponse;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AssignedOrderListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    AssignedOrderReportAdapter assignedOrderReportAdapter;
    List<AssignedOrderModel> data;
    TextView noResult;
    List<String> empNames, empIdList;
    String strEmp = null;
    String strEmpName = null;
    String employeeStr = "";
    String assignRequest="no";
    TextView employeeText;
    ProgressDialog progressDialog;
    LinearLayout employeeCodeLayout;
    Spinner spinnerStatus;
    boolean empClick = false;
    String orderId="";
    boolean loadedOnce = false;
    String[] productName;
    String[] productQuantity;
    String[] DonarName;
    String[] DonarcontactNumber;
    String[] Donaraddress;
    String[] Donaramount;
    String[] Donardate;
    String[] DonarQty;
    ArrayList<String> productNameList;
    ArrayList<String> productPriceList;
    ArrayList<String> productIdList;
    ArrayList<String> donarNameList;
    ArrayList<String> donarAddressList;
    ArrayList<String> donarQtyList;
    ArrayList<String> donarAmountList;
    ArrayList<String> donarContactNumnerList;
    ArrayList<String> donarDonationIdList;
    ArrayList<String> finalList;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        sharedPreferences = getSharedPreferences("water_management",0);
        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        noResult = findViewById(R.id.noResult);
        employeeText = findViewById(R.id.employeeText);
        employeeCodeLayout = findViewById(R.id.employeeCodeLayout);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        data = new ArrayList<>();
        empNames = new ArrayList<>();
        empIdList = new ArrayList<>();
        gson = new Gson();
        productNameList = new ArrayList<>();
        productPriceList = new ArrayList<>();
        productIdList = new ArrayList<>();
        donarNameList = new ArrayList<>();
        donarAddressList = new ArrayList<>();
        donarQtyList = new ArrayList<>();
        donarAmountList = new ArrayList<>();
        donarContactNumnerList = new ArrayList<>();
        donarDonationIdList= new ArrayList<>();
        finalList= new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(AssignedOrderListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        setSupportActionBar(mToolbar);

        if (getIntent().hasExtra("orderId")){
            orderId = getIntent().getExtras().getString("orderId");
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
            if (getIntent().hasExtra("assignRequest")){
                toolbarTitle.setText("Assigned Request");
                assignRequest="yes";
                spinnerStatus.setVisibility(View.GONE);
            }else if(getIntent().hasExtra("deliverDonation")){
                assignRequest="deliverDonation";
                toolbarTitle.setText("Deliver Donation");
                spinnerStatus.setVisibility(View.GONE);
            }
            else {
                toolbarTitle.setText("Sales Voucher");
                spinnerStatus.setVisibility(View.GONE);
            }
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        noResult.setText("No Orders");

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            employeeCodeLayout.setVisibility(View.VISIBLE);
        }
        else {
            employeeCodeLayout.setVisibility(View.GONE);
            employeeStr = sharedPreferences.getString("userid", "");
        }

        if (getIntent().hasExtra("orderId")){
            employeeCodeLayout.setVisibility(View.GONE);
            employeeStr = sharedPreferences.getString("userid", "");
        }

        employeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(AssignedOrderListActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegisterEmployee();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loadedOnce) {
            if (!(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId", "").equalsIgnoreCase("2"))) {
                getAssginedOrder(employeeStr);
            }
            else if (getIntent().hasExtra("orderId")){
                getAssginedOrder(employeeStr);
            }
        }
        else if (getIntent().hasExtra("orderId") && loadedOnce){
            Intent intent = new Intent(AssignedOrderListActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==3533 && resultCode == RESULT_OK){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    getAssginedOrder(employeeStr);
                }
            },2000);
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

    void getAssginedOrder(String userId){
        progressDialog = new ProgressDialog(AssignedOrderListActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(AssignedOrderListActivity.this);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            if(assignRequest.equals("yes")){
                jsonObject.put("status", "assigneeRequest");
            }else if(assignRequest.equals("deliverDonation")){
                jsonObject.put("status", "AssignedRequestToEmployee");
//                jsonObject.put("status", "Assigned");
            }
            else {
                jsonObject.put("status", "Assigned");
            }
            jsonObject.put("employeeId", userId);
            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getAssginedOrder, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response", response);

                    AssignedResponse responseData = gson.fromJson(response, AssignedResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        data = responseData.getResponseData().getOrderList();
                        assignedOrderReportAdapter = new AssignedOrderReportAdapter(AssignedOrderListActivity.this, data, new IClickInterfaceAssigned() {
                            @Override
                            public void click(AssignedOrderModel item) {
                                callNewIntent(item);
                            }
                        });
                        recyclerView.setAdapter(assignedOrderReportAdapter);

                        assignedOrderReportAdapter.notifyDataSetChanged();

                        if (responseData.getResponseData().getOrderList().isEmpty()){
                            noResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            noResult.setVisibility(View.GONE);
                        }

                        for (int i=0;i<data.size();i++){
                            if (data.get(i).getOrderId().equalsIgnoreCase(orderId)){
                                loadedOnce = true;
                                Intent intent = new Intent(AssignedOrderListActivity.this, AssignedVoucherOrderDescription.class);
                                intent.putExtra("order",data.get(i));
                                startActivityForResult(intent,3533);
                                return;
                            }
                        }
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if (assignedOrderReportAdapter!=null)
                            assignedOrderReportAdapter.clearAll();
                             noResult.setVisibility(View.VISIBLE);
                         }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressDialog.dismiss();
                if (assignedOrderReportAdapter!=null)
                    assignedOrderReportAdapter.clearAll();
                noResult.setVisibility(View.VISIBLE);
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }

    private void callNewIntent(AssignedOrderModel item) {
        try {
            Intent intent = new Intent(AssignedOrderListActivity.this, AssignedVoucherOrderDescription.class);
            VenderConstants.assignedOrderModel = item;
            //intent.putExtra("order", item);
            Log.e("Intent item", item.getAddressToBePlaced());
            Log.e("Intent", "intent start");
            if (getIntent().hasExtra("deliverDonation")) {
                Log.e("Intent", "deliver donaotion");
                intent.putExtra("deliverDonation", "deliverDonation");
            } else if (getIntent().hasExtra("assignRequest")) {
                Log.e("c", "assign request");
                intent.putExtra("AssignedRequest", "AssignedRequest");
            }
            Log.e("Intent", "lunch");
            startActivityForResult(intent,3533);
            Log.e("Intent", "close");
//             startActivity(intent);
        } catch (Exception e){
            Log.e("Intent", e.getMessage());
            e.printStackTrace();
        }
    }

    void getAllRegisterEmployee(){
        if (!empClick) {
            empClick = true;
            WebserviceController wss = new WebserviceController(AssignedOrderListActivity.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
                requestObject.put("requestData", jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            empNames.clear();
            empIdList.clear();

            wss.postLoginVolley(Constants.getUserDetailsByMapped, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    empClick = false;
                    try {
                        Log.e("getPlants response", response);

                        DistributorResponse distributorResponse = new Gson().fromJson(response, DistributorResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                            progressDialog.dismiss();
                            strEmp = "";
                            empNames.clear();
                            empIdList.clear();
                            for (UserInfo userInfo1 : distributorResponse.getResponseData().getUserDetails()) {
                                if (userInfo1.getUserRoleId().equalsIgnoreCase("3")) {
                                    empNames.add(userInfo1.getFirstName());
                                    empIdList.add(userInfo1.getUserId());
                                }
                            }

                            if (distributorResponse.getResponseData().getUserDetails().isEmpty()) {
                                Toast.makeText(AssignedOrderListActivity.this, "No Results", Toast.LENGTH_LONG).show();
                            }

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(AssignedOrderListActivity.this);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Select Partner</font>"));

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
                                    employeeText.setText(strEmpName );
                                    getAssginedOrder(employeeStr);
                                }
                            });
                            builderSingle.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    empClick = false;
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                }
            });
        }
    }
}

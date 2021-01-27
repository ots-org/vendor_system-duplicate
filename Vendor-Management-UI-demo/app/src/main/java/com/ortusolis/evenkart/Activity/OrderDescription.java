package com.ortusolis.evenkart.Activity;

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
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.evenkart.NetworkUtility.Constants;
import com.ortusolis.evenkart.NetworkUtility.IResult;
import com.ortusolis.evenkart.NetworkUtility.WebserviceController;
import com.ortusolis.evenkart.R;
import com.ortusolis.evenkart.Utility.CommonFunctions;
import com.ortusolis.evenkart.adapter.AssignedOrderGridAdapter;
import com.ortusolis.evenkart.pojo.DistributorResponse;
import com.ortusolis.evenkart.pojo.OrderResponse;
import com.ortusolis.evenkart.pojo.RegisterResponse;
import com.ortusolis.evenkart.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderDescription extends AppCompatActivity {

    TextView textName,orderDate,deliveryDate,orderCost,orderStatus,customerName,assignEmpLoyee,customerPhoneNumber,customerAddress,customerAddressSecondary,donarName,donarAddress,donarPhoneNumber,donarPaymentMethod,orderNumberText,orderDateText,orderCostText,orderStatusText;
    ProgressDialog progressDialog;
    Button assignButton,cancelOrder,closeOrder;
    Toolbar mToolbar;
    ActionBar action;
    int price = 0;
    OrderResponse.RequestS orderDetails;
    SharedPreferences sharedPreferences;
    Gson gson;
    String distributorStr = null;
    List<String> userNames, userIdList;
    String strDistName = null;
    String strDist = null;
    GridView gridview;
    TextView noResult;
    LinearLayout donarLayout;
    AssignedOrderGridAdapter assignedOrderGridAdapter;
    String assignRequest="no";
    String orderIdSalesVocher,orderDateSalesVocher,deliveryDateSalesVocher,orderCostSalesVocher,orderStatusSalesVocher,customerNameSalesVocher,customerIdSalesVocher;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        assignButton = findViewById(R.id.placeOrder);
        gson = new Gson();
        closeOrder=findViewById(R.id.closeOrderAssignOrder);
        mToolbar = findViewById(R.id.toolbar);
        textName = findViewById(R.id.productName);
        orderDate = findViewById(R.id.orderDate);
        deliveryDate = findViewById(R.id.deliveryDate);
        orderCost = findViewById(R.id.orderCost);
        orderStatus = findViewById(R.id.orderStatus);
        customerName = findViewById(R.id.customerName);
        customerPhoneNumber = findViewById(R.id.customerPhoneNmuber);
        customerAddress= findViewById(R.id.customerAddress);
        customerAddressSecondary= findViewById(R.id.customerAddressSecondary);
        assignEmpLoyee = findViewById(R.id.assignEmpLoyee);
        cancelOrder = findViewById(R.id.cancelOrder);
        gridview = findViewById(R.id.gridview);
        noResult = findViewById(R.id.noResult);
        donarName = findViewById(R.id.donarName);
        donarAddress = findViewById(R.id.donarAddress);
        donarPhoneNumber= findViewById(R.id.donarPhoneNumber);
        donarPaymentMethod= findViewById(R.id.donarPaymentMethod);
        donarLayout= findViewById(R.id.donarLayout);
        orderNumberText= findViewById(R.id.orderNumberText);
        orderDateText= findViewById(R.id.orderDateText);
        orderCostText= findViewById(R.id.orderCostText);
        orderStatusText= findViewById(R.id.orderStatusText);
        sharedPreferences = getSharedPreferences("water_management",0);
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

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
            if (getIntent().hasExtra("assignRequest")){
                toolbarTitle.setText("Assign Request");
                assignRequest="yes";
            }else {
                toolbarTitle.setText("Assign Orders");
            }
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (getIntent().hasExtra("order")){
            orderDetails = getIntent().getExtras().getParcelable("order");
        }
        if (getIntent().hasExtra("assignRequestEmp")){
            assignButton.setVisibility(View.GONE);
            cancelOrder.setVisibility(View.GONE);
        }

        setValues();
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignOrder(orderDetails);
            }
        });
        closeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("assignRequest")){
                    CloseRequest(orderDetails);
                }else {
                    closeOrder(orderDetails);
                }

            }
        });
    }

    void setValues(){
        orderIdSalesVocher=orderDetails.getOrderId();
        orderDateSalesVocher=CommonFunctions.converDateStr(orderDetails.getOrderDate());
        deliveryDateSalesVocher=CommonFunctions.converDateStr(orderDetails.getDelivaryDate());
        orderCostSalesVocher=orderDetails.getOrderCost();
        orderStatusSalesVocher=orderDetails.getOrderStatus();
        customerNameSalesVocher=orderDetails.getCustomerDetails().getFirstName();
        customerIdSalesVocher =orderDetails.getCustomerId();
        textName.setText(orderDetails.getOrderId());
        orderDate.setText(CommonFunctions.converDateStr(orderDetails.getOrderDate()));
        deliveryDate.setText(CommonFunctions.converDateStr(orderDetails.getDelivaryDate()));
        orderCost.setText(orderDetails.getOrderCost()+getString(R.string.Rs));
        orderStatus.setText(orderDetails.getOrderStatus());
        customerName.setText(orderDetails.getCustomerDetails().getFirstName());
        customerPhoneNumber.setText(orderDetails.getCustomerDetails().getContactNo());
        customerAddress.setText(orderDetails.getAddressToBePlaced());//getAddressToBePlaced());
        customerAddressSecondary.setText(orderDetails.getCustomerDetails().getAddress2());
        if (getIntent().hasExtra("assignRequest") && !(orderDetails.getDonationStatus() ==null)){
            orderNumberText.setText("Request Number");
            orderDateText.setText("Request Date");
            orderCostText.setText("Request Cost");
            orderStatusText.setText("Request Status");
            donarName.setText(orderDetails.getDonarDetails().getFirstName());
            donarAddress.setText(orderDetails.getDonarDetails().getAddress1());
            donarPhoneNumber.setText(orderDetails.getDonarDetails().getContactNo());
            donarPaymentMethod.setText(orderDetails.getDonationStatus());
        }else if(getIntent().hasExtra("assignRequest")){
            orderNumberText.setText("Request Number");
            orderDateText.setText("Request Date");
            orderCostText.setText("Request Cost");
            orderStatusText.setText("Request Status");
            donarLayout.setVisibility(View.GONE);
        }else {
            donarLayout.setVisibility(View.GONE);
        }

        assignEmpLoyee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(OrderDescription.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegister();
            }
        });

        assignedOrderGridAdapter = new AssignedOrderGridAdapter(OrderDescription.this,orderDetails.getOrderdProducts());
        gridview.setAdapter(assignedOrderGridAdapter);

        assignedOrderGridAdapter.notifyDataSetChanged();

        if (!orderDetails.getOrderStatus().equalsIgnoreCase("closed")){
            cancelOrder.setVisibility(View.VISIBLE);
            cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelOrder(orderDetails);
                }
            });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void assignOrder(OrderResponse.RequestS data){

        if (distributorStr!=null) {

            WebserviceController wss = new WebserviceController(OrderDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", data.getOrderId());
                jsonObject.put("assignedId", distributorStr);
                if(assignRequest.equals("yes")){
                    jsonObject.put("orderStatus", "AssignedRequestToEmployee");
                }else {
                    jsonObject.put("orderStatus", "Assigned");
                }

                jsonObject.put("deliveryDate", CommonFunctions.converDateStr(data.getDelivaryDate()));

                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.updateOrderAssgin, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            setResult(RESULT_OK);
                            Intent assignRequestIntent = new Intent(OrderDescription.this, OrderListActivity.class);
                            if(assignRequest.equals("yes")){
                                assignRequestIntent.putExtra("assignRequest", "assignRequest");
                            }
                            startActivity(assignRequestIntent);
                            finish();
                            Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Select Employee", Toast.LENGTH_LONG).show();
        }
    }

    void closeOrder(OrderResponse.RequestS data){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String CurrentDate = df.format(c);
        WebserviceController wss = new WebserviceController(OrderDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", orderIdSalesVocher);
            jsonObject.put("customerId", customerIdSalesVocher);
            jsonObject.put("outstandingAmount", "0");
            jsonObject.put("deliverdDate", CurrentDate);
            jsonObject.put("orderCost", orderCostSalesVocher);
            jsonObject.put("amountReceived", orderCostSalesVocher);
            jsonObject.put("amountReceived", orderCostSalesVocher);
            jsonObject.put("distributorId", sharedPreferences.getString("userid", ""));
            JSONArray jsonArray = new JSONArray();

            for (OrderResponse.RequestS.ProductOrder  productOrder: orderDetails.getOrderdProducts()){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("emptyCan","0");
                jsonObject1.put("orderQty",productOrder.getOtsOrderedQty());
                jsonObject1.put("prodcutId",productOrder.getOtsProductId());
                jsonObject1.put("productbalanceQty","0");
                jsonObject1.put("productCost",productOrder.getOtsOrderProductCost());
                jsonArray.put(jsonObject1);
            }

            jsonObject.put("orderProductlist", jsonArray);
            requestObject.put("request", jsonObject);
            Log.e("jsonObject",requestObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

            wss.postLoginVolley(Constants.saleVoucher, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderDescription.this);
                        alertDialogBuilder.setMessage("success");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(OrderDescription.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                    }
                                });
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            setResult(RESULT_OK);
                            Intent assignRequest = new Intent(OrderDescription.this, OrderListActivity.class);
                            if(assignRequest.equals("yes")){
                                assignRequest.putExtra("assignRequest", "assignRequest");
                            }
                            startActivity(assignRequest);
                            finish();
                            Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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
    void cancelOrder(OrderResponse.RequestS data){

        WebserviceController wss = new WebserviceController(OrderDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", data.getOrderId());
            if(assignRequest.equals("yes")){
                jsonObject.put("status", "cancelRequest");
            }else {
                jsonObject.put("status", "cancel");
            }
            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.updateOrderStatus, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response", response);

                    RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        setResult(RESULT_OK);
                        Intent assignRequestIntent = new Intent(OrderDescription.this, OrderListActivity.class);
                        if(assignRequest.equals("yes")){
                            assignRequestIntent.putExtra("assignRequest", "assignRequest");
                        }
                        startActivity(assignRequestIntent);
                        finish();
                        Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(OrderDescription.this);

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
                            Toast.makeText(OrderDescription.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(OrderDescription.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Employee</font>"));

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
    public void CloseRequest(OrderResponse.RequestS data){

            WebserviceController wss = new WebserviceController(OrderDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", data.getOrderId());
                jsonObject.put("status", "DoneDonation");
                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.updateOrderStatus, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            setResult(RESULT_OK);
                            finish();
                            Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(OrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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

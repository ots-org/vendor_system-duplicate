package com.ortusolis.subhaksha.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.Utility.CommonFunctions;
import com.ortusolis.subhaksha.adapter.AssignedOrderGridAdapter;
import com.ortusolis.subhaksha.pojo.AssignedOrderModel;
import com.ortusolis.subhaksha.pojo.DistributorResponse;
import com.ortusolis.subhaksha.pojo.OrderResponse;
import com.ortusolis.subhaksha.pojo.RegisterResponse;
import com.ortusolis.subhaksha.pojo.UserInfo;
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

    TextView textName,orderDate,deliveryDate,orderCost,orderStatus,customerName,assignEmpLoyee,customerPhoneNumber,customerAddress,customerAddressSecondary;
    //
    ProgressDialog progressDialog;
    Button  createSaleVoucherAssignOrder;
    //
    Button assignButton,cancelOrder;

    Toolbar mToolbar;
    ActionBar action;

    int price = 0;
    OrderResponse.RequestS orderDetails;
    SharedPreferences sharedPreferences;
    Gson gson;
    String distributorStr = null;
    List<String> userNames, userIdList;
//  List<OrderResponse.RequestS.ProductOrder> data;
    AssignedOrderModel assignedOrderModel;
    String strDistName = null;
    String strDist = null;
    GridView gridview;
    TextView noResult;
    AssignedOrderGridAdapter assignedOrderGridAdapter;

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
        //
        createSaleVoucherAssignOrder=findViewById(R.id.createSaleVoucherAssignOrder);
        //
        gson = new Gson()/*new GsonBuilder().serializeNulls().create()*/;

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

            //this.action.setTitle((CharSequence) "Update Stock");

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Close Orders");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (getIntent().hasExtra("order")){
            orderDetails = getIntent().getExtras().getParcelable("order");
//            assignedOrderModel = getIntent().getExtras().getParcelable("order");
//            data = assignedOrderModel.getOrderdProducts();
        }

        setValues();

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignOrder(orderDetails);
            }
        });
        createSaleVoucherAssignOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSaleVoucherAssignOrder();
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

         orderCost.setText(getString(R.string.Rs)+orderDetails.getOrderCost());
         orderStatus.setText(orderDetails.getOrderStatus());
         customerName.setText(orderDetails.getCustomerDetails().getFirstName());
         //
         customerPhoneNumber.setText(orderDetails.getCustomerDetails().getContactNo());
         customerAddress.setText(orderDetails.getCustomerDetails().getAddress1());
         customerAddressSecondary.setText(orderDetails.getCustomerDetails().getAddress2());
         //
         assignEmpLoyee.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //code
                 progressDialog = new ProgressDialog(OrderDescription.this);
                 progressDialog.setMessage("Loading...");
                 progressDialog.setCancelable(false);
                 progressDialog.show();
                 //
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
                jsonObject.put("orderStatus", "Assigned");
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
                    Toast.makeText(OrderDescription.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                }
            });

        }
        else {
            Toast.makeText(this, "Select Employee", Toast.LENGTH_LONG).show();
        }
    }

    void cancelOrder(OrderResponse.RequestS data){

            WebserviceController wss = new WebserviceController(OrderDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", data.getOrderId());
                jsonObject.put("status", "cancel");

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
                    Toast.makeText(OrderDescription.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                }
            });
    }

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(OrderDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));

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
                        //
                        progressDialog.dismiss();
                        //

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
                                //
                                assignEmpLoyee.setText(strDistName);
                                //
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
                Toast.makeText(OrderDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }

    public  void  createSaleVoucherAssignOrder(){
        //current date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String CurrentDate = df.format(c);
        //current date
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
            JSONArray jsonArray = new JSONArray();

//            for (OrderResponse.RequestS.ProductOrder  productOrder: orderDetails){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("deliveredQty","0");
                jsonObject1.put("emptyCan","0");
                jsonObject1.put("orderQty","0");
                jsonObject1.put("prodcutId","0");
                jsonObject1.put("productbalanceQty","");
                jsonObject1.put("productCost","");
                jsonArray.put(jsonObject1);
//            }

            jsonObject.put("orderProductlist", jsonArray);

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.saleVoucher, requestObject.toString(), new IResult() {
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
                Toast.makeText(OrderDescription.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.ortusolis.rotarytarana.Activity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.Utility.CommonFunctions;
import com.ortusolis.rotarytarana.adapter.AssignedOrderGridAdapter;
import com.ortusolis.rotarytarana.pojo.AssignedOrderModel;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.OrderResponse;
import com.ortusolis.rotarytarana.pojo.ProductsStock;
import com.ortusolis.rotarytarana.pojo.RegisterResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AssignedVoucherOrderDescription extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    GridView gridview;
    TextView noResult;
    AssignedOrderGridAdapter assignedOrderGridAdapter;
    List<OrderResponse.RequestS.ProductOrder> data;
    AssignedOrderModel assignedOrderModel;
    TextView orderNo;
    TextView customeName;
    TextView customerPhNumber;
    TextView customerAdd1;
    TextView customerAdd2;
    TextView orderDate;
    TextView orderCost;
    TextView deliveryDate;
    TextView deliveredBy;
    TextView outstandingAmt;
    TextView assignEmpLoyee;
    LinearLayout customerLay;
    Button createSaleVoucher;
    List<String> userNames, userIdList;
    String distributorStr = null;
    String strDistName = null;
    String strDist = null;
    LinearLayout employeeSelectionLL;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_details);

        sharedPreferences = getSharedPreferences("water_management",0);
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        mToolbar = findViewById(R.id.toolbar);
        gridview = findViewById(R.id.gridview);
        noResult = findViewById(R.id.noResult);
        orderNo = (TextView) findViewById(R.id.orderNo);
        customeName = (TextView) findViewById(R.id.customerName);
        customerPhNumber= (TextView) findViewById(R.id.customerPhNumber);
        customerAdd1= (TextView) findViewById(R.id.customerAdd1);
        customerAdd2= (TextView) findViewById(R.id.customerAdd2);
        orderDate = (TextView) findViewById(R.id.orderDate);
        orderCost = (TextView) findViewById(R.id.orderCost);
        deliveryDate = (TextView) findViewById(R.id.deliveryDate);
        deliveredBy = (TextView) findViewById(R.id.deliveredBy);
        outstandingAmt = (TextView) findViewById(R.id.outstandingAmt);
        createSaleVoucher = (Button) findViewById(R.id.createSaleVoucher);
        customerLay = (LinearLayout) findViewById(R.id.customer3);
        assignEmpLoyee = findViewById(R.id.assignEmpLoyee);
        employeeSelectionLL = findViewById(R.id.employeeSelectionLL);

        if (getIntent().hasExtra("order")){
            assignedOrderModel = getIntent().getParcelableExtra("order");
            data = assignedOrderModel.getOrderdProducts();
        }


        setSupportActionBar(mToolbar);

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            employeeSelectionLL.setVisibility(View.VISIBLE);
        }
        else {
            employeeSelectionLL.setVisibility(View.GONE);
        }

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
            customerPhNumber.setVisibility(View.VISIBLE);
            customerAdd1.setVisibility(View.VISIBLE);
            customerAdd2.setVisibility(View.VISIBLE);
        }
        else {
            customerPhNumber.setVisibility(View.GONE);
            customerAdd1.setVisibility(View.GONE);
            customerAdd2.setVisibility(View.GONE);
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
            if (getIntent().hasExtra("AssignedRequest")){
                toolbarTitle.setText("Assigned Request");
                createSaleVoucher.setText("Receive");
            }else if(getIntent().hasExtra("deliverDonation")){
                toolbarTitle.setText("Deliver Donation");
                createSaleVoucher.setText("Close Donation");
            }
            else {
                toolbarTitle.setText("Sales Voucher");
            }

            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        orderNo.setText(Html.fromHtml("<b>Order Number: </b>"+assignedOrderModel.getOrderId()));
        customeName.setText(Html.fromHtml("<b>Ordered Date: </b>"+CommonFunctions.converDateStr(assignedOrderModel.getOrderDate())));
        orderDate.setText(Html.fromHtml("<b>Customer Name: </b>"+assignedOrderModel.getCustomerDetails().getFirstName()));
        deliveryDate.setText(Html.fromHtml("<b>Delivery Date: </b>"+ CommonFunctions.converDateStr(assignedOrderModel.getDelivaryDate())));
        deliveredBy.setText(Html.fromHtml("<b>Distributor Name: </b>"+assignedOrderModel.getDistributorDetails().getFirstName()));
        customerPhNumber.setText(Html.fromHtml("<b>Phone Number: </b>"+assignedOrderModel.getCustomerDetails().getContactNo()));
        customerAdd1.setText(Html.fromHtml("<b>Primary Address: </b>"+assignedOrderModel.getCustomerDetails().getAddress1()));
        customerAdd2.setText(Html.fromHtml("<b>Secondary Address: </b>"+assignedOrderModel.getCustomerDetails().getAddress2()));
        assignedOrderGridAdapter = new AssignedOrderGridAdapter(AssignedVoucherOrderDescription.this,data);
        gridview.setAdapter(assignedOrderGridAdapter);

        assignedOrderGridAdapter.notifyDataSetChanged();

        if (assignedOrderModel.getEmployeeDetails() != null && assignedOrderModel.getEmployeeDetails().getFirstName()!=null && assignedOrderModel.getEmployeeDetails().getLastName()!=null ){
            assignEmpLoyee.setText(Html.fromHtml("<b>Assigned Partner: </b>"+assignedOrderModel.getEmployeeDetails().getFirstName()+" "+assignedOrderModel.getEmployeeDetails().getLastName()));
        }

        orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+assignedOrderModel.getOrderCost()));
        customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createSaleVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("AssignedRequest") || getIntent().hasExtra("deliverDonation")){
                    CloseRequest();
                }else {
                    final Calendar newCalendar = Calendar.getInstance();
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = format.format(newCalendar.getTime());
                    closeOrder("0",assignedOrderModel.getOrderCost(),assignedOrderModel.getOrderCost(),dateStr);
                }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void closeOrder(String outstandingAmt, String orderCost, String amountReceived,String dateStr){

            WebserviceController wss = new WebserviceController(AssignedVoucherOrderDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", assignedOrderModel.getOrderId());
                jsonObject.put("customerId", assignedOrderModel.getCustomerId());
                jsonObject.put("outstandingAmount", outstandingAmt);
                jsonObject.put("deliverdDate", (assignedOrderModel.getDelivaredDate()==null || TextUtils.isEmpty(assignedOrderModel.getDelivaredDate())? dateStr : CommonFunctions.converDateStr(assignedOrderModel.getDelivaryDate())));
                jsonObject.put("orderCost", orderCost);
                jsonObject.put("amountReceived", amountReceived);
                jsonObject.put("distributorId", sharedPreferences.getString("userid", ""));
                JSONArray jsonArray = new JSONArray();

                for (OrderResponse.RequestS.ProductOrder  productOrder: data){
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("deliveredQty",productOrder.getOtsDeliveredQty());
                    jsonObject1.put("emptyCan",productOrder.getCanRecieved());
                    jsonObject1.put("orderQty",productOrder.getOtsOrderedQty());
                    jsonObject1.put("prodcutId",productOrder.getOtsProductId());
                    jsonObject1.put("productbalanceQty",(productOrder.getOutSbalanceCan()!=null?productOrder.getOutSbalanceCan():productOrder.getBalanceCan()));
                    jsonObject1.put("productCost",productOrder.getOtsOrderProductCost());

                    jsonArray.put(jsonObject1);
                }
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
                            Toast.makeText(AssignedVoucherOrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(AssignedVoucherOrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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
        WebserviceController wss = new WebserviceController(AssignedVoucherOrderDescription.this);

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

                    DistributorResponse distributorResponse = new Gson().fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
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
                            Toast.makeText(AssignedVoucherOrderDescription.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AssignedVoucherOrderDescription.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Partner</font>"));

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
                                assignEmpLoyee.setText(strDistName+" ("+strDist+")");
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


    void getProductStock(final OrderResponse.RequestS.ProductOrder item, final int position, final Integer deliveryQuant, final TextView balanceCanEdit, final AlertDialog alertDialog, final EditText deliveryEdit, final EditText canRecievedEditText) {
        WebserviceController wss = new WebserviceController(this);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", item.getOtsProductId());
            jsonObject.put("distributorId", (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") ? sharedPreferences.getString("userid","") : sharedPreferences.getString("distId","")));
            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.Get_Product_Stock, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("login response", response);

                    ProductsStock responseData = new Gson().fromJson(response, ProductsStock.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData().getStockQuantity()!=null){
                            if (Integer.valueOf(responseData.getResponseData().getStockQuantity())>=deliveryQuant){
                                if (Integer.valueOf(balanceCanEdit.getText().toString())>=0) {
                                    alertDialog.dismiss();
                                    assignedOrderGridAdapter.getmData().get(position).setOtsDeliveredQty(deliveryEdit.getText().toString());
                                    if (item!=null && item.getType()!=null && item.getType().toLowerCase().equalsIgnoreCase("box")){
                                        assignedOrderGridAdapter.getmData().get(position).setCanRecieved(deliveryEdit.getText().toString());
                                    }
                                    else {
                                        assignedOrderGridAdapter.getmData().get(position).setCanRecieved(canRecievedEditText.getText().toString());
                                    }
                                    assignedOrderGridAdapter.getmData().get(position).setOutSbalanceCan(balanceCanEdit.getText().toString());
                                    assignedOrderGridAdapter.notifyDataSetChanged();
                                    orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+assignedOrderGridAdapter.getTotalCost()));
                                }
                                else {
                                    Toast.makeText(AssignedVoucherOrderDescription.this, "Balance Can cannot be Negative", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                deliveryEdit.setText(responseData.getResponseData().getStockQuantity());
                                Toast.makeText(AssignedVoucherOrderDescription.this, "In Stock is "+responseData.getResponseData().getStockQuantity(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(orderNo.getWindowToken(), 0);
    }
    public void CloseRequest(){

        WebserviceController wss = new WebserviceController(AssignedVoucherOrderDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", assignedOrderModel.getOrderId());
            if(getIntent().hasExtra("deliverDonation")){
                jsonObject.put("status", "DoneDonation");
            }else {
                jsonObject.put("status", "receiveDonation");
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
                        finish();
                        Toast.makeText(AssignedVoucherOrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(AssignedVoucherOrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(OrderDescription.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
            }
        });
    }

}

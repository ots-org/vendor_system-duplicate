package com.ortusolis.pravarthaka.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.Utility.CommonFunctions;
import com.ortusolis.pravarthaka.adapter.AssignedOrderGridAdapter;
import com.ortusolis.pravarthaka.pojo.AssignedOrderModel;
import com.ortusolis.pravarthaka.pojo.DistributorResponse;
import com.ortusolis.pravarthaka.pojo.OrderResponse;
import com.ortusolis.pravarthaka.pojo.ProductsStock;
import com.ortusolis.pravarthaka.pojo.RegisterResponse;
import com.ortusolis.pravarthaka.pojo.UserInfo;

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
            toolbarTitle.setText("Sales Voucher");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        orderNo.setText(Html.fromHtml("<b>Order Number: </b>"+assignedOrderModel.getOrderId()));
        customeName.setText(Html.fromHtml("<b>Ordered Date: </b>"+CommonFunctions.converDateStr(assignedOrderModel.getOrderDate())));
        orderDate.setText(Html.fromHtml("<b>Customer Name: </b>"+assignedOrderModel.getCustomerDetails().getFirstName()));
        deliveryDate.setText(Html.fromHtml("<b>Delivery Date: </b>"+ CommonFunctions.converDateStr(assignedOrderModel.getDelivaryDate())));
        deliveredBy.setText(Html.fromHtml("<b>Distributor Name: </b>"+assignedOrderModel.getDistributorDetails().getFirstName()));

//        outstandingAmt.setText(Html.fromHtml("<b>Outstanding Amount: </b>"+assignedOrderModel.getOutStandingAmount()+getString(R.string.Rs)));

        assignedOrderGridAdapter = new AssignedOrderGridAdapter(AssignedVoucherOrderDescription.this,data);
        gridview.setAdapter(assignedOrderGridAdapter);

        assignedOrderGridAdapter.notifyDataSetChanged();

        if (assignedOrderModel.getEmployeeDetails() != null && assignedOrderModel.getEmployeeDetails().getFirstName()!=null && assignedOrderModel.getEmployeeDetails().getLastName()!=null ){
            assignEmpLoyee.setText(Html.fromHtml("<b>Assigned Employee: </b>"+assignedOrderModel.getEmployeeDetails().getFirstName()+" "+assignedOrderModel.getEmployeeDetails().getLastName()));
        }

        orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+assignedOrderModel.getOrderCost()));

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                final OrderResponse.RequestS.ProductOrder item = (OrderResponse.RequestS.ProductOrder) assignedOrderGridAdapter.getItem(position);
//                final AlertDialog.Builder builder = new AlertDialog.Builder(AssignedVoucherOrderDescription.this);
//                View view1 = getLayoutInflater().inflate(R.layout.dialog_assigned_products,null);
//                builder.setView(view1);
//                builder.setTitle("Order Product");
//
//                final TextView orderedQuantityEditText = view1.findViewById(R.id.orderedQuantityEditText);
//                final TextView productNameEditText = view1.findViewById(R.id.productNameEditText);
//                final EditText deliveryEdit = view1.findViewById(R.id.deliveryEditText);
//                final EditText canRecievedEditText = view1.findViewById(R.id.canRecievedEditText);
//                final TextView outstandingEdit = view1.findViewById(R.id.outstandingEditText);
//                final TextView balanceCanEdit = view1.findViewById(R.id.balanceCanEditText);
//                final TextView productCostEdit = view1.findViewById(R.id.productCostEditText);
//                Button incrementDeliveryCan = view1.findViewById(R.id.incrementDeliveryCan);
//                Button decrementDeliveryCan = view1.findViewById(R.id.decrementDeliveryCan);
//                Button incrementCanRecieved = view1.findViewById(R.id.incrementCanRecieved);
//                Button decrementCanRecieved = view1.findViewById(R.id.decrementCanRecieved);
//                LinearLayout canRecievedLL = view1.findViewById(R.id.canRecievedLL);
//                Button updateButton = view1.findViewById(R.id.updateButton);
//
//                final AlertDialog alertDialog = builder.create();
//
//                productNameEditText.setText(item.getProductName());
//                 orderedQuantityEditText.setText(item.getOtsOrderedQty());
//                deliveryEdit.setText(item.getOtsDeliveredQty());
//                //
//                canRecievedEditText.setText(item.getCanRecieved()!=null ? item.getCanRecieved() : "");
//                //
//
//                outstandingEdit.setText(item.getBalanceCan());
//
//                if (item!=null && item.getType()!=null && item.getType().toLowerCase().equalsIgnoreCase("box")){
//
//                    canRecievedLL.setVisibility(View.GONE);
//
//                }
//
//                int balanceCan = (int) ((Double.valueOf(deliveryEdit.getText().toString().isEmpty()?"0":deliveryEdit.getText().toString()))+(Double.valueOf(outstandingEdit.getText().toString().isEmpty()?"0":outstandingEdit.getText().toString())) - ((Double.valueOf(canRecievedEditText.getText().toString().isEmpty()?"0":canRecievedEditText.getText().toString()))));
//                balanceCanEdit.setText(item!=null && item.getType()!=null && item.getType().toLowerCase().equalsIgnoreCase("box")?"0":balanceCan+"");
//                productCostEdit.setText((int)(Double.valueOf(deliveryEdit.getText().toString().isEmpty()?"0":deliveryEdit.getText().toString())*Double.valueOf(item.getOtsOrderProductCost()))+"");
//
//                incrementDeliveryCan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int delv = (Integer.valueOf(deliveryEdit.getText().toString().isEmpty()?"0":deliveryEdit.getText().toString()))+1;
//                        deliveryEdit.setText(delv+"");
//                    }
//                });
//
//                decrementDeliveryCan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (Integer.valueOf(deliveryEdit.getText().toString().isEmpty()?"0":deliveryEdit.getText().toString())>0) {
//                            int delv = (Integer.valueOf(deliveryEdit.getText().toString().isEmpty() ? "0" : deliveryEdit.getText().toString())) - 1;
//                            deliveryEdit.setText(delv+"");
//                        }
//                    }
//                });
//
//                decrementCanRecieved.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (Integer.valueOf(canRecievedEditText.getText().toString().isEmpty()?"0":canRecievedEditText.getText().toString())>0) {
//                            int delv = (Integer.valueOf(canRecievedEditText.getText().toString().isEmpty() ? "0" : canRecievedEditText.getText().toString())) - 1;
//                            canRecievedEditText.setText(delv+"");
//                        }
//                    }
//                });
//
//                incrementCanRecieved.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int delv = (Integer.valueOf(canRecievedEditText.getText().toString().isEmpty()?"0":canRecievedEditText.getText().toString()))+1;
//                        canRecievedEditText.setText(delv+"");
//                    }
//                });
//
//                assignEmpLoyee.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //getAllRegister();
//                    }
//                });
//
//                TextWatcher textWatcher = new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                        int balanceCan = (int) ((Double.valueOf(deliveryEdit.getText().toString().isEmpty()?"0":deliveryEdit.getText().toString()))+(Double.valueOf(outstandingEdit.getText().toString().isEmpty()?"0":outstandingEdit.getText().toString())) - ((Double.valueOf(canRecievedEditText.getText().toString().isEmpty()?"0":canRecievedEditText.getText().toString()))));
//                        balanceCanEdit.setText(item!=null && item.getType()!=null && item.getType().toLowerCase().equalsIgnoreCase("box")?"0":balanceCan+"");
//                        productCostEdit.setText((int)(Double.valueOf(deliveryEdit.getText().toString().isEmpty()?"0":deliveryEdit.getText().toString())*Double.valueOf(item.getOtsOrderProductCost()))+"");
//
//                    }
//                };
//
//                deliveryEdit.addTextChangedListener(textWatcher);
//                canRecievedEditText.addTextChangedListener(textWatcher);
//
//                updateButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        getProductStock(item,position,Integer.valueOf(deliveryEdit.getText().toString()),balanceCanEdit,alertDialog,deliveryEdit,canRecievedEditText);
//                    }
//                });
//
////                alertDialog.show();
//            }
//        });

        customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createSaleVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(AssignedVoucherOrderDescription.this);
//                View view1 = getLayoutInflater().inflate(R.layout.dialog_sale_voucher,null);
//                builder.setView(view1);
//                builder.setTitle("Sale Voucher");

                final Calendar newCalendar = Calendar.getInstance();

                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = format.format(newCalendar.getTime());
                closeOrder("0",assignedOrderModel.getOrderCost(),assignedOrderModel.getOrderCost(),dateStr);
//                final TextView outstandingAmt = view1.findViewById(R.id.outstandingAmt);
//                final EditText orderCostEditText = view1.findViewById(R.id.orderCostEditText);
//                final EditText amountRecievedEditText = view1.findViewById(R.id.amountRecievedEditText);
//                final EditText balanceAmountEditText = view1.findViewById(R.id.balanceAmountEditText);
//                final Button buttonDatePick = view1.findViewById(R.id.buttonDatePick);
//                Button closeOrderButton = view1.findViewById(R.id.closeOrderButton);
//
//                outstandingAmt.setText(assignedOrderModel.getOutStandingAmount());
//                orderCostEditText.setText(assignedOrderGridAdapter.getTotalCost());
//                amountRecievedEditText.setText("");
//                amountRecievedEditText.requestFocus();
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.toggleSoftInput(InputMethodManager.RESULT_SHOWN, 0);
//
//                Float balanceAmount = (Float.valueOf(TextUtils.isEmpty(outstandingAmt.getText().toString())?"0":outstandingAmt.getText().toString()) + Float.valueOf(TextUtils.isEmpty(orderCostEditText.getText().toString())?"0":orderCostEditText.getText().toString()))
//                                        -Float.valueOf(TextUtils.isEmpty(amountRecievedEditText.getText().toString())|| amountRecievedEditText.getText().toString().equalsIgnoreCase("-")?"0": amountRecievedEditText.getText().toString());
//                balanceAmountEditText.setText(CommonFunctions.fmt(balanceAmount));
//
////                final AlertDialog alertDialog = builder.create();
//
////                String dateStr = format.format(newCalendar.getTime());
//
//                buttonDatePick.setText(dateStr);
//
//                final DatePickerDialog startTime = new DatePickerDialog(AssignedVoucherOrderDescription.this, new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        newCalendar.set(year, monthOfYear, dayOfMonth);
//
//                        String dateStr = format.format(newCalendar.getTime());
//
//                        buttonDatePick.setText(dateStr);
//
//                    }
//                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//                buttonDatePick.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startTime.show();
//                    }
//                });
//
//                TextWatcher textWatcher = new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        Float balanceAmount = (Float.valueOf(TextUtils.isEmpty(outstandingAmt.getText().toString())?"0":outstandingAmt.getText().toString()) + Float.valueOf(TextUtils.isEmpty(orderCostEditText.getText().toString())?"0":orderCostEditText.getText().toString()))
//                                -Float.valueOf(TextUtils.isEmpty(amountRecievedEditText.getText().toString())|| amountRecievedEditText.getText().toString().equalsIgnoreCase("-")?"0": amountRecievedEditText.getText().toString());
//                        balanceAmountEditText.setText(CommonFunctions.fmt(balanceAmount));
//                    }
//                };
//
//                outstandingAmt.addTextChangedListener(textWatcher);
//                orderCostEditText.addTextChangedListener(textWatcher);
//                amountRecievedEditText.addTextChangedListener(textWatcher);
//
//
//                //
//                closeOrder(balanceAmountEditText.getText().toString(),assignedOrderModel.getOrderCost(),assignedOrderModel.getOrderCost(),buttonDatePick.getText().toString());
//                //
//                closeOrderButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                    alertDialog.dismiss();
////                    closeOrder(balanceAmountEditText.getText().toString(),assignedOrderModel.getOrderCost(),assignedOrderModel.getOrderCost(),buttonDatePick.getText().toString());
//                    }
//                });
//                alertDialog.show();
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
                    Toast.makeText(AssignedVoucherOrderDescription.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AssignedVoucherOrderDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
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
                Toast.makeText(AssignedVoucherOrderDescription.this , stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(orderNo.getWindowToken(), 0);

    }
}

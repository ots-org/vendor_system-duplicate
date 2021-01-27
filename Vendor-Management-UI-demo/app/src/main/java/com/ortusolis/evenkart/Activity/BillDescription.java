package com.ortusolis.evenkart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.evenkart.NetworkUtility.Constants;
import com.ortusolis.evenkart.NetworkUtility.IResult;
import com.ortusolis.evenkart.NetworkUtility.WebserviceController;
import com.ortusolis.evenkart.R;
import com.ortusolis.evenkart.pojo.BillReportModel;
import com.ortusolis.evenkart.pojo.BillReportResponse;
import com.ortusolis.evenkart.pojo.BillRequest;
import com.ortusolis.evenkart.pojo.GeneralResponse;
import com.ortusolis.evenkart.pojo.ProductDetails;
import com.ortusolis.evenkart.pojo.RegisterResponse;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BillDescription extends AppCompatActivity {

    TextView fromDateText,toDateTextView,totalOutstanding,distributorNameText,customerNameText,noResult,total,sgstcost,cgstcost,fulltotal;
    Button assignButton;
    LinearLayout addLL;
    EditText sgstEdit,cgstEdit;

    Toolbar mToolbar;
    ActionBar action;

    int price = 0;
    String customerId = "";
    SharedPreferences sharedPreferences;
    Gson gson;
    int valueOf = 0;
    String orderId = "";
    ArrayList<BillRequest.RequestS.OrderId> ordersList;
    ProgressDialog progressDialog;
    String fromDateStr, toDateStr;
    double fullProdTotal;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_description);
        assignButton = findViewById(R.id.placeOrder);
        gson = new Gson()/*new GsonBuilder().serializeNulls().create()*/;

        mToolbar = findViewById(R.id.toolbar);
        fromDateText = findViewById(R.id.fromDateText);
        toDateTextView = findViewById(R.id.toDateTextView);
        totalOutstanding = findViewById(R.id.totalOutstanding);
        distributorNameText = findViewById(R.id.distributorNameText);
        customerNameText = findViewById(R.id.customerNameText);
        noResult = findViewById(R.id.noResult);
        total = findViewById(R.id.total);
        sgstcost = findViewById(R.id.sgstcost);
        cgstcost = findViewById(R.id.cgstcost);
        fulltotal = findViewById(R.id.fulltotal);
        addLL = findViewById(R.id.addLL);
        sgstEdit = findViewById(R.id.sgstEdit);
        cgstEdit = findViewById(R.id.cgstEdit);

        sharedPreferences = getSharedPreferences("water_management",0);

        setSupportActionBar(mToolbar);
        progressDialog = new ProgressDialog(BillDescription.this);
        progressDialog.setMessage("Loading..");

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
            toolbarTitle.setText("Bill details");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (getIntent().hasExtra("outstanding")){
            price = getIntent().getExtras().getInt("outstanding");
        }
        if (getIntent().hasExtra("customerId")){
            customerId = getIntent().getExtras().getString("customerId");
        }
        if (getIntent().hasExtra("orderId")){
            orderId = getIntent().getExtras().getString("orderId");
        }

        Log.d("Order ID Bill Desc",orderId);
        if (getIntent().hasExtra("fromDate")){
            fromDateStr = getIntent().getExtras().getString("fromDate");
        }
        if (getIntent().hasExtra("toDate")){
            toDateStr = getIntent().getExtras().getString("toDate");
        }
        ordersList = new ArrayList<>();

        getCustomerOutstandingAmt();

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                addOrUpdateBill();
                //addOrUpdateCustomerOutstandingAmt();
            }
        });

    }

     void setValues(BillReportModel billReportModel){
        final DecimalFormat f = new DecimalFormat("##.00");

        fromDateText.setText(fromDateStr);
        toDateTextView.setText(toDateStr);
        distributorNameText.setText(billReportModel.getDistributorDetails().getFirstName());
        customerNameText.setText(billReportModel.getCustomerDetails().getFirstName());

        loadData(billReportModel.getProductDeatils());

        total.setText(f.format(fullProdTotal));

         TextWatcher textWatcher = new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }

             @Override
             public void afterTextChanged(Editable s) {
                    double ctot = Double.valueOf(TextUtils.isEmpty(cgstEdit.getText().toString()) ? "0":cgstEdit.getText().toString())*(fullProdTotal/100);
                    double stot = Double.valueOf(TextUtils.isEmpty(sgstEdit.getText().toString()) ? "0":sgstEdit.getText().toString())*(fullProdTotal/100);;

                    cgstcost.setText(f.format(ctot)+"");
                    sgstcost.setText(f.format(stot)+"");
                    fulltotal.setText(f.format(fullProdTotal+ctot+stot)+"");
             }
         };

         cgstEdit.addTextChangedListener(textWatcher);
         sgstEdit.addTextChangedListener(textWatcher);

         double ctot = Double.valueOf(TextUtils.isEmpty(cgstEdit.getText().toString()) ? "0":cgstEdit.getText().toString())*(fullProdTotal/100);
         double stot = Double.valueOf(TextUtils.isEmpty(sgstEdit.getText().toString()) ? "0":sgstEdit.getText().toString())*(fullProdTotal/100);;

         cgstcost.setText(f.format(ctot)+"");
         sgstcost.setText(f.format(stot)+"");
         fulltotal.setText(f.format(fullProdTotal+ctot+stot)+"");

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

    void makeOrderIDs(){
        ordersList.clear();
        String[] ordersL = orderId.split(",");
        for (String ord : ordersL){
            BillRequest.RequestS.OrderId orderId = new BillRequest.RequestS.OrderId();
            orderId.setOrderId(ord);
            ordersList.add(orderId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void addOrUpdateCustomerOutstandingAmt(){

            WebserviceController wss = new WebserviceController(BillDescription.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("customerId", customerId);
                int locvalueOf = valueOf + price;
                jsonObject.put("customerOutstandingAmt", locvalueOf+"");

                requestObject.put("requestData", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.addOrUpdateCustomerOutstandingAmt, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            addOrUpdateBill();
                        } else
                            Toast.makeText(BillDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    prog();
                }
            });

    }

    void addOrUpdateBill(){

            WebserviceController wss = new WebserviceController(BillDescription.this);

            int locvalueOf = valueOf + price;

            makeOrderIDs();

            BillRequest billRequest = new BillRequest();

        BillRequest.RequestS requestS = new BillRequest.RequestS();
        requestS.setBillId("0");
        requestS.setBillGenerated("2");
        requestS.setBillAmount(fulltotal.getText().toString());
        requestS.setBillAmountReceived(fulltotal.getText().toString());
        requestS.setBillGenerated("1");
        requestS.setOutstandingAmount(price+"");
        requestS.setCustomerId(customerId);
        requestS.setIgst(sgstcost.getText().toString());
        requestS.setCgst(cgstcost.getText().toString());
        requestS.setFromDate(fromDateStr);
        requestS.setToDate(toDateStr);

        requestS.setOrderId(ordersList);

        billRequest.setRequestData(requestS);

        String str = gson.toJson(billRequest);

            wss.postLoginVolley(Constants.addOrUpdateBill, str, new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("addOrUpdateBill resp", response);

                        GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
                        prog();
                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            setResult(RESULT_OK);
                            finish();

                        }
                            Toast.makeText(BillDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to generate Bill" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    prog();
                }
            });

    }

    void prog(){
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    void getCustomerOutstandingAmt(){

        WebserviceController wss = new WebserviceController(BillDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {

            String[] ordersL = orderId.split(",");
            for (String orderIdStr:ordersL){
                jsonArray.put(orderIdStr);
            }

            jsonObject.put("orderId", jsonArray);
            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getProductDetailsForBill, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("BillOutstandingResponse", response);

                    BillReportResponse responseData = gson.fromJson(response, BillReportResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (!responseData.getResponseData().getProductDeatils().isEmpty()){
                            setValues(responseData.getResponseData());
                        }
                    }
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

    void loadData(List<ProductDetails> requestS){
        addLL.removeAllViews();
        if (!requestS.isEmpty()) {
            addRow(null);
            for (ProductDetails requestSdata : requestS) {
                addRow(requestSdata);
            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(ProductDetails productDetails){

        if (productDetails!=null){
                View view = getLayoutInflater().inflate(R.layout.view_bill_report,null);
                TextView descriptionText = view.findViewById(R.id.descriptionText);
                TextView unitQuantity = view.findViewById(R.id.unitQuantity);
                TextView rate = view.findViewById(R.id.rate);
                TextView amount = view.findViewById(R.id.amount);
                descriptionText.setText(productDetails.getProductName());
                unitQuantity.setText(productDetails.getProductqty());
                rate.setText(productDetails.getProductPrice());
                float totalAmt = Float.valueOf(productDetails.getProductPrice()) * Float.valueOf(productDetails.getProductqty());
                amount.setText(""+totalAmt);
                fullProdTotal = fullProdTotal + totalAmt;
                addLL.addView(view);
        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_bill_report,null);
            addLL.addView(view);
        }

    }

}

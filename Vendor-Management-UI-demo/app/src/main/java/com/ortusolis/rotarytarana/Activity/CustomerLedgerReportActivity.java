package com.ortusolis.rotarytarana.Activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.OrderResponse;
import com.ortusolis.rotarytarana.pojo.ProductDetails;
import com.ortusolis.rotarytarana.pojo.ProductsResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerLedgerReportActivity extends AppCompatActivity {

    Button buttonDatePick,buttonDatePickEnd,getReport;
    LinearLayout addLL;
    Calendar newCalendar;
    Calendar newCalendarEnd;
    DatePickerDialog startTime,endTime;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    int slNumber = 0;

    LinearLayout distributorCodeLayout;
    String distributorStr = "";
    ProgressDialog progressDialog;
    String employeeStr = "";
    String productStr = "";
    TextView distributorText;
    TextView employeeText;
    TextView productText;
    String strDistName = null;
    String strDist = null;
    String strEmpName = null;
    String strEmp = null;
    String strProdName = null;
    String strProd = null;
    List<String> userNames, userIdList;
    List<String> empNames, empIdList;
    List<String> productNames, productIdList;
    String pdf="no";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ledger_report);
        addLL = findViewById(R.id.addLL);
        buttonDatePick = findViewById(R.id.buttonDatePick);
        buttonDatePickEnd = findViewById(R.id.buttonDateEndPick);
        getReport = findViewById(R.id.getReport);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        distributorCodeLayout = findViewById(R.id.distributorCodeLayout);
        distributorText = findViewById(R.id.distributorText);
        employeeText = findViewById(R.id.employeeText);
        productText = findViewById(R.id.productText);
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();

        setSupportActionBar(mToolbar);

        gson = new Gson();
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        empNames = new ArrayList<>();
        empIdList = new ArrayList<>();
        productNames = new ArrayList<>();
        productIdList = new ArrayList<>();

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
            toolbarTitle.setText("Requester/Donor Ledger Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(CustomerLedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendarEnd = Calendar.getInstance();
        endTime = new DatePickerDialog(CustomerLedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                getListOfOrderByDate();
            }
        });

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            distributorCodeLayout.setVisibility(View.GONE);
        }
        else {
            distributorCodeLayout.setVisibility(View.GONE);
        }

        distributorText.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CustomerLedgerReportActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegister();
            }
        });

        employeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CustomerLedgerReportActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegisterEmployee();
            }
        });

        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CustomerLedgerReportActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getProducts();
            }
        });

    }
    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            pdf="yes";
        }
    }

    void getListOfOrderByDate(){

        WebserviceController wss = new WebserviceController(CustomerLedgerReportActivity.this);

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

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
                jsonObject.put("distributorsId", "1");
            }else {
                jsonObject.put("distributorsId", usserId);
            }
            jsonObject.put("status", "close");
            jsonObject.put("fromTime", buttonDatePick.getText().toString());
            jsonObject.put("toTime", buttonDatePickEnd.getText().toString());
            jsonObject.put("pdf", pdf);
            if (!employeeStr.isEmpty())
            jsonObject.put("customerId", employeeStr);
            if (!productStr.isEmpty())
            jsonObject.put("productId", productStr);
            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }


        wss.postLoginVolley(Constants.orderReportByDate, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    OrderResponse responseData = gson.fromJson(response, OrderResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getOrderList()!=null) {
                            noResult.setVisibility(View.GONE);
                            loadData(responseData.getResponseData().getOrderList());
                            if(pdf.equals("yes")){
                                String txt=responseData.getResponseData().getPdf();
                                final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "Customer Ledger Report" + ".pdf");
                                byte[] pdfAsBytes = Base64.decode(txt, 0);
                                FileOutputStream os;
                                try {
                                    os = new FileOutputStream(dwldsPath, false);
                                    os.write(pdfAsBytes);
                                    os.flush();
                                    os.close();
                                    Toast.makeText(getApplicationContext(), "Requester/Donor Ledger Report Pdf Available", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(CustomerLedgerReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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

    void loadData(List<OrderResponse.RequestS> requestS){
        addLL.removeAllViews();
        slNumber = 0;
        if (!requestS.isEmpty()) {
            noResult.setVisibility(View.GONE);
            addRow(null);

            for (OrderResponse.RequestS requestSdata : requestS) {
                addRow(requestSdata);
            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(OrderResponse.RequestS requestSdata){


        if (requestSdata!=null){
            String orderIDRec = "";
            String amtRec = "";
            String outStd = "";
            String orderCo = "";
            String customerNameStr = "";

            for (OrderResponse.RequestS.ProductOrder productOrder : requestSdata.getOrderdProducts()) {
                slNumber = slNumber+1;

                View view = getLayoutInflater().inflate(R.layout.view_customer_ledger,null);
                TextView slNo = view.findViewById(R.id.slNo);
                TextView customerName = view.findViewById(R.id.customerName);
                TextView orderNo = view.findViewById(R.id.orderNo);
                TextView deliveredDate = view.findViewById(R.id.deliveredDate);
                TextView productName = view.findViewById(R.id.productName);
                TextView orderedQty = view.findViewById(R.id.orderedQty);
                TextView deliveredQty = view.findViewById(R.id.deliveredQty);
                TextView emptyCanRecieved = view.findViewById(R.id.emptyCanRecieved);
                TextView balanceCan = view.findViewById(R.id.balanceCan);
                TextView productCost = view.findViewById(R.id.productCost);
                TextView cashRecieved = view.findViewById(R.id.cashRecieved);
                TextView amountOutstanding = view.findViewById(R.id.amountOutstanding);

                slNo.setText(slNumber + "");

                productName.setText(productOrder.getProductName());
                orderedQty.setText(productOrder.getOtsOrderedQty());
                orderedQty.setGravity(Gravity.RIGHT);
                deliveredQty.setText(productOrder.getOtsDeliveredQty());
                deliveredQty.setGravity(Gravity.RIGHT);
                emptyCanRecieved.setText(productOrder.getEmptyCanRecived());
                emptyCanRecieved.setGravity(Gravity.RIGHT);
                balanceCan.setText(productOrder.getBalanceCan());
                balanceCan.setGravity(Gravity.RIGHT);

                    if (requestSdata.getOrderNumber()==null || requestSdata.getOrderNumber().equalsIgnoreCase(orderIDRec)) {
                        orderNo.setText("");
                        deliveredDate.setText("");
                        productCost.setText("");
                        customerName.setText("");
                    } else {
                        orderIDRec = requestSdata.getOrderNumber();
                        orderCo = requestSdata.getOrderCost();
                        customerNameStr = requestSdata.getCustomerDetails().getFirstName();
                        orderNo.setText(orderIDRec);
                        orderNo.setGravity(Gravity.RIGHT);
                        productCost.setText(orderCo);
                        productCost.setGravity(Gravity.RIGHT);
                        deliveredDate.setText(requestSdata.getDelivaredDate());
                        customerName.setText(customerNameStr);
                    }

                    if (requestSdata.getAmountRecived()==null || requestSdata.getAmountRecived().equalsIgnoreCase(amtRec)) {
                        cashRecieved.setText("");
                    } else {
                        amtRec = requestSdata.getAmountRecived();
                        cashRecieved.setText(amtRec);
                        cashRecieved.setGravity(Gravity.RIGHT);
                    }

                    if (requestSdata.getOrderOutStanding()==null || requestSdata.getOrderOutStanding().equalsIgnoreCase(outStd)) {
                        amountOutstanding.setText("");
                    } else {
                        outStd = requestSdata.getOrderOutStanding();
                        amountOutstanding.setText(outStd);
                        amountOutstanding.setGravity(Gravity.RIGHT);
                    }
                addLL.addView(view);
            }

        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_customer_ledger,null);
            addLL.addView(view);
        }
    }

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(CustomerLedgerReportActivity.this);

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
                            Toast.makeText(CustomerLedgerReportActivity.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerLedgerReportActivity.this);
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
                                distributorText.setText(strDistName);
                                distributorStr = strDist;
                                employeeStr = "";
                                productStr = "";
                                employeeText.setText("Select Requester/Donor");
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

    void getAllRegisterEmployee(){
        WebserviceController wss = new WebserviceController(CustomerLedgerReportActivity.this);

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
                        progressDialog.dismiss();
                        strEmp = "";
                        empNames.clear();
                        empIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
                            if (userInfo1.getUserRoleId().equalsIgnoreCase("4")) {
                                empNames.add(userInfo1.getFirstName());
                                empIdList.add(userInfo1.getUserId());
                            }
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(CustomerLedgerReportActivity.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerLedgerReportActivity.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Requester/Donor</font>"));

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
                                employeeText.setText(strEmpName);
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


    void getProducts() {
        WebserviceController wss = new WebserviceController(this);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        String usserId = "";
        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            usserId = distributorStr;
        }
        else if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            usserId = sharedPreferences.getString("userid","");
        }
        else {
            usserId = sharedPreferences.getString("distId","");
        }

        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", usserId);
            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.productNames.clear();
        this.productIdList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("login response", response);

                    ProductsResponse responseData = (ProductsResponse) gson.fromJson(response, ProductsResponse.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        progressDialog.dismiss();
                        strProd = "";
                        strProdName = "";
                        productNames.clear();
                        productIdList.clear();

                        Iterator it = responseData.getResponseData().getProductDetails().iterator();
                        while (it.hasNext()) {
                            ProductDetails productDetails = (ProductDetails) it.next();
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                        }
                        if (!productIdList.isEmpty()) {
                            strProd = (String) productIdList.get(0);
                            strProdName = (String) productNames.get(0);
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CustomerLedgerReportActivity.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product</font>"));

                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        strEmp = productIdList.get(checkedItem);
                        strEmpName = productNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                strProd = (String) productIdList.get(which);
                                strProdName = (String) productNames.get(which);
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
                                productStr = strProd;
                                productText.setText(strProdName);
                            }
                        });
                        builderSingle.show();

                    } else {
                        Toast.makeText(CustomerLedgerReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Context context = CustomerLedgerReportActivity.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
            }
        });
    }

}

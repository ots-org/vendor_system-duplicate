package com.ortusolis.water_distribution.Activity;

import android.app.DatePickerDialog;
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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.water_distribution.NetworkUtility.Constants;
import com.ortusolis.water_distribution.NetworkUtility.IResult;
import com.ortusolis.water_distribution.NetworkUtility.WebserviceController;
import com.ortusolis.water_distribution.R;
import com.ortusolis.water_distribution.adapter.BillListAdapter;
import com.ortusolis.water_distribution.pojo.CompleteOrderDetails;
import com.ortusolis.water_distribution.pojo.DistributorResponse;
import com.ortusolis.water_distribution.pojo.OrderReportResponse;
import com.ortusolis.water_distribution.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BillListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    Button buttonDatePick,buttonDatePickEnd,getReport;
    Calendar newCalendar;
    Calendar newCalendarEnd;
    DatePickerDialog startTime,endTime;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    BillListAdapter billGridAdapter;
    List<CompleteOrderDetails> data;
    List<CompleteOrderDetails> addedOrders;
    TextView noResult,customerText;
    //
    ProgressDialog progressDialog;
    //
    String customeId = "";
    String orderIds = "";
    String customerStrName = null;
    String strCustName = null;
    List<String> userNames, userIdList;
    LinearLayout customeLinearLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);

        sharedPreferences = getSharedPreferences("water_management",0);

        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        noResult = findViewById(R.id.noResult);
        buttonDatePick = findViewById(R.id.buttonDatePick);
        buttonDatePickEnd = findViewById(R.id.buttonDateEndPick);
        getReport = findViewById(R.id.getReport);
        customerText = findViewById(R.id.customerStr);
        customeLinearLayout = findViewById(R.id.customeLinearLayout);

        data = new ArrayList<>();
        addedOrders = new ArrayList<>();

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

        gson = new Gson();

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
            toolbarTitle.setText("Bills");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(BillListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(BillListActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendarEnd = Calendar.getInstance();
        endTime = new DatePickerDialog(BillListActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                getOrderByStatusAndDistributor();
            }
        });

        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code
                progressDialog = new ProgressDialog(BillListActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //
                getAllRegister();
            }
        });

        customeLinearLayout.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==444&& resultCode==RESULT_OK){

            if (billGridAdapter!=null) {
                billGridAdapter.clearAll();
            }
            onBackPressed();
        }
    }

    void getOrderByStatusAndDistributor(){

        WebserviceController wss = new WebserviceController(BillListActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("status", "close");
            jsonObject.put("userId", customeId);
            jsonObject.put("role", "Customer");
            jsonObject.put("startDate", buttonDatePick.getText().toString());
            jsonObject.put("endDate", buttonDatePickEnd.getText().toString());

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (billGridAdapter!=null){
            billGridAdapter.notifyDataSetChanged();
        }

        wss.postLoginVolley(Constants.getListOfOrderByDate, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    OrderReportResponse responseData = gson.fromJson(response, OrderReportResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200") && responseData.getResponseData()!=null && responseData.getResponseData().getCompleteOrderDetails()!=null && !responseData.getResponseData().getCompleteOrderDetails().isEmpty()) {

                            noResult.setVisibility(View.GONE);
                            addedOrders.clear();
                            billGridAdapter = new BillListAdapter(BillListActivity.this, responseData.getResponseData().getCompleteOrderDetails());
                            recyclerView.setAdapter(billGridAdapter);
                            billGridAdapter.notifyDataSetChanged();

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        addedOrders.clear();
                        if (billGridAdapter!=null)
                            billGridAdapter.clearAll();
                        Toast.makeText(BillListActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    addedOrders.clear();
                    if (billGridAdapter!=null)
                        billGridAdapter.clearAll();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                addedOrders.clear();
                if (billGridAdapter!=null)
                    billGridAdapter.clearAll();
                Toast.makeText(BillListActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean addBills(CompleteOrderDetails requestS){

        if (addedOrders.isEmpty()){
            addedOrders.add(requestS);
            return true;
        }

        if (checkIfSameCustomer(requestS)){

            if (checkIfSameOrder(requestS)){
                removeOrder(requestS);
                return false;
            }
            else {
                addedOrders.add(requestS);
                return true;
            }


        }
        else {
            Toast.makeText(this, "Please select the orders of same Customer", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public List<CompleteOrderDetails> getAddedOrders() {
        return addedOrders;
    }

    boolean checkIfSameCustomer(CompleteOrderDetails requestS){

        boolean sameCust = false;

        for (CompleteOrderDetails requestS1 : addedOrders){
            if (requestS1.getCustomerId().equals(requestS.getCustomerId())){
                sameCust = true;
            }
        }
        return sameCust;
    }

    public boolean checkIfSameOrder(CompleteOrderDetails requestS){

        boolean sameOrd = false;

        for (CompleteOrderDetails requestS1 : addedOrders){
            if (requestS1.getOrderId().equals(requestS.getOrderId())){
                sameOrd = true;
            }
        }
        return sameOrd;
    }

    public void removeOrder(CompleteOrderDetails requestS){

        for (int i=0;i< addedOrders.size();i++){
            if (addedOrders.get(i).getOrderId().equals(requestS.getOrderId())){
                addedOrders.remove(i);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.continueBill:
                continueBill();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int totalOfAllOrders(){
        float total = 0;
        orderIds = "";
        for (CompleteOrderDetails requestS1 : addedOrders){
            if (!orderIds.contains(requestS1.getOrderId())) {
                total = total + Float.valueOf(requestS1.getOrderCost());
                customeId = requestS1.getCustomerId();
                orderIds = orderIds.isEmpty() ? requestS1.getOrderId() : orderIds + "," + requestS1.getOrderId();
            }
        }
        return (int) total;
    }

    void continueBill(){
        if (!addedOrders.isEmpty()) {
            Intent intent = new Intent(this, BillDescription.class);
            intent.putExtra("outstanding", totalOfAllOrders());
            intent.putExtra("customerId", customeId);
            intent.putExtra("orderId", orderIds);
            intent.putExtra("fromDate", buttonDatePick.getText().toString());
            intent.putExtra("toDate", buttonDatePickEnd.getText().toString());
            startActivityForResult(intent, 444);
        }
    }

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(BillListActivity.this);

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
                        customeId = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){

                            if (userInfo1.getUserRoleId().equalsIgnoreCase("4")) {
                                userNames.add(userInfo1.getFirstName());
                                userIdList.add(userInfo1.getUserId());
                            }

                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(BillListActivity.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(BillListActivity.this);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Customer</font>"));

                            //First Step: convert ArrayList to an Object array.
                            Object[] objNames = userNames.toArray();

                            //Second Step: convert Object array to String array

                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                            int checkedItem = 0;
                                customeId = userIdList.get(checkedItem);
                                strCustName = userNames.get(checkedItem);
                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // user checked an item
                                        customeId = userIdList.get(which);
                                        strCustName = userNames.get(which);
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
                                    customerText.setText(strCustName );
                                    //
                                    customerStrName = strCustName;
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
                Toast.makeText(BillListActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }

}

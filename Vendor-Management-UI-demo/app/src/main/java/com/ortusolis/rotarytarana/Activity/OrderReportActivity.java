package com.ortusolis.rotarytarana.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.OrderReportAdapter;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.OrderReportResponse;
import com.ortusolis.rotarytarana.pojo.UserInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderReportActivity extends AppCompatActivity {

    Button buttonDatePick,buttonDatePickEnd,getReport;
    RecyclerView recyclerView;
    Calendar newCalendar;
    Calendar newCalendarEnd;
    DatePickerDialog startTime,endTime;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    OrderReportAdapter orderReportAdapter;
    Toolbar mToolbar;
    ActionBar action;
    Spinner selectStatus;
    ArrayList<String> statusList;
    LinearLayout distributorCodeLayout;
    String distributorStr = "";
    TextView distributorText;
    ProgressDialog progressDialog;
    String strDistName = null;
    String strDist = null;
    List<String> userNames, userIdList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report_actual);
        recyclerView = findViewById(R.id.recyclerView);
        buttonDatePick = findViewById(R.id.buttonDatePick);
        buttonDatePickEnd = findViewById(R.id.buttonDateEndPick);
        getReport = findViewById(R.id.getReport);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        selectStatus = findViewById(R.id.selectStatus);
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
            toolbarTitle.setText("Order Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderReportActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(OrderReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendarEnd = Calendar.getInstance();
        endTime = new DatePickerDialog(OrderReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                updateStatus();
            }
        });

        statusList = new ArrayList<>();
        statusList.add("Select Status");
        if (!(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3"))) {
            statusList.add("New");
        }
        statusList.add("Close");
        statusList.add("Assigned");
        if (!(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3"))) {
            statusList.add("Cancel");
        }

        ArrayAdapter userAdapter = new ArrayAdapter(OrderReportActivity.this, android.R.layout.simple_spinner_dropdown_item, statusList);

        selectStatus.setAdapter(userAdapter);

        selectStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
                progressDialog = new ProgressDialog(OrderReportActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegister();
            }
        });

    }

    public void updateStatus(){
        if (selectStatus.getSelectedItemPosition()!=0) {
            getListOfOrderByDate(statusList.get(selectStatus.getSelectedItemPosition()).equalsIgnoreCase("close")?"close":statusList.get(selectStatus.getSelectedItemPosition()));
        }
    }

    void getListOfOrderByDate(String status){
        progressDialog = new ProgressDialog(OrderReportActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(OrderReportActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            String userRole = "";
            String usserId = "";
            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                userRole = "Distributor";
                usserId = sharedPreferences.getString("userid", "");
            }
            else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
                userRole = "Distributor";
                usserId = sharedPreferences.getString("userid", "");
            }
            else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
                userRole = "Employee";
                usserId = sharedPreferences.getString("userid", "");
            }
            else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
                userRole = "Customer";
                usserId = sharedPreferences.getString("userid", "");
            }

            if (usserId.isEmpty()){
                Toast.makeText(this, "Select Facilitator", Toast.LENGTH_LONG).show();
                return;
            }

            jsonObject.put("status", status);
            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
                jsonObject.put("userId", "1");
            }else {
                jsonObject.put("userId", usserId);
            }

            jsonObject.put("role", userRole);
            jsonObject.put("startDate", buttonDatePick.getText().toString());
            jsonObject.put("endDate", buttonDatePickEnd.getText().toString());

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (orderReportAdapter!=null){
            orderReportAdapter.notifyDataSetChanged();
        }

        wss.postLoginVolley(Constants.getListOfOrderByDate, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);
                    progressDialog.dismiss();
                    OrderReportResponse responseData = gson.fromJson(response, OrderReportResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getCompleteOrderDetails()!=null && !responseData.getResponseData().getCompleteOrderDetails().isEmpty()) {
                            noResult.setVisibility(View.GONE);
                            orderReportAdapter = new OrderReportAdapter(OrderReportActivity.this, responseData.getResponseData().getCompleteOrderDetails(), statusList.get(selectStatus.getSelectedItemPosition()),false);
                            recyclerView.setAdapter(orderReportAdapter);
                            orderReportAdapter.notifyDataSetChanged();
                        }
                        else {
                            noResult.setVisibility(View.VISIBLE);
                            if (orderReportAdapter!=null)
                                orderReportAdapter.clearAll();
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        if (orderReportAdapter!=null)
                        orderReportAdapter.clearAll();
                        Toast.makeText(OrderReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    if (orderReportAdapter!=null)
                    orderReportAdapter.clearAll();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                if (orderReportAdapter!=null)
                orderReportAdapter.clearAll();
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

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(OrderReportActivity.this);

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
                            Toast.makeText(OrderReportActivity.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(OrderReportActivity.this);
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

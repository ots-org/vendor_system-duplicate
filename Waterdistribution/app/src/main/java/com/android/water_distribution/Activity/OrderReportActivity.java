package com.android.water_distribution.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.adapter.OrderReportAdapter;
import com.android.water_distribution.pojo.OrderReportResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();

        setSupportActionBar(mToolbar);

        gson = new Gson();

        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);
            //action.setTitle("Stock");

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
                if (selectStatus.getSelectedItemPosition()!=0) {
                    getListOfOrderByDate(statusList.get(selectStatus.getSelectedItemPosition()).equalsIgnoreCase("close")?"close":statusList.get(selectStatus.getSelectedItemPosition()));
                }
            }
        });

        statusList = new ArrayList<>();
        statusList.add("Select Status");
        statusList.add("New");
        statusList.add("Close");
        statusList.add("Generated");
        statusList.add("Assigned");
        statusList.add("Cancel");

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

    }

    void getListOfOrderByDate(String status){

        WebserviceController wss = new WebserviceController(OrderReportActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            String userRole = "";
            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
                userRole = "Distributor";
            }
            else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
                userRole = "Employee";
            }
            else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
                userRole = "Customer";
            }

            jsonObject.put("status", status);
            jsonObject.put("userId", sharedPreferences.getString("userid", ""));
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

                    OrderReportResponse responseData = gson.fromJson(response, OrderReportResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getCompleteOrderDetails()!=null) {
                            noResult.setVisibility(View.GONE);
                            orderReportAdapter = new OrderReportAdapter(OrderReportActivity.this, responseData.getResponseData().getCompleteOrderDetails());
                            recyclerView.setAdapter(orderReportAdapter);
                            orderReportAdapter.notifyDataSetChanged();
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        if (orderReportAdapter!=null)
                        orderReportAdapter.clearAll();
                        Toast.makeText(OrderReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
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
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                if (orderReportAdapter!=null)
                orderReportAdapter.clearAll();
                Toast.makeText(OrderReportActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
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


}

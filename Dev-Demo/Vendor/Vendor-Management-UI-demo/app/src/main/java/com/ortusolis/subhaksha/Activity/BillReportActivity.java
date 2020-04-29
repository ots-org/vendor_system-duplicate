package com.ortusolis.subhaksha.Activity;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.adapter.BillReportAdapter;
import com.ortusolis.subhaksha.pojo.BillResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BillReportActivity extends AppCompatActivity {

    Button buttonDatePick,buttonDatePickEnd,getReport;
    RecyclerView recyclerView;
    Calendar newCalendar;
    Calendar newCalendarEnd;
    DatePickerDialog startTime,endTime;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    BillReportAdapter billReportAdapter;
    Toolbar mToolbar;
    ActionBar action;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /***
     * Not used Activity
      */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);
        recyclerView = findViewById(R.id.recyclerView);
        buttonDatePick = findViewById(R.id.buttonDatePick);
        buttonDatePickEnd = findViewById(R.id.buttonDateEndPick);
        getReport = findViewById(R.id.getReport);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
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
            toolbarTitle.setText("Bill Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(BillReportActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(BillReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendarEnd = Calendar.getInstance();
        endTime = new DatePickerDialog(BillReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    }

    void getListOfOrderByDate(){

        WebserviceController wss = new WebserviceController(BillReportActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("fromDate", buttonDatePick.getText().toString());
            jsonObject.put("toDate", buttonDatePickEnd.getText().toString());

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (billReportAdapter!=null){
            billReportAdapter.notifyDataSetChanged();
        }

        wss.postLoginVolley(Constants.getBillReportByDate, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    BillResponse responseData = gson.fromJson(response, BillResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getBillNumber()!=null) {
                            noResult.setVisibility(View.GONE);
                            billReportAdapter = new BillReportAdapter(BillReportActivity.this, responseData.getResponseData().getBillNumber());
                            recyclerView.setAdapter(billReportAdapter);
                            billReportAdapter.notifyDataSetChanged();
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        if (billReportAdapter!=null)
                            billReportAdapter.clearAll();
                        Toast.makeText(BillReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    noResult.setVisibility(View.VISIBLE);
                    if (billReportAdapter!=null)
                        billReportAdapter.clearAll();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                if (billReportAdapter!=null)
                    billReportAdapter.clearAll();
                Toast.makeText(BillReportActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
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

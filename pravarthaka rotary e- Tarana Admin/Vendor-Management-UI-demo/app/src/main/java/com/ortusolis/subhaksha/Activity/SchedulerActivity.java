package com.ortusolis.subhaksha.Activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.ortusolis.subhaksha.adapter.SchedulerListAdapter;
import com.ortusolis.subhaksha.pojo.SchedulerResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SchedulerActivity extends AppCompatActivity {

    Button buttonDatePick;
    Calendar newCalendar;
    DatePickerDialog startTime;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    SchedulerListAdapter schedulerListAdapter;
    RecyclerView recyclerView;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_report);
        recyclerView = findViewById(R.id.recyclerView);
        buttonDatePick = findViewById(R.id.buttonDatePick);
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
            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("My Schedules");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(SchedulerActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(SchedulerActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);
                getListOfOrderByDate();

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        buttonDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime.show();

            }
        });

    }

    void getListOfOrderByDate(){

        WebserviceController wss = new WebserviceController(SchedulerActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("date", buttonDatePick.getText().toString());
            jsonObject.put("distributorId", sharedPreferences.getString("userid", ""));
            jsonObject.put("status", "Active");

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getSchedulerByStatus, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    SchedulerResponse responseData = gson.fromJson(response, SchedulerResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        recyclerView.setBackgroundColor(Color.parseColor("#2b6fdd"));
                        if (responseData.getResponseData()!=null && responseData.getResponseData().getResponse()!=null) {
                            noResult.setVisibility(View.GONE);
                            schedulerListAdapter = new SchedulerListAdapter(SchedulerActivity.this, responseData.getResponseData().getResponse());
                            recyclerView.setAdapter(schedulerListAdapter);
                            schedulerListAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        if (schedulerListAdapter!=null)
                            schedulerListAdapter.clearAll();
                        Toast.makeText(SchedulerActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    if (schedulerListAdapter!=null)
                        schedulerListAdapter.clearAll();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                if (schedulerListAdapter!=null)
                    schedulerListAdapter.clearAll();
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

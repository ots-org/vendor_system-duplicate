package com.ortusolis.pravarthaka.Activity;

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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.pravarthaka.Interfaces.IClickInterface;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.adapter.OrderListAdapter;
import com.ortusolis.pravarthaka.pojo.OrderResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    OrderListAdapter ordersAdapter;
    List<OrderResponse.RequestS> data;
    TextView noResult;
    String assignRequest="no";
    List<String> donationStatus;
    Spinner spinnerStatus;
    String donationStatusName="";
    String roleId="";
    TextView toolbarTitle;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        sharedPreferences = getSharedPreferences("water_management",0);

        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        noResult = findViewById(R.id.noResult);

        data = new ArrayList<>();

        gson = new Gson();

        donationStatus = new ArrayList<>();
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        if (getIntent().hasExtra("assignRequest")){
            donationStatus.add("Select Request Status");
            donationStatus.add("New Request");
            donationStatus.add("Close Request");
        }else {
            spinnerStatus.setVisibility(View.GONE);
            getOrderByStatusAndDistributor();
        }


        spinnerStatus.setAdapter(new ArrayAdapter(OrderListActivity.this, android.R.layout.simple_spinner_dropdown_item, donationStatus));
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                recyclerView.setAdapter(null);
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        donationStatusName="NewRequest";
                        getOrderByStatusAndDistributor();
                        break;
                    case 2:
                        donationStatusName="CloseRequest";
                        getOrderByStatusAndDistributor();
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

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
            toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            if (getIntent().hasExtra("assignRequest")){
                toolbarTitle.setText("Assign Request");
                assignRequest="yes";
            }else  {
                toolbarTitle.setText("Assign Orders");
            }
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!donationStatusName.equals("")){
            if(assignRequest.equals("yes")){
                toolbarTitle.setText("Assign Request");
            }
            else {
                toolbarTitle.setText("Assign Orders");
            }
            getOrderByStatusAndDistributor();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200 && resultCode==RESULT_OK){
            onBackPressed();
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

    void getOrderByStatusAndDistributor(){


            WebserviceController wss = new WebserviceController(OrderListActivity.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                if(assignRequest.equals("yes")){
                    jsonObject.put("status", donationStatusName);
                }else {
                    jsonObject.put("status", "NEW");
                }

                jsonObject.put("distrubitorId","1");

                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.getOrderByStatusAndDistributor, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        OrderResponse responseData = gson.fromJson(response, OrderResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            data = responseData.getResponseData().getOrderList();

                            ordersAdapter = new OrderListAdapter(OrderListActivity.this,data, new IClickInterface() {
                                @Override
                                public void click(OrderResponse.RequestS ordersss) {

                                    Intent intent = new Intent(OrderListActivity.this, OrderDescription.class);
                                    if(assignRequest.equals("yes")){
                                        intent.putExtra("assignRequest", "assignRequest");
                                    }
                                    intent.putExtra("order", ordersss);
                                    startActivityForResult(intent, 200);

                                }
                            });

                            recyclerView.setAdapter(ordersAdapter);

                            ordersAdapter.notifyDataSetChanged();

                        }
                        else {
                            noResult.setVisibility(View.VISIBLE);
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

}

package com.android.water_distribution.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.Interfaces.IClickInterfaceAssigned;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.adapter.AssignedOrderReportAdapter;
import com.android.water_distribution.pojo.AssignedOrderModel;
import com.android.water_distribution.pojo.AssignedResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AssignedOrderListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    AssignedOrderReportAdapter assignedOrderReportAdapter;
    List<AssignedOrderModel> data;
    TextView noResult;

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

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(AssignedOrderListActivity.this);
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
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Sales Voucher");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        noResult.setText("No Orders");
        /*if (sharedPreferences.getBoolean("distributor",false)){
            distributorNotification.setVisibility(View.VISIBLE);
            customerNotification.setVisibility(View.GONE);
        }
        else {
            distributorNotification.setVisibility(View.GONE);
            customerNotification.setVisibility(View.VISIBLE);
        }

        if (distributorNotification.getVisibility()==View.VISIBLE){
            distributorNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
            });
        }

        distributor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, DistributorAct.class);
                startActivity(intent);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, CustomerAct.class);
                startActivity(intent);
            }
        };

        customer1.setOnClickListener(onClickListener);
        customer2.setOnClickListener(onClickListener);
        customer3.setOnClickListener(onClickListener);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAssginedOrder();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==3533 && resultCode == RESULT_OK){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getAssginedOrder();
                }
            },2000);
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

    void getAssginedOrder(){

            WebserviceController wss = new WebserviceController(AssignedOrderListActivity.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", "Assigned");
                jsonObject.put("employeeId", sharedPreferences.getString("userid", ""));

                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.getAssginedOrder, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        AssignedResponse responseData = gson.fromJson(response, AssignedResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            data = responseData.getResponseData().getOrderList();

                            assignedOrderReportAdapter = new AssignedOrderReportAdapter(AssignedOrderListActivity.this, data, new IClickInterfaceAssigned() {
                                @Override
                                public void click(AssignedOrderModel item) {
                                    Intent intent = new Intent(AssignedOrderListActivity.this, AssignedVoucherOrderDescription.class);
                                    intent.putExtra("order",item);
                                    startActivityForResult(intent,3533);
                                }
                            });
                            recyclerView.setAdapter(assignedOrderReportAdapter);

                            assignedOrderReportAdapter.notifyDataSetChanged();

                            if (responseData.getResponseData().getOrderList().isEmpty()){
                                noResult.setVisibility(View.VISIBLE);
                            }
                            else {
                                noResult.setVisibility(View.GONE);
                            }

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
                    Toast.makeText(AssignedOrderListActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
                }
            });

    }

}

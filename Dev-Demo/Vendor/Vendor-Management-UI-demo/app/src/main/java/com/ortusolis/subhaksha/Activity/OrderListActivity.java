package com.ortusolis.subhaksha.Activity;

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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.Interfaces.IClickInterface;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.adapter.OrderListAdapter;
import com.ortusolis.subhaksha.pojo.OrderResponse;
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
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Assign Orders");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderByStatusAndDistributor();
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
                jsonObject.put("status", "NEW");
                jsonObject.put("distrubitorId", sharedPreferences.getString("userid", ""));

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

                                    Intent intent = new Intent(OrderListActivity.this, OrderDescription.class);// OrderDescription AssignedVoucherOrderDescription
                                    intent.putExtra("order", ordersss);
                                    startActivityForResult(intent, 200);

                                }
                            });

                            recyclerView.setAdapter(ordersAdapter);

                            ordersAdapter.notifyDataSetChanged();

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
                    Toast.makeText(OrderListActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                }
            });

    }

}

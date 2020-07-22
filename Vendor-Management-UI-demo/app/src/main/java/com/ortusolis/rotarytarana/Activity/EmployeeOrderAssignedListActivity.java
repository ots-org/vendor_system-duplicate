package com.ortusolis.rotarytarana.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.OrderGridAdapter;
import com.ortusolis.rotarytarana.pojo.OrderResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EmployeeOrderAssignedListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    GridView gridview;
    OrderGridAdapter ordersAdapter;
    List<OrderResponse.RequestS> data;
    TextView noResult;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        sharedPreferences = getSharedPreferences("water_management",0);

        mToolbar = findViewById(R.id.toolbar);
        gridview = findViewById(R.id.gridview);
        noResult = findViewById(R.id.noResult);

        data = new ArrayList<>();

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
            toolbarTitle.setText("Assigned Orders");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(EmployeeOrderAssignedListActivity.this, AssignedOrderDescription.class);
               /* Pair[] views = new Pair[]{
                        new Pair<View, String>(picture, "object_image"),
                        new Pair<View, String>(name, "object_name"),
                        new Pair<View, String>(description, "object_description")
                };*/

                if (position%2==0){
                    intent.putExtra("price",30);
                }
                else {
                    intent.putExtra("price",20);
                }
                intent.putExtra("order",data.get(position));
                /*ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity)context, views);*/

                startActivityForResult(intent,200);
            }
        });

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
        if (requestCode==200 && resultCode == RESULT_OK){
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

    void getAssginedOrder(){

            WebserviceController wss = new WebserviceController(EmployeeOrderAssignedListActivity.this);

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

                        OrderResponse responseData = gson.fromJson(response, OrderResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            data = responseData.getResponseData().getOrderList();

                            ordersAdapter = new OrderGridAdapter(EmployeeOrderAssignedListActivity.this,data);
                            gridview.setAdapter(ordersAdapter);

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
//                    Toast.makeText(EmployeeOrderAssignedListActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                }
            });

    }

}

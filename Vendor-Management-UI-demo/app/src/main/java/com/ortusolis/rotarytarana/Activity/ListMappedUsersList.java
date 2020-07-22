package com.ortusolis.rotarytarana.Activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.UserAdapter;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ListMappedUsersList extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    String userNameStr = "";
    String userId = "";
    boolean customerBool = false;
    RecyclerView recyclerView;
    TextView noResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        noResult = findViewById(R.id.noResult);
        setSupportActionBar(mToolbar);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ListMappedUsersList.this);
        recyclerView.setLayoutManager(mLayoutManager);

        if (getIntent().hasExtra("customer") && getIntent().getBooleanExtra("customer",false)){
            customerBool = true;
        }

        if (getIntent().hasExtra("userId")){
            userId = getIntent().getStringExtra("userId");
        }

        if (getIntent().hasExtra("username")){
            userNameStr = getIntent().getStringExtra("username");
        }

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
            toolbarTitle.setText(userNameStr+"'s "+(customerBool?"Donor/Requester":"Partner"));
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        noResult.setText("No Users");
        getAllRegister();
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
        WebserviceController wss = new WebserviceController(ListMappedUsersList.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "UserRoleId");
            jsonObject.put("searchvalue", customerBool?"4":"3");
            jsonObject.put("distributorId", userId);

            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {

                    DistributorResponse distributorResponse = new Gson().fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        UserAdapter notificationAdapter = new UserAdapter(distributorResponse.getResponseData().getUserDetails(),ListMappedUsersList.this,false,false);
                        recyclerView.setAdapter(notificationAdapter);
                        noResult.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                        noResult.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                recyclerView.setVisibility(View.GONE);
                noResult.setVisibility(View.VISIBLE);
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });

    }



}

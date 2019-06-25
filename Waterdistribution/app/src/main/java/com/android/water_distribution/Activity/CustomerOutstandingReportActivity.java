package com.android.water_distribution.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.pojo.OutstandingResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerOutstandingReportActivity extends AppCompatActivity {

    LinearLayout addLL;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_outstanding_report);
        addLL = findViewById(R.id.addLL);
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
            toolbarTitle.setText("Customer Outstanding Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        getListOfOrderByDate();

    }

    void getListOfOrderByDate(){

        WebserviceController wss = new WebserviceController(CustomerOutstandingReportActivity.this);

        //JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("distributorId", sharedPreferences.getString("userid", ""));

            //requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }


        wss.postLoginVolley(Constants.getCustomerOutstandingData, jsonObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    OutstandingResponse responseData = gson.fromJson(response, OutstandingResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getCustomerOutstandingList()!=null) {
                            loadData(responseData.getResponseData().getCustomerOutstandingList());
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        Toast.makeText(CustomerOutstandingReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                Toast.makeText(CustomerOutstandingReportActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
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

    void loadData(List<OutstandingResponse.RequestS> requestS){
        addLL.removeAllViews();
        if (!requestS.isEmpty()) {
            addRow(null, 0);
            int i = 0;
            for (OutstandingResponse.RequestS requestSdata : requestS) {
                i = i + 1;
                addRow(requestSdata, i);

            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(OutstandingResponse.RequestS requestSdata, int pos){

        if (requestSdata!=null){

            String customerStr = "";

            for (OutstandingResponse.RequestS.ProductBalanceCan productBalanceCan : requestSdata.getBalanceCan()){

                View view = getLayoutInflater().inflate(R.layout.view_customer_outstanding,null);

                TextView customerName = view.findViewById(R.id.customerName);
                TextView productName = view.findViewById(R.id.productName);
                TextView oustandingAmount = view.findViewById(R.id.oustandingAmount);
                TextView outstandingCan = view.findViewById(R.id.outstandingCan);


                productName.setText(productBalanceCan.getProductName());

                outstandingCan.setText(productBalanceCan.getBalanceCan());
                outstandingCan.setGravity(Gravity.RIGHT);

                if (requestSdata.getCustomerId()==null || requestSdata.getCustomerId().equalsIgnoreCase(customerStr)) {
                    customerName.setText("");
                    oustandingAmount.setText("");
                } else {
                    customerStr = requestSdata.getCustomerId();
                    customerName.setText(requestSdata.getCustomerName());
                    oustandingAmount.setText(requestSdata.getOutstandingAmount());
                    oustandingAmount.setGravity(Gravity.RIGHT);
                }

                addLL.addView(view);
            }

        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_customer_outstanding,null);

            addLL.addView(view);
        }

    }

}

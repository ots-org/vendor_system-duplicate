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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.adapter.ProductStockAdapter;
import com.android.water_distribution.pojo.OrderResponse;
import com.android.water_distribution.pojo.ProductDetails;
import com.android.water_distribution.pojo.ProductStockResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductTransactionReportActivity extends AppCompatActivity {

    Button buttonDatePick;
    LinearLayout addLL;
    Calendar newCalendar;
    DatePickerDialog startTime;
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
        setContentView(R.layout.activity_product_transaction_report);
        addLL = findViewById(R.id.addLL);
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
            //action.setTitle("Stock");

            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Product Transaction Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(ProductTransactionReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText("Selected Date ("+ dateStr+")");

                getProductStockList(dateStr);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        buttonDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime.show();
            }
        });

    }

    void getProductStockList(String dateString){

        WebserviceController wss = new WebserviceController(ProductTransactionReportActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") ? sharedPreferences.getString("userid","") : sharedPreferences.getString("distId","")));
            jsonObject.put("todaysDate", dateString);

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getProductStockList, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    ProductStockResponse responseData = gson.fromJson(response, ProductStockResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getProductStockDetail()!=null) {
                            noResult.setVisibility(View.GONE);
                            loadData(responseData.getResponseData().getProductStockDetail());
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        addLL.removeAllViews();
                        Toast.makeText(ProductTransactionReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    addLL.removeAllViews();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                addLL.removeAllViews();
                Toast.makeText(ProductTransactionReportActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
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

    void loadData(ArrayList<ProductDetails> requestS){
        addLL.removeAllViews();
        if (!requestS.isEmpty()) {
            addRow(null);

            for (ProductDetails requestSdata : requestS) {

                addRow(requestSdata);

            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(ProductDetails requestSdata){


        if (requestSdata!=null){

                View view = getLayoutInflater().inflate(R.layout.view_product_transaction,null);

                TextView name = (TextView) view.findViewById(R.id.title);
                TextView openBalance = (TextView) view.findViewById(R.id.openBalance);
                TextView stockAddition = (TextView) view.findViewById(R.id.stockAddition);
                TextView orderDelivered = (TextView) view.findViewById(R.id.orderDelivered);
                TextView closingBalance = (TextView) view.findViewById(R.id.closingBalance);

                name.setText(requestSdata.getProductName());
                openBalance.setText(requestSdata.getOtsProductOpenBalance());
                openBalance.setGravity(Gravity.RIGHT);
                stockAddition.setText(requestSdata.getOtsProductStockAddition());
                stockAddition.setGravity(Gravity.RIGHT);
                orderDelivered.setText(requestSdata.getOtsProductOrderDelivered());
                orderDelivered.setGravity(Gravity.RIGHT);

                Integer closingBal = (Integer.valueOf(requestSdata.getOtsProductOpenBalance())
                        +Integer.valueOf(requestSdata.getOtsProductStockAddition()))
                        -Integer.valueOf(requestSdata.getOtsProductOrderDelivered());

                closingBalance.setText(closingBal+"");
                closingBalance.setGravity(Gravity.RIGHT);

                addLL.addView(view);
        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_product_transaction,null);

            addLL.addView(view);
        }
    }

}

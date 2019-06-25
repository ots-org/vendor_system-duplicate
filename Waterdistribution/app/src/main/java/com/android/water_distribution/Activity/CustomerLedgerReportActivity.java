package com.android.water_distribution.Activity;

import android.app.DatePickerDialog;
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
import com.android.water_distribution.pojo.OrderResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerLedgerReportActivity extends AppCompatActivity {

    Button buttonDatePick,buttonDatePickEnd,getReport;
    LinearLayout addLL;
    Calendar newCalendar;
    Calendar newCalendarEnd;
    DatePickerDialog startTime,endTime;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    int slNumber = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ledger_report);
        addLL = findViewById(R.id.addLL);
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
            toolbarTitle.setText("Customer Ledger Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(CustomerLedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText(dateStr);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendarEnd = Calendar.getInstance();
        endTime = new DatePickerDialog(CustomerLedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        WebserviceController wss = new WebserviceController(CustomerLedgerReportActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("distributorsId", sharedPreferences.getString("userid", ""));
            jsonObject.put("status", "close");
            jsonObject.put("fromTime", buttonDatePick.getText().toString());
            jsonObject.put("toTime", buttonDatePickEnd.getText().toString());

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }


        wss.postLoginVolley(Constants.orderReportByDate, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    OrderResponse responseData = gson.fromJson(response, OrderResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (responseData.getResponseData()!=null && responseData.getResponseData().getOrderList()!=null) {
                            loadData(responseData.getResponseData().getOrderList());
                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        Toast.makeText(CustomerLedgerReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CustomerLedgerReportActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
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

    void loadData(List<OrderResponse.RequestS> requestS){
        addLL.removeAllViews();
        slNumber = 0;
        if (!requestS.isEmpty()) {
            addRow(null);

            for (OrderResponse.RequestS requestSdata : requestS) {

                addRow(requestSdata);

            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(OrderResponse.RequestS requestSdata){


        if (requestSdata!=null){

            String orderIDRec = "";
            String amtRec = "";
            String outStd = "";
            String orderCo = "";

            for (OrderResponse.RequestS.ProductOrder productOrder : requestSdata.getOrderdProducts()) {
                slNumber = slNumber+1;

                View view = getLayoutInflater().inflate(R.layout.view_customer_ledger,null);

                TextView slNo = view.findViewById(R.id.slNo);
                TextView customerName = view.findViewById(R.id.customerName);
                TextView orderNo = view.findViewById(R.id.orderNo);
                TextView deliveredDate = view.findViewById(R.id.deliveredDate);
                TextView productName = view.findViewById(R.id.productName);
                TextView orderedQty = view.findViewById(R.id.orderedQty);
                TextView deliveredQty = view.findViewById(R.id.deliveredQty);
                TextView emptyCanRecieved = view.findViewById(R.id.emptyCanRecieved);
                TextView balanceCan = view.findViewById(R.id.balanceCan);
                TextView productCost = view.findViewById(R.id.productCost);
                TextView cashRecieved = view.findViewById(R.id.cashRecieved);
                TextView amountOutstanding = view.findViewById(R.id.amountOutstanding);

                slNo.setText(slNumber + "");
                customerName.setText(requestSdata.getCustomerDetails().getFirstName());

                productName.setText(productOrder.getProductName());
                orderedQty.setText(productOrder.getOtsOrderedQty());
                orderedQty.setGravity(Gravity.RIGHT);

                deliveredQty.setText(productOrder.getOtsDeliveredQty());
                deliveredQty.setGravity(Gravity.RIGHT);
                emptyCanRecieved.setText(productOrder.getEmptyCanRecived());
                emptyCanRecieved.setGravity(Gravity.RIGHT);
                balanceCan.setText(productOrder.getBalanceCan());
                balanceCan.setGravity(Gravity.RIGHT);

                    if (requestSdata.getOrderNumber()==null || requestSdata.getOrderNumber().equalsIgnoreCase(orderIDRec)) {
                        orderNo.setText("");
                        deliveredDate.setText("");
                    } else {
                        orderIDRec = requestSdata.getOrderNumber();
                        orderCo = requestSdata.getOrderCost();
                        orderNo.setText(orderIDRec);
                        orderNo.setGravity(Gravity.RIGHT);

                        productCost.setText(orderCo);
                        productCost.setGravity(Gravity.RIGHT);
                        deliveredDate.setText(requestSdata.getDelivaredDate());

                    }

                    if (requestSdata.getAmountRecived()==null || requestSdata.getAmountRecived().equalsIgnoreCase(amtRec)) {
                        cashRecieved.setText("");
                    } else {
                        amtRec = requestSdata.getAmountRecived();
                        cashRecieved.setText(amtRec);
                        cashRecieved.setGravity(Gravity.RIGHT);
                    }

                    if (requestSdata.getOrderOutStanding()==null || requestSdata.getOrderOutStanding().equalsIgnoreCase(outStd)) {
                        amountOutstanding.setText("");
                    } else {
                        outStd = requestSdata.getOrderOutStanding();
                        amountOutstanding.setText(outStd);
                        amountOutstanding.setGravity(Gravity.RIGHT);
                    }
                addLL.addView(view);
            }

        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_customer_ledger,null);

            addLL.addView(view);
        }
    }

}

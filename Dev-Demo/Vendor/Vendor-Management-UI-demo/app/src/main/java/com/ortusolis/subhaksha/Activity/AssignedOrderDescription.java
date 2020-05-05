package com.ortusolis.subhaksha.Activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.pojo.OrderResponse;
import com.ortusolis.subhaksha.pojo.RegisterResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AssignedOrderDescription extends AppCompatActivity {

    TextView orderId,deliveryTime;
    Button closeOrder;

    Toolbar mToolbar;
    ActionBar action;

    int price = 0;
    OrderResponse.RequestS orderDetails;
    SharedPreferences sharedPreferences;
    Gson gson;
    String selectDateStr = null;
    Calendar newCalendar;
    DatePickerDialog startTime;
    SimpleDateFormat format;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_assign_description);
        closeOrder = findViewById(R.id.closeOrder);
        gson = new Gson()/*new GsonBuilder().serializeNulls().create()*/;

        mToolbar = findViewById(R.id.toolbar);
        orderId = findViewById(R.id.orderId);
        deliveryTime = findViewById(R.id.deliveryTime);
        format = new SimpleDateFormat("yyyy-MM-dd");

        sharedPreferences = getSharedPreferences("water_management",0);

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
            toolbarTitle.setText(getString(R.string.assignedOrder));
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (getIntent().hasExtra("order")){
            orderDetails = getIntent().getExtras().getParcelable("order");
        }

        setValues();

        closeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignOrder(orderDetails);
            }
        });

        deliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startTime = new DatePickerDialog(AssignedOrderDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        selectDateStr = format.format(newCalendar.getTime());

                        deliveryTime.setText( getString(R.string.selectedDate)+" ("+ selectDateStr+")");

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startTime.show();
            }
        });

        String currentDateStr = format.format(Calendar.getInstance().getTime());
        deliveryTime.setText(getString(R.string.selectedDate)+" ("+ currentDateStr+")");

    }

     void setValues(){
         orderId.setText(!orderDetails.getOrderId().isEmpty()? orderDetails.getOrderId():"");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void assignOrder(OrderResponse.RequestS data){

        if (selectDateStr!=null) {

            WebserviceController wss = new WebserviceController(AssignedOrderDescription.this);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", data.getOrderId());
                jsonObject.put("orderStatus", "Close");
                jsonObject.put("deliveredDate", selectDateStr);

                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.closeOrder, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        RegisterResponse responseData = new Gson().fromJson(response, RegisterResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            setResult(RESULT_OK);
                            finish();
                            Toast.makeText(AssignedOrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? " " : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(AssignedOrderDescription.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed to assign" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(AssignedOrderDescription.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                }
            });

        }
        else {
            Toast.makeText(this, getString(R.string.selectDeliveryDate), Toast.LENGTH_LONG).show();
        }
    }


}

package com.ortusolis.subhaksha.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.Utility.CommonFunctions;
import com.ortusolis.subhaksha.adapter.AssignedOrderGridAdapter;
import com.ortusolis.subhaksha.pojo.OrderResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MybillsDescription extends AppCompatActivity {

    TextView textName,orderDate,deliveryDate,orderCost,orderStatus,customerName,amountrecieved,outstandingbalance;

    Toolbar mToolbar;
    ActionBar action;

    int price = 0;
    OrderResponse.RequestS orderDetails;
    SharedPreferences sharedPreferences;
    Gson gson;
    String distributorStr = null;
    List<String> userNames, userIdList;
    String strDistName = null;
    String strDist = null;
    GridView gridview;
    TextView noResult;
    AssignedOrderGridAdapter assignedOrderGridAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybill_detail);
        gson = new Gson()/*new GsonBuilder().serializeNulls().create()*/;

        mToolbar = findViewById(R.id.toolbar);
        textName = findViewById(R.id.productName);
        orderDate = findViewById(R.id.orderDate);
        deliveryDate = findViewById(R.id.deliveryDate);
        orderCost = findViewById(R.id.orderCost);
        orderStatus = findViewById(R.id.orderStatus);
        customerName = findViewById(R.id.customerName);
        //code by raghu
        amountrecieved=findViewById(R.id.amountRecieved);
        outstandingbalance = findViewById(R.id.outstandingbalance);
        //code by raghu
        gridview = findViewById(R.id.gridview);
        noResult = findViewById(R.id.noResult);

        sharedPreferences = getSharedPreferences("water_management",0);

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

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
            toolbarTitle.setText("My Orders");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (getIntent().hasExtra("order")){
            orderDetails = getIntent().getExtras().getParcelable("order");
        }

        setValues();

    }//

     void setValues(){
         textName.setText(orderDetails.getOrderId());
         orderDate.setText(CommonFunctions.converDateStr(orderDetails.getOrderDate()));
         deliveryDate.setText(CommonFunctions.converDateStr(orderDetails.getDelivaryDate()));

         orderCost.setText(getString(R.string.Rs)+orderDetails.getOrderCost());
         orderStatus.setText(orderDetails.getOrderStatus());
         customerName.setText(orderDetails.getCustomerDetails().getFirstName());
         //code raghuram ots
         amountrecieved.setText(orderDetails.getAmountRecived());
         outstandingbalance.setText(orderDetails.getOutStandingAmount());
         //code raghuram ots
         assignedOrderGridAdapter = new AssignedOrderGridAdapter(MybillsDescription.this,orderDetails.getOrderdProducts());
         gridview.setAdapter(assignedOrderGridAdapter);

         assignedOrderGridAdapter.notifyDataSetChanged();
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

}

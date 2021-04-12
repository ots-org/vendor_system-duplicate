package com.ortusolis.rotarytarana.Activity;

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

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.ProductDetailAdapter;
import com.ortusolis.rotarytarana.pojo.OrderResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDetailReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayout headerLL;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    ProductDetailAdapter productDetailAdapter;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_report);
        recyclerView = findViewById(R.id.recyclerView);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        headerLL = findViewById(R.id.headerLL);
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();
        setSupportActionBar(mToolbar);

        gson = new Gson();

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
            toolbarTitle.setText("Product Details");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ProductDetailReportActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        if (getIntent().getExtras().containsKey("prods")){
            ArrayList<OrderResponse.RequestS.ProductOrder> dataList = getIntent().getParcelableArrayListExtra("prods");
            headerLL.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
            productDetailAdapter = new ProductDetailAdapter(ProductDetailReportActivity.this, dataList);
            recyclerView.setAdapter(productDetailAdapter);
            productDetailAdapter.notifyDataSetChanged();
        }
        else {
            noResult.setVisibility(View.VISIBLE);
            headerLL.setVisibility(View.GONE);
            if (productDetailAdapter!=null)
                productDetailAdapter.clearAll();
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


}

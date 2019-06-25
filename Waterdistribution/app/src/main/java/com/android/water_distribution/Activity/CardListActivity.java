package com.android.water_distribution.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.adapter.CartListAdapter;
import com.android.water_distribution.pojo.DistributorResponse;
import com.android.water_distribution.pojo.ProductRequest;
import com.android.water_distribution.pojo.ProductRequestCart;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CardListActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    CartListAdapter cartListAdapter;
    TextView noResult;
    String customeId = "";
    String orderIds = "";
    List<ProductRequestCart> productRequests;
    Button checkout;
    ProgressDialog progressDialog;
    ProductRequest.RequestS requestS;
    ArrayList<ProductRequest.RequestS.ProductOrder> productOrders;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("water_management",0);

        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        noResult = findViewById(R.id.noResult);
        checkout = findViewById(R.id.checkout);

        productRequests = new ArrayList<>();

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
            toolbarTitle.setText("Cart");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CardListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        productRequests = gson.fromJson(sharedPreferences.getString("prodDesc",""), new TypeToken<ArrayList<ProductRequestCart>>(){}.getType());

        noResult.setText("No Items in Cart");

        if (productRequests == null || productRequests.isEmpty()) {
            noResult.setVisibility(View.VISIBLE);
            checkout.setVisibility(View.GONE);
        }
        else {
            cartListAdapter = new CartListAdapter(CardListActivity.this,productRequests);
            recyclerView.setAdapter(cartListAdapter);
            noResult.setVisibility(View.GONE);
            checkout.setVisibility(View.VISIBLE);
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!productRequests.isEmpty()){

                    progressDialog = new ProgressDialog(CardListActivity.this);
                    progressDialog.setMessage("Placing Order..");
                    progressDialog.show();

                    requestS =  new ProductRequest.RequestS();

                    requestS.setCustomerId(productRequests.get(0).getCustomerId());
                    requestS.setCustomerName(productRequests.get(0).getCustomerName());
                    requestS.setOrderDate(productRequests.get(0).getOrderDate());
                    requestS.setDelivaryDate(productRequests.get(0).getDelivaryDate());
                    requestS.setDistributorId(productRequests.get(0).getDistributorId());
                    requestS.setOrderCost(productRequests.get(0).getOrderCost());
                    requestS.setAssignedId(productRequests.get(0).getAssignedId());
                    requestS.setOrderStatus(productRequests.get(0).getOrderStatus());
                    requestS.setOrderNumber(productRequests.get(0).getOrderNumber());
                    requestS.setDeliverdDate(productRequests.get(0).getDeliverdDate());

                    placeOrder(prepareOrder(productRequests));
                }

            }
        });

    }

    ProductRequest prepareOrder(List<ProductRequestCart> productRequestCartList){
        ProductRequest productRequest = new ProductRequest();

        if (productOrders==null){
            productOrders = new ArrayList<>();
        }

        for (ProductRequestCart productRequestCart : productRequestCartList) {
            ProductRequest.RequestS.ProductOrder productOrder = new ProductRequest.RequestS.ProductOrder();
            productOrder.setOts_delivered_qty(productRequestCart.getOts_delivered_qty());
            productOrder.setOrderProductId(productRequestCart.getOrderProductId());
            productOrder.setOrderdId(productRequestCart.getOrderdId());
            productOrder.setProductId(productRequestCart.getProductId());
            productOrder.setOrderedQty(productRequestCart.getOrderedQty());
            productOrder.setProductStatus(productRequestCart.getProductStatus());
            productOrder.setProductCost(productRequestCart.getProductCost());
            productOrders.add(productOrder);
        }
        requestS.setProductList(productOrders);

        productRequest.setRequest(requestS);

        return productRequest;
    }


    void placeOrder(final ProductRequest productRequest){

        WebserviceController wss = new WebserviceController(CardListActivity.this);

        String str = gson.toJson(productRequest);

        wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            if (progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
                            builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "")+"<br/><br/> Delivery date is "+productRequest.getRequest().getDelivaryDate()));
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().remove("prodDesc").apply();
                                }
                            });

                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    onBackPressed();
                                }
                            });
                            builder.show();

                    }else
                        Toast.makeText(CardListActivity.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(CardListActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getOrderByStatusAndDistributor();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==444&& resultCode==RESULT_OK){
            onBackPressed();
        }
    }

    public void deleteFormCart(){
        sharedPreferences.edit().remove("prodDesc").apply();

        if (cartListAdapter.getDataList().isEmpty()){
            noResult.setVisibility(View.VISIBLE);
            checkout.setVisibility(View.GONE);
        }
        else {
            noResult.setVisibility(View.GONE);
            checkout.setVisibility(View.VISIBLE);
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

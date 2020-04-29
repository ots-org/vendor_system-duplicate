package com.ortusolis.subhaksha.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.adapter.CartListAdapter;
import com.ortusolis.subhaksha.pojo.AssignedResponse;
import com.ortusolis.subhaksha.pojo.ProductRequest;
import com.ortusolis.subhaksha.pojo.ProductRequestCart;
import com.ortusolis.subhaksha.service.RazorPayActivity;

import org.json.JSONArray;
import org.json.JSONObject;

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
    //
    String paymentMethod="";
    float totalAmountPayment=0;
    float PaymentTotal=0;
    //
    String Flag="no";
    Integer Count=1;
    //
    public AssignedResponse CashResponseOnly;
    String distributorIdPaymentUpdate,delivaryDatePaymentUpdate,orderStatusPaymentUpdate,customerIdPaymentUpdate,orderIdPaymentUpdate,orderCostPaymentUpdate,orderDatePaymentUpdate,orderNumberPaymentUpdate;
    //

    List<ProductRequestCart> productRequests;
    Button checkout, salesVoucher;
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
        salesVoucher = findViewById(R.id.salesVoucher);

        productRequests = new ArrayList<>();
//
//        prepareOrder(productRequests);
        //
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
            salesVoucher.setVisibility(View.GONE);
        }
        else {
            cartListAdapter = new CartListAdapter(CardListActivity.this,productRequests);
            recyclerView.setAdapter(cartListAdapter);
            noResult.setVisibility(View.GONE);
            checkout.setVisibility(View.VISIBLE);
            salesVoucher.setVisibility(View.VISIBLE);
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
                    requestS.setDistributorId("2");
                    requestS.setAssignedId(productRequests.get(0).getAssignedId());
                    requestS.setOrderStatus(productRequests.get(0).getOrderStatus());
                    requestS.setOrderNumber(productRequests.get(0).getOrderNumber());
                    requestS.setDeliverdDate(productRequests.get(0).getDeliverdDate());

                    if(Count!=1){
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
                        builder.setMessage("Your Order is "+PaymentTotal+", click Confirm to place order")
                                .setCancelable(true)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        placeOrder(prepareOrder(productRequests),false);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else {
                        placeOrder(prepareOrder(productRequests),false);
                    }

//
                }

            }
        });

        salesVoucher.setOnClickListener(new View.OnClickListener() {
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
                    requestS.setAssignedId(sharedPreferences.getString("userid",""));
                    requestS.setOrderStatus("Assigned");
                    requestS.setOrderNumber(productRequests.get(0).getOrderNumber());
                    requestS.setDeliverdDate(productRequests.get(0).getDeliverdDate());

                    placeOrder(prepareOrder(productRequests),true);
                }

            }
        });

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
            salesVoucher.setVisibility(View.GONE);
        }

    }

    ProductRequest prepareOrder(List<ProductRequestCart> productRequestCartList){
        ProductRequest productRequest = new ProductRequest();

        if (productOrders==null){
            productOrders = new ArrayList<>();
        }
        if(Count!=1) {
        float totalCost = 0;
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
            totalCost = (Float.valueOf(productRequestCart.getOrderedQty()) * Float.valueOf(productRequestCart.getProductCost())) + totalCost;
            totalAmountPayment=totalCost;
        }
        requestS.setOrderCost((String.format("%.02f", totalCost))+"");
        requestS.setProductList(productOrders);
        PaymentTotal=totalCost;

            productRequest.setRequest(requestS);
        }else {
            float totalCost = 0;
            for (ProductRequestCart productRequestCart : productRequestCartList) {

                totalCost = (Float.valueOf(productRequestCart.getOrderedQty()) * Float.valueOf(productRequestCart.getProductCost())) + totalCost;
                PaymentTotal=totalCost;
            }
        }
        return productRequest;
    }


    void placeOrder(final ProductRequest productRequest, final boolean salesVoucher){
        Flag ="yes";
        if(Count==1){
            Count++;
            Alertpayment();
            return;
        }
        WebserviceController wss = new WebserviceController(CardListActivity.this);

        String str = gson.toJson(productRequest);

        wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                deleteFormCart();
                try {
                    Log.e("getPlants response", response);
                    //
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData  = obj.getJSONObject("responseData");
                    JSONArray orderList = responseData.getJSONArray("orderList");
                    distributorIdPaymentUpdate = orderList.getJSONObject(0).getString("distributorId");
                    delivaryDatePaymentUpdate = orderList.getJSONObject(0).getString("delivaryDate");
                    orderStatusPaymentUpdate = orderList.getJSONObject(0).getString("orderStatus");
                    customerIdPaymentUpdate = orderList.getJSONObject(0).getString("customerId");
                    orderIdPaymentUpdate = orderList.getJSONObject(0).getString("orderId");
                    orderCostPaymentUpdate = orderList.getJSONObject(0).getString("orderCost");
                    orderDatePaymentUpdate = orderList.getJSONObject(0).getString("orderDate");
                    orderNumberPaymentUpdate = orderList.getJSONObject(0).getString("orderNumber");
                    //
                    final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            if (progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                        if (salesVoucher) {
                            sharedPreferences.edit().remove("prodDesc").apply();
                            Intent intent = new Intent(CardListActivity.this, AssignedOrderListActivity.class);
                            if (!distributorResponse.getResponseData().getOrderList().isEmpty())
                                intent.putExtra("orderId",distributorResponse.getResponseData().getOrderList().get(0).getOrderId());
                            startActivity(intent);
                        }
                        else {

                       /*     AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
                            builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "") + "<br/><br/> Delivery date is " + productRequest.getRequest().getDelivaryDate()));
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().remove("prodDesc").apply();
                                }
                            });*/

                           /* builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    onBackPressed();
                                }
                            });
                            builder.show();*/
                            //
                            CashResponseOnly=distributorResponse;
                            Toast.makeText(CardListActivity.this, "SuccessFull", Toast.LENGTH_LONG).show();
//                            showAlertDialog(productRequest);
                            onBackPressed();
                            //
                        }

                    }else
                        Toast.makeText(CardListActivity.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();
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
                Toast.makeText(CardListActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    private void showAlertDialog(final ProductRequest productRequest) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CardListActivity.this);
        alertDialog.setTitle("Choose Payment Option");
        String[] items = {"Cash"};
        int checkedItem = 0;
        paymentMethod="";
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(CardListActivity.this, "Clicked on Cash", Toast.LENGTH_LONG).show();
                        paymentMethod="Cash";
                        break;
                    case 1:
                        Toast.makeText(CardListActivity.this, "Clicked on Payment", Toast.LENGTH_LONG).show();
                        paymentMethod="Payment";
                        break;
                }
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // navigation
                Toast.makeText(CardListActivity.this, paymentMethod, Toast.LENGTH_LONG).show();
               if (paymentMethod.equals("Payment")) {
                   /* //
                    //                    Intent myIntent = new Intent(CardListActivity.this, RazorPayActivity.class);
                    //                    myIntent.putExtra("totalAmountPayment", Float.toString(totalAmountPayment));
                    //                    startActivity(myIntent);
                    //
                    //                    //*/
//                    if (paymentMethod.equals("Payment")) {
                    Intent totalAmountPaymentintent = new Intent(CardListActivity.this, RazorPayActivity.class);
//                    // pass amount
                    totalAmountPaymentintent.putExtra("totalAmountPayment",totalAmountPayment );
                    totalAmountPaymentintent.putExtra("distributorIdPaymentUpdate",distributorIdPaymentUpdate );
                    totalAmountPaymentintent.putExtra("delivaryDatePaymentUpdate",delivaryDatePaymentUpdate );
                    totalAmountPaymentintent.putExtra("orderStatusPaymentUpdate",orderStatusPaymentUpdate );
                    totalAmountPaymentintent.putExtra("customerIdPaymentUpdate",customerIdPaymentUpdate );
                    totalAmountPaymentintent.putExtra("orderIdPaymentUpdate",orderIdPaymentUpdate );
                    totalAmountPaymentintent.putExtra("orderCostPaymentUpdate",orderCostPaymentUpdate );
                    totalAmountPaymentintent.putExtra("orderDatePaymentUpdate",orderDatePaymentUpdate );
                    totalAmountPaymentintent.putExtra("orderNumberPaymentUpdate",orderNumberPaymentUpdate );

                    startActivity(totalAmountPaymentintent);

//                    }
                }else {
                    dialog.dismiss();
                    CashAlert(productRequest);
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //

    //
    public  void  CashAlert(ProductRequest productRequest){
//        ProductRequest productRequest = new ProductRequest();
        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
//        builder.setMessage(Html.fromHtml((CashResponseOnly.getResponseDescription() != null ? CashResponseOnly.getResponseDescription() : "") + "<br/><br/> Delivery date is " + ""));
        builder.setMessage(Html.fromHtml((CashResponseOnly.getResponseDescription() != null ? CashResponseOnly.getResponseDescription() : "") + "<br/><br/> Delivery date is " + productRequest.getRequest().getDelivaryDate()));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onBackPressed();
            }
        });
        builder.show();
    }

//

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
    public  void Alertpayment(){
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
        builder.setMessage("Your Order is "+PaymentTotal+", click Confirm to place order")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        placeOrder(prepareOrder(productRequests),false);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

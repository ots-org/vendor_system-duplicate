package com.ortusolis.rotarytarana.Activity;

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
import android.widget.EditText;
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
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.CartListAdapter;
import com.ortusolis.rotarytarana.pojo.AssignedResponse;
import com.ortusolis.rotarytarana.pojo.ProductRequest;
import com.ortusolis.rotarytarana.pojo.ProductRequestCart;
import com.ortusolis.rotarytarana.service.RazorPayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
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
    String m_Text = "";
    String paymentMethod="";
    String delivaryAddress="";
    float totalAmountPayment=0;
    String Flag="no";
    String salesVaocherFalg="no";
    Integer Count=1;
    float PaymentTotal=0;
    public AssignedResponse CashResponseOnly;
    ProductRequest productRequestFinal;
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
        gson = new Gson();
        String item=sharedPreferences.getString("prodDesc","");
        Log.e("item",item);
        setSupportActionBar(mToolbar);

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
                    requestS =  new ProductRequest.RequestS();
                    requestS.setCustomerId(productRequests.get(0).getCustomerId());
                    requestS.setCustomerName(productRequests.get(0).getCustomerName());
                    requestS.setOrderDate(productRequests.get(0).getOrderDate());
                    requestS.setDelivaryDate(productRequests.get(0).getDelivaryDate());
                    requestS.setDistributorId(productRequests.get(0).getDistributorId());
                    requestS.setAssignedId(productRequests.get(0).getAssignedId());
                    requestS.setOrderNumber(productRequests.get(0).getOrderNumber());
                    requestS.setDeliverdDate(productRequests.get(0).getDeliverdDate());
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CardListActivity.this);
                    alertDialog.setTitle("Choose Delivery Address");
                    String[] items = {"Primary Address","Secondary Address","New Address"};
                    int checkedItem = 0;
                    delivaryAddress="Primary";
                    LinearLayout layout = new LinearLayout(CardListActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final TextView input = new TextView (CardListActivity.this);
                    input.setPadding(80, 20, 20, 20);
                    layout.addView(input);

                    final EditText Address_textbox = new EditText(CardListActivity.this);
                    layout.addView(Address_textbox);

                    alertDialog.setView(layout);
                    Address_textbox.setVisibility(View.GONE);
                    input.setText("Address: "+sharedPreferences.getString("userAddress1",""));
                    alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(CardListActivity.this, "Primary Address", Toast.LENGTH_LONG).show();
                                    delivaryAddress="Primary";
                                    input.setText("Address: "+sharedPreferences.getString("userAddress1",""));
                                    Address_textbox.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    Toast.makeText(CardListActivity.this, "Secondary Address", Toast.LENGTH_LONG).show();
                                    delivaryAddress="Secondary";
                                    input.setText("Address: "+sharedPreferences.getString("userAddress2",""));
                                    Address_textbox.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    delivaryAddress="New";
                                    input.setText("Enter New Address");
                                    Address_textbox.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    });
                    alertDialog.setNeutralButton("Profile", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CardListActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
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
                            if (delivaryAddress.equals("Primary")) {
                                input.setText(sharedPreferences.getString("userAddress1", ""));
                                requestS.setAddress(sharedPreferences.getString("userAddress1", ""));
                                requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile", ""));
                                requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile", ""));
                            }
//                            else {
//                                requestS.setAddress(sharedPreferences.getString("userAddress2",""));
//                                requestS.setUserLat(sharedPreferences.getString("userlatitudeSecondProfile",""));
//                                requestS.setUserLong(sharedPreferences.getString("userlangitudeSecondProfile",""));
//                            }
                            else if (delivaryAddress.equals("Secondary")) {
                                input.setText(sharedPreferences.getString("userAddress2", ""));
                                requestS.setAddress(sharedPreferences.getString("userAddress2", ""));
                                requestS.setUserLat(sharedPreferences.getString("userlatitudeSecondProfile", ""));
                                requestS.setUserLong(sharedPreferences.getString("userlangitudeSecondProfile", ""));
                            } else {
                                requestS.setAddress(Address_textbox.getText().toString());
                                requestS.setUserLat(sharedPreferences.getString("userlatitudeSecondProfile", ""));
                                requestS.setUserLong(sharedPreferences.getString("userlangitudeSecondProfile", ""));
                            }
                            dialog.dismiss();

                            if (sharedPreferences.contains("userSwitchRoleId") && (sharedPreferences.getString("userSwitchRoleId", "").equalsIgnoreCase("1")) || (sharedPreferences.getString("userSwitchRoleId", "").equalsIgnoreCase("2"))) {

                            AlertDialog.Builder alertDialogPay = new AlertDialog.Builder(CardListActivity.this);
                            alertDialogPay.setTitle("Choose Payment Option");
                            String[] items = {"Request", "Payment"};
                            int checkedItem = 1;
                            paymentMethod = "Payment";
                            alertDialogPay.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            paymentMethod = "Cash";
                                            break;
                                        case 1:
                                            paymentMethod = "Payment";
                                            break;
                                    }

                                }
                            });
                            alertDialogPay.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alertDialogPay.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // navigation
                                    PayON();
//                                    if(paymentMethod.equals("Cash")){
//                                        requestS.setPaymentStatus("newRequest");
//                                        requestS.setOrderStatus("newRequest");
//                                    }else {
//                                        requestS.setPaymentStatus("Paid");
//                                        requestS.setOrderStatus("New");
//                                    }
//                                    if(Count!=1){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
//                                        builder.setMessage("Your Order Cost is "+PaymentTotal+", click Confirm to place order")
//                                                .setCancelable(true)
//                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        //do things
//                                                        placeOrder(prepareOrder(productRequests),false);
//                                                    }
//                                                });
//                                        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Count=1;
//                                            }
//                                        });
//                                        AlertDialog alert = builder.create();
//                                        alert.setCanceledOnTouchOutside(false);
//                                        alert.show();
//                                    }else {
//                                        placeOrder(prepareOrder(productRequests),false);
//                                    }


                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertCash = alertDialogPay.create();
                            alertCash.setCanceledOnTouchOutside(false);
                            alertCash.show();
                        }else {
                                paymentMethod = "Payment";
                                PayON();
                            }
                        }
                    });
                    AlertDialog alertAddress = alertDialog.create();
                    alertAddress.setCanceledOnTouchOutside(false);
                    alertAddress.show();
                }
            }
        });

        salesVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!productRequests.isEmpty()){
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
                    salesVaocherFalg="yes";
                    if(Count!=1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
                        builder.setMessage("Your Order Cost is "+PaymentTotal+", click Confirm to place order")
                                .setCancelable(false)
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

        productRequest.setRequest(requestS);
        }else {
            float totalCost = 0;
            for (ProductRequestCart productRequestCart : productRequestCartList) {

                totalCost = (Float.valueOf(productRequestCart.getOrderedQty()) * Float.valueOf(productRequestCart.getProductCost())) + totalCost;
                PaymentTotal=totalCost;
                productRequestFinal=productRequest;
            }
        }
        return productRequest;
    }


    void placeOrder(final ProductRequest productRequest,  boolean salesVoucher){
        final boolean salesVoucherValue=salesVoucher;
        Flag ="yes";
        if(Count==1){
            Count++;
            Alertpayment();
            return;
        }else {
            String str = gson.toJson(productRequest);
            if (paymentMethod.equals("Cash")) {
                CashAlert(productRequest);
            } else {
                final String strProductRequests = str;
                onBackPressed();
                Intent totalAmountPaymentintent = new Intent(CardListActivity.this, RazorPayActivity.class);
//                Intent totalAmountPaymentintent = new Intent(CardListActivity.this, InitialScreenActivity.class);
                totalAmountPaymentintent.putExtra("totalAmountPayment", totalAmountPayment);
                totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
                totalAmountPaymentintent.putExtra("classFlag", "cart");
                totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
                startActivity(totalAmountPaymentintent);
            }
            return;
        }
    }

    //
    private void showAlertDialog(final ProductRequest productRequest,final String strProductRequest) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CardListActivity.this);
        alertDialog.setTitle("Choose Payment Option");
        String[] items = {"Cash","Payment"};
        int checkedItem = 1;
        paymentMethod="";
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        paymentMethod="Cash";
                        break;
                    case 1:
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
                if (paymentMethod.equals("Cash")) {
                    dialog.dismiss();
                    CashAlert(productRequest);
                } else {
                    final String strProductRequests = strProductRequest;

                        Intent totalAmountPaymentintent = new Intent(CardListActivity.this, RazorPayActivity.class);
                        totalAmountPaymentintent.putExtra("totalAmountPayment", totalAmountPayment);
                        totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
                        totalAmountPaymentintent.putExtra("classFlag", "cart");
                        totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
                        startActivity(totalAmountPaymentintent);
                    onBackPressed();
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public  void  CashAlert(final ProductRequest productRequest){

        WebserviceController wss = new WebserviceController(CardListActivity.this);

        String str = gson.toJson(productRequest);
        Log.e("cashrequest",str);

        wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        sharedPreferences.edit().remove("prodDesc").apply();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
                        builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "") + "<br/><br/> Delivery date is " + productRequest.getRequest().getDelivaryDate()));
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

                    else {
                        Toast.makeText(CardListActivity.this, distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "", Toast.LENGTH_LONG).show();
                    }
                    } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==444&& resultCode==RESULT_OK){
            onBackPressed();
        }
    }

    public  void deleteFormCart(){
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
        builder.setMessage("Your Order Cost is "+PaymentTotal+", click Confirm to place order")
                .setCancelable(false) //
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        placeOrder(prepareOrder(productRequests),false);
                    }
                });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Count=1;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deleteItemFromList( int position){
        String item=sharedPreferences.getString("prodDesc","");
        if(item != null){
            JSONArray completeArray = null;
            JSONArray finalArray = null;
            try {
                completeArray = new JSONArray(item);
                finalArray= new JSONArray();
                JSONObject finalEntry= null;
                for (int i=0;i<completeArray.length();i++){

                    JSONObject positionEntry= null;
                        finalEntry = (JSONObject) completeArray .get(i);
                        positionEntry= (JSONObject) completeArray .get(position);
                        if(finalEntry.getString("productId").equals(positionEntry.getString("productId"))){
                            completeArray.remove(i);
                        }else {
                    }
                }
                Log.e("finalEntry",gson.toJson(completeArray).toString());
                Log.e("finalEntry",gson.toJson(finalArray).toString());
                sharedPreferences.edit().putString("prodDesc",completeArray.toString()).apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void PayON(){
        if(paymentMethod.equals("Cash")){
            requestS.setPaymentStatus("newRequest");
            requestS.setOrderStatus("newRequest");
        }else {
            requestS.setPaymentStatus("Paid");
            requestS.setOrderStatus("New");
        }
        if(Count!=1){
            AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
            builder.setMessage("Your Order Cost is "+PaymentTotal+", click Confirm to place order")
                    .setCancelable(true)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            placeOrder(prepareOrder(productRequests),false);
                        }
                    });
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Count=1;
                }
            });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }else {
            placeOrder(prepareOrder(productRequests),false);
        }
    }
}

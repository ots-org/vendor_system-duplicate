package com.ortusolis.pravarthaka.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
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
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.adapter.CartListAdapter;
import com.ortusolis.pravarthaka.pojo.AssignedResponse;
import com.ortusolis.pravarthaka.pojo.ProductRequest;
import com.ortusolis.pravarthaka.pojo.ProductRequestCart;
import com.ortusolis.pravarthaka.service.RazorPayActivity;

import org.json.JSONArray;
import org.json.JSONException;
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
    String m_Text = "";
    //
    String paymentMethod="";
    float totalAmountPayment=0;
    //
    String Flag="no";
    String salesVaocherFalg="no";
    Integer Count=1;
    float PaymentTotal=0;
    //
    public AssignedResponse CashResponseOnly;
    ProductRequest productRequestFinal;
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
                    requestS.setDistributorId(productRequests.get(0).getDistributorId());
                    requestS.setAssignedId(productRequests.get(0).getAssignedId());
                    requestS.setOrderStatus(productRequests.get(0).getOrderStatus());
                    requestS.setOrderNumber(productRequests.get(0).getOrderNumber());
                    requestS.setDeliverdDate(productRequests.get(0).getDeliverdDate());
                    requestS.setUserLat(sharedPreferences.getString("latitude",""));
                    requestS.setUserLong(sharedPreferences.getString("longitude",""));

                    if(Count!=1){
                        progressDialog.dismiss();
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
//                                finish();
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

                    salesVaocherFalg="yes";
                    if(Count!=1){
                        progressDialog.dismiss();
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
//                        placeOrder(prepareOrder(productRequests),true);

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
            showAlertDialog(productRequest,str);
//            deleteFormCart();
//            onBackPressed();
            return;
        }

//        WebserviceController wss = new WebserviceController(CardListActivity.this);
//
//        String str = gson.toJson(productRequest);
//
////        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
////        alertDialogBuilder.setMessage("api request "+str);
////        alertDialogBuilder.setPositiveButton("yes",
////                new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface arg0, int arg1) {
////                        Toast.makeText(CardListActivity.this,"You clicked yesbutton",Toast.LENGTH_LONG).show();
////                    }
////                });
////
////
////
////        AlertDialog alertDialog = alertDialogBuilder.create();
////        alertDialog.show();
//
//        wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
//            @Override
//            public void notifySuccess(String response, int statusCode) {
////                deleteFormCart();
//                try {
//                    Log.e("getPlants response", response);
//
//                    final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);
//
//                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
////                        Intent intente = new Intent(CardListActivity.this, AssignedOrderListActivity.class);
//////                            if (!distributorResponse.getResponseData().getOrderList().isEmpty())
////                        intente.putExtra("orderId",distributorResponse.getResponseData().getOrderList().get(0).getOrderId());
////                        startActivity(intente);
//
////                            if (progressDialog!=null && progressDialog.isShowing()){
////                                progressDialog.dismiss();
////                            }
////                        Toast.makeText(CardListActivity.this,""+salesVoucherValue+"AssignedOrderListActivity",Toast.LENGTH_LONG).show();
//                        if (salesVaocherFalg.equals("yes")) {
//                            sharedPreferences.edit().remove("prodDesc").apply();
//                            Intent intent = new Intent(CardListActivity.this, AssignedOrderListActivity.class);
////                            if (!distributorResponse.getResponseData().getOrderList().isEmpty())
//                                intent.putExtra("orderId",distributorResponse.getResponseData().getOrderList().get(0).getOrderId());
//                            startActivity(intent);
//                            Toast.makeText(CardListActivity.this,"AssignedOrderListActivity",Toast.LENGTH_LONG).show();
//                        }
//                        else {
//                            deleteFormCart();
////                            onBackPressed();
//
////                            OnBackPressed()
//                       /*     AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
//                            builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "") + "<br/><br/> Delivery date is " + productRequest.getRequest().getDelivaryDate()));
//                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    sharedPreferences.edit().remove("prodDesc").apply();
//                                }
//                            });*/
//
//                           /* builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                @Override
//                                public void onDismiss(DialogInterface dialog) {
//                                    onBackPressed();
//                                }
//                            });
//                            builder.show();*/
//                            //
//                            CashResponseOnly=distributorResponse;
////                            showAlertDialog(productRequest);
//                            //
//                        }
//
//                    }else
//                        Toast.makeText(CardListActivity.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void notifyError(VolleyError error) {
//                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                if (progressDialog!=null && progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                }
//                Toast.makeText(CardListActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
//            }
//        });
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
                if (paymentMethod.equals("Cash")) {
                    dialog.dismiss();
                    CashAlert(productRequest);
                } else {
                    //
//                                        Intent myIntent = new Intent(CardListActivity.this, RazorPayActivity.class);
                    //                    myIntent.putExtra("totalAmountPayment", Float.toString(totalAmountPayment));
                    //                    startActivity(myIntent);
                    //
                    //                    //
//                    if (paymentMethod.equals("Payment")) {
//                    Toast.makeText(CardListActivity.this, sharedPreferences.getString("emailIdUser",""), Toast.LENGTH_LONG).show();
//
//                    String useremail= sharedPreferences.getString("emailIdUser","");
//                    Log.e("useremail",sharedPreferences.getString("emailIdUser",""));
                    final String strProductRequests = strProductRequest;
//                    if( useremail.equals("")){
////                       String m_Text = "";
//                        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
//                        builder.setTitle("Enter EmailId for payment Process");
//
//// Set up the input
//                        final EditText input = new EditText(CardListActivity.this);
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                        input.setInputType(InputType.TYPE_CLASS_TEXT );
//                        builder.setView(input);
//
//// Set up the buttons
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                m_Text = input.getText().toString();
//                                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches()){
//                                    Toast.makeText(CardListActivity.this,"Please Enter valid Email Id",Toast.LENGTH_LONG).show();
//                                }else {
//                                    sharedPreferences.edit().putString("emailIdUser", m_Text).commit();
//                                    onBackPressed();
//                                    Intent totalAmountPaymentintent = new Intent(CardListActivity.this, RazorPayActivity.class);
////                               // pass amount
//                                    totalAmountPaymentintent.putExtra("totalAmountPayment", totalAmountPayment);
//                                    totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
//                                    totalAmountPaymentintent.putExtra("classFlag", "cart");
//                                    totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
////                                 Log.d("hi",Parsef totalAmountPayment);
//                                    startActivity(totalAmountPaymentintent);
//                                }
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                        builder.show();
//                    }else {
                    onBackPressed();

                    Intent totalAmountPaymentintent = new Intent(CardListActivity.this, RazorPayActivity.class);
//                               // pass amount
                    totalAmountPaymentintent.putExtra("totalAmountPayment", totalAmountPayment);
                    totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
                    totalAmountPaymentintent.putExtra("classFlag", "cart");
                    totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
//                                 Log.d("hi",Parsef totalAmountPayment);
                    startActivity(totalAmountPaymentintent);
//                    }

//                    }
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //

    //
    public  void  CashAlert(final ProductRequest productRequest){

        WebserviceController wss = new WebserviceController(CardListActivity.this);

        String str = gson.toJson(productRequest);

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("api request "+str);
//        alertDialogBuilder.setPositiveButton("yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        Toast.makeText(CardListActivity.this,"You clicked yesbutton",Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();

        wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
//                deleteFormCart();
                try {
                    Log.e("getPlants response", response);

                    final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        sharedPreferences.edit().remove("prodDesc").apply();
//        ProductRequest productRequest = new ProductRequest();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
//        builder.setMessage(Html.fromHtml((CashResponseOnly.getResponseDescription() != null ? CashResponseOnly.getResponseDescription() : "") + "<br/><br/> Delivery date is " + ""));
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
                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
//                Toast.makeText(CardListActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });



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
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
        builder.setMessage("Your Order Cost is "+PaymentTotal+", click Confirm to place order")
                .setCancelable(false) //
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
//                        showAlertDialog(productRequestFinal);
                        placeOrder(prepareOrder(productRequests),false);
                    }
                });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Count=1;
//                finish();
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
//                        finalEntry.getString("productId");
                    if(finalEntry.getString("productId").equals(positionEntry.getString("productId"))){
                        completeArray.remove(i);// for remove only one entry object you need to add your own logic
//                            finalArray.put(finalEntry);
                    }else {
//                            finalArray.put(finalEntry);
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
}

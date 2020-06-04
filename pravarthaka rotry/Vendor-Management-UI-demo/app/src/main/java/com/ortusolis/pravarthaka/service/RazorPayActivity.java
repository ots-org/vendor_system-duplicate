package com.ortusolis.pravarthaka.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.pravarthaka.Activity.AssignedOrderListActivity;
import com.ortusolis.pravarthaka.Activity.CardListActivity;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.pojo.AssignedResponse;
import com.ortusolis.pravarthaka.pojo.ProductRequest;
import com.ortusolis.pravarthaka.pojo.ProductRequestCart;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RazorPayActivity extends Activity implements PaymentResultListener {
    private String TAG = "Pravarthaka";

    //
    Float totalAmountPayment ;
    String salesVaocherFalg,productRequestsStr,emailIdUser;
    Gson gson;
    List<ProductRequestCart> productRequestsfinal;
    List<ProductRequestCart> productRequests;
    public AssignedResponse CashResponseOnly;
    SharedPreferences sharedPreferences;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_payment);

        //   get intent and receive cost
        /*Intent PaymentIntent = getIntent();
        totalAmountPayment = PaymentIntent.getStringExtra("totalAmountPayment");*/
        //
        sharedPreferences = getSharedPreferences("water_management",0);
        emailIdUser=sharedPreferences.getString("emailIdUser","");

//
        gson = new Gson();
        productRequests = new ArrayList<>();
        setUpWidgets();
        //get intent and receive cost

        Intent intent = getIntent();

        totalAmountPayment = intent.getFloatExtra("totalAmountPayment", 0);
        salesVaocherFalg =intent.getStringExtra("salesVaocherFalg");
        productRequestsStr= intent.getStringExtra("productRequests");
//        productRequests= (List<ProductRequestCart>) getIntent().getSerializableExtra("productRequests");
//        productRequestsfinal=productRequests;
/*
       Intent intent = new Intent(ProductDescription.this,AddProductActivity.class);
       intent.putExtra("product", getIntent().getExtras().getParcelable("product"));
       startActivity(intent);
        //*/
        startPayment();
    }

    public void setUpWidgets(){

        /** Preload payment resources */
        Checkout.preload(getApplicationContext());
    }

    public void startPayment() {

        /** Instantiate Checkout */
        Checkout checkout = new Checkout();

        /** Set your logo here*/
        checkout.setImage(R.drawable.cart);

        /** Reference to current activity */
        final Activity activity = this;


        /** Pass your payment options to the Razorpay Checkout as a JSONObject */
        try {
            JSONObject options = new JSONObject();
            JSONObject jsonUserInfo = new JSONObject();
            jsonUserInfo.put("contact" , sharedPreferences.getString("userPhoneNumberPayment",""));
            jsonUserInfo.put("email", emailIdUser);
            options.put("prefill" , jsonUserInfo);

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Ortusolis");

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Order #123456");

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            Double finalAmount= totalAmountPayment * 100.0;
            Log.d("totalAmountPayment",finalAmount.toString());

            String amountStr = finalAmount.toString();
            options.put("amount", amountStr);


            checkout.open(activity, options);

        } catch(Exception e) {

        }
    }

    @Override
    public void onPaymentSuccess(String paymentID) {
        Log.e("paymentID",paymentID);
        sharedPreferences.edit().remove("prodDesc").apply();
        placeOrder();
        Toast.makeText(getApplicationContext(),"payment Success",Toast.LENGTH_SHORT).show();
        onBackPressed();
//        onBackPressed();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                onBackPressed();
//            }
//        },500);
    }

    private void finishActivity(){
        this.finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        //Toast.makeText(getApplicationContext(),"payment fail",Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        //TODO should move to Near you screen or something relevant . Sabin please see it.
        this.finish();
    }

    void placeOrder(){

        WebserviceController wss = new WebserviceController(RazorPayActivity.this);

        String str = productRequestsStr;
        Log.e("place Request",str);
        wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        if (salesVaocherFalg.equals("yes")) {
                            sharedPreferences.edit().remove("prodDesc").apply();
                            Intent intent = new Intent(RazorPayActivity.this, AssignedOrderListActivity.class);
                            intent.putExtra("orderId",distributorResponse.getResponseData().getOrderList().get(0).getOrderId());
                            startActivity(intent);
                            Toast.makeText(RazorPayActivity.this,"AssignedOrderListActivity",Toast.LENGTH_LONG).show();
                        }
                        else {
                            CashResponseOnly=distributorResponse;
                        }
//                        onBackPressed();
                    }else
                        Toast.makeText(RazorPayActivity.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(RazorPayActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
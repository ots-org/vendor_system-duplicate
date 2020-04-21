package com.ortusolis.water_distribution.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ortusolis.water_distribution.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
public class RazorPayActivity extends Activity implements PaymentResultListener {
    private String TAG = "Pravarthaka";

//
Float totalAmountPayment ;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_payment);

        //   get intent and receive cost
        /*Intent PaymentIntent = getIntent();
        totalAmountPayment = PaymentIntent.getStringExtra("totalAmountPayment");*/
        //

        setUpWidgets();

        //get intent and receive cost

       Intent intent = getIntent();

      totalAmountPayment = intent.getFloatExtra("totalAmountPayment", 0);
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
            jsonUserInfo.put("contact" , "8546995058");
            jsonUserInfo.put("email", "manoj.vg@ortusolis.com");
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

        Toast.makeText(getApplicationContext(),"payment Success",Toast.LENGTH_SHORT).show();
        onBackPressed();
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
}
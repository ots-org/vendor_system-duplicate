package com.ortusolis.subhaksha.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
public class RazorPayActivity extends Activity implements PaymentResultListener {
    private String TAG = "Pravarthaka";
    SharedPreferences sharedPreferences;

//
Float totalAmountPayment ;
    final boolean salesVoucher=true;
    String distributorIdPaymentUpdate,delivaryDatePaymentUpdate,orderStatusPaymentUpdate,customerIdPaymentUpdate,orderIdPaymentUpdate,orderCostPaymentUpdate,orderDatePaymentUpdate,orderNumberPaymentUpdate;
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

        distributorIdPaymentUpdate=intent.getStringExtra("distributorIdPaymentUpdate");
        delivaryDatePaymentUpdate=intent.getStringExtra("delivaryDatePaymentUpdate");
        orderStatusPaymentUpdate=intent.getStringExtra("orderStatusPaymentUpdate");
        customerIdPaymentUpdate=intent.getStringExtra("customerIdPaymentUpdate");
        orderIdPaymentUpdate=intent.getStringExtra("orderIdPaymentUpdate");
        orderCostPaymentUpdate=intent.getStringExtra("orderCostPaymentUpdate");
        orderDatePaymentUpdate=intent.getStringExtra("orderDatePaymentUpdate");
        orderNumberPaymentUpdate=intent.getStringExtra("orderNumberPaymentUpdate");

        totalAmountPayment = intent.getFloatExtra("totalAmountPayment", 0);
        sharedPreferences = getSharedPreferences("water_management",0);
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
           String userPhoneNumberPay= sharedPreferences.getString("userPhoneNumber","");
            String userEmailIdPay= sharedPreferences.getString("userEmailId","");
            Log.e("payment Credentials",sharedPreferences.getString("userPhoneNumber","")+sharedPreferences.getString("userEmailId",""));
            JSONObject options = new JSONObject();
            JSONObject jsonUserInfo = new JSONObject();
            jsonUserInfo.put("contact" , userPhoneNumberPay);
            jsonUserInfo.put("email", userEmailIdPay);
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
        //
//        ProductDescription productDescription = new ProductDescription();
//        productDescription.insertOrderAndProduct(paymentID);
        WebserviceController wss = new WebserviceController(RazorPayActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("modeOfPayment","Payment");
            jsonObject.put("rzpyTransactionId", paymentID);
            jsonObject.put("distributorId", distributorIdPaymentUpdate); //done
            jsonObject.put("deliveryDate",delivaryDatePaymentUpdate); //done
            jsonObject.put("custLong",sharedPreferences.getString("longitude",""));
            jsonObject.put("custLat", sharedPreferences.getString("latitude",""));
            jsonObject.put("orderStatus",orderStatusPaymentUpdate); //done
            jsonObject.put("customerId", customerIdPaymentUpdate); //done
            jsonObject.put("orderId", orderIdPaymentUpdate); //done
            jsonObject.put("orderCost", orderCostPaymentUpdate); //done
            jsonObject.put("deliverdDate",delivaryDatePaymentUpdate);
            jsonObject.put("orderDate", orderDatePaymentUpdate); //done
            jsonObject.put("orderNumber",orderNumberPaymentUpdate); //done

            requestObject.put("request",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.UpdateOrder, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);
                    JSONObject obj = new JSONObject(response);
                    String responseData  = obj.getString("responseCode");

                    if (responseData.equals("200")) {

                    }
                    else {
                        Toast.makeText(RazorPayActivity.this, TextUtils.isEmpty(obj.getString("responseDescription"))? "backed payment Update failed" :obj.getString("responseDescription"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(RazorPayActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
        //
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
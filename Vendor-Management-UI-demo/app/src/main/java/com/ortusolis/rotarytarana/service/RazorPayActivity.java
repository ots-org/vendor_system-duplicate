package com.ortusolis.rotarytarana.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.Activity.AssignedOrderListActivity;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.AssignedResponse;
import com.ortusolis.rotarytarana.pojo.ProductRequestCart;
import com.ortusolis.rotarytarana.pojo.UserInfo;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RazorPayActivity extends Activity implements PaymentResultWithDataListener {
    private String TAG = "Pravarthaka";
    String orderID="";

//
Float totalAmountPayment ;
String salesVaocherFalg,productRequestsStr,emailIdUser;
    Gson gson;
    List<ProductRequestCart> productRequestsfinal;
    List<ProductRequestCart> productRequests;
    public AssignedResponse CashResponseOnly;
    SharedPreferences sharedPreferences;
    ArrayList<String> donationlist;
    ArrayList<String> donationCashlist;
    String donation="no";
    UserInfo userInfo;
    String productId,userId,dontaionAmount,donatedQty,paymentId,presentStock,donationStatus,donationRequestId,requestStatus,productName,BeneficieryName;
String donationCashlistAmount,donationCashlistDiscription,donationCashlistGST,donationCashlistPan;
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
        donationlist = new ArrayList<>();
        donationCashlist = new ArrayList<>();
//
        gson = new Gson();
        productRequests = new ArrayList<>();

        //get intent and receive cost

       Intent intent = getIntent();



        if (getIntent().hasExtra("productRequests")){
            totalAmountPayment = intent.getFloatExtra("totalAmountPayment", 0);
            salesVaocherFalg =intent.getStringExtra("salesVaocherFalg");
            productRequestsStr= intent.getStringExtra("productRequests");
            DemoOrderId();
        }
        if (getIntent().hasExtra("donationlist")){
            donationlist = (ArrayList<String>)getIntent().getSerializableExtra("donationlist");
            productId=donationlist.get(0);
            userId=sharedPreferences.getString("userid", "");
            dontaionAmount= donationlist.get(1);
            donatedQty=donationlist.get(2);
            paymentId="";
            presentStock=donationlist.get(3);
            requestStatus=donationlist.get(4);
            donationRequestId=donationlist.get(5);
            donationStatus=donationlist.get(6);

            totalAmountPayment= Float.valueOf(dontaionAmount);
            productName=donationlist.get(11);
            BeneficieryName=donationlist.get(12);
            donation="yes";
            DemoOrderId();
        }
        if (getIntent().hasExtra("donationCashlist")){
            donationCashlist = (ArrayList<String>)getIntent().getSerializableExtra("donationCashlist");
            donationCashlistAmount=donationCashlist.get(0);
            userId=sharedPreferences.getString("userid", "");
            donationCashlistDiscription= donationCashlist.get(1);
            donationCashlistPan=donationCashlist.get(2);
            paymentId="";
            donationCashlistGST=donationCashlist.get(3);
            totalAmountPayment= Float.valueOf(donationCashlistAmount);
            donation="donationCashlist";
            DemoOrderId();
        }
//        productRequests= (List<ProductRequestCart>) getIntent().getSerializableExtra("productRequests");
//        productRequestsfinal=productRequests;
/*
       Intent intent = new Intent(ProductDescription.this,AddProductActivity.class);
       intent.putExtra("product", getIntent().getExtras().getParcelable("product"));
       startActivity(intent);
        //*/

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
            options.put("name", "ORTUSOLIS TECHNOLOGY SERVICES LLP");

            options.put("order_id", orderID);
//            options.put("signature","razorpay_signature");
            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Order ID "+orderID);

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
           float finalAmount= totalAmountPayment * 100;
           Log.d("totalAmountPayment", String.valueOf(finalAmount));

           String amountStr = String.valueOf(finalAmount);
            options.put("amount", amountStr.substring(0,amountStr.length()-2));
//            Order order = razorpay.Orders.create(options);

            checkout.open(activity, options);

        } catch(Exception e) {

        }
    }

    @Override
    public void onPaymentSuccess(String paymentID, PaymentData paymentData) {
        paymentId=paymentID;
        if(donation.equals("yes")){
            donationPayment();
        }else if(donation.equals("donationCashlist")){
            donationCashPayment();
        }
        else{
            sharedPreferences.edit().remove("prodDesc").apply();
            placeOrder();
        }


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
    public void onPaymentError(int i, String s, PaymentData paymentData) {
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
//                        Toast.makeText(getApplicationContext(),"payment Success",Toast.LENGTH_SHORT).show();
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

    public void donationPayment(){
        WebserviceController wss = new WebserviceController(RazorPayActivity.this);

        JSONObject requestObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();

                JSONObject jsonObject1 = new JSONObject();

                jsonObject1.put("productId", productId);
                jsonObject1.put("donorId", userId);
                jsonObject1.put("dontaionAmount", dontaionAmount);
                jsonObject1.put("donatedQty", donatedQty);
                jsonObject1.put("paymentId", paymentId);
                jsonObject1.put("presentStock", presentStock);
                jsonObject1.put("requestStatus", requestStatus);
                jsonObject1.put("donationStatus", donationStatus);
                jsonObject1.put("donationMethod", "cash");
                jsonObject1.put("donationRequestId", donationRequestId);
                if(donationlist.get(7).equals("")){
                    jsonObject1.put("orderId", "any");
                }else {
                    jsonObject1.put("orderId", donationlist.get(7));
                }
                if(donationlist.get(8).equals("")){
                    jsonObject1.put("otherNumber", JSONObject.NULL);
                }else {
                    jsonObject1.put("otherNumber", donationlist.get(8));
                }

                if(donationlist.get(9).equals("")){
                    jsonObject1.put("panNumber", JSONObject.NULL);
                }else {
                    jsonObject1.put("panNumber", donationlist.get(9));
                }
            if(donationlist.get(7).equals("")){
                jsonObject1.put("orderQty", JSONObject.NULL);
            }else {
                jsonObject1.put("orderQty", donationlist.get(10));
            }
                jsonArray.put(jsonObject1);
                requestObject.put("request",jsonArray);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.addNewDonation, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        new Thread(new Runnable() {
                            public void run(){
                                try {
                                    if(BeneficieryName.equals("Any ")){
                                        BeneficieryName="Beneficiary";
                                    }
                                    DonationImage(""+sharedPreferences.getString("userFirstName", "")+" "+sharedPreferences.getString("userLastName", "")+" of "+ sharedPreferences.getString("userAddress1", "")+ " has donated  "+productName +" of worth "+getString(R.string.Rs) + dontaionAmount+" towards "+BeneficieryName.split("\\(")[0]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
//                        Toast.makeText(getApplicationContext(),"payment Success",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void donationCashPayment(){
        WebserviceController wss = new WebserviceController(RazorPayActivity.this);

        JSONObject requestObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("productId", "3");
            jsonObject1.put("donorId", sharedPreferences.getString("userid", ""));
            jsonObject1.put("dontaionAmount",donationCashlistAmount);
            jsonObject1.put("donatedQty", "0");
            jsonObject1.put("paymentId", "11-paid");
            jsonObject1.put("requestStatus", "newRequest");
            jsonObject1.put("donationStatus", "directDonation");
            jsonObject1.put("donationMethod", "cash");
            jsonObject1.put("donationRequestId", JSONObject.NULL);
            jsonObject1.put("description", donationCashlistDiscription);
            if (donationCashlistGST.equals("")){
                jsonObject1.put("otherNumber", JSONObject.NULL);
            }else {
                jsonObject1.put("otherNumber", donationCashlistGST);
            }

            if (donationCashlistPan.equals("")){
                jsonObject1.put("panNumber", JSONObject.NULL);
            }else {
                jsonObject1.put("panNumber", donationCashlistPan);
            }
            jsonObject1.put("orderId", "any");
            jsonArray.put(jsonObject1);
            requestObject.put("request",jsonArray);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.addNewDonation, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
//                        Toast.makeText(getApplicationContext(),"payment Success",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void DemoOrderId(){
        WebserviceController wss = new WebserviceController(RazorPayActivity.this);

        JSONObject requestObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject1 = new JSONObject();
//            Integer finalAmount= Math.round(totalAmountPayment) * 100;
            float finalAmount= totalAmountPayment * 100;
            Log.d("totalAmountPayment", String.valueOf(finalAmount));
            String amountStr = String.valueOf(finalAmount);
//            options.put("amount", amountStr.substring(0,amountStr.length()-2));
            jsonObject1.put("distributorId", "customerId_123_randomValue");
            jsonObject1.put("orderCost", amountStr.substring(0,amountStr.length()-2));

//            jsonArray.put(jsonObject1);

            requestObject.put("request",jsonObject1);
           Log.e("ssss",requestObject.toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getRazorPayOrder, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj= new JSONObject(response);
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
//                        Toast.makeText(getApplicationContext(),"payment Success",Toast.LENGTH_SHORT).show();
                    }
                    orderID=obj.getJSONObject("responseData").getJSONArray("orderDetails").getJSONObject(0).getString("orderId");
                    setUpWidgets();
                    startPayment();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }
    void DonationImage(final String text) throws IOException {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text+" using e-Taarana for Rotary App \n https://play.google.com/store/apps/details?id=com.ortusolis.rotarytarana \n We thank you for this contribution.");
        startActivity(Intent.createChooser(shareIntent, "share the donation info:"));
//        share image in social media //

//        Bitmap image = null;
//        try {
//            URL url = new URL("http://....");
//             image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch(IOException e) {
//            System.out.println(e);
//        }
//        Bitmap icon = image;
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("image/jpeg");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
//        try {
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
//        startActivity(Intent.createChooser(share, "Share Image"));

//        share image in social media //


//        final Paint textPaint = new Paint() {
//            {
//                setColor(Color.WHITE);
//                setTextAlign(Paint.Align.LEFT);
//                setTextSize(20f);
//                setAntiAlias(true);
//            }
//        };
//        final Rect bounds = new Rect();
//        textPaint.getTextBounds(text, 0, text.length(), bounds);
//
//        final Bitmap bmp = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.RGB_565); //use ARGB_8888 for better quality
//        final Canvas canvas = new Canvas(bmp);
//        canvas.drawText(text, 0, 20f, textPaint);
//
//        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"donation");
//        if (!dir.exists())
//            dir.mkdirs();
//
//        File file = new File(dir, "capture.jpg");
//
//        FileOutputStream stream = new FileOutputStream(file); //create your FileOutputStream here
//        bmp.compress(Bitmap.CompressFormat.PNG, 85, stream);
//        bmp.recycle();
//        stream.close();
    }
}
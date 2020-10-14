package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.DonationAdapter;
import com.ortusolis.rotarytarana.service.RazorPayActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DonationActivityDiscription extends AppCompatActivity {
    private Toolbar toolbar;
    ActionBar action;
    ArrayList<String> finalList;
    ArrayList<String> donationlist;
    TextView productname,productprice,totalAmount,requiredQuantity,noResult;
    EditText donationQuantity,pan,gst,donorName,donorAddress;
    Button decrease,increase,pay;
    Double req,finalRequried;
    Double totalP;
    String status,paymentMethod,paymentStatus,selctBen,orderIdBen,orderQntBen;
    Spinner spinnerBeneficiary;
    List<String> Beneficiary;
    List<String> orderId;
    List<String> BeneficiaryProductQuantity;
    LinearLayout requiredQuantityLayout,mainLayout;
    SharedPreferences sharedPreferences;
    String productName,productPrice,addedStock,stock,donationRequestId,productId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_discription);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        requiredQuantityLayout= (LinearLayout) findViewById(R.id.requiredQuantityLayout);
        productname= findViewById(R.id.productname);
        productprice= findViewById(R.id.productprice);
        totalAmount= findViewById(R.id.totalAmount);
        pay=findViewById(R.id.Pay);
        noResult=findViewById(R.id.noResult);
        mainLayout=findViewById(R.id.mainLayout);
        paymentMethod="";
        Beneficiary = new ArrayList();
        orderId= new ArrayList();
        orderIdBen="any";
        orderQntBen="";
        productName="";
        productPrice="0.0";
        addedStock="0.0";
        stock="0.0";
        donationRequestId="";
        productId="";
        BeneficiaryProductQuantity = new ArrayList();
        Beneficiary.add("Any ");
        donationQuantity= findViewById(R.id.donationQuantity);
        requiredQuantity= findViewById(R.id.requiredQuantity);
        pan= findViewById(R.id.pan);
        gst= findViewById(R.id.gst);
        donorName= findViewById(R.id.donorName);
        donorAddress= findViewById(R.id.donorAddress);
        sharedPreferences = getSharedPreferences("water_management",0);
        spinnerBeneficiary = (Spinner) findViewById(R.id.spinnerBeneficiary);
        selctBen="no";
        spinnerBeneficiary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i == 0) {
                    selctBen = "no";
                    orderIdBen = "any";
                    Toast.makeText(getApplicationContext(), "Any Beneficiary", Toast.LENGTH_LONG).show();
                    req = 1.0;
                    donationQuantity.setText(req.toString().substring(0, req.toString().length() - 2));
                    req = Double.valueOf(stock) - Double.valueOf(addedStock);
                    requiredQuantity.setText(req.toString().substring(0, req.toString().length() - 2));
                    totalP = Double.valueOf(productPrice) * Double.valueOf(String.valueOf(donationQuantity.getText()));
                    totalAmount.setText(String.format("%.2f", totalP));
                    requiredQuantityLayout.setVisibility(View.VISIBLE);
                } else {
                    selctBen = "yes";
                    orderIdBen = orderId.get(i - 1);
                    orderQntBen=BeneficiaryProductQuantity.get(i-1);
                    Log.e("BQuantity", BeneficiaryProductQuantity.size() + "");
                    Integer BeneficiaryQuantity = Integer.parseInt(BeneficiaryProductQuantity.get(i - 1));
                    req = BeneficiaryQuantity + 0.0;
                    finalRequried = BeneficiaryQuantity + 0.0;
                    donationQuantity.setText(req.toString().substring(0, req.toString().length() - 2));
                    totalP = Double.valueOf(productPrice) * Double.valueOf(String.valueOf(donationQuantity.getText()));
                    totalAmount.setText(String.format("%.2f", totalP));
                    requiredQuantityLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        donationQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(donationQuantity.getText().toString())) {
                    totalP=Double.valueOf(productPrice)*Double.valueOf(String.valueOf(donationQuantity.getText()));
                    totalAmount.setText(String.format("%.2f", totalP));
                }
            }
        });
        decrease= findViewById(R.id.decrease);
        increase= findViewById(R.id.increase);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Decrease();
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Increase();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pan.getText().toString().isEmpty() && Double.parseDouble(totalAmount.getText().toString()) > 50000){
                    pan.setError("Please Enter Pan Number");
                    return;
                }

                if(donationQuantity.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Donation Quantity Can not be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                Double dAmount=Double.valueOf(String.valueOf(donationQuantity.getText()));
                if(selctBen.equals("yes")){
                    if(dAmount<finalRequried){
                        Toast.makeText(getApplicationContext(), "Donation quantity must be greater than "+finalRequried, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(donorName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "80G Name cannot be empty ", Toast.LENGTH_LONG).show();
                    return;
                }
                if(donorAddress.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "80G Address cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                paymentMethod="Payment";
                pay();
            }
        });
        donationlist = new ArrayList<>();
        donationlist.clear();
        finalList = new ArrayList<>();
        if(getIntent().hasExtra("finalList")){
            finalList = (ArrayList<String>)getIntent().getSerializableExtra("finalList");
            productName=finalList.get(0);
            productPrice=finalList.get(1);
            addedStock=finalList.get(2);
            stock=finalList.get(3);
            donationRequestId=finalList.get(4);
            productId=finalList.get(5);
            productname.setText(productName);
            productprice.setText(productPrice);
            req=1.0;
            finalRequried=1.0;
            donationQuantity.setText(req.toString().substring(0,req.toString().length()-2));
            req= Double.valueOf(stock)-Double.valueOf(addedStock);
            requiredQuantity.setText(req.toString().substring(0,req.toString().length()-2));
            req= Double.valueOf(stock)-Double.valueOf(addedStock)-Double.valueOf(String.valueOf(donationQuantity.getText()));
            totalP=Double.valueOf(productPrice)*Double.valueOf(String.valueOf(donationQuantity.getText()));
            totalAmount.setText(String.format("%.2f", totalP));
            donorName.setText(sharedPreferences.getString("username",""));
            donorAddress.setText(sharedPreferences.getString("userAddress1",""));
            BenifiaryList();
        }else{
            SingleProductDonation(getIntent().getExtras().getString("productID"));
        }





        setSupportActionBar(toolbar);
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
            toolbarTitle.setText("Donation");

            action.setCustomView(viewActionBar, params);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
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

    public void Decrease(){
        if(donationQuantity.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Donation Quantity Can not be Empty", Toast.LENGTH_LONG).show();
            return;
        }
        Double dAmount=Double.valueOf(String.valueOf(donationQuantity.getText()))-1.0;
        if(dAmount<finalRequried){
            return;
        }
        donationQuantity.setText(dAmount.toString().substring(0,dAmount.toString().length()-2));
        req= Double.valueOf(stock)-Double.valueOf(addedStock)-Double.valueOf(String.valueOf(donationQuantity.getText()));
        totalP=Double.valueOf(productPrice)*Double.valueOf(String.valueOf(donationQuantity.getText()));
        totalAmount.setText(String.format("%.2f", totalP));
    }
    public void Increase(){
        if(donationQuantity.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Donation Quantity Can not be Empty", Toast.LENGTH_LONG).show();
            return;
        }
        Double iAmount=Double.valueOf(String.valueOf(donationQuantity.getText()))+1.0;
        donationQuantity.setText(iAmount.toString().substring(0,iAmount.toString().length()-2));
        req= Double.valueOf(stock)-Double.valueOf(addedStock)-Double.valueOf(String.valueOf(donationQuantity.getText()));
        totalP=Double.valueOf(productPrice)*Double.valueOf(String.valueOf(donationQuantity.getText()));
        totalAmount.setText(String.format("%.2f", totalP));
    }
    public void pay(){

        Integer dQ=Integer.valueOf(String.valueOf(donationQuantity.getText()));
        Integer rQ=Integer.valueOf(String.valueOf(requiredQuantity.getText()));

        if(dQ>rQ || dQ==rQ||(BeneficiaryProductQuantity.size()==1 && selctBen.equals("yes"))){
            status="closerequest";
        }else {
            status="newRequest";
        }
        if(paymentMethod.equals("Kind")){
            paymentStatus=paymentMethod;
            donationKind();
            return;
        }else {
            paymentStatus=paymentMethod;
        }
        donationlist.clear();
        donationlist.add(productId);
        donationlist.add(totalAmount.getText().toString());
        if(donationQuantity.getText().toString().equals("")||donationQuantity.getText().toString().equals("0")){
            donationlist.add("1");
        }else {
            donationlist.add(donationQuantity.getText().toString());
        }

        donationlist.add(requiredQuantity.getText().toString());
        donationlist.add(status);
        donationlist.add(donationRequestId);
        donationlist.add(paymentStatus);
        donationlist.add(orderIdBen);
        donationlist.add(gst.getText().toString());
        donationlist.add(pan.getText().toString());
        if(orderIdBen.equals("any")){
            donationlist.add("");
        }else {
            donationlist.add(orderQntBen);
        }
        donationlist.add(productName);
        donationlist.add(spinnerBeneficiary.getSelectedItem().toString());
        donationlist.add(donorName.getText().toString());
        donationlist.add(donorAddress.getText().toString());
        onBackPressed();
        Intent donationlistIntent = new Intent(DonationActivityDiscription.this, RazorPayActivity.class);
        donationlistIntent.putExtra("donationlist", donationlist);
        startActivity(donationlistIntent);
    }

    public void BenifiaryList(){
        progressDialog = new ProgressDialog(DonationActivityDiscription.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(DonationActivityDiscription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestId", donationRequestId);
            requestObject.put("requestId",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getListOfOrderDetailsForRequest, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {

                    Log.e("DonatnList response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray requestProductDetails = responseData.getJSONArray("requestProductDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        for (int ProdCategory=0;ProdCategory<requestProductDetails.length();ProdCategory++){
                            JSONObject productDetailsobject = requestProductDetails.getJSONObject(ProdCategory);
                            JSONObject orderDetails = productDetailsobject.getJSONObject("orderDetails");
                            orderId.add(orderDetails.getString("orderId"));
                            JSONArray orderdProducts = orderDetails.getJSONArray("orderdProducts");
                            JSONObject orderdProductsobject = orderdProducts.getJSONObject(0);
                            BeneficiaryProductQuantity.add(orderdProductsobject.getString("otsOrderedQty"));
                            JSONObject userDetails = productDetailsobject.getJSONObject("userDetails");
                            Beneficiary.add(userDetails.getString("firstName")+"("+orderDetails.getString("orderId")+")");
                        }
                        spinnerBeneficiary.setAdapter(new ArrayAdapter(DonationActivityDiscription.this, android.R.layout.simple_spinner_dropdown_item, Beneficiary){
                            @Override
                            public boolean isEnabled(int position){
                                if(position == 0)
                                {
                                    // Disable the second item from Spinner
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView textview = (TextView) view;
                                if (position == 0) {
                                    textview.setTextColor(Color.BLACK);
                                } else {
                                    textview.setTextColor(Color.GRAY);
                                }
                                return view;
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
                catch (Exception e){
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }
    public void donationKind(){
        WebserviceController wss = new WebserviceController(DonationActivityDiscription.this);

        JSONObject requestObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject1 = new JSONObject();

            jsonObject1.put("productId", productId);
            jsonObject1.put("donorId", sharedPreferences.getString("userid", ""));
            jsonObject1.put("dontaionAmount",totalAmount.getText().toString());
            jsonObject1.put("donatedQty", donationQuantity.getText().toString());
            jsonObject1.put("paymentId", JSONObject.NULL);
            jsonObject1.put("presentStock", requiredQuantity.getText().toString());
            jsonObject1.put("requestStatus", status);
            jsonObject1.put("donationStatus", "donationPending");
            jsonObject1.put("donationMethod", "kind");
            jsonObject1.put("donationRequestId", donationRequestId);
            if (gst.getText().toString().equals("")){
                jsonObject1.put("otherNumber", JSONObject.NULL);
            }else {
                jsonObject1.put("otherNumber", gst.getText().toString());
            }

            if (pan.getText().toString().equals("")){
                jsonObject1.put("panNumber", JSONObject.NULL);
            }else {
                jsonObject1.put("panNumber", pan.getText().toString());
            }
            jsonObject1.put("orderId", orderIdBen);
            if(orderIdBen.equals("any")){
                jsonObject1.put("orderQty", JSONObject.NULL);
            }else {
                jsonObject1.put("orderQty", orderQntBen);
            }
            jsonObject1.put("companyName", donorName);
            jsonObject1.put("atgAddress", donorAddress);
            jsonObject1.put("paymentFlowStatus", "donation");
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
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    public void SingleProductDonation(String ProductID){
        WebserviceController wss = new WebserviceController(DonationActivityDiscription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "NewRequest");
            jsonObject.put("assgineId", "string");
            jsonObject.put("productId", ProductID);
            requestObject.put("request",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getDonationListBystatus, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("DonatnList response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productList = responseData.getJSONArray("productList");
//                    productList
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        JSONObject productDetailsobject = productList.getJSONObject(0);
                        productName=productDetailsobject.getString("productName");
                        productPrice= productDetailsobject.getString("productPrice");
                        addedStock=productDetailsobject.getString("addedStock");
                        stock=productDetailsobject.getString("stock");
                        donationRequestId=productDetailsobject.getString("donationRequestId");
                        productId=productDetailsobject.getString("productId");
                        productname.setText(productName);
                        productprice.setText(productPrice);
                        req=1.0;
                        finalRequried=1.0;
                        donationQuantity.setText(req.toString().substring(0,req.toString().length()-2));
                        req= Double.valueOf(stock)-Double.valueOf(addedStock);
                        requiredQuantity.setText(req.toString().substring(0,req.toString().length()-2));
                        req= Double.valueOf(stock)-Double.valueOf(addedStock)-Double.valueOf(String.valueOf(donationQuantity.getText()));
                        totalP=Double.valueOf(productPrice)*Double.valueOf(String.valueOf(donationQuantity.getText()));
                        totalAmount.setText(String.format("%.2f", totalP));
                        donorName.setText(sharedPreferences.getString("username",""));
                        donorAddress.setText(sharedPreferences.getString("userAddress1",""));
                        BenifiaryList();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    mainLayout.setVisibility(View.GONE);
                    noResult.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "No request for this Product", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }

}
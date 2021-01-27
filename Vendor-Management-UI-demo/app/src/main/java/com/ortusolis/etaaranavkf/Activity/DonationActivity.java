package com.ortusolis.etaaranavkf.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.NetworkUtility.IResult;
import com.ortusolis.etaaranavkf.NetworkUtility.WebserviceController;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.adapter.DonationAdapter;
import com.ortusolis.etaaranavkf.service.RazorPayActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonationActivity extends AppCompatActivity implements Serializable {
    private Toolbar toolbar;
    ActionBar action;
    ListView list;
    String[] productName;
    String[] productPrice;
    String[] addedStock;
    String[] stock;

    ArrayList<String> productNameList;
    ArrayList<String> productPriceList;
    ArrayList<String> addedStockList;
    ArrayList<String> stockList;
    ArrayList<String> donationRequestId;
    ArrayList<String> productId;
    ArrayList<String> finalList;
    ArrayList<String> donationCashlist;

    Button DonateCash,donateButton;
    LinearLayout donationLayout;
    EditText donarName,donarPhone,donarAmount,donarGst,donarPan,donarDescription,donarAddress;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

//        Donation();
        productNameList = new ArrayList<>();
        productPriceList = new ArrayList<>();
        addedStockList = new ArrayList<>();
        stockList = new ArrayList<>();
        donationRequestId = new ArrayList<>();
        productId = new ArrayList<>();
        finalList = new ArrayList<>();
        donationCashlist = new ArrayList<>();

        sharedPreferences = getSharedPreferences("water_management",0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list=(ListView)findViewById(R.id.list);
        donationLayout=(LinearLayout) findViewById(R.id.donationLayout);
        DonateCash=findViewById(R.id.DonateCash);
        donarAmount=findViewById(R.id.donarAmount);
        donarPan=findViewById(R.id.donarPan);
        donarGst=findViewById(R.id.donarGst);
        donarName=findViewById(R.id.donarName);
        donarAddress=findViewById(R.id.donarAddress);
        donarDescription=findViewById(R.id.donarDescription);
        donateButton=findViewById(R.id.donateButton);
        DonateCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (donationLayout.getVisibility() == View.VISIBLE) {
                    // Its visible
                    donationLayout.setVisibility(View.GONE);
                } else {
                    // Either gone or invisible
                    donationLayout.setVisibility(View.VISIBLE);
                }

            }
        });
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donationCash();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                finalList.clear();
                // TODO Auto-generated method stub
                    list.getItemAtPosition(position);
                    finalList.add(productNameList.get(position));
                    finalList.add(productPriceList.get(position));
                    finalList.add(addedStockList.get(position));
                    finalList.add(stockList.get(position));
                    finalList.add(donationRequestId.get(position));
                    finalList.add(productId.get(position));
                    Intent intent = new Intent(DonationActivity.this, DonationActivityDiscription.class);
                    intent.putExtra("finalList", finalList);
                    startActivityForResult(intent,3533);
                    Log.e("item",list.getItemAtPosition(position).toString());
            }
        });
        donarName.setText(sharedPreferences.getString("username",""));
        donarAddress.setText(sharedPreferences.getString("userAddress1",""));
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
    @Override
    public void onResume() {
        super.onResume();
        productNameList.clear();
        productPriceList.clear();
        addedStockList.clear();
        stockList.clear();
        list.invalidateViews();
        Donation();
    }
    public void Donation(){
        progressDialog = new ProgressDialog(DonationActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(DonationActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "NewRequest");
            jsonObject.put("assgineId", "string");
            jsonObject.put("productId", JSONObject.NULL);
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
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productName= new String[productList.length()];
                        productPrice= new String[productList.length()];
                        addedStock= new String[productList.length()];
                        stock= new String[productList.length()];
                        for (int ProdCategory=0;ProdCategory<productList.length();ProdCategory++){
                            JSONObject productDetailsobject = productList.getJSONObject(ProdCategory);
                            productName[ProdCategory]=productDetailsobject.getString("productName");
                            productPrice[ProdCategory]=productDetailsobject.getString("productPrice");
                            addedStock[ProdCategory]=productDetailsobject.getString("addedStock");
                            stock[ProdCategory]=productDetailsobject.getString("stock");
                            productNameList.add(productName[ProdCategory]);
                            productPriceList.add( productPrice[ProdCategory]);
                            addedStockList.add(addedStock[ProdCategory]);
                            stockList.add( stock[ProdCategory]);
                            donationRequestId.add(productDetailsobject.getString("donationRequestId"));
                            productId.add(productDetailsobject.getString("productId"));
                        }
                        DonationAdapter adapter=new DonationAdapter(DonationActivity.this, productName, productPrice,addedStock,stock);
                        list.setAdapter(adapter);

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
    boolean validate(){
        boolean valid = true;
        Pattern mPattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher mMatcher = mPattern.matcher(donarPan.getText().toString());
        boolean status=mMatcher.matches();
        if (donarAmount.getText().toString().isEmpty()){
            donarAmount.setError("Please Enter Amount");
            valid = false;
        }
        else if (donarDescription.getText().toString().isEmpty() ){
            donarDescription.setError("Please Enter Description");
            valid = false;
        }

        else if ( status==false && Integer.parseInt(donarAmount.getText().toString()) > 50000){
            donarPan.setError("Please Enter Pan Number");
            valid = false;
        }
        else if(donarName.getText().equals("")){
            donarName.setError("Donor Name cannot be empty ");
            valid = false;
        }
        else if(donarAddress.getText().equals("")){
            donarAddress.setError("ATG Address cannot be empty");
            valid = false;
        }
        return valid;
    }
    public void donationCash(){
        if(validate()){
            onBackPressed();
            donationCashlist.clear();
            donationCashlist.add(donarAmount.getText().toString());
            donationCashlist.add(donarDescription.getText().toString());
            if (donarGst.getText().toString().isEmpty()){
                donationCashlist.add("");
            }else {
                donationCashlist.add(donarPan.getText().toString());
            }

            if (donarPan.getText().toString().isEmpty()){
                donationCashlist.add("");
            }else {
                donationCashlist.add(donarGst.getText().toString());
            }
            donationCashlist.add(donarName.getText().toString());
            donationCashlist.add(donarAddress.getText().toString());

            Intent donationCashIntent = new Intent(DonationActivity.this, RazorPayActivity.class);
            donationCashIntent.putExtra("donationCashlist", donationCashlist);
            startActivity(donationCashIntent);
        }

    }
}
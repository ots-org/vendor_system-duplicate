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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.NetworkUtility.IResult;
import com.ortusolis.etaaranavkf.NetworkUtility.WebserviceController;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.adapter.DonationListAdapter;
import com.ortusolis.etaaranavkf.adapter.DonorDonationListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DonationStatus extends AppCompatActivity {
    Toolbar mToolbar;
    ActionBar action;
    List<String> donationStatus;
    Spinner spinnerStatus;
    ListView list;
    TextView noResult;
    String Status,roleId;
    String[] productName;
    String[] productQuantity;
    String[] DonarName;
    String[] DonarDiscription;
    String[] DonarcontactNumber;
    String[] Donaraddress;
    String[] Donaramount;
    String[] Donardate;
    String[] DonarQty;
    ArrayList<String> productNameList;
    ArrayList<String> productPriceList;
    ArrayList<String> productIdList;
    ArrayList<String> donarNameList;
    ArrayList<String> donarAddressList;
    ArrayList<String> donarQtyList;
    ArrayList<String> donarAmountList;
    ArrayList<String> donarContactNumnerList;
    ArrayList<String> donarDonationIdList;
    ArrayList<String> finalList;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_status);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        list=(ListView)findViewById(R.id.list);
        productNameList = new ArrayList<>();
        productPriceList = new ArrayList<>();
        productIdList = new ArrayList<>();
        donarNameList = new ArrayList<>();
        donarAddressList = new ArrayList<>();
        donarQtyList = new ArrayList<>();
        donarAmountList = new ArrayList<>();
        donarContactNumnerList = new ArrayList<>();
        donarDonationIdList= new ArrayList<>();
        finalList= new ArrayList<>();
        donationStatus = new ArrayList<>();
        sharedPreferences = getSharedPreferences("water_management",0);
        Status="";
        roleId="";
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        if (getIntent().hasExtra("assignRequest")){
            roleId=getIntent().getExtras().getString("assignRequest");
        }
        if(roleId.equals("3")){
            spinnerStatus.setVisibility(View.GONE);
            EmpAsignRequest();
        }
        donationStatus.add("Select Donation Status");
        donationStatus.add("Request - Cash");
        donationStatus.add("Request - Kind");
        donationStatus.add("Direct Cash");
        spinnerStatus.setAdapter(new ArrayAdapter(DonationStatus.this, android.R.layout.simple_spinner_dropdown_item, donationStatus));
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                switch (i) {
                    case 0:
                        Status="";
                        break;
                    case 1:
                        Status="Payment";
                        break;
                    case 2:
                        Status="donationPending";
                        break;
                    case 3:
                        Status="directDonation";
                        break;
                }
                if(!Status.equals("")) {
                    progressDialog = new ProgressDialog(DonationStatus.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
                    DonationStatusList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                if(Status.equals("directDonation")){
                    return;
                }
                finalList.clear();
                // TODO Auto-generated method stub
                list.getItemAtPosition(position);
                finalList.add(productNameList.get(position));
                finalList.add(productPriceList.get(position));
                finalList.add(donarNameList.get(position));
                finalList.add(donarAddressList.get(position));
                finalList.add(donarQtyList.get(position));
                finalList.add(donarAmountList.get(position));
                finalList.add(donarContactNumnerList.get(position));
                finalList.add(donarDonationIdList.get(position));
                finalList.add(productIdList.get(position));
                finalList.add(sharedPreferences.getString("userRoleId",""));
                finalList.add(Status);
                Intent intent = new Intent(DonationStatus.this, DonationListStatusDescription.class);
                intent.putExtra("finalList", finalList);
                if(roleId.equals("3")){
                    intent.putExtra("AssignedRequest","AssignedRequest");
                }
                startActivityForResult(intent,3533);
                Log.e("item",list.getItemAtPosition(position).toString());
            }
        });

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
            if(roleId.equals("3")){
                toolbarTitle.setText("Assigned Request");
            }  else {
                toolbarTitle.setText("Donation Status");
            }
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(roleId.equals("3")){
            EmpAsignRequest();
        }else {
            productNameList.clear();
            productPriceList.clear();
             productIdList.clear();
            donarNameList.clear();
            donarAddressList.clear();
            donarQtyList.clear();
            donarAmountList.clear();
            donarContactNumnerList.clear();
            donarDonationIdList.clear();
            finalList.clear();
            list.invalidateViews();
            progressDialog = new ProgressDialog(DonationStatus.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            DonationStatusList();
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
    public void DonationStatusList(){

        WebserviceController wss = new WebserviceController(DonationStatus.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("status",Status);
            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getDonationForUpdateStatus, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {

                    Log.e("DonatnList response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productList = responseData.getJSONArray("donationList");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {

                        productName= new String[productList.length()];
                        productQuantity= new String[productList.length()];
                        DonarName= new String[productList.length()];
                        DonarcontactNumber= new String[productList.length()];
                        Donaraddress= new String[productList.length()];
                        DonarQty= new String[productList.length()];
                        Donaramount= new String[productList.length()];
                        Donardate= new String[productList.length()];
                        DonarDiscription= new String[productList.length()];
                        for (int ProdCategory=0;ProdCategory<productList.length();ProdCategory++){
                            JSONObject productDetailsobject = productList.getJSONObject(ProdCategory);
                            JSONObject productDetailsobjecta = productDetailsobject.getJSONObject("productDetails");
                                JSONObject userDetailsobject = productDetailsobject.getJSONObject("userDetails");
                            DonarName[ProdCategory]=userDetailsobject.getString("firstName");

                            if(!Status.equals("directDonation")){
                                productName[ProdCategory]=productDetailsobjecta.getString("productName");
                                productNameList.add(productName[ProdCategory]);
                                productIdList.add(productDetailsobjecta.getString("productId"));
                                productPriceList.add( productDetailsobjecta.getString("productPrice"));
                                productQuantity[ProdCategory]=productDetailsobject.getString("donatedQty");
                            }else {
                                Donardate[ProdCategory]=productDetailsobject.getString("donationDate");
                                DonarDiscription[ProdCategory]=productDetailsobject.getString("donationDescription");
                            }

                            DonarName[ProdCategory]=userDetailsobject.getString("firstName");

                            DonarcontactNumber[ProdCategory]=userDetailsobject.getString("contactNo");
                            Donaraddress[ProdCategory]=userDetailsobject.getString("address1");
                            DonarQty[ProdCategory]=productDetailsobject.getString("donatedQty");
                            Donaramount[ProdCategory]=productDetailsobject.getString("donationAmount");
                            donarAddressList.add(userDetailsobject.getString("address1"));
                            donarNameList.add(userDetailsobject.getString("firstName"));
                            donarQtyList.add(productDetailsobject.getString("donatedQty"));
                            donarAmountList.add(productDetailsobject.getString("donationAmount"));
                            donarContactNumnerList.add(userDetailsobject.getString("contactNo"));
                            donarDonationIdList.add(productDetailsobject.getString("donationId"));
                        }
                        if(Status.equals("directDonation")){
                            DonorDonationListAdapter adapter=new DonorDonationListAdapter(DonationStatus.this, DonarName, DonarcontactNumber,Donaraddress, DonarQty, Donaramount,Donardate,DonarDiscription);
                            list.setAdapter(adapter);
                        }else {
                            DonationListAdapter adapter=new DonationListAdapter(DonationStatus.this, productName, productQuantity,DonarName);
                            list.setAdapter(adapter);
                        }
                        progressDialog.dismiss();
                    }
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

    public void EmpAsignRequest(){
        WebserviceController wss = new WebserviceController(DonationStatus.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("status", "assigneeRequest");

            jsonObject.put("assgineId",sharedPreferences.getString("userid", ""));

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getDonationForUpdateStatus, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("DonatnList response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productList = responseData.getJSONArray("donationList");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productName= new String[productList.length()];
                        productQuantity= new String[productList.length()];
                        DonarName= new String[productList.length()];
                        for (int ProdCategory=0;ProdCategory<productList.length();ProdCategory++){
                            JSONObject productDetailsobject = productList.getJSONObject(ProdCategory);
                            JSONObject productDetailsobjecta = productDetailsobject.getJSONObject("productDetails");
                            JSONObject userDetailsobject = productDetailsobject.getJSONObject("userDetails");
                            productName[ProdCategory]=productDetailsobjecta.getString("productName");
                            productQuantity[ProdCategory]=productDetailsobject.getString("donatedQty");
                            DonarName[ProdCategory]=userDetailsobject.getString("firstName");
                            productNameList.add(productName[ProdCategory]);
                            productPriceList.add( productDetailsobjecta.getString("productPrice"));
                            productIdList.add(productDetailsobjecta.getString("productId"));
                            donarAddressList.add(userDetailsobject.getString("address1"));
                            donarNameList.add(userDetailsobject.getString("firstName"));
                            donarQtyList.add(productDetailsobject.getString("donatedQty"));
                            donarAmountList.add(productDetailsobject.getString("donationAmount"));
                            donarContactNumnerList.add(userDetailsobject.getString("contactNo"));
                            donarDonationIdList.add(productDetailsobject.getString("donationId"));
                        }
                        DonationListAdapter adapter=new DonationListAdapter(DonationStatus.this, productName, productQuantity,DonarName);
                        list.setAdapter(adapter);
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
}
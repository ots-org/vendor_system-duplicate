package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.adapter.PendingRequestAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmRequest extends AppCompatActivity {
    private Toolbar toolbar;
    ActionBar action;
    ListView list;
    String[] productName;
    String[] productPrice;
    String[] description;
    ArrayList<String> productNameList;
    ArrayList<String> productPriceList;
    ArrayList<String> DescptionList;
    ArrayList<String> productIdList;
    ArrayList<String> productStatusList;
    ArrayList<String> productImage;
    ArrayList<String> finalList;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_request);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        productNameList = new ArrayList<>();
        productPriceList = new ArrayList<>();
        DescptionList = new ArrayList<>();
        productIdList = new ArrayList<>();
        productStatusList = new ArrayList<>();
        productImage = new ArrayList<>();
        finalList = new ArrayList<>();
        list=(ListView)findViewById(R.id.pendingList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                finalList.clear();
                // TODO Auto-generated method stub
//                if(position == 0) {
                //code specific to first list item
                list.getItemAtPosition(position);
                finalList.add(productNameList.get(position));
                finalList.add(productPriceList.get(position));
                finalList.add(DescptionList.get(position));
                finalList.add(productIdList.get(position));
                finalList.add(productStatusList.get(position));
                finalList.add(productImage.get(position));
                Intent confirmRequestProduct = new Intent(ConfirmRequest.this, AddProductActivity.class);
                confirmRequestProduct.putExtra("confirmRequestProduct", "confirmRequestProduct");
                confirmRequestProduct.putExtra("finalList", finalList);
                startActivityForResult(confirmRequestProduct,3533);
                Log.e("item",list.getItemAtPosition(position).toString());
            }
        });
        PendingRequest();

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
            toolbarTitle.setText("Confirm Product");

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
        list.invalidateViews();
        PendingRequest();
    }
    public void PendingRequest(){
        WebserviceController wss = new WebserviceController(ConfirmRequest.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("status", "pending");
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e(" response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productList = responseData.getJSONArray("productDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productName= new String[productList.length()];
                        productPrice= new String[productList.length()];
                        description= new String[productList.length()];
                        for (int ProdCategory=0;ProdCategory<productList.length();ProdCategory++){
                            JSONObject productDetailsobject = productList.getJSONObject(ProdCategory);
                            productName[ProdCategory]=productDetailsobject.getString("productName");
                            productPrice[ProdCategory]=productDetailsobject.getString("productPrice");
                            description[ProdCategory]=productDetailsobject.getString("productDescription");
                            productNameList.add(productName[ProdCategory]);
                            productPriceList.add( productPrice[ProdCategory]);
                            DescptionList.add(description[ProdCategory]);
                            productIdList.add(productDetailsobject.getString("productId"));
                            if(productDetailsobject.has("productImage")){
                                productImage.add(productDetailsobject.getString("productImage"));
                            }
                             productStatusList.add(productDetailsobject.getString("productStatus"));
                        }
                        PendingRequestAdapter adapter=new PendingRequestAdapter(ConfirmRequest.this, productName, productPrice,description);
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
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
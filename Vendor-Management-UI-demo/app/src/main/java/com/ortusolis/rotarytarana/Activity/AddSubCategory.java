package com.ortusolis.rotarytarana.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.GeneralResponse;
import com.ortusolis.rotarytarana.pojo.ProductDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddSubCategory extends AppCompatActivity {
    private Toolbar toolbar;
    ActionBar action;
    TextView selectCategory;
    EditText subCatagoryName,subCategoryDiscription;
    ProgressDialog progressDialog;
    List<String> productCatagoryNames, productCatagoryIdList;
    SharedPreferences sharedPreferences;
    String productNameCat = null;
    String productCust = null;
    Gson gson;
    Button AddSubCatagoryToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_category);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        selectCategory=findViewById(R.id.selectCategory);
        subCatagoryName=findViewById(R.id.selectSubCategory);
        subCategoryDiscription=findViewById(R.id.productDescriptionEdit);
        AddSubCatagoryToServer=findViewById(R.id.addSubCategoryToServer);

        gson = new Gson();
        ProductDetails productDetails;
        sharedPreferences = getSharedPreferences("water_management",0);
        productCatagoryNames = new ArrayList<>();
        productCatagoryIdList = new ArrayList<>();

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectProductcat();
            }
        });
        AddSubCatagoryToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddSubCatagoryToServer();
            }
        });

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
            toolbarTitle.setText("Add Sub-Category");
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


    public void SelectProductcat(){
        progressDialog = new ProgressDialog(AddSubCategory.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(AddSubCategory.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "category");
            jsonObject.put("searchvalue", "1");
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        productCatagoryNames.clear();
        productCatagoryIdList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                progressDialog.dismiss();
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productDetails = responseData.getJSONArray("productDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productCatagoryNames.clear();
                        productCatagoryIdList.clear();
                        for (int ProdCategory=0;ProdCategory<productDetails.length();ProdCategory++){
                            JSONObject productDetailsobject = productDetails.getJSONObject(ProdCategory);
                            productCatagoryNames.add(productDetailsobject.getString("productName"));
                            productCatagoryIdList.add(productDetailsobject.getString("productId"));
                        }
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddSubCategory.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product Category</font>"));
                        builderSingle.setCancelable(false);
                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productCatagoryNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        productCust = productCatagoryIdList.get(checkedItem);
                        productNameCat = productCatagoryNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                productCust = productCatagoryIdList.get(which);
                                productNameCat = productCatagoryNames.get(which);
                            }
                        });

                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectCategory.setText(productNameCat);
                                Toast.makeText(getApplicationContext(), "selected Product category is "+ productNameCat, Toast.LENGTH_LONG).show();
                            }
                        });

                        builderSingle.show();
                    }

                }
                catch (Exception e){
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

    boolean isValid(){
        boolean ret = true;
        if (TextUtils.isEmpty(selectCategory.getText().toString())){
            ret = false;
        }
        else if (TextUtils.isEmpty(subCatagoryName.getText().toString()))
        {
            ret = false;
        }
        else if (TextUtils.isEmpty(subCategoryDiscription.getText().toString())){
            ret = false;
        }

        return ret;
    }

    void setAddSubCatagoryToServer()  {
        if (isValid()) {

            progressDialog = new ProgressDialog(AddSubCategory.this);
            progressDialog.setMessage("Adding Product..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            WebserviceController wss = new WebserviceController(AddSubCategory.this);

            JSONObject requestObject = new JSONObject();

            byte[] byteArray = null;

            try   {
                JSONObject jsonObjectUser = new JSONObject();
                jsonObjectUser.put("userId",sharedPreferences.getString("userid", ""));
                jsonObjectUser.put("key","MainAndSub");
                jsonObjectUser.put("productCategoryId",productCust); // level 2 id
                JSONArray  jsonArray= new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productName", subCatagoryName.getText().toString());
                jsonObject.put("productDescription",subCategoryDiscription.getText().toString());
                jsonObject.put("productStatus", "active");
                jsonObject.put("productLevel", "2");
                jsonArray.put(jsonObject);
                jsonObjectUser.put("productDetails", jsonArray);
                requestObject.put("requestData", jsonObjectUser);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Log.e("request base64",requestObject.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            wss.postLoginVolley(Constants.addProductAndCategory, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e(" response",response);
                        GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            finish();
                            Toast.makeText(AddSubCategory.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Success" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(AddSubCategory.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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
        else {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show();
        }
    }
}

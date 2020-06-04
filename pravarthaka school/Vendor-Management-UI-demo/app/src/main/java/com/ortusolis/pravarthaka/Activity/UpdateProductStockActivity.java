package com.ortusolis.pravarthaka.Activity;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.pojo.DistributorResponse;
import com.ortusolis.pravarthaka.pojo.ProductDetails;
import com.ortusolis.pravarthaka.pojo.ProductsResponse;
import com.ortusolis.pravarthaka.pojo.ProductsStock;
import com.ortusolis.pravarthaka.pojo.UserInfo;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UpdateProductStockActivity extends AppCompatActivity {
    ActionBar action;
    Button cancel;
    //
    ProgressDialog progressDialog;
    //
    Gson gson;
    List<String> productIdList;
    List<String> productNames;
    List<String> productPriceList;
    String productStr = "1";
    String productStrPrice = "1";
    EditText quantityEdit;
    SharedPreferences sharedPreferences;
    Spinner spinnerUserType;
    String strProd = "";
    String strProdName = "";
    private Toolbar toolbar;
    Button updateStock;
    LinearLayout distributorCodeLayout;
    String distributorStr = "1";
    TextView distributorText;
    String strName = null;
    String strDistName = null;
    String strDist = null;
    List<String> userNames, userIdList;
    TextView presentStock;
    int initialQty = 0;
    boolean isgettingUsers = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_prod_stock);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        quantityEdit = (EditText) findViewById(R.id.quantityEdit);
        presentStock = findViewById(R.id.presentStock);
        updateStock = (Button) findViewById(R.id.updateStock);
        cancel = (Button) findViewById(R.id.cancel);
        distributorText = findViewById(R.id.distributorText);
        distributorCodeLayout = findViewById(R.id.distributorCodeLayout);

        setSupportActionBar(this.toolbar);
        gson = new Gson();
        sharedPreferences = getSharedPreferences("water_management", 0);
        productNames = new ArrayList();
        productIdList = new ArrayList();
        productPriceList = new ArrayList();
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
  //
            progressDialog = new ProgressDialog(UpdateProductStockActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //
        if (getSupportActionBar() != null) {

            this.action = getSupportActionBar();
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
            toolbarTitle.setText("Update Stock");
            action.setCustomView(viewActionBar, params);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getProducts();
        this.updateStock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductStock();
            }
        });
        this.cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")) {

            distributorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllRegister(true);


            }
        });
    }
        else{
        distributorText.setTextColor(getResources().getColor(R.color.textColor));
    }

        getAllRegister(false);
    }

    void getAllRegister(final boolean showPop){
        if (!isgettingUsers) {
            isgettingUsers = true;
            WebserviceController wss = new WebserviceController(UpdateProductStockActivity.this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("searchKey", "UserRoleId");
                jsonObject.put("searchvalue", "2");
                jsonObject.put("distributorId", "1");

                requestObject.put("requestData", jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            userNames.clear();
            userIdList.clear();

            wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    isgettingUsers = false;
                    try {
                        Log.e("getPlants response", response);

                        DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            strDist = "";

                            userNames.clear();
                            userIdList.clear();

                            for (UserInfo userInfo1 : distributorResponse.getResponseData().getUserDetails()) {
                                userNames.add(userInfo1.getFirstName());
                                userIdList.add(userInfo1.getUserId());

                                if (!showPop && sharedPreferences.getString("userid", "").equalsIgnoreCase(userInfo1.getUserId())) {
                                    strDist = userInfo1.getUserId();
                                    strDistName = userInfo1.getFirstName();
                                    //
                                    distributorText.setText(strDistName);
                                    //
                                    distributorStr = strDist;
                                }
                            }

                            if (distributorResponse.getResponseData().getUserDetails().isEmpty()) {
                                Toast.makeText(UpdateProductStockActivity.this, "No Results", Toast.LENGTH_LONG).show();
                            }

                            if (showPop) {

                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(UpdateProductStockActivity.this);
                                builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose distributor</font>"));

                                //First Step: convert ArrayList to an Object array.
                                Object[] objNames = userNames.toArray();

                                //Second Step: convert Object array to String array

                                final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                                int checkedItem = 0;
                                strDist = userIdList.get(checkedItem);
                                strDistName = userNames.get(checkedItem);
                                builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // user checked an item
                                        strDist = userIdList.get(which);
                                        strDistName = userNames.get(which);
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

                                        distributorText.setText(strDistName + " (" + strDist + ")");
                                        distributorStr = strDist;
                                    }
                                });

                                builderSingle.show();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    isgettingUsers = false;
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                    Toast.makeText(UpdateProductStockActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    void updateProductStock() {
        if (TextUtils.isEmpty(this.quantityEdit.getText().toString())) {
            Toast.makeText(this, "Enter quantity", Toast.LENGTH_LONG).show();
            return;
        }
        if (spinnerUserType.getSelectedItemPosition()==0){
            Toast.makeText(this, "Select Product to Update", Toast.LENGTH_LONG).show();
            return;
        }
        WebserviceController wss = new WebserviceController(this);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        //int prodStock = initialQty + Integer.parseInt(quantityEdit.getText().toString());

        try {
            jsonObject.put("productStockQty", quantityEdit.getText().toString());
            jsonObject.put("productStockStatus", "NEW");
            jsonObject.put("productStockAddDate", df.format(new Date()));
            jsonObject.put("productId", this.productStr);
            jsonObject.put("orderId", "");
            jsonObject.put("usersId", this.sharedPreferences.getString("userid", ""));
            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.addProductStock, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response", response);

                    ProductsResponse responseData = (ProductsResponse) UpdateProductStockActivity.this.gson.fromJson(response, ProductsResponse.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        spinnerUserType.setSelection(0);

                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(
                                quantityEdit.getWindowToken(), 0);

                    }
                    Toast.makeText(UpdateProductStockActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Context context = UpdateProductStockActivity.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
//                Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void getProducts() {
        WebserviceController wss = new WebserviceController(this);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", "1"/*sharedPreferences.getString("userid", "")*/);
            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.productNames.clear();
        this.productIdList.clear();
        this.productPriceList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("login response", response);

                    ProductsResponse responseData = (ProductsResponse) gson.fromJson(response, ProductsResponse.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        //
                        progressDialog.dismiss();
                        //
                        strProd = "";
                        strProdName = "";
                        productStrPrice = "";

                        productNames.clear();
                        productIdList.clear();
                        productPriceList.clear();

                        productNames.add("Select Product");
                        productIdList.add("0");
                        productPriceList.add("0");

                        Iterator it = responseData.getResponseData().getProductDetails().iterator();
                        while (it.hasNext()) {
                            ProductDetails productDetails = (ProductDetails) it.next();
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                            productPriceList.add(productDetails.getProductPrice());
                        }
                        /*if (!productIdList.isEmpty()) {
                            strProd = (String) productIdList.get(0);
                            strProdName = (String) productNames.get(0);
                            productStrPrice = (String) productPriceList.get(0);
                        }*/

                        spinnerUserType.setAdapter(new ArrayAdapter(UpdateProductStockActivity.this, android.R.layout.simple_spinner_dropdown_item, productNames));
                        spinnerUserType.setOnItemSelectedListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                                if (i==0) {
                                    presentStock.setText("0");
                                    quantityEdit.setText("");
                                }
                                else
                                {
                                    strProd = (String) productIdList.get(i);
                                    strProdName = (String) productNames.get(i);
                                    productStrPrice = (String) productPriceList.get(i);
                                    productStr = strProd;
                                    getProductStock(strProd);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        Toast.makeText(UpdateProductStockActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Context context = UpdateProductStockActivity.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
//                Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void getProductStock(String productId) {
        WebserviceController wss = new WebserviceController(this);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productId);
            jsonObject.put("distributorId", sharedPreferences.getString("userid", ""));
            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.Get_Product_Stock, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("login response", response);

                    ProductsStock responseData = gson.fromJson(response, ProductsStock.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                    initialQty = Integer.parseInt(responseData.getResponseData().getStockQuantity());
                    presentStock.setText(responseData.getResponseData().getStockQuantity());

                    } else {
                        presentStock.setText("0");
//                        Toast.makeText(UpdateProductStockActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    presentStock.setText("0");
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                presentStock.setText("0");
                Context context = UpdateProductStockActivity.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
//                Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }
}

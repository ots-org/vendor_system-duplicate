package com.ortusolis.water_distribution.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.water_distribution.NetworkUtility.Constants;
import com.ortusolis.water_distribution.NetworkUtility.IResult;
import com.ortusolis.water_distribution.NetworkUtility.WebserviceController;
import com.ortusolis.water_distribution.R;
import com.ortusolis.water_distribution.app.Config;
import com.ortusolis.water_distribution.pojo.DistributorResponse;
import com.ortusolis.water_distribution.pojo.ProductDetails;
import com.ortusolis.water_distribution.pojo.ProductsResponse;
import com.ortusolis.water_distribution.pojo.RegisterResponse;
import com.ortusolis.water_distribution.pojo.UserInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RegisterActivity extends AppCompatActivity implements LocationListener {

    Toolbar mToolbar;
    //
    ProgressDialog progressDialog;
    //
    ActionBar action;
    EditText firstNameEdit, lastNameEdit, phoneEdit, emailEdit, passwordEdit, confirmpasswordEdit, address1Edit, address2Edit, pincodeEdit;
    LinearLayout customerLayout, employeeRemoveLayout, distributorCodeLayoutLL;
    TextView productText, distributorCodeEdit;
    //
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    String userLat, userLong;


    //
    String strName = null;
    String strDistName = null;
    String strDist = null;
    Bundle bundle;
    boolean distributor = false;
    boolean employee = false;
    boolean admin = false;
    boolean customer = false;
    String distributorStr = "1";
    Gson gson;
    List<String> userNames, userIdList;
    List<String> productNames, productIdList;
    String productDistName = null;
    String productDist = null;
    String errorMsg = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_register);
        mToolbar = findViewById(R.id.toolbar);
        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        emailEdit = findViewById(R.id.emailEdit);
        address1Edit = findViewById(R.id.addressEdit);
        address2Edit = findViewById(R.id.address2Edit);
        pincodeEdit = findViewById(R.id.pincodeEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        confirmpasswordEdit = findViewById(R.id.confirmpasswordEdit);
        productText = findViewById(R.id.productText);
        customerLayout = findViewById(R.id.customerLayout);
        employeeRemoveLayout = findViewById(R.id.employeeRemoveLayout);
        distributorCodeLayoutLL = findViewById(R.id.distributorCodeLayoutLL);
        distributorCodeEdit = findViewById(R.id.distributorCodeEdit);

        //
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermision();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 0, this);
        //


        setSupportActionBar(mToolbar);
        gson = new Gson();

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        productNames = new ArrayList<>();
        productIdList = new ArrayList<>();

        bundle = getIntent().getExtras();
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF,0);

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

            if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as an employee")) {
                toolbarTitle.setText("Register as Employee");
                employeeRemoveLayout.setVisibility(View.GONE);
                distributorCodeLayoutLL.setVisibility(View.VISIBLE);
                employee = true;
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as an admin")) {
                toolbarTitle.setText("Register");
                admin = true;
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as distributor")) {
                distributor = true;
                toolbarTitle.setText("Register as distributor");
                customerLayout.setVisibility(View.GONE);
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as customer")) {
                customer = true;
                toolbarTitle.setText("Register as Customer");
                customerLayout.setVisibility(View.VISIBLE);
                distributorCodeLayoutLL.setVisibility(View.VISIBLE);
            }

            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        distributorCodeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //

                getAllRegister();


            }
        });

        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //

                getProducts();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.register:
                doneRegister();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean validate(){
        boolean valid = true;
        errorMsg = "";
        if (firstNameEdit.getText().toString().isEmpty()){
            firstNameEdit.setError("Please Enter First name");
            // errorMsg = "Please Enter First name";
            valid = false;
        }
        else if (lastNameEdit.getText().toString().isEmpty()){
            lastNameEdit.setError("Please Enter Last name");
            //errorMsg = "Please Enter Last name";
            valid = false;
        }
        else if (phoneEdit.getText().toString().isEmpty() ||  phoneEdit.getText().toString().length()!=10){
            phoneEdit.setError("Please Enter Valid Phone number");
            //errorMsg = "Please Enter Phone number";
            valid = false;
        }
        else if (address1Edit.getText().toString().isEmpty()){
            address1Edit.setError("Please Enter Address");
            //errorMsg = "Please Enter Address";
            valid = false;
        }
        else if (pincodeEdit.getText().toString().isEmpty() || pincodeEdit.getText().toString().length() < 6){
            pincodeEdit.setError("Please Enter valid pincode");
            //errorMsg = "Please Enter pincode";
            valid = false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()){
            emailEdit.setError("Please Enter valid Email Id");
            //errorMsg = "Please Enter Email Id";
            valid = false;
        }
        else if (passwordEdit.getText().toString().isEmpty() && !employee){
            passwordEdit.setError("Please Enter Password");
            //errorMsg = "Please Enter Password";
            valid = false;
        }
        else if (!passwordEdit.getText().toString().equals(confirmpasswordEdit.getText().toString()) && !employee){
            confirmpasswordEdit.setError("Password is not matching");
            //errorMsg = "Please Confirm password";
            valid = false;
        }
        /*else if (address2Edit.getText().toString().isEmpty()){
            valid = false;
        }*/
        return valid;
    }

    void doneRegister(){
        if (validate()) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Registration form have been submitted successfully.");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();*/

            customerRegister();

        }
        /*else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("You have not filled all the details.");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            },3000);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_register, menu);
        return true;
    }

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(RegisterActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "UserRoleId");
            jsonObject.put("searchvalue", "2");


            //
            jsonObject.put("userLat",userLat);
            jsonObject.put("userLong",userLong);

            //


            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        userNames.clear();
        userIdList.clear();

        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        //
                        progressDialog.dismiss();
                        //

                        strDist = "";

                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo: distributorResponse.getResponseData().getUserDetails()){
                            userNames.add(userInfo.getFirstName());
                            userIdList.add(userInfo.getUserId());
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(RegisterActivity.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterActivity.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose distributor</font>"));

                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = userNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        strDist = userIdList.get(checkedItem);;
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
                                //
                                distributorCodeEdit.setText(strDistName);
                                //
                                distributorStr = strDist;
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
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(RegisterActivity.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }


//
    //Getting the location


    //Raghuram
    private void requestPermision() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

//
    void customerRegister(){

        WebserviceController wss = new WebserviceController(RegisterActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName", firstNameEdit.getText().toString());
            jsonObject.put("lastName", lastNameEdit.getText().toString());
            jsonObject.put("emailId", emailEdit.getText().toString());
            jsonObject.put("status", "NEW");
            jsonObject.put("phonenumber", phoneEdit.getText().toString());
            jsonObject.put("address1", address1Edit.getText().toString());
            jsonObject.put("address2", address2Edit.getText().toString());
            jsonObject.put("pincode", pincodeEdit.getText().toString());
            jsonObject.put("deviceId", sharedPreferences.getString("regId",""));
            jsonObject.put("productId", productDist==null ? JSONObject.NULL : productDist);
            String userId = "2";

            if (customer){
                userId = "4";
            }
            else if (employee){
                userId = "3";
            }
            else if (distributor){
                userId = "2";
            }

            jsonObject.put("userRoleId", userId);
            jsonObject.put("password", passwordEdit.getText().toString());
            jsonObject.put("mappedTo", distributor ? "1" : distributorStr);

            //
            jsonObject.put("userLat",userLat);
            jsonObject.put("userLong",userLong);

            //

            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.AddUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response",response);

                    RegisterResponse areaResponse = new Gson().fromJson(response,RegisterResponse.class);

                    if (areaResponse.getResponseCode().equalsIgnoreCase("200")) {
                        onBackPressed();
                    }
                    Toast.makeText(RegisterActivity.this, areaResponse.getResponseDescription(), Toast.LENGTH_LONG).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(RegisterActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });

    }

    void getProducts(){

        WebserviceController wss = new WebserviceController(RegisterActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", distributor ? "1" : distributorStr);
            jsonObject.put("customerId", "");


            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        productNames.clear();
        productIdList.clear();

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);

                    ProductsResponse responseData = gson.fromJson(response,ProductsResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        //
                        progressDialog.dismiss();
                        //

                        productDist = "";

                        productNames.clear();
                        productIdList.clear();

                        for (ProductDetails productDetails: responseData.getResponseData().getProductDetails()){
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterActivity.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product</font>"));

                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        productDist = productIdList.get(checkedItem);
                        productDistName = productNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                productDist = productIdList.get(which);
                                productDistName = productNames.get(which);
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
                                //
                                productText.setText(productDistName);
                        //
                            }
                        });

                        builderSingle.show();

                    }
                    else {
                        Toast.makeText(RegisterActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(RegisterActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }
//
    @Override
    public void onLocationChanged(Location location) {
        userLat= String.valueOf(location.getLatitude());
       userLong= String.valueOf(location.getLongitude());
        Log.d("lat",userLat);
        Log.d("lat",userLong);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //
}
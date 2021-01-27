package com.ortusolis.evenkart.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.CheckBox;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ortusolis.evenkart.NetworkUtility.Constants;
import com.ortusolis.evenkart.NetworkUtility.IResult;
import com.ortusolis.evenkart.NetworkUtility.WebserviceController;
import com.ortusolis.evenkart.R;
import com.ortusolis.evenkart.app.Config;
import com.ortusolis.evenkart.pojo.DistributorResponse;
import com.ortusolis.evenkart.pojo.ProductDetails;
import com.ortusolis.evenkart.pojo.ProductsResponse;
import com.ortusolis.evenkart.pojo.UserInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RegisterActivity extends AppCompatActivity implements LocationListener {

    Toolbar mToolbar;
    ProgressDialog progressDialog;
    ActionBar action;
    EditText firstNameEdit, lastNameEdit, phoneEdit, emailEdit, passwordEdit, confirmpasswordEdit, address1Edit, address2Edit, pincodeEdit;
    LinearLayout customerLayout, employeeRemoveLayout, distributorCodeLayoutLL;
    TextView productText, distributorCodeEdit;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    String userLat, userLong;
    LatLng p1First = null;
    String lat,lng;
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
    CheckBox adminAccess,policy;

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
        customerLayout = findViewById(R.id.customerLayout);
        employeeRemoveLayout = findViewById(R.id.employeeRemoveLayout);
        distributorCodeLayoutLL = findViewById(R.id.distributorCodeLayoutLL);
        adminAccess = findViewById(R.id.adminAccess);
        policy = findViewById(R.id.policy);
        setSupportActionBar(mToolbar);
        gson = new Gson();
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        productNames = new ArrayList<>();
        productIdList = new ArrayList<>();
        bundle = getIntent().getExtras();
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF,0);
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("https://www.ortusolis.com/"); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
            }
        });
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

            if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as an Employee")) {
                toolbarTitle.setText("Register as Employee");
                employeeRemoveLayout.setVisibility(View.VISIBLE);
                distributorCodeLayoutLL.setVisibility(View.VISIBLE);
                employee = true;
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as an admin")) {
                toolbarTitle.setText("Register");
                admin = true;
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as Distributor")) {
                distributor = true;
                toolbarTitle.setText("Register as Distributor");
                customerLayout.setVisibility(View.GONE);
                adminAccess.setVisibility(View.VISIBLE);
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as Customer")) {
                customer = true;
                toolbarTitle.setText("Register as Customer");
                customerLayout.setVisibility(View.VISIBLE);
                distributorCodeLayoutLL.setVisibility(View.VISIBLE);
            }

            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
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
            valid = false;
        }
        else if (lastNameEdit.getText().toString().isEmpty()){
            lastNameEdit.setError("Please Enter Last name");
            valid = false;
        }
        else if (phoneEdit.getText().toString().isEmpty() ||  phoneEdit.getText().toString().length()!=10){
            phoneEdit.setError("Please Enter Valid Phone number");
            valid = false;
        }
        else if (address1Edit.getText().toString().isEmpty()){
            address1Edit.setError("Please Enter Address");
            valid = false;
        }
        else if (pincodeEdit.getText().toString().isEmpty() || pincodeEdit.getText().toString().length() < 6){
            pincodeEdit.setError("Please Enter valid pincode");
            valid = false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()){
            emailEdit.setError("Please Enter valid Email Id");
            valid = false;
        }
        else if (passwordEdit.getText().toString().isEmpty() && !employee){
            passwordEdit.setError("Please Enter Password");
            valid = false;
        }
        else if (!passwordEdit.getText().toString().equals(confirmpasswordEdit.getText().toString()) && !employee){
            confirmpasswordEdit.setError("Password is not matching");
            valid = false;
        }
        return valid;
    }

    void doneRegister(){
        if (validate()) {
            if(policy.isChecked()){
                customerRegister();
            }else {
                Toast.makeText(RegisterActivity.this, "please accept terms and conditions", Toast.LENGTH_LONG).show();
            }

        }
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
            jsonObject.put("userLat",userLat);
            jsonObject.put("userLong",userLong);
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

                        progressDialog.dismiss();
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
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>"+"Choose distributor"+"</font>"));

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

                        builderSingle.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
            }
        });
    }

    private void requestPermision() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    void customerRegister(){
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
        final ProgressDialog progressDialogRegistration;
        progressDialogRegistration = new ProgressDialog(RegisterActivity.this);
        progressDialogRegistration.setMessage("Please Wait...");
        progressDialogRegistration.setIndeterminate(false);
        progressDialogRegistration.setCancelable(false);
        progressDialogRegistration.show();
        try {
            Geocoder coder = new Geocoder(this);
            List<Address> address;
            String primryAddress= address1Edit.getText().toString();
            address = coder.getFromLocationName(primryAddress,5);
            if(address.size()!=0) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1First = new LatLng(location.getLatitude(), location.getLongitude());
                lat = p1First.latitude + "";
                lng = p1First.longitude + "";
            }else {
                progressDialogRegistration.dismiss();
                Toast.makeText(RegisterActivity.this, "check primary address", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (
        IOException e) {
            e.printStackTrace();
        }

        WebserviceController wss = new WebserviceController(RegisterActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        sharedPreferences.edit().putString("emailIdUser",emailEdit.getText().toString()).commit();
        try {
            jsonObject.put("userId", JSONObject.NULL );
            jsonObject.put("firstName", firstNameEdit.getText().toString());
            jsonObject.put("lastName", lastNameEdit.getText().toString());
            jsonObject.put("emailId", emailEdit.getText().toString());
            if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as an Employee")) {
                jsonObject.put("usrStatus", "pending");
                jsonObject.put("userAdminFlag", "0");
            }
            else if (bundle.containsKey("customer") && bundle.getString("customer").contains("Register as Distributor")) {
                jsonObject.put("usrStatus", "pending");
                if (adminAccess.isChecked()){
                    jsonObject.put("userAdminFlag", "1");
                }else {
                    jsonObject.put("userAdminFlag", "0");
                }
            }else{
                jsonObject.put("usrStatus", "active");
                jsonObject.put("userAdminFlag", "0");
            }

            jsonObject.put("usrPassword", passwordEdit.getText().toString());
            jsonObject.put("contactNo", phoneEdit.getText().toString());
            jsonObject.put("address1", address1Edit.getText().toString());
            jsonObject.put("address2", address2Edit.getText().toString());
            jsonObject.put("pincode", pincodeEdit.getText().toString());
            jsonObject.put("profilePic", "");
            jsonObject.put("mappedTo", distributor ? "1" : JSONObject.NULL );
            jsonObject.put("deviceId", sharedPreferences.getString("regId",""));
            jsonObject.put("userRoleId", userId);
            jsonObject.put("userLat", lat);
            jsonObject.put("userLong", lng);
            requestObject.put("requestData",jsonObject);
            Log.e("jsonObject",requestObject.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.addUser, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                progressDialogRegistration.dismiss();
                try {
                    Log.e("getPlants response",response);
                    JSONObject apiResponce = new JSONObject(response);
                    String  responseCode = apiResponce.getString("responseCode");
                    if (responseCode.equals("200")) {
                        SharedPreferences sharedPreferences;
                        sharedPreferences = getSharedPreferences("water_management",0);
                        sharedPreferences.edit().putString("userPhoneNumber",phoneEdit.getText().toString()).commit();
                        sharedPreferences.edit().putString("userEmailId",emailEdit.getText().toString()).commit();
                        String paymentPhoneNumber=sharedPreferences.getString("userPhoneNumber", "");
                        String paymentEmailId=sharedPreferences.getString("userEmailId", "");
                        Log.e("paymentPhoneNumber",paymentPhoneNumber);
                        Log.e("paymentEmailId",paymentEmailId);
                        onBackPressed();
                    }
                    Toast.makeText(RegisterActivity.this, apiResponce.getString("responseDescription"), Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressDialogRegistration.dismiss();
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

                        progressDialog.dismiss();
                        productDist = "";
                        productNames.clear();
                        productIdList.clear();

                        for (ProductDetails productDetails: responseData.getResponseData().getProductDetails()){
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterActivity.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>"+"Choose Product"+"</font>"));

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

                        builderSingle.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
            }
        });
    }

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
}

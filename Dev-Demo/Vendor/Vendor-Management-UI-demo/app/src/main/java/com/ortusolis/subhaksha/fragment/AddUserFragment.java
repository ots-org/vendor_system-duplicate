package com.ortusolis.subhaksha.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.cropper.CropImage;
import com.ortusolis.subhaksha.cropper.CropImageView;
import com.ortusolis.subhaksha.pojo.DistributorResponse;
import com.ortusolis.subhaksha.pojo.ProductDetails;
import com.ortusolis.subhaksha.pojo.ProductsResponse;
import com.ortusolis.subhaksha.pojo.UserInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddUserFragment extends  Fragment implements LocationListener {

    private static final Object REQUEST_LOCATION =1 ;
    Spinner spinnerUserType;
    TextView productText,distributorText,textSelect;
    FrameLayout photoLL;
    CheckBox userStatusCheck;
    LinearLayout distributorCodeLayout,productIdLayout;
    EditText firstNameEdit,lastNameEdit,phoneEdit,emailEdit,passwordEdit,address1Edit,address2Edit,pincodeEdit,productEdit,password2Edit;
   //code Raghuram OTS
    ProgressDialog progressDialog;
    //code Raghuram OTS

    //
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    private String userLat, userLong;
    //


    ArrayList<String> userTypeList = new ArrayList<>();
    UserInfo addUser = null;
    String strName = null;
    String strDistName = null;
    String strDist = null;
    Gson gson;
    String distributorStr = "";
    List<String> userNames, userIdList;
    String userRoleId = "1";
    //
    String flag="1";
    //
    Button addUserButton;
    String existingUserId = null;

    List<String> productNames, productIdList, productPriceList;
    String productDistName = null;
    String productDist = null;
    ArrayAdapter userAdapter;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    Bitmap uploadBitmap = null;
    String prodId = "";
    RelativeLayout passwordLayout;

    public static AddUserFragment newInstance() {

        Bundle args = new Bundle();

        AddUserFragment fragment = new AddUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_user,null);

        spinnerUserType = view.findViewById(R.id.spinnerUserType);
        distributorCodeLayout = view.findViewById(R.id.distributorCodeLayout);
        productIdLayout = view.findViewById(R.id.productIdLayout);
        distributorText = view.findViewById(R.id.distributorText);
//        productText = view.findViewById(R.id.productText);
        imageView = view.findViewById(R.id.imageView);
        textSelect = view.findViewById(R.id.textSelect);
        userStatusCheck = view.findViewById(R.id.userStatusCheck);
        photoLL = view.findViewById(R.id.photoLL);
        passwordLayout = view.findViewById(R.id.passwordLayout);

        firstNameEdit = view.findViewById(R.id.firstNameEdit);
        lastNameEdit = view.findViewById(R.id.lastNameEdit);
        phoneEdit = view.findViewById(R.id.phoneEdit);
        emailEdit = view.findViewById(R.id.emailEdit);
        address1Edit = view.findViewById(R.id.addressEdit);
        address2Edit = view.findViewById(R.id.address2Edit);
        pincodeEdit = view.findViewById(R.id.pincodeEdit);
//        productEdit = view.findViewById(R.id.productEdit);
        password2Edit = view.findViewById(R.id.password2Edit);
        addUserButton = view.findViewById(R.id.addUser);


        sharedPreferences = getActivity().getSharedPreferences("water_management",0);

        gson = new Gson();

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        productNames = new ArrayList<>();
        productIdList = new ArrayList<>();
        productPriceList = new ArrayList<>();
        Bundle bundle = getArguments();



        //
        locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           requestPermision();
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 0, this);
        //


        /*setSupportActionBar(toolbar);



        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);
            action.setTitle(addUser?"Add User":"Update User");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }*/

        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")) {
            //
            userTypeList.add("Select User Type");
            //
            userTypeList.add("Admin");
            distributorCodeLayout.setVisibility(View.GONE);
        }else{
            //
            userTypeList.add("Select User Type");
            //
        }

        if (!sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")) {

            userTypeList.add("Distributor");
            distributorCodeLayout.setVisibility(View.GONE);
        }
        userTypeList.add("Employee");
        userTypeList.add("Customer");

        userAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, userTypeList);

        spinnerUserType.setAdapter(userAdapter);

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (userTypeList.get(i).equalsIgnoreCase("Admin")){
                    distributorCodeLayout.setVisibility(View.GONE);
                    productIdLayout.setVisibility(View.GONE);
                    userRoleId = "1";
                }else if (userTypeList.get(i).equalsIgnoreCase("Distributor")){
                    distributorCodeLayout.setVisibility(View.GONE);
                    productIdLayout.setVisibility(View.GONE);
                    userRoleId = "2";
                }
                else if (userTypeList.get(i).equalsIgnoreCase("Employee")){
                    distributorCodeLayout.setVisibility(View.VISIBLE);
                    productIdLayout.setVisibility(View.GONE);
                    userRoleId = "3";
                }
                else if (userTypeList.get(i).equalsIgnoreCase("Customer")){
                    distributorCodeLayout.setVisibility(View.VISIBLE);
                    productIdLayout.setVisibility(View.VISIBLE);
                    userRoleId = "4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")) {

            distributorText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //progress code
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //

                    getAllRegister(true,"");


                }
            });
        }
        else{
            distributorText.setTextColor(getActivity().getResources().getColor(R.color.textColor));
        }

//        productText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //progress code
//                            progressDialog = new ProgressDialog(getActivity());
//                            progressDialog.setMessage("Loading...");
//                            progressDialog.setCancelable(false);
//                            progressDialog.show();
//                //
//                getProducts("");
//            }
//        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        if (bundle.containsKey("addUser")) {
            addUser = bundle.getParcelable("addUser");
            loadExistingUser();
        }

        getAllRegister(false,"");

        photoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getActivity(),AddUserFragment.this);
            }
        });

        return view;


    }

    void loadExistingUser(){
        //
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        flag="0";
        //
        if (addUser !=null){
            existingUserId = addUser.getUserId();
            firstNameEdit.setText(addUser.getFirstName()!=null? addUser.getFirstName():"");
            lastNameEdit.setText(addUser.getLastName()!=null?addUser.getLastName():"");
            emailEdit.setText(addUser.getEmailId()!=null?addUser.getEmailId():"");
            phoneEdit.setText(addUser.getContactNo()!=null?addUser.getContactNo():"");
            address1Edit.setText(addUser.getAddress1()!=null?addUser.getAddress1():"");
            address2Edit.setText(addUser.getAddress2()!=null?addUser.getAddress2():"");
            pincodeEdit.setText(addUser.getPincode()!=null?addUser.getPincode():"");
            userRoleId = addUser.getUserRoleId();
            distributorStr = addUser.getMappedTo();

            /*if (addUser.getCustomerProductDetails()!=null && !addUser.getCustomerProductDetails().isEmpty()){
                if (addUser.getCustomerProductDetails().get(0).getProductId()!=null && !TextUtils.isEmpty(addUser.getCustomerProductDetails().get(0).getProductId())){
                   prodId = addUser.getCustomerProductDetails().get(0).getProductId();
                }
                else if (addUser.getCustomerProductDetails().get(0).getCustomerProductId()!=null && !TextUtils.isEmpty(addUser.getCustomerProductDetails().get(0).getCustomerProductId())){
                    prodId = addUser.getCustomerProductDetails().get(0).getCustomerProductId();
                }

            }*/
            prodId = addUser.getProductId();

            password2Edit.setText(addUser.getUsrPassword());

            addUserButton.setText("Update user");
            passwordLayout.setVisibility(View.GONE);

            try {

                if (addUser.getProfilePic() != null) {
                    byte[] decodedString = Base64.decode(addUser.getProfilePic(), Base64.DEFAULT);
                    uploadBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                if (uploadBitmap != null) {
                    imageView.setImageBitmap(uploadBitmap);
                    textSelect.setVisibility(View.GONE);
                } else {
                    imageView.setImageResource(R.drawable.profle_icon);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            if(prodId!= null) {
                getAllRegister(false, prodId);
            }

            userStatusCheck.setChecked(addUser.getUsrStatus().equalsIgnoreCase("active"));

            if (addUser.getUserRoleId()!=null) {

                if (addUser.getUserRoleId().equals("1")) {
                    spinnerUserType.setSelection(userTypeList.indexOf("Admin"));
                }
                else if (addUser.getUserRoleId().equals("2")) {
                    spinnerUserType.setSelection(userTypeList.indexOf("Distributor"));
                }
                else if (addUser.getUserRoleId().equals("3")) {
                    spinnerUserType.setSelection(userTypeList.indexOf("Employee"));
                }
                else if (addUser.getUserRoleId().equals("4")) {
                    spinnerUserType.setSelection(userTypeList.indexOf("Customer"));
                }
            }

        }
    }


    boolean isValid(){
        boolean valid = true;
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
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()){
            emailEdit.setError("Please Enter valid Email Id");
            //errorMsg = "Please Enter Email Id";
            valid = false;
        }
        else if (phoneEdit.getText().toString().isEmpty() ||  phoneEdit.getText().toString().length()!=10){
            phoneEdit.setError("Please Enter Valid Phone number");
            //errorMsg = "Please Enter Phone number";
            valid = false;
        }
        else if (pincodeEdit.getText().toString().isEmpty() || pincodeEdit.getText().toString().length() < 6){
            pincodeEdit.setError("Please Enter valid pincode");
            //errorMsg = "Please Enter pincode";
            valid = false;
        }
        else if (address1Edit.getText().toString().isEmpty()){
            address1Edit.setError("Please Enter Address");
            //errorMsg = "Please Enter Address";
            valid = false;
        }else if (password2Edit.getText().toString().isEmpty()){
            password2Edit.setError("Please Enter Password");
            //errorMsg = "Please Enter Address";
            valid = false;
        }
//        else if((distributorStr==null || TextUtils.isEmpty(distributorStr)) && distributorCodeLayout.getVisibility()==View.VISIBLE){
//            Toast.makeText(getActivity(), "Select Distributor", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }


        return valid;
    }


    //Raghuram
    private void requestPermision() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //

    void addUser(){

    if (isValid()) {


      /*  progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating..");
        progressDialog.setCancelable(false);
        progressDialog.show()*/;

        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

//        byte[] byteArray = null;
//        if (uploadBitmap!=null) {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
//            byteArray = byteArrayOutputStream.toByteArray();
//        }
//        else {
//            uploadBitmap = BitmapFactory.decodeResource(getActivity().getResources(),
//                    R.drawable.profle_icon);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
//            byteArray = byteArrayOutputStream.toByteArray();
//        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", JSONObject.NULL);
//            jsonObject.put("userId", existingUserId==null ? JSONObject.NULL : existingUserId);
            jsonObject.put("firstName", firstNameEdit.getText().toString());
            jsonObject.put("lastName", lastNameEdit.getText().toString());
            jsonObject.put("emailId", emailEdit.getText().toString());
            jsonObject.put("usrStatus", existingUserId==null? "NEW" : userStatusCheck.isChecked()?"Active":"Inactive");
            jsonObject.put("usrPassword", password2Edit.getText().toString());
            jsonObject.put("contactNo", phoneEdit.getText().toString());
            jsonObject.put("address1", address1Edit.getText().toString());
            jsonObject.put("address2", address2Edit.getText().toString());
            jsonObject.put("pincode", pincodeEdit.getText().toString());
            jsonObject.put("profilePic", "");
//            jsonObject.put("profilePic", (uploadBitmap == null) ? JSONObject.NULL : Base64.encodeToString(byteArray, Base64.DEFAULT));
//            jsonObject.put("usersTimestamp", new Date().getTime());
//            jsonObject.put("usersCreated", new Date().getTime());
//            jsonObject.put("usersCreated", JSONObject.NULL);
//            jsonObject.put("registrationId", JSONObject.NULL);

            if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1") && addUser == null){
                jsonObject.put("mappedTo", "1");
            }
            else {
                jsonObject.put("mappedTo", distributorStr == null ? JSONObject.NULL : distributorStr);
            }

//            jsonObject.put("productPrice", productEdit.getText().toString().isEmpty()?JSONObject.NULL:productEdit.getText().toString());
//            jsonObject.put("productId", (productDist==null || productDist.isEmpty())? JSONObject.NULL : productDist);
            jsonObject.put("userRoleId", "4");

            //
//            jsonObject.put("userLat",userLat);
//            jsonObject.put("userLong",userLong);

            //

            jsonObject.put("deviceId", sharedPreferences.getString("regId",""));
            jsonObject.put("mappedTo",  JSONObject.NULL);
            //
            requestObject.put("requestData", jsonObject);



        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.addUser, requestObject.toString(), new IResult() {

            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("getPlants response", response);

                    /*progressDialog.show();*/

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), "User added Successfully", Toast.LENGTH_LONG).show();
                        firstNameEdit.setText("");
                        lastNameEdit.setText("");
                        emailEdit.setText("");
                        phoneEdit.setText("");
                        address1Edit.setText("");
                        address2Edit.setText("");
                        pincodeEdit.setText("");
                        password2Edit.setText("");
//                        productEdit.setText("");
//                        productText.setText("Select Product");
                        distributorText.setText("Select Distributor");
                        strName = null;
                        strDistName = null;
                        strDist = null;
                        existingUserId = null;
                        distributorStr = "";

                        getAllRegister(false,"");

                        Toast.makeText(getActivity(), distributorResponse.getResponseDescription(), Toast.LENGTH_LONG).show();

                        if (addUser!=null && addUser.getUserId()!=null){
                            getActivity().onBackPressed();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
              /*  progressDialog.dismiss();*/
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }
    else {
        Toast.makeText(getActivity(), "Enter all fields", Toast.LENGTH_LONG).show();
    }

    }

    void getAllRegister(final boolean showPop, final String prod){


        WebserviceController wss = new WebserviceController(getActivity());


        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "UserRoleId");
            jsonObject.put("searchvalue", "2");

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

if(flag.equals("0")){
       // cancel progress
                        progressDialog.dismiss();
                        //
}


                        strDist = "";

                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
                            userNames.add(userInfo1.getFirstName());
                            userIdList.add(userInfo1.getUserId());

                            if (!showPop){

                                if (addUser!=null) {

                                    if (addUser.getMappedTo() != null && addUser.getMappedTo().equalsIgnoreCase(userInfo1.getUserId())) {
                                        strDist = userInfo1.getUserId();
                                        strDistName = userInfo1.getFirstName();
                                        distributorText.setText(strDistName );
                                        distributorStr = strDist;

                                    }
                                }

                                if (sharedPreferences.getString("userid", "").equalsIgnoreCase(userInfo1.getUserId())){
                                    strDist = userInfo1.getUserId();
                                    strDistName = userInfo1.getFirstName();
                                    distributorText.setText(strDistName );
                                    distributorStr = strDist;
                                }


                            }

                        }

                        if (!TextUtils.isEmpty(prod)) {
                            getProducts(prodId);
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(getActivity(), "No Results", Toast.LENGTH_LONG).show();
                        }

                        if (showPop){

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
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
                                    //
                                    progressDialog.dismiss();
                                    //
                                    //dialog.dismiss();
                                }
                            });

                            builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    distributorText.setText(strDistName );
                                    distributorStr = strDist;

                                    //
                                    progressDialog.dismiss();
                                    //
                                }
                            });

                            builderSingle.show();
                        }

                    }
                    else{
                        if (!TextUtils.isEmpty(prod)) {

                            getProducts(prodId);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }


            @Override
            public void notifyError(VolleyError error) {

                if (!TextUtils.isEmpty(prod)) {
                    getProducts(prodId);
                }
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }

        });


    }



    void getProducts(final String prod){


        if (distributorStr==null || distributorStr.isEmpty()){

            Toast.makeText(getActivity(), "Select Distributor", Toast.LENGTH_LONG).show();
            return;
        }



        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");
            jsonObject.put("distributorId", distributorStr);
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
                        productPriceList.clear();

                        for (ProductDetails productDetails: responseData.getResponseData().getProductDetails()){
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                            productPriceList.add(productDetails.getTotalProductPrice());

                            if (!TextUtils.isEmpty(prod)) {
                                if (prod.equalsIgnoreCase(productDetails.getProductId())) {
                                    productDist = productDetails.getProductId();
                                    productDistName = productDetails.getProductName();
                                    //
//                                    productText.setText(productDistName );
                                    //
//                                    productEdit.setText(addUser.getProductPrice());

                        /*  // cancel progress
                        progressDialog.dismiss();
                        //*/
                                }
                            }

                        }


                        if (TextUtils.isEmpty(prod)) {

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product</font>"));

                            //First Step: convert ArrayList to an Object array.
                            Object[] objNames = productNames.toArray();

                            //Second Step: convert Object array to String array

                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                            int checkedItem = 0;
                            productDist = productIdList.get(checkedItem);
                            productDistName = productNames.get(checkedItem);
//                            productEdit.setText(productPriceList.get(checkedItem));
                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // user checked an item
                                    productDist = productIdList.get(which);
                                    productDistName = productNames.get(which);
//                                    productEdit.setText(productPriceList.get(which));
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
//                                    productEdit.setText("");
                                    //
//                                    productText.setText(productDistName );
                            //
                                }
                            });

                            builderSingle.show();
                        }

                    }
                    else {
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        //Getting the Bitmap from Gallery
                        uploadBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                        //Setting the Bitmap to ImageView

                        if (uploadBitmap != null) {
                            imageView.setImageBitmap(uploadBitmap);
                            textSelect.setVisibility(View.GONE);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(getActivity(), "Cropping failed", Toast.LENGTH_LONG).show();
                }
                break;
        }
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
}
//
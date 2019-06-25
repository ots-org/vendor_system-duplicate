package com.android.water_distribution.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.cropper.CropImage;
import com.android.water_distribution.cropper.CropImageView;
import com.android.water_distribution.pojo.DistributorResponse;
import com.android.water_distribution.pojo.ProductDetails;
import com.android.water_distribution.pojo.ProductsResponse;
import com.android.water_distribution.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddUserFragment extends Fragment {

    Spinner spinnerUserType;
    TextView productText,distributorText,textSelect;
    FrameLayout photoLL;
    LinearLayout distributorCodeLayout,productIdLayout;
    EditText firstNameEdit,lastNameEdit,phoneEdit,emailEdit,passwordEdit,address1Edit,address2Edit,pincodeEdit;
    ArrayList<String> userTypeList = new ArrayList<>();
    UserInfo addUser = null;
    String strName = null;
    String strDistName = null;
    String strDist = null;
    Gson gson;
    String distributorStr = "1";
    List<String> userNames, userIdList;
    String userRoleId = "1";
    Button addUserButton;
    String existingUserId = null;

    List<String> productNames, productIdList;
    String productDistName = null;
    String productDist = null;
    ArrayAdapter userAdapter;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    Bitmap uploadBitmap = null;
    ProgressDialog progressDialog;

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
        productText = view.findViewById(R.id.productText);
        imageView = view.findViewById(R.id.imageView);
        textSelect = view.findViewById(R.id.textSelect);
        photoLL = view.findViewById(R.id.photoLL);

        firstNameEdit = view.findViewById(R.id.firstNameEdit);
        lastNameEdit = view.findViewById(R.id.lastNameEdit);
        phoneEdit = view.findViewById(R.id.phoneEdit);
        emailEdit = view.findViewById(R.id.emailEdit);
        address1Edit = view.findViewById(R.id.addressEdit);
        address2Edit = view.findViewById(R.id.address2Edit);
        pincodeEdit = view.findViewById(R.id.pincodeEdit);
        addUserButton = view.findViewById(R.id.addUser);
        sharedPreferences = getActivity().getSharedPreferences("water_management",0);

        gson = new Gson();

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        productNames = new ArrayList<>();
        productIdList = new ArrayList<>();
        Bundle bundle = getArguments();


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
                if (userTypeList.get(i).equalsIgnoreCase("Distributor")){
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

        distributorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllRegister(true);


            }
        });

        productText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProducts();
            }
        });

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

        getAllRegister(false);

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

            addUserButton.setText("Update user");

            try {
                Bitmap decodedByte = null;

                if (addUser.getProfilePic() != null) {
                    byte[] decodedString = Base64.decode(addUser.getProfilePic(), Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                if (decodedByte != null) {
                    imageView.setImageBitmap(decodedByte);
                    textSelect.setVisibility(View.GONE);
                } else {
                    imageView.setImageResource(R.drawable.profle_icon);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }



            getAllRegister(false);

            if (addUser.getUserRoleId()!=null) {

                if (addUser.getUserRoleId().equals("2")) {
                    spinnerUserType.setSelection(0);
                }
                else if (addUser.getUserRoleId().equals("3")) {
                    spinnerUserType.setSelection(1);
                }
                else if (addUser.getUserRoleId().equals("4")) {
                    spinnerUserType.setSelection(2);
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
        }


        return valid;
    }

    void addUser(){

    if (isValid()) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        byte[] byteArray = null;
        if (uploadBitmap!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }
        else {
            uploadBitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.profle_icon);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", existingUserId==null ? JSONObject.NULL : existingUserId);
            jsonObject.put("firstName", firstNameEdit.getText().toString());
            jsonObject.put("lastName", lastNameEdit.getText().toString());
            jsonObject.put("emailId", emailEdit.getText().toString());
            jsonObject.put("usrStatus", "NEW");
            jsonObject.put("usrPassword", JSONObject.NULL);
            jsonObject.put("contactNo", phoneEdit.getText().toString());
            jsonObject.put("address1", address1Edit.getText().toString());
            jsonObject.put("address2", address2Edit.getText().toString());
            jsonObject.put("pincode", pincodeEdit.getText().toString());
            jsonObject.put("profilePic", (uploadBitmap == null) ? JSONObject.NULL : Base64.encodeToString(byteArray, Base64.DEFAULT));
            jsonObject.put("usersTimestamp", new Date().getTime());
            jsonObject.put("usersCreated", new Date().getTime());
            jsonObject.put("usersCreated", JSONObject.NULL);
            jsonObject.put("registrationId", JSONObject.NULL);

            if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                jsonObject.put("mappedTo", "1");
            }
            else {
                jsonObject.put("mappedTo", distributorStr == null ? JSONObject.NULL : distributorStr);
            }

            jsonObject.put("productPrice", JSONObject.NULL);
            jsonObject.put("productId", JSONObject.NULL);
            jsonObject.put("userRoleId", userRoleId);

            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.addUser, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    progressDialog.dismiss();

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        firstNameEdit.setText("");
                        lastNameEdit.setText("");
                        emailEdit.setText("");
                        phoneEdit.setText("");
                        address1Edit.setText("");
                        address2Edit.setText("");
                        pincodeEdit.setText("");
                        productText.setText("Select Product");
                        distributorText.setText("Select Distributor");
                        strName = null;
                        strDistName = null;
                        strDist = null;
                        existingUserId = null;

                        getAllRegister(false);

                        Toast.makeText(getActivity(), distributorResponse.getResponseDescription(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });
    }
    else {
        Toast.makeText(getActivity(), "Enter all fields", Toast.LENGTH_SHORT).show();
    }

    }

    void getAllRegister(final boolean showPop){
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
                                        distributorText.setText(strDistName + " (" + strDist + ")");
                                        distributorStr = strDist;
                                    }
                                }

                                if (sharedPreferences.getString("userid", "").equalsIgnoreCase(userInfo1.getUserId())){
                                    strDist = userInfo1.getUserId();
                                    strDistName = userInfo1.getFirstName();
                                    distributorText.setText(strDistName + " (" + strDist + ")");
                                    distributorStr = strDist;
                                }
                            }
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();
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
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getProducts(){

        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "All");
            jsonObject.put("searchvalue", "");

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

                        productDist = "";

                        productNames.clear();
                        productIdList.clear();

                        for (ProductDetails productDetails: responseData.getResponseData().getProductDetails()){
                            productNames.add(productDetails.getProductName());
                            productIdList.add(productDetails.getProductId());
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
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
                                productText.setText(productDistName+" ("+productDist+")");
                            }
                        });

                        builderSingle.show();

                    }
                    else {
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_SHORT).show();
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
}

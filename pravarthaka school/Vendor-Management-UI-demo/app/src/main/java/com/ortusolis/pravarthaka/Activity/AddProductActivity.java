package com.ortusolis.pravarthaka.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.entity.BufferedHttpEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.loopj.android.http.HttpGet;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.cropper.CropImage;
import com.ortusolis.pravarthaka.cropper.CropImageView;
import com.ortusolis.pravarthaka.fragment.ProductsFragment;
import com.ortusolis.pravarthaka.pojo.GeneralResponse;
import com.ortusolis.pravarthaka.pojo.ProductDetails;
import com.ortusolis.pravarthaka.pojo.ProductsResponse;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    byte[] compressedImg;
    ActionBar action;
    private Toolbar toolbar;
    ImageView imageView;
    EditText productName,productDescription,productPrice,addSubCategoryProduct;
    TextView textSelect,selectProductCategory,selectProductSubCategory;
    FrameLayout photoLL;
    Button addProduct,addSubCategoryButtton;
//    CheckBox box;
    Gson gson;
    ProductDetails productDetails;
    Bitmap uploadBitmap = null;
    ProgressDialog progressDialog;
    android.widget.RelativeLayout subCatLayout,catLayout;
    //
    String flagBulk="";
    Button bulkUpload;
    File tempFile;
    String UpdateYes="no";
    String imageSelected="no";
//    Spinner spinnerProductCategory,spinnerSubProductCategory;
    List<String> productCategory;
    List<String> productCategoryId;
    List<String> subProductCategory;
    List<String> subProductCategoryId;
    android.widget.RelativeLayout RelativeLayout;
    String selctedProductCategory, displayProductCategory;
    SharedPreferences sharedPreferences;
    String productNameCat = null;
    String productCust = null;
    String productCustMain = null;
    String subCat="";
    List<String> productCatagoryNames, productCatagoryIdList;
    LinearLayout linearLayout;
    //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        productName = findViewById(R.id.productNameEdit);
        productDescription = findViewById(R.id.productDescriptionEdit);
        productPrice = findViewById(R.id.productPriceEdit);
        addProduct = findViewById(R.id.addProduct);
        imageView = findViewById(R.id.imageView);
        textSelect = findViewById(R.id.textSelect);
        photoLL = findViewById(R.id.photoLL);
//        box = findViewById(R.id.box);
        //
        bulkUpload = findViewById(R.id.bulkUpload);
        productCategory = new ArrayList();
        productCategoryId= new ArrayList();
        subProductCategory = new ArrayList();
        subProductCategoryId = new ArrayList();
        selectProductCategory=findViewById(R.id.selectCategory);
        selectProductSubCategory=findViewById(R.id.selectSubCategory);
//        addSubCategoryButtton= findViewById(R.id.addSubCategoryButtton);
        RelativeLayout = findViewById(R.id.addSubCategoryLayout);
        productCatagoryNames = new ArrayList<>();
        productCatagoryIdList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("water_management",0);
//        getProductCatqgory();
        subCatLayout=findViewById(R.id.subCatLayout);
        catLayout=findViewById(R.id.catLayout);
        selctedProductCategory="no";
        displayProductCategory="yes";
        linearLayout = findViewById(R.id.bulkLayout);

        gson = new Gson();

        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("product"))
        productDetails = getIntent().getExtras().getParcelable("product");

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
            if (getIntent().hasExtra("product")){
                toolbarTitle.setText("Update Product");
                addProduct.setText("Update product");
                UpdateYes="yes";
            }
            else {
                toolbarTitle.setText("Add Product");
            }

            action.setCustomView(viewActionBar, params);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (productDetails!=null){
            displayProductCategory="no";



            subCatLayout.setVisibility(View.GONE);
            catLayout.setVisibility(View.GONE);
            productName.setText(productDetails.getProductName());
            productDescription.setText(productDetails.getProductDescription());
            productPrice.setText(productDetails.getProductPrice());
//            imageView.s


            if(productDetails.getProductType()!=null && productDetails.getProductType().equalsIgnoreCase("box")){
//                box.setChecked(true);
            }
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            if (productDetails.getProductImage()!=null){
                Picasso.get().invalidate((productDetails.getProductImage()));
                Picasso.get().load(productDetails.getProductImage()).into(imageView);
                    textSelect.setVisibility(View.GONE);
                    //
                Runnable runnable;
                Handler newHandler;

                newHandler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Getting the Bitmap from url
                            try {
                                URL url = new URL(productDetails.getProductImage());
                                uploadBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch(IOException e) {
                                System.out.println(e);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                runnable.run();



            }

        }

        photoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddProductActivity.this);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AddProductActivity.this);
                progressDialog.setMessage("Adding Product..");
                progressDialog.setCancelable(false);
                progressDialog.show();
                    addProducts();

            }
        });
        //
        selectProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selctedProductCategory="yes";
                SelectProductcat();
            }
        });
        selectProductSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selctedProductCategory.equals("yes")){
                    getProductSubCatqgory(productCustMain);
                }else {
                    Toast.makeText(AddProductActivity.this,"Select Product Category", Toast.LENGTH_LONG).show();
                }

            }
        });

        //
        bulkUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagBulk="bulk";
                if (TextUtils.isEmpty(selectProductCategory.getText().toString())){
                    Toast.makeText(AddProductActivity.this, "Select Category to upload", Toast.LENGTH_LONG).show();
                    return;

                }
                else if (TextUtils.isEmpty(selectProductSubCategory.getText().toString()))
                {
                    Toast.makeText(AddProductActivity.this, "Select Sub-Category to upload", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                // Update with mime types
                intent.setType("*/*");

                // Update with additional mime types here using a String[].
                intent.putExtra(Intent.EXTRA_MIME_TYPES, "text/csv");

                // Only pick openable and local files. Theoretically we could pull files from google drive
                // or other applications that have networked files, but that's unnecessary for this example.
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                // REQUEST_CODE = <some-integer>
                startActivityForResult(intent, 0);

               // addProducts();
            }
        });
//
//        getProductCatqgory();

        //
    }

    boolean isValid(){
        boolean ret = true;
        if (TextUtils.isEmpty(productName.getText().toString())){
            ret = false;

        }
        else if (TextUtils.isEmpty(productDescription.getText().toString()))
        {
            ret = false;
        }
        else if (TextUtils.isEmpty(productPrice.getText().toString())){
            ret = false;
        }
        else if(displayProductCategory.equals("yes")) {
         if (TextUtils.isEmpty(selectProductCategory.getText().toString())) {
                ret = false;
            } else if (TextUtils.isEmpty(selectProductSubCategory.getText().toString())) {
                ret = false;
            }
        }
        if(!ret){
            progressDialog.dismiss();
        }
        return ret;
    }

    void addProducts()  {
        if (isValid()) {
//            if (addSubCategoryProduct.getText().equals("")){
//
//            }else {
//                subCat=addSubCategoryProduct.getText().toString();
//            }


            WebserviceController wss = new WebserviceController(AddProductActivity.this);

            JSONObject requestObject = new JSONObject();

            byte[] byteArray = null;
            if (uploadBitmap!=null) {
//                uploadBitmap.toByteArray().length / 1024 > 400
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                uploadBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }
            try   {
            JSONObject jsonObjectUser = new JSONObject();
            if(UpdateYes.equals("yes")){
                jsonObjectUser.put("key","productUpdate");
            }else {
                jsonObjectUser.put("key","subAndProd");
            }
            jsonObjectUser.put("productCategoryId",productCust); // level 2 id
            JSONArray  jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
                if(UpdateYes.equals("yes")){
                    jsonObject.put("productId",productDetails.getProductId());
                }
                jsonObject.put("productName", productName.getText().toString());
                jsonObject.put("productDescription", productDescription.getText().toString());
                jsonObject.put("productStatus", "ACTIVE");
                jsonObject.put("productPrice", productPrice.getText().toString());
//                jsonObject.put("productType", box.isChecked()?"BOX":"");
                if(UpdateYes.equals("yes")){
                    String image="";

                    jsonObject.put("productImage", (imageSelected.equals("no")) ? Base64.encodeToString(byteArray, Base64.DEFAULT) : Base64.encodeToString(byteArray, Base64.DEFAULT));
                }else {
                    jsonObject.put("productImage", (uploadBitmap == null) ? JSONObject.NULL : Base64.encodeToString(byteArray, Base64.DEFAULT));
                }
                jsonObject.put("productLevel", "3");
                jsonArray.put(jsonObject);
                jsonObjectUser.put("productDetails", jsonArray);
                requestObject.put("requestData", jsonObjectUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
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
                            Log.e(" response", response);
                            progressDialog.dismiss();
                            GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
                            if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                                finish();
                                Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Success" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void notifyError(VolleyError error) {
                        progressDialog.dismiss();
                        Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                        Toast.makeText(AddProductActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
                    }
                });
        }
        else {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button h
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//
//        Toast.makeText(AddProductActivity.this, "select option", Toast.LENGTH_LONG).show();
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String flag="bulk";
//        if(data.getFlags() == 0)
//        {
//            return ;
//        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        imageSelected="yes";
                        //
                        flag="SelectProductPhoto";
                        //

                        //Getting the Bitmap from Gallery
                        uploadBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        //Setting the Bitmap to ImageView

                        if (uploadBitmap != null) {
                           //
                            File file = new File(result.getUri().getPath());
                            double  fileSize = (double) file.length();//in Bytes
                            if (fileSize > 100000 ) {  //Greater than 100kb
                                Log.d("file_size is high", fileSize + "   " + fileSize);
                                Toast.makeText(AddProductActivity.this, "Pick image less than 100kb", Toast.LENGTH_LONG).show();
                                return;
//                                doMiB();

//                        } else if (fileSize > 1024 && fileSize <500*1024 ) {
//                                Log.d("file_size is in Kb",  fileSize+"   "+ fileSize);
////                            doKiB();
                        } else {
                                Log.d("file_size is in Range",  fileSize+"   "+ fileSize);
//                            doByte();
                        }

                            imageView.setImageBitmap(uploadBitmap);
                            textSelect.setVisibility(View.GONE);
                            //
                            compressedImg = getFileDataFromDrawable(uploadBitmap);

                            //

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(AddProductActivity.this, "Cropping failed", Toast.LENGTH_LONG).show();
                }else {
                    try {
                    Toast.makeText(AddProductActivity.this, "Image not selected", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                e.printStackTrace();
            }
                }

//                break;
        }
        //
//        if(flagBulk.equals("bulk")){
//            try {
//
//                final Uri exclUri = data.getData();
//                final InputStream exclStream = getContentResolver().openInputStream(exclUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(exclStream);
//                String encodedExcl = encodeExcl(selectedImage,exclUri);
//                Log.e("encodedImage",encodedExcl);
//                //
////                FileInputStream fis = new FileInputStream(data.getDataString());
////                final Bitmap selectedImage =   BitmapFactory.decodeStream( fis);
////                String encodedExcl="";
////                try {
////                    byte[] bytes = exclStream.toString().getBytes();
////                    String strImage = data.getDataString();
//
////                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
////                    int bufferSize = 1024;
////                    byte[] buffer = new byte[bufferSize];
////
////                    // we need to know how may bytes were read to write them to the byteBuffer
////                    int len = 0;
////                    while ((len = exclStream.read(buffer)) != -1) {
////                        byteBuffer.write(buffer, 0, len);
////                    }
////                    byteBuffer.toByteArray();
////                    File file= new File(data.getDataString());
////                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
////                    byte[] decodedString = Base64.decode(strImage, Base64.DEFAULT);
////                    Bitmap bmp= BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
////                    encodedExcl= encodeExcl(bmp,exclUri);
////                    Log.e("encodedImage",encodedExcl);
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                }
////                //
//
////                try {
////                    final Bitmap selectedImage = BitmapFactory.decodeStream(exclStream);
////                    Log.e("BitmapFactory", BitmapFactory.decodeStream(exclStream).toString());
////                    encodedExcl= encodeExcl(selectedImage,exclUri);
////                    Log.e("encodedImage",encodedExcl);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//
////                InputStream(exclUri);
//
//
////
////                HttpGet httpRequest = new HttpGet(URI.create(data.getDataString()) );
////                HttpClient httpclient = new DefaultHttpClient();
////                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
////                HttpEntity entity = response.getEntity();
////                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
////                InputStream instream = bufHttpEntity.getContent();
//                //
//                WebserviceController wss = new WebserviceController(AddProductActivity.this);
//
//
//                JSONObject requestObject = new JSONObject();
//
//                  JSONObject jsonObject = new JSONObject();
//                try {
//                     jsonObject.put("base64ExcelString", encodedExcl);
//                     jsonObject.put("subcategoryId", productCust);
//                    requestObject.put("base64ExcelString",jsonObject);
//                    Log.e("request base64",requestObject.toString());
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
////                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
////                alertDialogBuilder.setMessage("Api request "+requestObject.toString());
////                        alertDialogBuilder.setPositiveButton("yes",
////                                new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface arg0, int arg1) {
////                                        Toast.makeText(AddProductActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
////                                    }
////                                });
////
////
////                AlertDialog alertDialog = alertDialogBuilder.create();
////                alertDialog.show();
//
//                wss.postLoginVolley(Constants.updateExcel, requestObject.toString(), new IResult() {
//                    @Override
//                    public void notifySuccess(String response, int statusCode) {
//                        try {
//                            Log.e("Excel response",response);
//
//                            GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
//                            if (responseData.getResponseCode().equalsIgnoreCase("200")) {
//                                finish();
//                                Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Success" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
//                            }else{
//                                Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void notifyError(VolleyError error) {
//                        Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                        Toast.makeText(AddProductActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
//                    }
//                });
//                //
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        //
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    //Bitmap imageBitmap;

          /*  if(uploadBitmap.equals("NEW")) {
        imageBitmap = ((BitmapDrawable)itemImage.getDrawable()).getBitmap();
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
    byteArrayToUpload = byteArrayOutputStream.toByteArray(); */
    //




    //
    private String encodeExcl(Bitmap bm,Uri exclUri) throws IOException {

        String base64 = "";
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ProductBulkUploadFormat.xlsx");


            byte[] buffer = new byte[(int) file.length() + 100];

            int length = new FileInputStream(String.valueOf(file)).read(buffer);
            Log.i("length", String.valueOf(length));
            base64 = Base64.encodeToString(buffer, 0, length,
                    Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
//        return base64;
    }


private static byte[] loadFile(File file) throws IOException {
    InputStream is = new FileInputStream(file);

    long length = file.length();
    if (length > Integer.MAX_VALUE) {
        // File is too large
    }
    byte[] bytes = new byte[(int)length];

    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
            && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
        offset += numRead;
    }

    if (offset < bytes.length) {
        throw new IOException("Could not completely read file "+file.getName());
    }

    is.close();
    return bytes;
}
    //
    public void getProductCatqgory(){
        //code
        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //
        WebserviceController wss = new WebserviceController(AddProductActivity.this);

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

        wss.postLoginVolley(Constants.GET_PRODUCT_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject responseData =obj.getJSONObject("responseData");
                    JSONArray productDetails = responseData.getJSONArray("productDetails");
                    obj.getString("responseCode");
                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        productCategory.clear();
                        productCategoryId.clear();
                        productCategory.add("Select Product Category");
                        for (int ProdCategory=0;ProdCategory<productDetails.length();ProdCategory++){
                            JSONObject productDetailsobject = productDetails.getJSONObject(ProdCategory);
                            productCategory.add(productDetailsobject.getString("productName"));
                            productCategoryId.add(productDetailsobject.getString("productId"));
                        }
                        progressDialog.dismiss();
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
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getProductSubCatqgory(String prdCatId){
        //code
        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //
        WebserviceController wss = new WebserviceController(AddProductActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "subcategory");
            jsonObject.put("searchvalue", prdCatId);
            jsonObject.put("distributorId", 1);
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
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddProductActivity.this);
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
                                //
                                selectProductSubCategory.setText(productNameCat);
                                subCat=productNameCat;
//                                getProductfinalList(productCust);
//                                getProducts();
                                Toast.makeText(getApplicationContext(), "selected Product Sub-Category is "+ productNameCat, Toast.LENGTH_LONG).show();
//                                //
//                                customerStrName = strCustName;
//                                customerStr = strCust;
//                                getProducts();
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
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void SelectProductcat(){
        //code
        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //
        WebserviceController wss = new WebserviceController(AddProductActivity.this);

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
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddProductActivity.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Product Catagory</font>"));
                        builderSingle.setCancelable(false);
                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = productCatagoryNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        productCustMain = productCatagoryIdList.get(checkedItem);
                        productCust = productCatagoryIdList.get(checkedItem);
                        productNameCat = productCatagoryNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                productCustMain = productCatagoryIdList.get(which);
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
                                //
                                selectProductCategory.setText(productNameCat);
                                getProductSubCatqgory(productCustMain);
                                Toast.makeText(getApplicationContext(), "selected Product category is "+ productNameCat, Toast.LENGTH_LONG).show();
//                                //
//                                customerStrName = strCustName;
//                                customerStr = strCust;
//                                getProducts();
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
//                Toast.makeText(getApplicationContext(), WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });
    }
//    private static InputStream fetch(String address) throws MalformedURLException,IOException {
//        HttpGet httpRequest = new HttpGet(URI.create(address) );
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
//        HttpEntity entity = response.getEntity();
//        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
//        InputStream instream = bufHttpEntity.getContent();
//        return instream;
//    }

}

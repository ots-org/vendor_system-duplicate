package com.ortusolis.water_distribution.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.water_distribution.NetworkUtility.Constants;
import com.ortusolis.water_distribution.NetworkUtility.IResult;
import com.ortusolis.water_distribution.NetworkUtility.WebserviceController;
import com.ortusolis.water_distribution.R;
import com.ortusolis.water_distribution.cropper.CropImage;
import com.ortusolis.water_distribution.cropper.CropImageView;
import com.ortusolis.water_distribution.pojo.GeneralResponse;
import com.ortusolis.water_distribution.pojo.ProductDetails;
import com.ortusolis.water_distribution.pojo.ProductsResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class AddProductActivity extends AppCompatActivity {
    byte[] compressedImg;
    ActionBar action;
    private Toolbar toolbar;
    ImageView imageView;
    EditText productName,productDescription,productPrice;
    TextView textSelect;
    FrameLayout photoLL;
    Button addProduct;
    CheckBox box;
    Gson gson;
    ProductDetails productDetails;
    Bitmap uploadBitmap = null;
    ProgressDialog progressDialog;
    //
    Button bulkUpload;
    File tempFile;
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
        box = findViewById(R.id.box);
        //
        bulkUpload = findViewById(R.id.bulkUpload);
        //

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
            }
            else {
                toolbarTitle.setText("Add Product");
            }

            action.setCustomView(viewActionBar, params);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (productDetails!=null){
            productName.setText(productDetails.getProductName());
            productDescription.setText(productDetails.getProductDescription());
            productPrice.setText(productDetails.getProductPrice());

            if(productDetails.getProductType()!=null && productDetails.getProductType().equalsIgnoreCase("box")){
                box.setChecked(true);
            }

            if (productDetails.getProductImage()!=null){

                if (productDetails.getProductImage()!=null) {
                    byte[] decodedString = Base64.decode(productDetails.getProductImage(), Base64.DEFAULT);
                    uploadBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                if (uploadBitmap != null) {
                    imageView.setImageBitmap(uploadBitmap);
                    textSelect.setVisibility(View.GONE);
                }
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
                addProducts();
            }
        });

        //
        bulkUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return ret;
    }

    void addProducts(){

        if (isValid()) {

            progressDialog = new ProgressDialog(AddProductActivity.this);
            progressDialog.setMessage("Adding Product..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            WebserviceController wss = new WebserviceController(AddProductActivity.this);

            JSONObject requestObject = new JSONObject();

            byte[] byteArray = null;
            if (uploadBitmap!=null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                uploadBitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }

            JSONObject jsonObject = new JSONObject();
            try   {
                jsonObject.put("productId", productDetails != null ? productDetails.getProductId() : "");
                jsonObject.put("productName", productName.getText().toString());
                jsonObject.put("productDescription", productDescription.getText().toString());
                jsonObject.put("productStatus", "ACTIVE");
                jsonObject.put("productPrice", productPrice.getText().toString());
                jsonObject.put("productTimestamp", new Date().getTime());
                jsonObject.put("productCreated", new Date().getTime());
                jsonObject.put("productType", box.isChecked()?"BOX":"");
                jsonObject.put("productImage", (uploadBitmap == null) ? JSONObject.NULL : Base64.encodeToString(byteArray, Base64.DEFAULT));

                requestObject.put("requestData", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            try {
                // jsonObject.put("base64ExcelString", encodedExcl);
                requestObject.put("base64ExcelString",compressedImg);
                //
                String encodedImage = Base64.encodeToString(compressedImg, Base64.DEFAULT);
                //
                Log.e("request base64",requestObject.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            wss.postLoginVolley(Constants.compressedImg, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e(" response",response);

                        GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                            finish();
                            Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Success" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(AddProductActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
                }
            });
            //
            wss.postLoginVolley(Constants.addorUpdateProduct, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        progressDialog.dismiss();

                        ProductsResponse responseData = gson.fromJson(response, ProductsResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            Intent i = new Intent("broadCastName");
                            // Data you need to pass to activity
                            i.putExtra("productId", productDetails != null ? productDetails.getProductId() : "");
                            i.putExtra("productName", productName.getText().toString());
                            i.putExtra("productDescription", productDescription.getText().toString());
                            i.putExtra("productPrice", productPrice.getText().toString());
                            sendBroadcast(i);
                            finish();

                        }

                        Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Login Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    progressDialog.dismiss();
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(AddProductActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
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
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String flag="bulk";
//        if(data.getFlags() == 0)
//        {
//            return ;
//        }
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    try {
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
                }

                break;
        }
        //
        if(flag.equals("bulk")){
            try {
                final Uri exclUri = data.getData();
                final InputStream exclStream = getContentResolver().openInputStream(exclUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(exclStream);
                String encodedExcl = encodeExcl(selectedImage,exclUri);
                Log.e("encodedImage",encodedExcl);

                //
                WebserviceController wss = new WebserviceController(AddProductActivity.this);


                JSONObject requestObject = new JSONObject();

                //  JSONObject jsonObject = new JSONObject();
                try {
                    // jsonObject.put("base64ExcelString", encodedExcl);
                    requestObject.put("base64ExcelString",encodedExcl);
                    Log.e("request base64",requestObject.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                wss.postLoginVolley(Constants.updateExcel, requestObject.toString(), new IResult() {
                    @Override
                    public void notifySuccess(String response, int statusCode) {
                        try {
                            Log.e("Excel response",response);

                            GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
                            if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                                finish();
                                Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Success" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void notifyError(VolleyError error) {
                        Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                        Toast.makeText(AddProductActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
                    }
                });
                //


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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


}

package com.android.water_distribution.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.cropper.CropImage;
import com.android.water_distribution.cropper.CropImageView;
import com.android.water_distribution.pojo.ProductDetails;
import com.android.water_distribution.pojo.ProductsResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class AddProductActivity extends AppCompatActivity {

    ActionBar action;
    private Toolbar toolbar;
    ImageView imageView;
    EditText productName,productDescription,productPrice;
    TextView textSelect;
    FrameLayout photoLL;
    Button addProduct;
    Gson gson;
    ProductDetails productDetails;
    Bitmap uploadBitmap = null;
    ProgressDialog progressDialog;

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

            if (productDetails.getProductImage()!=null){
                Bitmap decodedByte = null;

                if (productDetails.getProductImage()!=null) {
                    byte[] decodedString = Base64.decode(productDetails.getProductImage(), Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                if (decodedByte != null) {
                    imageView.setImageBitmap(decodedByte);
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

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            uploadBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("productId", productDetails != null ? productDetails.getProductId() : "");
                jsonObject.put("productName", productName.getText().toString());
                jsonObject.put("productDescription", productDescription.getText().toString());
                jsonObject.put("productStatus", "ACTIVE");
                jsonObject.put("productPrice", productPrice.getText().toString());
                jsonObject.put("productTimestamp", new Date().getTime());
                jsonObject.put("productCreated", new Date().getTime());
                jsonObject.put("productImage", (uploadBitmap == null) ? JSONObject.NULL : Base64.encodeToString(byteArray, Base64.DEFAULT));

                requestObject.put("requestData", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

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

                        Toast.makeText(AddProductActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Login Failed" : responseData.getResponseDescription(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    progressDialog.dismiss();
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(AddProductActivity.this, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
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
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        //Getting the Bitmap from Gallery
                        uploadBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        //Setting the Bitmap to ImageView

                        if (uploadBitmap != null) {
                            imageView.setImageBitmap(uploadBitmap);
                            textSelect.setVisibility(View.GONE);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(AddProductActivity.this, "Cropping failed", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

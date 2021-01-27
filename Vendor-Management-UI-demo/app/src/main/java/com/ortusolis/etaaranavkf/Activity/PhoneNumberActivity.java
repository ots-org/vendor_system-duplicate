package com.ortusolis.etaaranavkf.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.NetworkUtility.IResult;
import com.ortusolis.etaaranavkf.NetworkUtility.WebserviceController;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.pojo.ForgotPasswordResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PhoneNumberActivity extends AppCompatActivity {

    Button done;
    EditText enterPhoneNumber;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);
        enterPhoneNumber = findViewById(R.id.enterPhoneNumber);
        done = findViewById(R.id.done);
        sharedPreferences = getSharedPreferences("water_management",0);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enterPhoneNumber.getText().toString().isEmpty()) {

                    progressDialog = new ProgressDialog(PhoneNumberActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    login();
                }
            }
        });
    }

    void login(){

        WebserviceController wss = new WebserviceController(PhoneNumberActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobileNumber", enterPhoneNumber.getText().toString());

            requestObject.put("request",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.forgotPassword, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    progressDialog.dismiss();
                    Log.e("login response",response);

                    ForgotPasswordResponse responseData = new Gson().fromJson(response, ForgotPasswordResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        Intent intent = new Intent(PhoneNumberActivity.this,OTPActivity.class);
                        intent.putExtra("otp",responseData.getResponseData().getOtp());
                        intent.putExtra("userId",responseData.getResponseData().getUserId());
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(PhoneNumberActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();

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
}

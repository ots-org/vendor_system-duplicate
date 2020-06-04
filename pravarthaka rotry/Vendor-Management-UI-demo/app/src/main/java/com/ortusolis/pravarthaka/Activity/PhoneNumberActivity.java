package com.ortusolis.pravarthaka.Activity;

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
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.pojo.ForgotPasswordResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PhoneNumberActivity extends AppCompatActivity {

    Button done;
    EditText enterPhoneNumber;
    SharedPreferences sharedPreferences;

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
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(PhoneNumberActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }

}

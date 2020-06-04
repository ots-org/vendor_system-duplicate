package com.ortusolis.pravarthaka.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.pojo.GeneralResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PasswordActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    EditText newPassword,reEnterNewPassword;
   //
    EditText existingPassword;
    String value="";
    String valueGlobal="";
    String ChangePassword="ChangePassword";
    String changepasswordGlobal="changepasswordGlobal";
    SharedPreferences sharedPreferences;
    //
    Button submit;
    String userId = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mToolbar = findViewById(R.id.toolbar);
        newPassword = findViewById(R.id.enterPassword);
        reEnterNewPassword = findViewById(R.id.enterNewPassword);
        //
        existingPassword= findViewById(R.id.existingPassword);
        sharedPreferences = getSharedPreferences("water_management",0);
        //
        submit = findViewById(R.id.submit);
        Intent intent1 = getIntent();
        if (getIntent().hasExtra("userId")){
            userId = getIntent().getStringExtra("userId");
        }else if(ChangePassword.equals(intent1.getExtras().getString("ChangePassword"))) {
            //userid

            userId = sharedPreferences.getString("userid","");
            // recive intent

            value = intent1.getExtras().getString("ChangePassword");

//            if (value.equals("ChangePassword")||valueGlobal.equals("changepasswordGlobal")) {
            Log.e("","");
                existingPassword.setVisibility(View.VISIBLE);
//            }
        }else if(changepasswordGlobal.equals(intent1.getExtras().getString("changepasswordGlobal"))) {
            userId = sharedPreferences.getString("userid","");
            // recive intent


            valueGlobal  = intent1.getExtras().getString("changepasswordGlobal");
//            if (value.equals("ChangePassword")||valueGlobal.equals("changepasswordGlobal")) {
                existingPassword.setVisibility(View.VISIBLE);
//            }
        }
        //
        setSupportActionBar(mToolbar);

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
            toolbarTitle.setText("Password");
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    doneRegister();

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
        }
        return super.onOptionsItemSelected(item);
    }

    boolean validate(){
        boolean valid = true;
        //
        if (value.equals(ChangePassword)||valueGlobal.equals(changepasswordGlobal)) {
            if (existingPassword.getText().toString().isEmpty()){
                valid = false;
            }
        }
        //

        if (newPassword.getText().toString().isEmpty()){
            valid = false;
        }
        else if (reEnterNewPassword.getText().toString().isEmpty()){
            valid = false;
        }
        else if (!newPassword.getText().toString().equals(reEnterNewPassword.getText().toString())){
            valid = false;
        }

        return valid;
    }

    void doneRegister(){
        if (validate()) {
            //
            if (value.equals(ChangePassword)||valueGlobal.equals(changepasswordGlobal)) {
                ChangePassword();
            }
            else {
                login();
            }
            //
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
            builder.setMessage("Enter Passwords");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            },3000);
        }
    }

    void login(){

        WebserviceController wss = new WebserviceController(PasswordActivity.this);


        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userId);
            jsonObject.put("pasword", newPassword.getText().toString());
            requestObject.put("request",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.changePassword, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);

                    GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        sharedPreferences.edit().putString("password",newPassword.getText().toString()).commit();
                        finish();

                    }
                    Toast.makeText(PasswordActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();



                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(PasswordActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }
    //create function change password Raghuram ots
    void ChangePassword(){

        WebserviceController wss = new WebserviceController(PasswordActivity.this);


        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("newPassword", newPassword.getText().toString());
            jsonObject.put("oldPassword", existingPassword.getText().toString());
            requestObject.put("updatePassword",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getupdatePassword, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);

                    GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        finish();
                        Toast.makeText(PasswordActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Success" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(PasswordActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(PasswordActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }
//code raghuram ots

}

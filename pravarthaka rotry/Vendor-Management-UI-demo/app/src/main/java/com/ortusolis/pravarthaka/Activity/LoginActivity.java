package com.ortusolis.pravarthaka.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.app.Config;
import com.ortusolis.pravarthaka.pojo.LoginResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity{
    //
    Spinner spinnerctrl;
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    ProgressDialog progressDialog;
    //
    TextView register,forgotPassword,lang;
    Button login;
    LinearLayout rootView;
    CheckBox rememberMe;
    EditText enterUsername,enterPassword;
    String strName = "";
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesFCM;
    Gson gson;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.register);
        forgotPassword = findViewById(R.id.forgotPassword);
        login = findViewById(R.id.login);
        enterUsername = findViewById(R.id.enterUsername);
        enterPassword = findViewById(R.id.enterPassword);
        rootView = findViewById(R.id.rootView);
        rememberMe = findViewById(R.id.rememberMe);

        gson = new Gson();

        /*FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rootView)
                .setTransitionDuration(4000)
                .start();*/

        sharedPreferences = getSharedPreferences("water_management",0);
        sharedPreferencesFCM = getSharedPreferences(Config.SHARED_PREF,0);

        if (sharedPreferencesFCM.contains("username") && sharedPreferencesFCM.contains("password")){
            enterUsername.setText(sharedPreferencesFCM.getString("username",""));
            enterPassword.setText(sharedPreferencesFCM.getString("password",""));
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(LoginActivity.this);
                builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose your choice</font>"));

                // add a radio button list
                final String[] strOpts = {"Register as customer"/*, "Register as distributor", "Register as an admin", "Register as an employee"*/};
                int checkedItem = 0;
                strName = strOpts[0];
                builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                        strName = strOpts[which];
                    }
                });

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
                        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                        intent.putExtra("customer",strName);
                        startActivity(intent);
                    }
                });

                builderSingle.show();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PhoneNumberActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (isValid()){
                   login();

                  /*sharedPreferences.edit().putString("userid","1").commit();
                  sharedPreferences.edit().putString("userRoleId","1").commit();
                  sharedPreferences.edit().putBoolean("login",true).commit();

                  Intent intent = new Intent(LoginActivity.this,PhoneNumberActivity.class);
                  //intent.putExtra("mobile",responseData.getResponseData().getCustomer().getPhone1());
                  startActivity(intent);

                  finish();*/
                  /*if (enterPassword.getText().toString().equalsIgnoreCase("123456")) {
                      Intent intent = null;
                      if (enterUsername.getText().toString().contains("dist")) {
                          intent = new Intent(LoginActivity.this, DistributorActivity.class);
                          sharedPreferences.edit().putBoolean("distributor",true).commit();
                      }
                      else {
                          intent = new Intent(LoginActivity.this, PhoneNumberActivity.class);
                      }
                      sharedPreferences.edit().putBoolean("login",true).commit();
                      startActivity(intent);
                      finish();
                  }*/
              }
              else {
                  final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                  builder.setMessage("Please Enter Username and Password.");
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
        });

        // Ask the user for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // permission denied, boo!

                requestPermissions(
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }

        }
        //spinner
        currentLanguage = getIntent().getStringExtra(currentLang);

        spinnerctrl  = (Spinner) findViewById(R.id.language_spinner);

        List<String> Language_array = new ArrayList<String>();

        Language_array.add("Select language");
        Language_array.add("English");
        Language_array.add("Kannada");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Language_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerctrl.setAdapter(adapter);

        spinnerctrl .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,  View view, int position, long id) {
                switch (position) {
                    case 1:
                        Toast.makeText(adapterView.getContext(),
                                "You have selected English", Toast.LENGTH_SHORT)
                                .show();
                        setLocale("en");
                        break;
                    case 2:
                        Toast.makeText(adapterView.getContext(),
                                "You have selected Kannada", Toast.LENGTH_SHORT)
                                .show();
                        setLocale("kn");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
       /* Locale locale = new Locale("kn");
        Locale.setDefault(locale);
        // Create a new configuration object
        Configuration config = new Configuration();
        // Set the locale of the new configuration
        config.locale = locale;
        // Update the configuration of the Accplication context
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }


    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        refresh.putExtra(currentLang, lang);
        startActivity(refresh);

        }
    //spinner


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                //showFileChooser();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    boolean isValid(){
        if (enterUsername.getText().toString().isEmpty() || enterPassword.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    void login(){
        //code
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //
        WebserviceController wss = new WebserviceController(LoginActivity.this);


        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", enterUsername.getText().toString());
            jsonObject.put("password", enterPassword.getText().toString());
            jsonObject.put("deviceId", sharedPreferencesFCM.getString("regId",""));

            requestObject.put("requestData",jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.LOGIN_API, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response",response);

                    LoginResponse responseData = gson.fromJson(response,LoginResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        //
                        progressDialog.dismiss();
                        //

                        if (rememberMe.isChecked()) {
                            sharedPreferencesFCM.edit().putString("username", enterUsername.getText().toString()).commit();
                            sharedPreferencesFCM.edit().putString("password", enterPassword.getText().toString()).commit();
                        }

                        //sharedPreferences.edit().putString("mobile",responseData.getResponseData().getCustomer().getPhone1()).commit();
                        sharedPreferences.edit().putString("userid",responseData.getResponseData().getUserDetails().getUserId()).commit();
                        sharedPreferences.edit().putString("username",responseData.getResponseData().getUserDetails().getFirstName()+" "+responseData.getResponseData().getUserDetails().getLastName()).commit();
                        sharedPreferences.edit().putString("userRoleId",responseData.getResponseData().getUserDetails().getUserRoleId()).commit();
                        //
                        sharedPreferences.edit().putString("userSwitchRoleId",responseData.getResponseData().getUserDetails().getUserRoleId()).commit();
                        //
                        sharedPreferences.edit().putString("distId",responseData.getResponseData().getUserDetails().getDistributorId()).commit();
                        sharedPreferences.edit().putString("profilePic",responseData.getResponseData().getUserDetails().getProfilePic()).commit();
                        sharedPreferences.edit().putBoolean("login",true).commit();

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        //intent.putExtra("mobile",responseData.getResponseData().getCustomer().getPhone1());
                        startActivity(intent);

                        finish();

                    }
                    else {
                        Toast.makeText(LoginActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        //
                        progressDialog.dismiss();
                        //
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
                Toast.makeText(LoginActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }
}

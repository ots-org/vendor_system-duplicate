package com.ortusolis.rotarytarana.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.app.Config;
import com.ortusolis.rotarytarana.pojo.LoginResponse;

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
    TextView register,forgotPassword,lang,ots,skip;
    Button login;
    LinearLayout rootView;
    CheckBox rememberMe;
    EditText enterUsername,enterPassword;
    String strName = "";
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesFCM;
    Gson gson;
    View v;

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
        ots = findViewById(R.id.ots);
        skip = findViewById(R.id.skip);
        gson = new Gson();
        v= this.findViewById(android.R.id.content);
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
                final String[] strOpts = {"Register as Donor", "Register as Facilitator", "Register as an Partner"}; //, "Register as an admin"
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
        ots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.ortusolis.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("userRoleId","000").commit();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (isValid()){
                   login();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        sharedPreferences.edit().putString("userPhoneNumberPayment",enterUsername.getText().toString()).commit();
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
                        progressDialog.dismiss();
                        if(responseData.getResponseData().getUserDetails().getUserRoleId().equals("5")){
                            Toast.makeText(LoginActivity.this, "No access for this user", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (rememberMe.isChecked()) {
                            sharedPreferencesFCM.edit().putString("username", enterUsername.getText().toString()).commit();
                            sharedPreferencesFCM.edit().putString("password", enterPassword.getText().toString()).commit();
                        }

                        sharedPreferences.edit().putString("userid",responseData.getResponseData().getUserDetails().getUserId()).commit();
                        sharedPreferences.edit().putString("username",responseData.getResponseData().getUserDetails().getFirstName()+" "+responseData.getResponseData().getUserDetails().getLastName()).commit();
                        sharedPreferences.edit().putString("userRoleId",responseData.getResponseData().getUserDetails().getUserRoleId()).commit();
                        sharedPreferences.edit().putString("emailIdUser", responseData.getResponseData().getUserDetails().getEmailId()).commit();
                        sharedPreferences.edit().putString("userSwitchRoleId",responseData.getResponseData().getUserDetails().getUserRoleId()).commit();
                        sharedPreferences.edit().putString("userFirstName",responseData.getResponseData().getUserDetails().getFirstName()).commit();
                        sharedPreferences.edit().putString("userLastName",responseData.getResponseData().getUserDetails().getLastName()).commit();
                        sharedPreferences.edit().putString("userPhoneNumber",responseData.getResponseData().getUserDetails().getContactNo()).commit();
                        sharedPreferences.edit().putString("userAddress1",responseData.getResponseData().getUserDetails().getAddress1()).commit();
                        sharedPreferences.edit().putString("userAddress2",responseData.getResponseData().getUserDetails().getAddress2()).commit();
                        sharedPreferences.edit().putString("userPincode",responseData.getResponseData().getUserDetails().getPincode()).commit();
                        sharedPreferences.edit().putString("password",responseData.getResponseData().getUserDetails().getPassword()).commit();
                        sharedPreferences.edit().putString("userAdminFlag",responseData.getResponseData().getUserDetails().getUerAdminFlag()).commit();

                       Geocoder coder = new Geocoder(getBaseContext());
                        List<Address> address;
                        address = coder.getFromLocationName(responseData.getResponseData().getUserDetails().getAddress1(),5);
                        if(address.size()!=0){
                            Address location=address.get(0);
                            location.getLatitude();
                            location.getLongitude();
                            LatLng p1First = new LatLng(location.getLatitude(), location.getLongitude() );
                            String lat= p1First.latitude+"";
                            String lng=p1First.longitude+"";
                            sharedPreferences.edit().putString("userlatitudeProfile", lat).commit();
                            sharedPreferences.edit().putString("userlangitudeProfile",lng).commit();
                        }


                        Geocoder coderSecond = new Geocoder(getBaseContext());
                        List<Address> addressSecond;
                        addressSecond = coderSecond.getFromLocationName(responseData.getResponseData().getUserDetails().getAddress2(),5);
                        if(addressSecond.size()!=0){
                            Address locationSecond=addressSecond.get(0);
                            locationSecond.getLatitude();
                            locationSecond.getLongitude();
                            LatLng p1Second = new LatLng(locationSecond.getLatitude(), locationSecond.getLongitude() );
                            String latSecond= p1Second.latitude+"";
                            String lngSecond=p1Second.longitude+"";
                            sharedPreferences.edit().putString("userlatitudeSecondProfile", latSecond).commit();
                            sharedPreferences.edit().putString("userlangitudeSecondProfile",lngSecond).commit();
                        }

                        sharedPreferences.edit().putString("distId",responseData.getResponseData().getUserDetails().getDistributorId()).commit();
                        sharedPreferences.edit().putString("profilePic",responseData.getResponseData().getUserDetails().getProfilePic()).commit();
                        sharedPreferences.edit().putBoolean("login",true).commit();
                        hideKeybaord(v);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, TextUtils.isEmpty(responseData.getResponseDescription())? "Login Failed" :responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(LoginActivity.this, WebserviceController.returnErrorMessage(error)+"", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}

package com.ortusolis.evenkart.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.ortusolis.evenkart.NetworkUtility.Constants;
import com.ortusolis.evenkart.NetworkUtility.IResult;
import com.ortusolis.evenkart.NetworkUtility.WebserviceController;
import com.ortusolis.evenkart.R;
import com.ortusolis.evenkart.pojo.DistributorResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements OnMapReadyCallback {
    SharedPreferences sharedPreferences;
    EditText firstNameEdit,lastNameEdit,phoneEdit,emailEdit,address1Edit,address2Edit,pincodeEdit;
    GoogleMap googleMap;
    ActionBar action;
    private Toolbar toolbar;
    Button updateUserDetails;
    Gson gson;
    LatLng p1First = null;
    LatLng p1Second = null;
    String lat,lng, latSecond,lngSecond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        sharedPreferences = getSharedPreferences("water_management",0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit =findViewById(R.id.lastNameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        emailEdit = findViewById(R.id.emailEdit);
        address1Edit = findViewById(R.id.addressEdit);
        address2Edit = findViewById(R.id.address2Edit);
        pincodeEdit = findViewById(R.id.pincodeEdit);
        updateUserDetails= findViewById(R.id.updateUserDetails);
        firstNameEdit.setText( sharedPreferences.getString("userFirstName",""));
        lastNameEdit.setText( sharedPreferences.getString("userLastName",""));
        emailEdit.setText( sharedPreferences.getString("emailIdUser",""));
        phoneEdit.setText(sharedPreferences.getString("userPhoneNumber",""));
        address1Edit.setText(sharedPreferences.getString("userAddress1",""));
        address2Edit.setText(sharedPreferences.getString("userAddress2",""));
        pincodeEdit.setText( sharedPreferences.getString("userPincode",""));
        lat=sharedPreferences.getString("userlatitudeProfile","");
        lng=sharedPreferences.getString("userlangitudeProfile","");
        latSecond=sharedPreferences.getString("userlatitudeSecondProfile","");
        lngSecond=sharedPreferences.getString("userlangitudeSecondProfile","");;

        gson = new Gson();

        updateUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDetails();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPrimary);
        mapFragment.getMapAsync( this);

        setSupportActionBar(toolbar);
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

            toolbarTitle.setText("Profile");
            action.setCustomView(viewActionBar, params);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
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
    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng loaction) {
            }
        });
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                           Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                if(arg0.getTitle().equals("Primary delivery location")){
                    lat= arg0.getPosition().latitude+"";
                    lng=arg0.getPosition().longitude+"";
                }else{
                    latSecond= arg0.getPosition().latitude+"";
                    lngSecond=arg0.getPosition().longitude+"";
                }
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;
        Geocoder coderSecond = new Geocoder(this);
        List<Address> addressSecond;
        Barcode.GeoPoint p2 = null;
        String primryAddress= address1Edit.getText().toString();
        String secondaryAddress= address2Edit.getText().toString();
        try {
            address = coder.getFromLocationName(primryAddress,5);
            if (address==null) {
            }
            addressSecond = coderSecond.getFromLocationName(secondaryAddress,5);
            if (addressSecond==null) {
            }
            if(!sharedPreferences.getString("userlatitudeProfile","").equals("")){
                LatLng latLng1 = new LatLng(Double.parseDouble(sharedPreferences.getString("userlatitudeProfile","")), Double.parseDouble(sharedPreferences.getString("userlangitudeProfile","")));
                lat= sharedPreferences.getString("userlatitudeProfile","");
                lng=sharedPreferences.getString("userlangitudeProfile","");
                LatLng latLng2 = new LatLng(Double.parseDouble(sharedPreferences.getString("userlatitudeSecondProfile","")),Double.parseDouble(sharedPreferences.getString("userlangitudeSecondProfile","")));
                mapLocator(latLng1,latLng2);
            }else {
                Address location=address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1First = new LatLng(location.getLatitude(), location.getLongitude() );
                p1 = new Barcode.GeoPoint((double) (location.getLatitude() * 1E6),
                        (double) (location.getLongitude() * 1E6));
                Address locationSecond=addressSecond.get(0);
                locationSecond.getLatitude();
                locationSecond.getLongitude();
                p1Second= new LatLng(locationSecond.getLatitude(), locationSecond.getLongitude() );
                p2 = new Barcode.GeoPoint((double) (locationSecond.getLatitude() * 1E6),
                        (double) (locationSecond.getLongitude() * 1E6));
                LatLng latLng1 = new LatLng(p1First.latitude, p1First.longitude);
                LatLng latLng2 = new LatLng(p1Second.latitude, p1Second.longitude);
                lat= p1First.latitude+"";
                lng=p1First.longitude+"";
                latSecond= p1Second.latitude+"";
                lngSecond=p1Second.longitude+"";
                mapLocator(latLng1,latLng2 );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void mapLocator(LatLng latLng1, LatLng latLng2){
        googleMap.addMarker(new MarkerOptions().position(latLng1)
                .title("Primary delivery location")
                .draggable(true));
        googleMap.addMarker(new MarkerOptions().position(latLng2)
                .title("Secondary delivery location")
                .draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
    }


    boolean isValid() {
        boolean valid = true;
        if (firstNameEdit.getText().toString().isEmpty()) {
            firstNameEdit.setError("Please Enter First name");
            valid = false;
        } else if (lastNameEdit.getText().toString().isEmpty()) {
            lastNameEdit.setError("Please Enter Last name");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()) {
            emailEdit.setError("Please Enter valid Email Id");
            valid = false;
        } else if (phoneEdit.getText().toString().isEmpty() || phoneEdit.getText().toString().length() != 10) {
            phoneEdit.setError("Please Enter Valid Phone number");
            valid = false;
        } else if (pincodeEdit.getText().toString().isEmpty() || pincodeEdit.getText().toString().length() < 6) {
            pincodeEdit.setError("Please Enter valid pincode");
            valid = false;
        } else if (address1Edit.getText().toString().isEmpty()) {
            address1Edit.setError("Please Enter Address");
            valid = false;
        }
        return valid;
    }


    void updateUserDetails(){

        if (isValid()) {

            WebserviceController wss = new WebserviceController(this);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userId",sharedPreferences.getString("userid", ""));
                jsonObject.put("firstName", firstNameEdit.getText().toString());
                jsonObject.put("lastName", lastNameEdit.getText().toString());
                jsonObject.put("emailId", emailEdit.getText().toString());
                jsonObject.put("usrPassword",sharedPreferences.getString("password", ""));
                jsonObject.put("contactNo", phoneEdit.getText().toString());
                jsonObject.put("address1", address1Edit.getText().toString());
                jsonObject.put("address2", address2Edit.getText().toString());
                jsonObject.put("pincode", pincodeEdit.getText().toString());
                jsonObject.put("profilePic", JSONObject.NULL );
                jsonObject.put("usersTimestamp", new Date().getTime());
                jsonObject.put("usersCreated", new Date().getTime());
                jsonObject.put("usersCreated", JSONObject.NULL);
                jsonObject.put("registrationId", JSONObject.NULL);
                jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
                jsonObject.put("userRoleId", sharedPreferences.getString("userRoleId",""));
                if(sharedPreferences.contains("userAdminFlag") && sharedPreferences.getString("userAdminFlag","").equalsIgnoreCase("1")) {
                    jsonObject.put("userAdminFlag", "1");
                }else{
                    jsonObject.put("userAdminFlag", "0");
                }
                jsonObject.put("usrStatus", "active");
                jsonObject.put("userLat",lat);
                jsonObject.put("userLong",lng);

                requestObject.put("requestData", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.addUser, requestObject.toString(), new IResult() {

                @Override
                public void notifySuccess(String response, int statusCode) {

                    try {
                        Log.e("getPlants response", response);

                        DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            sharedPreferences.edit().putString("emailIdUser", emailEdit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userFirstName",firstNameEdit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userLastName",lastNameEdit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userPhoneNumber",phoneEdit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userAddress1", address1Edit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userAddress2",address2Edit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userPincode", pincodeEdit.getText().toString()).commit();
                            sharedPreferences.edit().putString("userlatitudeProfile", lat).commit();
                            sharedPreferences.edit().putString("userlangitudeProfile",lng).commit();
                            sharedPreferences.edit().putString("userlatitudeSecondProfile", latSecond).commit();
                            sharedPreferences.edit().putString("userlangitudeSecondProfile",lngSecond).commit();

                            onBackPressed();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                }
            });
        }
        else {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_LONG).show();
        }
    }
}

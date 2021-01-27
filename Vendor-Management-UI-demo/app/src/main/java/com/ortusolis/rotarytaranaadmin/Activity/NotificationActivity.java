package com.ortusolis.rotarytaranaadmin.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.rotarytaranaadmin.NetworkUtility.Constants;
import com.ortusolis.rotarytaranaadmin.NetworkUtility.IResult;
import com.ortusolis.rotarytaranaadmin.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytaranaadmin.R;
import com.ortusolis.rotarytaranaadmin.adapter.NotificationAdminAdapter;
import com.ortusolis.rotarytaranaadmin.pojo.DistributorResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotificationActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar action;
    ListView list;
    TextView noResult;
    String[] userId;
    String[] firstName;
    String[] lastName;
    String[] emailId;
    String[] contactNo;
    String[] address1;
    String[] address2;
    String[] pincode;
    String[] userRoleId;
    String[] userAdminFlag;
    String[] lat;
    String[] lag;
    String status="";
    String userAdminFlagCode="";
    Gson gson;
    SharedPreferences sharedPreferences;
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_activity);
        mToolbar = findViewById(R.id.toolbar);
        list=findViewById(R.id.list);
        gson = new Gson();
        sharedPreferences = getSharedPreferences("water_management",0);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                list.getItemAtPosition(position);
                final int i=position;

                View checkBoxView = View.inflate(NotificationActivity.this, R.layout.checkbox, null);
                final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                if(userAdminFlag[i].equals("1")){
                    userAdminFlagCode="1";
                    checkBox.setChecked(true);
                }else{
                    userAdminFlagCode="0";
                    checkBox.setChecked(false);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(checkBox.isChecked()){
                            userAdminFlagCode="1";
                        }else {
                            userAdminFlagCode="0";
                        }
//
                        // Save to shared preferences
                    }
                });
                checkBox.setText("Admin Access");

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                builder.setTitle(Html.fromHtml("<font color='#000000'>User Approve</font>"));
//                builder.setCanceledOnTouchOutside(true);
                builder.setMessage("Could you like to approve or reject")
                        .setView(checkBoxView)
                        .setCancelable(true)
                        .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                status="active";
                                UserApproveAndReject(i);
                            }
                        })
                        .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                status="reject";
                                UserApproveAndReject(i);
                                dialog.cancel();
                            }
                        });
                if(userRoleId[i].equals("3")){
                    builder.setView(null);
                    checkBoxView.setVisibility(View.GONE);
                    checkBox.setVisibility(View.GONE);
                }

//                        .setCanceledOnTouchOutside()
//                        .show();

                //                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
//                builder.setTitle(Html.fromHtml("<font color='#000000'>User Approve</font>"));
//                builder.setMessage("Could you like to approve or reject")
//                        .setCancelable(true)
//                        .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //do things
//                                status="active";
//                                UserApproveAndReject(i);
//                            }
//                        });
//                builder.setNegativeButton("Reject",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        status="reject";
//                        UserApproveAndReject(i);
//                    }
//                });
                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();

                Log.e("item",list.getItemAtPosition(position).toString());
            }
        });
        noResult = findViewById(R.id.noResult);
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
            toolbarTitle.setText("Notifications");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

//        Fragment presentfragment = NotificationFragment.newInstance();
//        presentfragment.setArguments(getIntent().getExtras());
//        changeFragment(presentfragment, false, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        getNewUserRegistration();
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
    void getNewUserRegistration(){

        WebserviceController wss = new WebserviceController(NotificationActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "pending");

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response", response);
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        JSONArray userList = responseData.getJSONArray("userDetails");
                        userId= new String[userList.length()];
                        firstName= new String[userList.length()];
                        lastName= new String[userList.length()];
                        emailId= new String[userList.length()];
                        contactNo= new String[userList.length()];
                        address1= new String[userList.length()];
                        address2= new String[userList.length()];
                        pincode= new String[userList.length()];
                        userRoleId= new String[userList.length()];
                        userAdminFlag= new String[userList.length()];
                        lat= new String[userList.length()];
                        lag= new String[userList.length()];
                        for (int userListObject=0;userListObject<userList.length();userListObject++){
                            JSONObject userListDetailsobject = userList.getJSONObject(userListObject);
                            userId[userListObject]=  userListDetailsobject.getString("userId");
                            firstName[userListObject]=  userListDetailsobject.getString("firstName");
                            lastName[userListObject]= userListDetailsobject.getString("lastName");
                            emailId[userListObject]= userListDetailsobject.getString("emailId");
                            contactNo[userListObject]= userListDetailsobject.getString("contactNo");
                            address1[userListObject]= userListDetailsobject.getString("address1");
                            address2[userListObject]= userListDetailsobject.getString("address2");
                            pincode[userListObject]= userListDetailsobject.getString("pincode");
                            lat[userListObject]= userListDetailsobject.getString("userLat");
                            lag[userListObject]= userListDetailsobject.getString("userLong");
                            userAdminFlag[userListObject]=userListDetailsobject.getString("userAdminFlag");
//                            if(userListDetailsobject.getString("userRoleId").equals("1")){
                                userRoleId[userListObject]= userListDetailsobject.getString("userRoleId");
//                            }else if(userListDetailsobject.getString("userRoleId").equals("2")){
//                                userRoleId[userListObject]= "Facilitator";
//                            }
//                            else if(userListDetailsobject.getString("userRoleId").equals("3")){
//                                userRoleId[userListObject]= "Partner";
//                            }
//                            else if(userListDetailsobject.getString("userRoleId").equals("4")){
//                                userRoleId[userListObject]= "Donor";
//                            }

                            NotificationAdminAdapter adapter=new NotificationAdminAdapter(NotificationActivity.this, firstName, lastName,emailId, contactNo, address1,address2,pincode,userRoleId,userAdminFlag);
                            list.setAdapter(adapter);
                        }
//                        notificationAdapter = new NotificationAdapter(obj.getJSONObject("responseData"),getActivity(),NotificationFragment.this);
//                        recyclerView.setAdapter(notificationAdapter);

                        if (responseData.has("userDetails")){
                            noResult.setVisibility(View.GONE);
                        }
                        else {
                            noResult.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                        Toast.makeText(NotificationActivity.this, "No data" , Toast.LENGTH_LONG).show();
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


    public void changeFragment(Fragment fragment, boolean addToBackStack, boolean isAdd) {
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            String tag = fragment.getClass().getSimpleName();
            if (isAdd) {
                fragmentTransaction.add(R.id.main_fragment, fragment, tag);
            } else {
                fragmentTransaction.replace(R.id.main_fragment, fragment, tag);
            }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.commit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==333 && resultCode==RESULT_OK){
            //onBackPressed();
        }
    }
    public void UserApproveAndReject(Integer i){
        WebserviceController wss = new WebserviceController(this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId[i]);
            jsonObject.put("firstName", firstName[i]);
            jsonObject.put("lastName", lastName[i]);
            jsonObject.put("emailId", emailId[i]);
            jsonObject.put("usrPassword",sharedPreferences.getString("password", ""));
            jsonObject.put("contactNo", contactNo[i]);
            jsonObject.put("usrStatus", status);
            jsonObject.put("address1", address1[i]);
            jsonObject.put("address2", address2[i]);
            jsonObject.put("pincode", pincode[i]);
            jsonObject.put("profilePic", JSONObject.NULL );
            jsonObject.put("usersTimestamp", new Date().getTime());
            jsonObject.put("usersCreated", new Date().getTime());
            jsonObject.put("usersCreated", JSONObject.NULL);
            jsonObject.put("registrationId", JSONObject.NULL);
            jsonObject.put("userAdminFlag", userAdminFlagCode);
            jsonObject.put("mappedTo", "1");
            jsonObject.put("userRoleId", userRoleId[i]);
            jsonObject.put("userLat",lat[i]);
            jsonObject.put("userLong",lag[i]);

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
}

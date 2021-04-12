package com.ortusolis.subhaksha.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.Activity.CustomerAct;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.adapter.NotificationAdapter;
import com.ortusolis.subhaksha.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotificationFragment extends Fragment {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    ListView list;
    NotificationAdapter notificationAdapter;
    TextView noResult;
    String[] firstName;
    String[] lastName;
    String[] emailId;
    String[] contactNo;
    String[] address1;
    String[] address2;
    String[] pincode;
    String[] userRoleId;

    public static NotificationFragment newInstance() {

        Bundle args = new Bundle();

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notifications,null);

        sharedPreferences = getActivity().getSharedPreferences("water_management",0);
        list=view.findViewById(R.id.list);
        mToolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        noResult = view.findViewById(R.id.noResult);

        mToolbar.setVisibility(View.GONE);

        gson = new Gson();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewUserRegistration();
    }

    public void approvalScreen(UserInfo data){
        Intent intent = new Intent(getActivity(), CustomerAct.class);
        intent.putExtra("user",data);
        startActivity(intent);
    }

    void getNewUserRegistration(){

        WebserviceController wss = new WebserviceController(getActivity());

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
                    JSONObject responseData = obj.getJSONObject("responseData");
                    String responseDescription =obj.getString("responseDescription");
                        JSONArray userList = responseData.getJSONArray("userDetails");
                        if (obj.getString("responseCode").equalsIgnoreCase("200")) {
                            firstName= new String[userList.length()];
                            lastName= new String[userList.length()];
                            emailId= new String[userList.length()];
                            contactNo= new String[userList.length()];
                            address1= new String[userList.length()];
                            address2= new String[userList.length()];
                            pincode= new String[userList.length()];
                            userRoleId= new String[userList.length()];
                            for (int userListObject=0;userListObject<userList.length();userListObject++){
                                JSONObject userListDetailsobject = userList.getJSONObject(userListObject);
                                firstName[userListObject]=  userListDetailsobject.getString("firstName");
                                lastName[userListObject]= userListDetailsobject.getString("lastName");
                                emailId[userListObject]= userListDetailsobject.getString("emailId");
                                contactNo[userListObject]= userListDetailsobject.getString("contactNo");
                                address1[userListObject]= userListDetailsobject.getString("address1");
                                address2[userListObject]= userListDetailsobject.getString("address2");
                                pincode[userListObject]= userListDetailsobject.getString("pincode");
                                userRoleId[userListObject]= userListDetailsobject.getString("userRoleId");
//                                NotificationAdminAdapter adapter=new NotificationAdminAdapter(NotificationActivity.this, firstName, lastName,emailId, contactNo, address1,address2,pincode,userRoleId);
//                                list.setAdapter(adapter);
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
                        Toast.makeText(getActivity(), "notification Failed" + responseDescription, Toast.LENGTH_LONG).show();
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

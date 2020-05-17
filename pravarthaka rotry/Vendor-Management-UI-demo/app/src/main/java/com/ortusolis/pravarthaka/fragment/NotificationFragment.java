package com.ortusolis.pravarthaka.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.ortusolis.pravarthaka.Activity.CustomerAct;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.adapter.NotificationAdapter;
import com.ortusolis.pravarthaka.pojo.NotificationResponse;
import com.ortusolis.pravarthaka.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

public class NotificationFragment extends Fragment {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    TextView noResult;

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
            jsonObject.put("mappedTo", sharedPreferences.getString("userid",""));

            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getNewRegistration, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("login response", response);

                    NotificationResponse responseData = gson.fromJson(response, NotificationResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        notificationAdapter = new NotificationAdapter(responseData.getResponseData(),getActivity(),NotificationFragment.this);
                        recyclerView.setAdapter(notificationAdapter);

                        if (responseData.getResponseData().isEmpty()){
                            noResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            noResult.setVisibility(View.GONE);
                        }
                    }
                    else
                        Toast.makeText(getActivity(), TextUtils.isEmpty(responseData.getResponseDescription()) ? "Login Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
            }
        });

    }

}

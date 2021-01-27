package com.ortusolis.evenkart.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ortusolis.evenkart.Interfaces.IClickInterface;
import com.ortusolis.evenkart.NetworkUtility.Constants;
import com.ortusolis.evenkart.NetworkUtility.IResult;
import com.ortusolis.evenkart.NetworkUtility.WebserviceController;
import com.ortusolis.evenkart.R;
import com.ortusolis.evenkart.adapter.OrderListAdapter;
import com.ortusolis.evenkart.pojo.OrderResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerStatusListFragment extends Fragment {

    Toolbar mToolbar;
    ActionBar action;
    SharedPreferences sharedPreferences;
    LinearLayout distributorNotification, customerNotification,distributor1,customer1,customer2,customer3;
    Gson gson;
    RecyclerView recyclerView;
    OrderListAdapter ordersAdapter;
    List<OrderResponse.RequestS> data;
    TextView noResult;
    String statusTex;
    Spinner Spinner;

    public static CustomerStatusListFragment newInstance() {

        Bundle args = new Bundle();

        CustomerStatusListFragment fragment = new CustomerStatusListFragment();
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
        Spinner =view.findViewById(R.id.spinnerStatus);
        Spinner.setVisibility(View.GONE);
        data = new ArrayList<>();
        gson = new Gson();
        statusTex = getArguments().getString("status");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mToolbar.setVisibility(View.GONE);
        noResult.setText("No Data");
        getCustomerOrderStatus();
        return view;
    }

    void getCustomerOrderStatus(){

            WebserviceController wss = new WebserviceController(getActivity());

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", statusTex);
                jsonObject.put("customerId", sharedPreferences.getString("userid", ""));

                requestObject.put("request", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            wss.postLoginVolley(Constants.getCustomerOrderStatus, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("login response", response);

                        OrderResponse responseData = gson.fromJson(response, OrderResponse.class);

                        if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                            data = responseData.getResponseData().getOrderList();

                            ordersAdapter = new OrderListAdapter(getActivity(),data, new IClickInterface() {
                                @Override
                                public void click(OrderResponse.RequestS ordersss) {

                                    Intent intent = new Intent(getActivity(), MybillsDescription.class);
                                    if(statusTex.contains("Request")){
                                        intent.putExtra("request", "request");
                                    }
                                    intent.putExtra("order", ordersss);
                                    startActivityForResult(intent, 200);

                                }
                            });

                            recyclerView.setAdapter(ordersAdapter);

                            ordersAdapter.notifyDataSetChanged();

                            if (responseData.getResponseData().getOrderList().isEmpty()){
                                noResult.setVisibility(View.VISIBLE);
                            }
                            else {
                                noResult.setVisibility(View.GONE);
                            }

                        }
                        else {
                            noResult.setVisibility(View.VISIBLE);
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

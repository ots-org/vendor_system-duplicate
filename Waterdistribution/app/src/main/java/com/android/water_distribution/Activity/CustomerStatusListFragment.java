package com.android.water_distribution.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.Interfaces.IClickInterface;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.adapter.OrderListAdapter;
import com.android.water_distribution.pojo.OrderResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

        data = new ArrayList<>();

        gson = new Gson();

        statusTex = getArguments().getString("status");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        mToolbar.setVisibility(View.GONE);
        /*setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);

            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);

            //this.action.setTitle((CharSequence) "Update Stock");

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("My Orders");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }*/
        noResult.setText("No Data");
        getCustomerOrderStatus();

        return view;
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200 && resultCode==RESULT_OK){
            onBackPressed();
        }
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
    }*/

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
                    Toast.makeText(getActivity(), WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_SHORT).show();
                }
            });

    }

}

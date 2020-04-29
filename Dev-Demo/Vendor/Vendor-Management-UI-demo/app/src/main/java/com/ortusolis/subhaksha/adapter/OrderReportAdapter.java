package com.ortusolis.subhaksha.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.subhaksha.Activity.ProductDetailReportActivity;
import com.ortusolis.subhaksha.Activity.TransferOrderActivity;
import com.ortusolis.subhaksha.NetworkUtility.Constants;
import com.ortusolis.subhaksha.NetworkUtility.IResult;
import com.ortusolis.subhaksha.NetworkUtility.WebserviceController;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.pojo.CompleteOrderDetails;
import com.ortusolis.subhaksha.pojo.DistributorResponse;
import com.ortusolis.subhaksha.pojo.GeneralResponse;
import com.ortusolis.subhaksha.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderReportAdapter extends RecyclerView.Adapter<OrderReportAdapter.ModuleViewHolder> {
    private List<CompleteOrderDetails> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";

    String distributorStr = "";
    //
    ProgressDialog progressDialog;
    //
    String strDistName = null;
    String strDist = null;
    List<String> userNames, userIdList;
    SharedPreferences sharedPreferences;
    String status = "";
    boolean isAssigned = false;
    boolean isLoadingS = false;

    public OrderReportAdapter(Context context, List<CompleteOrderDetails> dataList ,String status,boolean isAssigned) {
        this.dataList = dataList;
        this.context = context;
        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("water_management",0);
        this.status = status;
        this.isAssigned = isAssigned;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView orderNo;
        TextView customeName;
        TextView orderDate;
        TextView orderCost;
        TextView deliveryDate;
        TextView deliveredBy;
        TextView employeeText;

        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            orderNo = (TextView) itemView.findViewById(R.id.orderNo);
            customeName = (TextView) itemView.findViewById(R.id.customerName);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            orderCost = (TextView) itemView.findViewById(R.id.orderCost);
            deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
            deliveredBy = (TextView) itemView.findViewById(R.id.deliveredBy);
            employeeText = (TextView) itemView.findViewById(R.id.employeeText);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_order_report, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModuleViewHolder holder, final int position) {
        final CompleteOrderDetails item = dataList.get(position);

        holder.orderNo.setText(Html.fromHtml("<b>Order No: </b>"+item.getOrderId()));
        holder.customeName.setText(Html.fromHtml("<b>Customer Name: </b>"+((item.getCustomerDetails()!=null && item.getCustomerDetails().getFirstName()!=null)?item.getCustomerDetails().getFirstName():"")+"<br/>"+(status.equalsIgnoreCase("new")?"":"<b>Assigned to: </b>"+(item.getEmployeeDetails()!=null ?(item.getEmployeeDetails().getFirstName()+" "+item.getEmployeeDetails().getLastName()):""))));
        holder.orderDate.setText(Html.fromHtml("<b>Order Date: </b>"+item.getOrderDate()));

        /*float totalCost = 0;
        for (OrderResponse.RequestS.ProductOrder productRequestCart : dataList.get(position).getOrderProductDetails()) {
            totalCost = (Float.valueOf(productRequestCart.getOtsOrderedQty()) * Float.valueOf(productRequestCart.getOtsOrderProductCost())) + totalCost;
        }*/

        holder.orderCost.setText(Html.fromHtml("<b>Order Cost: </b>"+context.getString(R.string.Rs)+(String.format("%.02f", (Float.valueOf(item.getOrderCost()))))));
        holder.deliveryDate.setText(Html.fromHtml(((status.equalsIgnoreCase("close") || status.equalsIgnoreCase("generated"))?"<b>Delivered Date: </b>":"<b>Delivery Date: </b>")+item.getOrderDeliveryDate()));
        if (status.equalsIgnoreCase("new") || status.equalsIgnoreCase("assigned") || status.equalsIgnoreCase("cancel")){
            holder.deliveredBy.setVisibility(View.GONE);
        }
        else {
            holder.deliveredBy.setVisibility(View.VISIBLE);
        }

        if (item.getEmployeeDetails()!=null) {
            holder.deliveredBy.setVisibility(View.VISIBLE);
            holder.deliveredBy.setText(Html.fromHtml("<b>Delivered By: </b>" + item.getEmployeeDetails().getFirstName() + " " + item.getEmployeeDetails().getLastName()));
        }
        else {
            holder.deliveredBy.setVisibility(View.GONE);
        }


        if (isAssigned && sharedPreferences.contains("userRoleId") && (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2"))) {
            holder.employeeText.setVisibility(View.VISIBLE);
        }
        else {
            holder.employeeText.setVisibility(View.GONE);
        }
        holder.employeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //
                getAllRegister(item.getOrderId());
            }
        });

        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailReportActivity.class);
                intent.putParcelableArrayListExtra("prods", (ArrayList<? extends Parcelable>) dataList.get(position).getOrderProductDetails());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return 0;
    }

    public void clearAll(){
        dataList.clear();
        notifyDataSetChanged();
    }

    void getAllRegister(final String orderId){
        if (!isLoadingS) {
            isLoadingS = true;
            WebserviceController wss = new WebserviceController(context);

            JSONObject requestObject = new JSONObject();

            JSONObject jsonObject = new JSONObject();
            try {
                if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId", "").equalsIgnoreCase("3")) {
                    jsonObject.put("mappedTo", sharedPreferences.getString("distId", ""));
                } else {
                    jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
                }

                requestObject.put("requestData", jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }


            userNames.clear();
            userIdList.clear();

            wss.postLoginVolley(Constants.getUserDetailsByMapped, requestObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    isLoadingS = false;
                    try {
                        Log.e("getPlants response", response);

                        DistributorResponse distributorResponse = new Gson().fromJson(response, DistributorResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            //
                            progressDialog.dismiss();
                            //

                            strDist = "";

                            userNames.clear();
                            userIdList.clear();

                            for (UserInfo userInfo1 : distributorResponse.getResponseData().getUserDetails()) {
                                if (userInfo1.getUserRoleId().equalsIgnoreCase("3")) {
                                    userNames.add(userInfo1.getFirstName());
                                    userIdList.add(userInfo1.getUserId());
                                }
                            }

                            if (distributorResponse.getResponseData().getUserDetails().isEmpty()) {
                                Toast.makeText(context, "No Results", Toast.LENGTH_LONG).show();
                            }

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Employee</font>"));

                            //First Step: convert ArrayList to an Object array.
                            Object[] objNames = userNames.toArray();

                            //Second Step: convert Object array to String array

                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                            int checkedItem = 0;
                            strDist = userIdList.get(checkedItem);
                            strDistName = userNames.get(checkedItem);
                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // user checked an item
                                    strDist = userIdList.get(which);
                                    strDistName = userNames.get(which);
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
                                    distributorStr = strDist;
                                    assignOrder(orderId, distributorStr);
                                }
                            });

                            builderSingle.show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    isLoadingS = false;
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(context, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    void assignOrder(String orderId, String empId){

        WebserviceController wss = new WebserviceController(context);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("employeeId", empId);
            jsonObject.put("orderId", orderId);

            requestObject.put("request", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.employeeTransferOrder, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    GeneralResponse responseData = new Gson().fromJson(response, GeneralResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {
                        Toast.makeText(context, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Successfully Updated" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                        ((TransferOrderActivity)context).updateStatus();
                    }
                    else {
                        Toast.makeText(context, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(context, WebserviceController.returnErrorMessage(error) + "", Toast.LENGTH_LONG).show();
            }
        });

    }

}

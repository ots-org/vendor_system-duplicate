package com.ortusolis.etaaranavkf.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.NetworkUtility.IResult;
import com.ortusolis.etaaranavkf.NetworkUtility.WebserviceController;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.pojo.DistributorResponse;
import com.ortusolis.etaaranavkf.pojo.ProductDetails;
import com.ortusolis.etaaranavkf.pojo.ProductStockResponse;
import com.ortusolis.etaaranavkf.pojo.UserInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductTransactionReportActivity extends AppCompatActivity {

    Button buttonDatePick;
    Button report;
    LinearLayout addLL;
    Calendar newCalendar;
    DatePickerDialog startTime;
    TextView noResult;
    Gson gson;
    SharedPreferences sharedPreferences;
    Toolbar mToolbar;
    ActionBar action;
    ProgressDialog progressDialog;
    LinearLayout distributorCodeLayout;
    String distributorStr = "";
    TextView distributorText;
    String strDistName = null;
    String strDist = null;
    String pdf="no";
    List<String> userNames, userIdList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_transaction_report);
        addLL = findViewById(R.id.addLL);
        buttonDatePick = findViewById(R.id.buttonDatePick);
        mToolbar = findViewById(R.id.toolbar);
        noResult = findViewById(R.id.noResult);
        distributorCodeLayout = findViewById(R.id.distributorCodeLayout);
        distributorText = findViewById(R.id.distributorText);
        sharedPreferences = getSharedPreferences("water_management",0);
        gson = new Gson();
        setSupportActionBar(mToolbar);
        gson = new Gson();

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();

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
            toolbarTitle.setText("Product Transaction Report");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        newCalendar = Calendar.getInstance();
        startTime = new DatePickerDialog(ProductTransactionReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String dateStr = format.format(newCalendar.getTime());

                buttonDatePick.setText("Selected Date ("+ dateStr+")");

                getProductStockList(dateStr);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        buttonDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime.show();
            }
        });

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            distributorCodeLayout.setVisibility(View.GONE);
        }
        else {
            distributorCodeLayout.setVisibility(View.GONE);
        }

        distributorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ProductTransactionReportActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getAllRegister();
            }
        });
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            pdf="yes";
        }
    }
    void getProductStockList(String dateString){

        WebserviceController wss = new WebserviceController(ProductTransactionReportActivity.this);

        JSONObject requestObject = new JSONObject();

        String usserId = "";
        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            usserId =  sharedPreferences.getString("userid","");
        }
        else if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            usserId = sharedPreferences.getString("userid","");
        }
        else {
            usserId = sharedPreferences.getString("distId","");
        }

        if (usserId.isEmpty()){
            Toast.makeText(this, "Select Facilitator", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
                jsonObject.put("userId", "1");
            }else {
                jsonObject.put("userId", usserId);
            }

            jsonObject.put("productId", "");
            jsonObject.put("todaysDate", dateString);
            jsonObject.put("pdf", pdf);
            requestObject.put("requestData", jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.getProductStockList, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getNewRegistration resp", response);

                    ProductStockResponse responseData = gson.fromJson(response, ProductStockResponse.class);

                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {


                        if (responseData.getResponseData()!=null && responseData.getResponseData().getProductStockDetail()!=null) {
                            noResult.setVisibility(View.GONE);
                            loadData(responseData.getResponseData().getProductStockDetail());
                            if(pdf.equals("yes")){
                                String txt=responseData.getResponseData().getPdf();
                                final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "Product Transaction Report" + ".pdf");
                                byte[] pdfAsBytes = Base64.decode(txt, 0);
                                FileOutputStream os;
                                try {
                                    os = new FileOutputStream(dwldsPath, false);
                                    os.write(pdfAsBytes);
                                    os.flush();
                                    os.close();
                                    Toast.makeText(getApplicationContext(), "Product Transaction Report Pdf Available", Toast.LENGTH_LONG).show();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                    else {
                        noResult.setVisibility(View.VISIBLE);
                        addLL.removeAllViews();
                        Toast.makeText(ProductTransactionReportActivity.this, TextUtils.isEmpty(responseData.getResponseDescription()) ? "Failed" : responseData.getResponseDescription(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    noResult.setVisibility(View.VISIBLE);
                    addLL.removeAllViews();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                noResult.setVisibility(View.VISIBLE);
                addLL.removeAllViews();
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

    void loadData(ArrayList<ProductDetails> requestS){
        addLL.removeAllViews();
        if (!requestS.isEmpty()) {
            addRow(null);
            for (ProductDetails requestSdata : requestS) {
                addRow(requestSdata);
            }
        }
        else {
            noResult.setVisibility(View.VISIBLE);
        }
    }

    void addRow(ProductDetails requestSdata){
        if (requestSdata!=null){

                View view = getLayoutInflater().inflate(R.layout.view_product_transaction,null);
                TextView name = (TextView) view.findViewById(R.id.title);
                TextView openBalance = (TextView) view.findViewById(R.id.openBalance);
                TextView stockAddition = (TextView) view.findViewById(R.id.stockAddition);
                TextView orderDelivered = (TextView) view.findViewById(R.id.orderDelivered);
                TextView closingBalance = (TextView) view.findViewById(R.id.closingBalance);
                name.setText(requestSdata.getProductName());
                openBalance.setText(requestSdata.getOtsProductOpenBalance());
                openBalance.setGravity(Gravity.RIGHT);
                stockAddition.setText(requestSdata.getOtsProductStockAddition());
                stockAddition.setGravity(Gravity.RIGHT);
                orderDelivered.setText(requestSdata.getOtsProductOrderDelivered());
                orderDelivered.setGravity(Gravity.RIGHT);
                float closingBal = (Float.valueOf(requestSdata.getOtsProductOpenBalance())
                        +Float.valueOf(requestSdata.getOtsProductStockAddition()))
                        -Float.valueOf(requestSdata.getOtsProductOrderDelivered());

                closingBalance.setText((int)closingBal+"");
                closingBalance.setGravity(Gravity.RIGHT);
                addLL.addView(view);
        }
        else {
            View view = getLayoutInflater().inflate(R.layout.view_product_transaction,null);
            addLL.addView(view);
        }
    }

    void getAllRegister(){
        WebserviceController wss = new WebserviceController(ProductTransactionReportActivity.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "UserRoleId");
            jsonObject.put("searchvalue", "2");
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        userNames.clear();
        userIdList.clear();

        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        progressDialog.dismiss();
                        strDist = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
                            userNames.add(userInfo1.getFirstName());
                            userIdList.add(userInfo1.getUserId());
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(ProductTransactionReportActivity.this, "No Results", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductTransactionReportActivity.this);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose distributor</font>"));

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
                                    distributorText.setText(strDistName );
                                    distributorStr = strDist;
                                }
                            });

                            builderSingle.show();

                    }
                }
                catch (Exception e){
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

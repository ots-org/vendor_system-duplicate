package com.android.water_distribution.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.water_distribution.NetworkUtility.Constants;
import com.android.water_distribution.NetworkUtility.IResult;
import com.android.water_distribution.NetworkUtility.WebserviceController;
import com.android.water_distribution.R;
import com.android.water_distribution.pojo.DistributorResponse;
import com.android.water_distribution.pojo.GeneralResponse;
import com.android.water_distribution.pojo.ProductDetails;
import com.android.water_distribution.pojo.ProductRequest;
import com.android.water_distribution.pojo.ProductRequestCart;
import com.android.water_distribution.pojo.ProductsStock;
import com.android.water_distribution.pojo.UserInfo;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDescription extends AppCompatActivity {

    TextView minusQuantity, plusQuantity,minusQuantityReturn, plusQuantityReturn,textName,descriptionText,distributorText,customerText,dayText;
    TextView returnQuantity,returnQu,deliveryTime,startTimeText,endTimeText,scheduleText;
    Button placeOrder,addToCart,scheduleButton;
    ImageButton editProduct;
    LinearLayout orderPlaceLayout,customerLayout,normalLL,scheduleLL;
    View customeLinearLayoutView;

    Toolbar mToolbar;
    ActionBar action;

    int price = 0;
    TextView totalPrice,description;
    ProductDetails productDetails;
    SharedPreferences sharedPreferences;
    Gson gson;
    String distributorStr = null;
    String customerStr = null;
    String customerStrName = null;
    List<String> userNames, userIdList;
    String strDistName = null;
    String strDist = null;
    String strCustName = null;
    String strCust = null;
    String selectDateStr = null;
    String selectStartDateStr = null;
    String selectEndDateStr = null;
    Calendar newCalendar;
    DatePickerDialog startTime;
    DatePickerDialog startScheduleTime;
    DatePickerDialog endScheduleTime;
    boolean schedule = false;
    String scheduleString = "Daily";
    List<String> scheduleDays = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        returnQu = findViewById(R.id.quantity);
        returnQuantity = findViewById(R.id.returnQuantity);
        minusQuantity = findViewById(R.id.minusQuantity);
        plusQuantity = findViewById(R.id.plusQuantity);
        minusQuantityReturn = findViewById(R.id.minusQuantityReturn);
        plusQuantityReturn = findViewById(R.id.plusQuantityReturn);
        placeOrder = findViewById(R.id.placeOrder);
        scheduleButton = findViewById(R.id.scheduleButton);
        addToCart = findViewById(R.id.addToCart);
        totalPrice = findViewById(R.id.totalPrice);
        description = findViewById(R.id.description);
        textName = findViewById(R.id.text);
        editProduct = findViewById(R.id.editProduct);
        descriptionText = findViewById(R.id.descriptionText);
        distributorText = findViewById(R.id.distributoStr);
        customerLayout = findViewById(R.id.customeLinearLayout);
        normalLL = findViewById(R.id.normalLL);
        scheduleLL = findViewById(R.id.scheduleLL);
        customerText = findViewById(R.id.customerStr);
        startTimeText = findViewById(R.id.startTime);
        endTimeText = findViewById(R.id.endTime);
        scheduleText = findViewById(R.id.scheduleText);
        deliveryTime = findViewById(R.id.deliveryTime);
        orderPlaceLayout = findViewById(R.id.orderPlace);
        customeLinearLayoutView = findViewById(R.id.customeLinearLayoutView);
        dayText = findViewById(R.id.dayText);
        gson = new Gson()/*new GsonBuilder().serializeNulls().create()*/;

        mToolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences("water_management",0);

        userNames = new ArrayList<>();
        userIdList = new ArrayList<>();
        scheduleDays = new ArrayList<>();

        setSupportActionBar(mToolbar);

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
            toolbarTitle.setText("Product");
            action.setCustomView(viewActionBar, params);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        if (getIntent().hasExtra("product")){
            productDetails = getIntent().getExtras().getParcelable("product");
        }

        if (getIntent().hasExtra("schedule")){
            schedule = getIntent().getExtras().getBoolean("schedule");
        }

        setValues();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductStock(productDetails.getProductId(),false);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductStock(productDetails.getProductId(),true);

            }
        });

        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(returnQu.getText().toString());
                quant = quant - 1;
                if (quant>=1) {
                    int total = price * quant;
                    totalPrice.setText(getString(R.string.Rs)+total);
                    returnQu.setText(quant + "");
                }
            }
        });

        minusQuantityReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(returnQuantity.getText().toString());
                quant = quant - 1;
                if (quant>=1)
                returnQuantity.setText(quant+"");

            }
        });

        plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(returnQu.getText().toString());
                quant = quant + 1;
                int total = price * quant;
                totalPrice.setText(getString(R.string.Rs)+total);
                returnQu.setText(quant+"");

            }
        });

        plusQuantityReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(returnQuantity.getText().toString());
                quant = quant + 1;
                returnQuantity.setText(quant+"");

            }
        });


        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            editProduct.setVisibility(View.VISIBLE);
            editProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProductDescription.this,AddProductActivity.class);
                    intent.putExtra("product", getIntent().getExtras().getParcelable("product"));
                    startActivity(intent);
                }
            });
        }
        else {
            editProduct.setVisibility(View.INVISIBLE);
        }

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
            customerLayout.setVisibility(View.GONE);
            customeLinearLayoutView.setVisibility(View.GONE);
            customerStr = sharedPreferences.getString("userid","");
            customerStrName = sharedPreferences.getString("username","");
        }

        distributorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllRegister(true,false);
            }
        });

        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMappedRegister();
                //getAllRegister(true,true);
            }
        });

        if (schedule){
            normalLL.setVisibility(View.GONE);
            scheduleLL.setVisibility(View.VISIBLE);
            orderPlaceLayout.setVisibility(View.GONE);
            scheduleButton.setVisibility(View.VISIBLE);
        }
        else {
            normalLL.setVisibility(View.VISIBLE);
            scheduleLL.setVisibility(View.GONE);
            orderPlaceLayout.setVisibility(View.VISIBLE);
            scheduleButton.setVisibility(View.GONE);
        }

        deliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startTime = new DatePickerDialog(ProductDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        selectDateStr = format.format(newCalendar.getTime());

                        deliveryTime.setText("Selected Date ("+ selectDateStr+")");

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                startTime.show();
            }
        });

        startTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startTime = new DatePickerDialog(ProductDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        selectStartDateStr = format.format(newCalendar.getTime());

                        startTimeText.setText("Selected Date ("+ selectStartDateStr+")");

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                startTime.show();
            }
        });

        endTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startTime = new DatePickerDialog(ProductDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        selectEndDateStr = format.format(newCalendar.getTime());

                        endTimeText.setText("Selected Date ("+ selectEndDateStr+")");

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                startTime.show();
            }
        });

        scheduleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ProductDescription.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_schedule,null);
                dialog.setContentView(view);

                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
                window.setDimAmount(0.5f);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                window.setAttributes(wlp);
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setWindowAnimations(R.style.DialogAnimation);

                Button dailyButton = view.findViewById(R.id.daily);
                Button weeklyButton = view.findViewById(R.id.weekly);

                dailyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       scheduleString = "Daily";
                       scheduleDays.clear();
                       scheduleText.setText(scheduleString);
                       dayText.setText("");
                       dayText.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });

                weeklyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scheduleString = "Weekly";
                        scheduleText.setText(scheduleString);
                        dialog.dismiss();
                        scheduleDays();
                    }
                });

                dialog.show();

            }
        });


        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    scheduleN();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));

        getAllRegister(false,false);
    }


    void scheduleDays(){

        final Dialog dialog = new Dialog(ProductDescription.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_schedule_days,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
        window.setDimAmount(0.5f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        window.setAttributes(wlp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        CheckBox mondayButton = view.findViewById(R.id.monday);
        CheckBox tuesdayButton = view.findViewById(R.id.tuesday);
        CheckBox wednesdayButton = view.findViewById(R.id.wednesday);
        CheckBox thursdayButton = view.findViewById(R.id.thursday);
        CheckBox fridayButton = view.findViewById(R.id.friday);
        CheckBox saturdayButton = view.findViewById(R.id.saturday);
        CheckBox sundayButton = view.findViewById(R.id.sunday);
        Button cancel = view.findViewById(R.id.cancel);
        Button ok = view.findViewById(R.id.ok);

        final List<String> scheduleList = new ArrayList<>();
        scheduleList.addAll(scheduleDays);

        if (scheduleDays.contains(mondayButton.getText().toString())){
            mondayButton.setChecked(true);
        }
        if (scheduleDays.contains(tuesdayButton.getText().toString())){
            tuesdayButton.setChecked(true);
        }
        if (scheduleDays.contains(wednesdayButton.getText().toString())){
            wednesdayButton.setChecked(true);
        }
        if (scheduleDays.contains(thursdayButton.getText().toString())){
            thursdayButton.setChecked(true);
        }
        if (scheduleDays.contains(fridayButton.getText().toString())){
            fridayButton.setChecked(true);
        }
        if (scheduleDays.contains(saturdayButton.getText().toString())){
            saturdayButton.setChecked(true);
        }
        if (scheduleDays.contains(sundayButton.getText().toString())){
            sundayButton.setChecked(true);
        }

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !scheduleList.contains(buttonView.getText().toString())){
                    scheduleList.add(buttonView.getText().toString());
                }
                else {
                    scheduleList.remove(buttonView.getText().toString());
                }
            }
        };

        mondayButton.setOnCheckedChangeListener(onCheckedChangeListener);
        tuesdayButton.setOnCheckedChangeListener(onCheckedChangeListener);
        wednesdayButton.setOnCheckedChangeListener(onCheckedChangeListener);
        thursdayButton.setOnCheckedChangeListener(onCheckedChangeListener);
        fridayButton.setOnCheckedChangeListener(onCheckedChangeListener);
        saturdayButton.setOnCheckedChangeListener(onCheckedChangeListener);
        sundayButton.setOnCheckedChangeListener(onCheckedChangeListener);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDays.clear();
                scheduleDays.addAll(scheduleList);
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!scheduleDays.isEmpty()) {
                    dayText.setVisibility(View.VISIBLE);
                    dayText.setText(TextUtils.join(",", scheduleDays));
                }
                else {
                    dayText.setVisibility(View.GONE);
                }
            }
        });

        dialog.show();

    }


     void setValues(){
         textName.setText(""+productDetails.getProductName());
         description.setText("Price "+getString(R.string.Rs)+productDetails.getProductPrice());
         descriptionText.setText(""+productDetails.getProductDescription());
         if (productDetails.getProductPrice().matches("-?\\d+")) {
             price = Integer.valueOf(productDetails.getProductPrice());
             totalPrice.setText(getString(R.string.Rs)+price);
         }

         if (productDetails.getStock().equalsIgnoreCase("no")){
             orderPlaceLayout.setVisibility(View.GONE);
         }
         else {
             orderPlaceLayout.setVisibility(View.VISIBLE);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    // Add this inside your class
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();
            if (b.containsKey("productId")) {
                if (productDetails.getProductId().equalsIgnoreCase(b.getString("productId"))){
                    productDetails.setProductName(b.getString("productName"));
                    productDetails.setProductDescription(b.getString("productDescription"));
                    productDetails.setProductPrice(b.getString("productPrice"));
                    setValues();
                }
            }
        }
    };

    void insertOrderAndProduct(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());

        if (distributorStr != null && customerStr != null && selectDateStr!=null) {
            WebserviceController wss = new WebserviceController(ProductDescription.this);

            int quant = Integer.parseInt(returnQu.getText().toString());

            ProductRequest productRequest = new ProductRequest();

            ProductRequest.RequestS requestS =  new ProductRequest.RequestS();

            requestS.setCustomerId(customerStr);
            requestS.setCustomerName(customerStrName);
            requestS.setOrderDate(currentDate);
            requestS.setDelivaryDate(selectDateStr);
            requestS.setDistributorId(distributorStr);
            requestS.setOrderCost((price * quant)+"");
            requestS.setAssignedId(null);
            requestS.setOrderStatus("New");
            requestS.setOrderNumber("");
            requestS.setDeliverdDate("");

            ProductRequest.RequestS.ProductOrder productOrder = new ProductRequest.RequestS.ProductOrder();
            productOrder.setOts_delivered_qty("0");
            productOrder.setOrderProductId(null);
            productOrder.setOrderdId(null);
            productOrder.setProductId(productDetails.getProductId());
            productOrder.setOrderedQty(quant+"");
            productOrder.setProductStatus("New");
            productOrder.setProductCost(price+"");
            ArrayList<ProductRequest.RequestS.ProductOrder> productOrders = new ArrayList<>();
            productOrders.add(productOrder);

            requestS.setProductList(productOrders);

            productRequest.setRequest(requestS);

            /*JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("customerId", sharedPreferences.getString("userid", ""));
                jsonObject.put("orderDate", dateStr);
                jsonObject.put("delivaryDate", JSONObject.NULL);
                jsonObject.put("distributorId", distributorStr);
                jsonObject.put("orderCost", price+"");
                jsonObject.put("assignedId", JSONObject.NULL);
                jsonObject.put("orderStatus", "NEW");
                jsonObject.put("orderNumber", JSONObject.NULL);
                jsonObject.put("deliverdDate", JSONObject.NULL);

                JSONArray jsonArray = new JSONArray();

                JSONObject jsonObject1 = new JSONObject();

                jsonObject1.put("ots_delivered_qty", JSONObject.NULL);
                jsonObject1.put("orderProductId", JSONObject.NULL);
                jsonObject1.put("orderdId", JSONObject.NULL);
                jsonObject1.put("productId", productDetails.getProductId());
                jsonObject1.put("orderedQty", quant+"");
                jsonObject1.put("productStatus", "Active");
                jsonObject1.put("productCost", price+"");

                jsonArray.put(jsonObject1);

                jsonObject.put("productList", jsonObject1);

                requestObject.put("requestData", jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            String str = gson.toJson(productRequest);

            wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("getPlants response", response);

                        DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
                            builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "")+"<br/><br/> Delivery date is "+selectDateStr));
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    onBackPressed();
                                }
                            });
                            builder.show();


                        }else
                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (selectDateStr == null){
            Toast.makeText(this, "Please select Delivery Date", Toast.LENGTH_SHORT).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Distributor", Toast.LENGTH_SHORT).show();
        }
    }

    void insertOrderAndProducttoCart(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());

        if (distributorStr != null && customerStr != null && selectDateStr!=null) {

            int quant = Integer.parseInt(returnQu.getText().toString());
            ProductRequestCart requestS = null;
            ArrayList<ProductRequestCart> productOrders;

            if (TextUtils.isEmpty(sharedPreferences.getString("prodDesc",""))) {
                requestS = new ProductRequestCart();

                requestS.setCustomerId(customerStr);
                requestS.setCustomerName(customerStrName);
                requestS.setOrderDate(currentDate);
                requestS.setDelivaryDate(selectDateStr);
                requestS.setDistributorId(distributorStr);
                requestS.setOrderCost((price * quant)+"");
                requestS.setAssignedId(null);
                requestS.setOrderStatus("New");
                requestS.setOrderNumber("");
                requestS.setDeliverdDate("");

                requestS.setOts_delivered_qty("0");
                requestS.setOrderProductId(null);
                requestS.setOrderdId(null);
                requestS.setProductId(productDetails.getProductId());
                requestS.setOrderedQty(quant+"");
                requestS.setProductStatus("New");
                requestS.setProductCost(price+"");
                productOrders = new ArrayList<>();
                productOrders.add(requestS);


            }
            else {
                productOrders = gson.fromJson(sharedPreferences.getString("prodDesc",""), new TypeToken<ArrayList<ProductRequestCart>>(){}.getType());

                int position = findProdS(productOrders);

                requestS  =  new ProductRequestCart();
                requestS.setCustomerId(customerStr);
                requestS.setCustomerName(customerStrName);
                requestS.setOrderDate(currentDate);
                requestS.setDelivaryDate(selectDateStr);
                requestS.setDistributorId(distributorStr);
                requestS.setOrderCost((price * quant)+"");
                requestS.setAssignedId(null);
                requestS.setOrderStatus("New");
                requestS.setOrderNumber("");
                requestS.setDeliverdDate("");

                requestS.setOts_delivered_qty("0");
                requestS.setOrderProductId(null);
                requestS.setOrderdId(null);
                requestS.setProductId(productDetails.getProductId());
                requestS.setOrderedQty(quant+"");
                requestS.setProductStatus("New");
                requestS.setProductCost(price+"");

                if (position!=-1) {
                    productOrders.set(position, requestS);
                }
                else {
                    productOrders.add(requestS);
                }

            }



            sharedPreferences.edit().putString("prodDesc",gson.toJson(productOrders)).apply();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent intent = new Intent(ProductDescription.this, CardListActivity.class);
                    startActivity(intent);
                }
            },1000);


            /*wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("getPlants response", response);

                        DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
                            builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "")+"<br/><br/> Delivery date is "+selectDateStr));
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    onBackPressed();
                                }
                            });
                            builder.show();


                        }else
                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
                }
            });*/
        }
        else if (selectDateStr == null){
            Toast.makeText(this, "Please select Delivery Date", Toast.LENGTH_SHORT).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Distributor", Toast.LENGTH_SHORT).show();
        }
    }

    int findProdS(ArrayList<ProductRequestCart> cartArrayList){

        for (int i=0;i<cartArrayList.size();i++) {

            if ((cartArrayList.get(i).getOrderdId()!=null && cartArrayList.get(i).getOrderdId().equalsIgnoreCase(productDetails.getProductId())) && (cartArrayList.get(i).getCustomerId()!=null && cartArrayList.get(i).getCustomerId().equalsIgnoreCase(customerStr))){
                return i;
            }

        }
        return -1;
    }


    void getAllRegister(final boolean showPop, final boolean customer){
        WebserviceController wss = new WebserviceController(ProductDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", "UserRoleId");
            jsonObject.put("searchvalue", customer?"4":"2");

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

                        if (customer){
                            strCust = "";
                        }
                        else {
                            strDist = "";
                        }

                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){
                            userNames.add(userInfo1.getFirstName());
                            userIdList.add(userInfo1.getUserId());

                            if (sharedPreferences.getString("userid", "").equalsIgnoreCase(userInfo1.getUserId())){

                                if (customer){
                                    strCust = userInfo1.getUserId();
                                    strCustName = userInfo1.getFirstName();
                                    customerText.setText(strCustName + " (" + strCust + ")");
                                    customerStr = strCust;
                                    customerStrName = strCustName;
                                }
                                else {
                                    strDist = userInfo1.getUserId();
                                    strDistName = userInfo1.getFirstName();
                                    distributorText.setText(strDistName + " (" + strDist + ")");
                                    distributorStr = strDist;
                                }

                            }
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(ProductDescription.this, "No Results", Toast.LENGTH_SHORT).show();
                        }

                        if (showPop){

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductDescription.this);
                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose distributor</font>"));

                            //First Step: convert ArrayList to an Object array.
                            Object[] objNames = userNames.toArray();

                            //Second Step: convert Object array to String array

                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                            int checkedItem = 0;
                            if (customer){
                                strCust = userIdList.get(checkedItem);
                                strCustName = userNames.get(checkedItem);
                            }
                            else {
                                strDist = userIdList.get(checkedItem);
                                strDistName = userNames.get(checkedItem);
                            }
                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // user checked an item
                                    if (customer){
                                        strCust = userIdList.get(which);
                                        strCustName = userNames.get(which);
                                    }
                                    else {
                                        strDist = userIdList.get(which);
                                        strDistName = userNames.get(which);
                                    }
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
                                    if (customer){
                                        customerText.setText(strCustName + " (" + strCust + ")");
                                        customerStr = strCust;
                                        customerStrName = strCustName;
                                    }
                                    else {
                                        distributorText.setText(strDistName + " (" + strDist + ")");
                                        distributorStr = strDist;
                                    }
                                }
                            });

                            builderSingle.show();
                        }
                        else if (!showPop){
                            if (!userIdList.isEmpty() && !userNames.isEmpty()) {

                                for (int i = 0;i<userIdList.size();i++){
                                    if (sharedPreferences.getString("distId","").equalsIgnoreCase(userIdList.get(i))){
                                        strDist = userIdList.get(i);
                                        strDistName = userNames.get(i);
                                        break;
                                    }
                                }

                                distributorText.setText(strDistName + " (" + strDist + ")");
                                distributorStr = strDist;
                            }
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getProductStock(String productId, final boolean yesT) {
        WebserviceController wss = new WebserviceController(this);
        JSONObject requestObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", productId);
            jsonObject.put("distributorId", (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2") ? sharedPreferences.getString("userid","") : sharedPreferences.getString("distId","")));
            requestObject.put("requestData", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wss.postLoginVolley(Constants.Get_Product_Stock, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {

                try {
                    Log.e("login response", response);

                    ProductsStock responseData = gson.fromJson(response, ProductsStock.class);
                    if (responseData.getResponseCode().equalsIgnoreCase("200")) {

                        if (Integer.valueOf(responseData.getResponseData().getStockQuantity())>0 && (Integer.parseInt(returnQu.getText().toString()) <= Integer.valueOf(responseData.getResponseData().getStockQuantity()))){

                            if (yesT){
                                insertOrderAndProduct();
                            }
                            else {
                                insertOrderAndProducttoCart();
                            }

                        }
                        else {
                            if((Integer.parseInt(returnQu.getText().toString()) > Integer.valueOf(responseData.getResponseData().getStockQuantity()))){
                                Toast.makeText(ProductDescription.this, "Please order a smaller quantity, available quantity is only "+responseData.getResponseData().getStockQuantity() , Toast.LENGTH_SHORT).show();
                            }
                            else
                            Toast.makeText(ProductDescription.this, "Out of Stock", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WebserviceController.returnErrorMessage(error));
                stringBuilder.append("");
                Toast.makeText(ProductDescription.this , stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    void scheduleN() throws JSONException {

        String weekDays = "";

        for (String s:scheduleDays){
            weekDays = weekDays + (TextUtils.isEmpty(weekDays)? "" : ",") + "\""+s+"\"";
        }

        if (distributorStr != null && customerStr != null && selectStartDateStr!=null && selectEndDateStr!=null) {
            WebserviceController wss = new WebserviceController(ProductDescription.this);

            int quant = Integer.parseInt(returnQu.getText().toString());

            String requestSSS = "{\n" +
                    "  \"typeOfSchedule\": \""+scheduleString+"\",\n" +
                    "  \"distributorId\": \""+sharedPreferences.getString("userid","")+"\",\n" +
                    "  \"addScheduler\": [\n" +
                    "    {\n" +
                    "      \"prodcutId\": \""+productDetails.getProductId()+"\",\n" +
                    "      \"prodcutQty\": \""+quant+"\",\n" +
                    "      \"startDate\": \""+selectStartDateStr+"\",\n" +
                    "      \"endDate\": \""+selectEndDateStr+"\",\n" +
                    "      \"schduleweekDays\": [\n" +
                    "        "+(TextUtils.isEmpty(weekDays)? "\"null\"" : weekDays)+"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"customerId\": \""+customerStr+"\"\n" +
                    "}";

            JSONObject jsonObject = new JSONObject(requestSSS);

            wss.postLoginVolley(Constants.insertScheduler, jsonObject.toString(), new IResult() {
                @Override
                public void notifySuccess(String response, int statusCode) {
                    try {
                        Log.e("getPlants response", response);

                        GeneralResponse distributorResponse = gson.fromJson(response, GeneralResponse.class);

                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                            onBackPressed();



                        }

                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (selectStartDateStr == null){
            Toast.makeText(this, "Please select Start Date", Toast.LENGTH_SHORT).show();
        }
        else if (selectEndDateStr == null){
            Toast.makeText(this, "Please select End Date", Toast.LENGTH_SHORT).show();
        }
        else if (customerStr == null){
            Toast.makeText(this, "Please select Customer", Toast.LENGTH_SHORT).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Distributor", Toast.LENGTH_SHORT).show();
        }
    }

    void getMappedRegister(){
        WebserviceController wss = new WebserviceController(ProductDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));

            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        userNames.clear();
        userIdList.clear();

        wss.postLoginVolley(Constants.getUserDetailsByMapped, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        strCust = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){

                            if (userInfo1.getUserRoleId().equalsIgnoreCase("4")) {
                                userNames.add(userInfo1.getFirstName());
                                userIdList.add(userInfo1.getUserId());
                            }

                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(ProductDescription.this, "No Results", Toast.LENGTH_SHORT).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductDescription.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Customer</font>"));

                        //First Step: convert ArrayList to an Object array.
                        Object[] objNames = userNames.toArray();

                        //Second Step: convert Object array to String array

                        final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);

                        int checkedItem = 0;
                        strCust = userIdList.get(checkedItem);
                        strCustName = userNames.get(checkedItem);
                        builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // user checked an item
                                strCust = userIdList.get(which);
                                strCustName = userNames.get(which);
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
                                customerText.setText(strCustName + " (" + strCust + ")");
                                customerStrName = strCustName;
                                customerStr = strCust;
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
                Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        });

    }

}

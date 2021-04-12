package com.ortusolis.rotarytarana.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.gson.reflect.TypeToken;
import com.ortusolis.rotarytarana.NetworkUtility.Constants;
import com.ortusolis.rotarytarana.NetworkUtility.IResult;
import com.ortusolis.rotarytarana.NetworkUtility.WebserviceController;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.pojo.AssignedResponse;
import com.ortusolis.rotarytarana.pojo.DistributorResponse;
import com.ortusolis.rotarytarana.pojo.GeneralResponse;
import com.ortusolis.rotarytarana.pojo.ProductDetails;
import com.ortusolis.rotarytarana.pojo.ProductRequest;
import com.ortusolis.rotarytarana.pojo.ProductRequestCart;
import com.ortusolis.rotarytarana.pojo.ProductsStock;
import com.ortusolis.rotarytarana.pojo.UserInfo;
import com.ortusolis.rotarytarana.service.RazorPayActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDescription extends AppCompatActivity {

    TextView minusQuantity, plusQuantity,minusQuantityReturn, plusQuantityReturn,textName,descriptionText,distributorText,customerText,dayText;
    TextView returnQuantity,returnQu,deliveryTime,startDateText,endDateText,scheduleText;
    Button placeOrder,addToCart,salesVoucher;
    ImageButton editProduct;
    ImageView picture;
    LinearLayout orderPlaceLayout,customerLayout,normalLL,scheduleLL;
    View customeLinearLayoutView;
    String paymentMethod="";
    String salesVaocherFalg="no";
    String delivaryAddress="";
    float totalAmountPayment=0;
    String m_Text = "";
    ProductRequest productRequestFinal = new ProductRequest();
    List<ProductRequestCart> productRequests;
    java.util.HashMap<String,String> productlistMap=new HashMap<String,String>();
    ProgressDialog progressDialog;
    Toolbar mToolbar;
    ActionBar action;
//
final ProductRequest productRequest = new ProductRequest();

    final ProductRequest.RequestS requestS =  new ProductRequest.RequestS();
    //
    float price = 0;
    TextView totalPrice,description,totalPriceWithGst;
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
    DatePickerDialog startDate;
    DatePickerDialog startScheduleTime;
    DatePickerDialog endScheduleTime;
    boolean schedule = false;
    String scheduleString = "Daily";
    List<String> scheduleDays = null;
    public AssignedResponse CashResponseOnly;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

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
        addToCart = findViewById(R.id.addToCart);
        totalPrice = findViewById(R.id.totalPrice);
        totalPriceWithGst = findViewById(R.id.totalPriceWithGst);
        description = findViewById(R.id.description);
        textName = findViewById(R.id.text);
        editProduct = findViewById(R.id.editProduct);
        descriptionText = findViewById(R.id.descriptionText);
        distributorText = findViewById(R.id.distributoStr);
        customerLayout = findViewById(R.id.customeLinearLayout);
        normalLL = findViewById(R.id.normalLL);
        scheduleLL = findViewById(R.id.scheduleLL);
        customerText = findViewById(R.id.customerStr);
        startDateText = findViewById(R.id.startDate);
        endDateText = findViewById(R.id.endDate);
        scheduleText = findViewById(R.id.scheduleText);
        deliveryTime = findViewById(R.id.deliveryTime);
        orderPlaceLayout = findViewById(R.id.orderPlace);
        customeLinearLayoutView = findViewById(R.id.customeLinearLayoutView);
        dayText = findViewById(R.id.dayText);
        picture = findViewById(R.id.picture);
        salesVoucher = findViewById(R.id.salesVoucher);
        gson = new Gson()/*new GsonBuilder().serializeNulls().create()*/;

        mToolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences("water_management",0);
        float totalCost =0;
        totalAmountPayment=totalCost;
        productRequests = new ArrayList<>();
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
            paymentMethod=getIntent().getStringExtra("paymentMethod");
        }

        if (getIntent().hasExtra("schedule")){
            schedule = getIntent().getExtras().getBoolean("schedule");
        }

        if (getIntent().hasExtra("customerId") && sharedPreferences.contains("userRoleId") && !(sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4"))){
        }

        setValues();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("000")) {
                    Intent intent = new Intent(ProductDescription.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    insertOrderAndProducttoCart();
                }
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("000")) {
                    Intent intent = new Intent(ProductDescription.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    insertOrderAndProduct(false);
                }

            }
        });

        salesVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesVaocherFalg="yes";
                insertOrderAndProduct(true);
            }
        });

        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(returnQu.getText().toString())) {
                    int quant = Integer.parseInt(returnQu.getText().toString());
                    quant = quant - 1;
                    if (quant >= 1) {
                        float total = price * quant;
                        totalPrice.setText(getString(R.string.Rs) + (String.format("%.02f", Float.valueOf(productDetails.getProductBasePrice())*quant)));
                        Double priceWithGst= total+total* (Float.valueOf(productDetails.getGst())*0.01);
                        totalPriceWithGst.setText(getString(R.string.Rs)+String.format("%.2f", total));
                        returnQu.setText(quant + "");
                    }
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
                    if (TextUtils.isEmpty(returnQu.getText().toString())) {
                        returnQu.setText("1");
                    }
                    int quant = Integer.parseInt(returnQu.getText().toString());
                    quant = quant + 1;
                    float total = price * quant;
                     totalPrice.setText(getString(R.string.Rs) + (String.format("%.02f", Float.valueOf(productDetails.getProductBasePrice())*quant)));
                     Double priceWithGst= total+total* (Float.valueOf(productDetails.getGst())*0.01);
                     totalPriceWithGst.setText(getString(R.string.Rs)+String.format("%.2f", total));
                    returnQu.setText(quant + "");
            }
        });


        returnQu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(returnQu.getText().toString())) {
                    int quant = Integer.parseInt(returnQu.getText().toString());
                    float total = price * quant;
                    totalPrice.setText(getString(R.string.Rs) + (String.format("%.02f", Float.valueOf(productDetails.getProductBasePrice())*quant)));
                    Double priceWithGst= total+total* (Float.valueOf(productDetails.getGst())*0.01);
                    totalPriceWithGst.setText(getString(R.string.Rs)+String.format("%.2f", total));
                }
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
        else if(sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            salesVoucher.setVisibility(View.GONE);
        }

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
            customerLayout.setVisibility(View.GONE);
            salesVoucher.setVisibility(View.GONE);
            customeLinearLayoutView.setVisibility(View.GONE);
            customerStr = sharedPreferences.getString("userid","");
            customerStrName = sharedPreferences.getString("username","");
        }
        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("000")){
            customerLayout.setVisibility(View.GONE);
            salesVoucher.setVisibility(View.GONE);
            customeLinearLayoutView.setVisibility(View.GONE);
//            customerStr = sharedPreferences.getString("userid","");
//            customerStrName = sharedPreferences.getString("username","");
        }
        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("2")){
            customeLinearLayoutView.setVisibility(View.VISIBLE);
            customerLayout.setVisibility(View.VISIBLE);
        }
        if(sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")){
            customeLinearLayoutView.setVisibility(View.VISIBLE);
            customerLayout.setVisibility(View.VISIBLE);
        }

        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMappedRegister("");
            }
        });

        if (schedule){
            normalLL.setVisibility(View.GONE);
            scheduleLL.setVisibility(View.VISIBLE);
            orderPlaceLayout.setVisibility(View.GONE);
        }
        else {
            normalLL.setVisibility(View.VISIBLE);
            scheduleLL.setVisibility(View.GONE);
            orderPlaceLayout.setVisibility(View.VISIBLE);
        }

        deliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startDate = new DatePickerDialog(ProductDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        selectDateStr = format.format(newCalendar.getTime());

                        deliveryTime.setText( selectDateStr);

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                startDate.show();
            }
        });

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startDate = new DatePickerDialog(ProductDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        selectStartDateStr = format.format(newCalendar.getTime());

                        startDateText.setText( selectStartDateStr);

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                startDate.show();
            }
        });

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCalendar = Calendar.getInstance();
                startDate = new DatePickerDialog(ProductDescription.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newCalendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        selectEndDateStr = format.format(newCalendar.getTime());

                        endDateText.setText( selectEndDateStr);

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                startDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                startDate.show();
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());
        selectDateStr=currentDate;
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


        if (productDetails.getProductImage()!=null) {
            Picasso.get().load(productDetails.getProductImage()).into(picture);
            Log.i("tag", "This'll run 3000 milliseconds later");
        } else {
            picture.setImageResource(R.drawable.no_image);
        }
        distributorStr="1";

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
         description.setText("Price "+getString(R.string.Rs)+String.format("%.2f", Float.valueOf(productDetails.getProductPrice())));
         descriptionText.setText(""+productDetails.getProductDescription());
         price = Float.valueOf(productDetails.getProductPrice());
//         if(productDetails.getProductPrice()!=null && productDetails.getGst() !=null){
//             Double priceWithGst= Float.valueOf(productDetails.getProductPrice())+Float.valueOf(productDetails.getProductPrice())* (Float.valueOf(productDetails.getGst())*0.01);
//             totalPriceWithGst.setText(getString(R.string.Rs)+priceWithGst);
////           price = priceWithGst.floatValue();
//         }
         totalPriceWithGst.setText(getString(R.string.Rs)+String.format("%.2f", Float.valueOf(productDetails.getProductPrice())));
         totalPrice.setText(getString(R.string.Rs)+String.format("%.2f", Float.valueOf(productDetails.getProductBasePrice())));
         orderPlaceLayout.setVisibility(View.VISIBLE);
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

    void insertOrderAndProduct(final boolean salesVoucher){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());

        if (distributorStr != null && customerStr != null ) { //&& selectDateStr!=null
            showAlertDialog(salesVoucher);
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Facilitator", Toast.LENGTH_LONG).show();
        }
    }

    void insertOrderAndProducttoCart(){

        productRequests = gson.fromJson(sharedPreferences.getString("prodDesc",""), new TypeToken<ArrayList<ProductRequestCart>>(){}.getType());
        if( productRequests!=null) {
            Log.e("productRequests", String.valueOf(productRequests.size()));
            for (int productCountId = 0; productCountId < productRequests.size(); productCountId++) {
                productlistMap.put(productRequests.get(productCountId).getProductId(), productRequests.get(productCountId).getDelivaryDate());
            }

            Set<String> keys = productlistMap.keySet(); // The set of keys in the map.

            Iterator<String> keyIter = keys.iterator();
            for (Map.Entry map : productlistMap.entrySet()) {
                String getProductIdValue = "";
                getProductIdValue = productlistMap.get(productDetails.getProductId());
                if (getProductIdValue != null) {
                    if (getProductIdValue.equals(selectDateStr)) {
                        Toast.makeText(ProductDescription.this, productDetails.getProductName() + " already exist in cart with same date", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());

        if (distributorStr != null && customerStr != null ) {//&& selectDateStr!=null

            int quant = Integer.parseInt(returnQu.getText().toString());
            ProductRequestCart requestS = null;
            ArrayList<ProductRequestCart> productOrders;

            if (TextUtils.isEmpty(sharedPreferences.getString("prodDesc",""))) {
                requestS = new ProductRequestCart();
                requestS.setCustomerId(customerStr);
                requestS.setOrderProductName(productDetails.getProductName());
                requestS.setProductImageUrl(productDetails.getProductImage());
                requestS.setCustomerName(customerStrName);
                requestS.setOrderDate(currentDate);
                requestS.setDelivaryDate(selectDateStr);
                requestS.setDistributorId(distributorStr);
                requestS.setOrderCost((price * quant)+"");
                requestS.setAddress(sharedPreferences.getString("userAddress1",""));
                if(sharedPreferences.getString("userlatitudeProfile", "").equals("")){
                    requestS.setUserLat(sharedPreferences.getString("latitude", ""));
                    requestS.setUserLong(sharedPreferences.getString("longitude", ""));
                }else {
                    requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile", ""));
                    requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile", ""));
                }
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
                requestS.setOrderProductName(productDetails.getProductName());
                requestS.setProductImageUrl(productDetails.getProductImage());
                requestS.setCustomerName(customerStrName);
                requestS.setOrderDate(currentDate);
                requestS.setDelivaryDate(selectDateStr);
                requestS.setDistributorId(distributorStr);
                requestS.setOrderCost((price * quant)+"");
                requestS.setAddress(sharedPreferences.getString("userAddress1",""));
                if(sharedPreferences.getString("userlatitudeProfile", "").equals("")){
                    requestS.setUserLat(sharedPreferences.getString("latitude", ""));
                    requestS.setUserLong(sharedPreferences.getString("longitude", ""));
                }else {
                    requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile", ""));
                    requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile", ""));
                }
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
                    Toast.makeText(ProductDescription.this, productDetails.getProductName()+" added to cart successfully", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            },1000);
        }

        else if (distributorStr == null){
            Toast.makeText(this, "Please select Facilitator", Toast.LENGTH_LONG).show();
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
            jsonObject.put("searchvalue", customer?"5":"2");
            jsonObject.put("distributorId", "1");
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
                                    distributorStr = strDist;
                                }

                            }
                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(ProductDescription.this, "No Results", Toast.LENGTH_LONG).show();
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
                            if (yesT){
                            }
                            else {
                                insertOrderAndProducttoCart();
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
            }
        });
    }


    void scheduleN() throws JSONException {

        String weekDays = "";

        for (String s:scheduleDays){
            weekDays = weekDays + (TextUtils.isEmpty(weekDays)? "" : ",") + "\""+s+"\"";
        }

        if (distributorStr != null && customerStr != null && selectStartDateStr!=null) {
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
                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();

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
        else if (selectStartDateStr == null){
            Toast.makeText(this, "Please select Start Date", Toast.LENGTH_LONG).show();
        }
        else if (customerStr == null){
            Toast.makeText(this, "Please select Requester", Toast.LENGTH_LONG).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Facilitator", Toast.LENGTH_LONG).show();
        }
    }

    void getMappedRegister(final String custId){
        progressDialog = new ProgressDialog(ProductDescription.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(ProductDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mappedTo", "1");
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
                progressDialog.dismiss();
                try {
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                        strCust = "";
                        userNames.clear();
                        userIdList.clear();

                        for (UserInfo userInfo1: distributorResponse.getResponseData().getUserDetails()){

                            if (userInfo1.getUserRoleId().equalsIgnoreCase("5")) {
                                userNames.add(userInfo1.getFirstName());
                                userIdList.add(userInfo1.getUserId());
                            }

                            if (userInfo1.getUserId().equalsIgnoreCase(custId)){
                                customerText.setText(userInfo1.getFirstName() + " (" + userInfo1.getUserId() + ")");
                                customerStr = userInfo1.getUserId();
                                customerStrName = userInfo1.getFirstName();
                            }

                        }

                        if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                            Toast.makeText(ProductDescription.this, "No Results", Toast.LENGTH_LONG).show();
                        }

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductDescription.this);
                        builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose Beneficiary</font>"));

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

                        if (custId.isEmpty())
                        builderSingle.show();
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
            }
        });

    }

    private void showAlertDialog(Boolean salesVoucher) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());
        int quant = Integer.parseInt(returnQu.getText().toString());

//        final ProductRequest productRequest = new ProductRequest();
//
//        final ProductRequest.RequestS requestS =  new ProductRequest.RequestS();

        requestS.setCustomerId(customerStr);
        requestS.setCustomerName(customerStrName);
        requestS.setOrderDate(currentDate);
        requestS.setDelivaryDate(selectDateStr);
        requestS.setDistributorId("1"); //distributorStr
        requestS.setOrderCost((price * quant)+"");
        requestS.setAssignedId(salesVoucher?sharedPreferences.getString("userid","") : null);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDescription.this);
        alertDialog.setTitle("Choose Delivery Address");
        String[] items = {"Primary Address","Secondary Address","New Address"};
        int checkedItem = 0;
        delivaryAddress="Primary";

        LinearLayout layout = new LinearLayout(ProductDescription.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView input = new TextView (ProductDescription.this);
        input.setPadding(80, 20, 20, 20);
        layout.addView(input);

        final EditText Address_textbox = new EditText(ProductDescription.this);
        layout.addView(Address_textbox);

        alertDialog.setView(layout);
        Address_textbox.setVisibility(View.GONE);
        input.setText("Address: "+sharedPreferences.getString("userAddress1",""));
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(ProductDescription.this, "Primary Address", Toast.LENGTH_LONG).show();
                        delivaryAddress="Primary";
                        input.setText("Address: "+sharedPreferences.getString("userAddress1",""));
                        Address_textbox.setVisibility(View.GONE);
                        break;
                    case 1:
                        Toast.makeText(ProductDescription.this, "Secondary Address", Toast.LENGTH_LONG).show();
                        delivaryAddress="Secondary";
                        input.setText("Address: "+sharedPreferences.getString("userAddress2",""));
                        Address_textbox.setVisibility(View.GONE);
                        break;
                    case 2:
                        delivaryAddress="New";
                        input.setText("Enter New Address");
                        Address_textbox.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        alertDialog.setNeutralButton("Profile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ProductDescription.this, UserProfileActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });;

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // navigation
//                Toast.makeText(ProductDescription.this, paymentMethod, Toast.LENGTH_LONG).show();
                if (delivaryAddress.equals("Primary")) {
                    input.setText(sharedPreferences.getString("userAddress1", ""));
                    requestS.setAddress(sharedPreferences.getString("userAddress1", ""));
                    if(sharedPreferences.getString("userlatitudeProfile", "").equals("")){
                        requestS.setUserLat(sharedPreferences.getString("latitude", ""));
                        requestS.setUserLong(sharedPreferences.getString("longitude", ""));
                    }else {
                        requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile", ""));
                        requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile", ""));
                    }
                } else if (delivaryAddress.equals("Secondary")) {
                    input.setText(sharedPreferences.getString("userAddress2", ""));
                    requestS.setAddress(sharedPreferences.getString("userAddress2", ""));
                    if(sharedPreferences.getString("userlatitudeSecondProfile", "").equals("")){
                        requestS.setUserLat(sharedPreferences.getString("latitude", ""));
                        requestS.setUserLong(sharedPreferences.getString("longitude", ""));
                    }else {
                        requestS.setUserLat(sharedPreferences.getString("userlatitudeSecondProfile", ""));
                        requestS.setUserLong(sharedPreferences.getString("userlangitudeSecondProfile", ""));
                    }

                } else {
                    requestS.setAddress(Address_textbox.getText().toString());
                    if(sharedPreferences.getString("userlatitudeSecondProfile", "").equals("")){
                        requestS.setUserLat(sharedPreferences.getString("latitude", ""));
                        requestS.setUserLong(sharedPreferences.getString("longitude", ""));
                    }else {
                        requestS.setUserLat(sharedPreferences.getString("userlatitudeSecondProfile", ""));
                        requestS.setUserLong(sharedPreferences.getString("userlangitudeSecondProfile", ""));
                    }
                }
                dialog.dismiss();
                final String finalprice = totalPriceWithGst.getText().toString();
                final float totalfinalprice = Float.valueOf(finalprice.substring(1, finalprice.length()));
                if (sharedPreferences.contains("userSwitchRoleId") && (sharedPreferences.getString("userSwitchRoleId", "").equalsIgnoreCase("1")) || (sharedPreferences.getString("userSwitchRoleId", "").equalsIgnoreCase("2"))) {


                AlertDialog.Builder alertDialogCash = new AlertDialog.Builder(ProductDescription.this);
                alertDialogCash.setTitle("Choose Payment Option");
                String[] itemsCash = {"Request", "Payment"};
                int checkedItemCash = 1;
                paymentMethod = "";
                alertDialogCash.setSingleChoiceItems(itemsCash, checkedItemCash, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                paymentMethod = "Request";
                                break;
                            case 1:
                                paymentMethod = "Payment";
                                break;
                        }
                    }
                });
                alertDialogCash.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialogCash.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                         navigation
                        final String finalprice = totalPrice.getText().toString();
                        final float totalfinalprice = Float.valueOf(finalprice.substring(1, finalprice.length()));
                        payOn();


//                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
//                        if (paymentMethod.equals("Request")) {
//                            builder.setMessage("Your Order Cost is "+finalprice+", click Confirm to place request");
//                        }else {
//                            builder.setMessage("Your Order Cost is "+finalprice+", click Confirm to place order");
//                        }
//                        builder.setCancelable(true)
//                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        if (paymentMethod.equals("Request")) {
//                                            dialog.dismiss();
//                                            requestS.setPaymentStatus("NewRequest");
//                                            requestS.setOrderStatus("NewRequest");
//                                            productRequest.setRequest(requestS);
//                                            productRequestFinal=productRequest;
//                                            String str = gson.toJson(productRequest);
//                                            CashAlert();
//                                        } else {
//                                            requestS.setPaymentStatus("paid");
//                                            requestS.setOrderStatus("New");
//                                            productRequest.setRequest(requestS);
//                                            productRequestFinal=productRequest;
//                                            String str = gson.toJson(productRequest);
//                                            String useremail= sharedPreferences.getString("emailIdUser","");
//                                            Log.e("useremail",sharedPreferences.getString("emailIdUser",""));
//                                            final String strProductRequests = str;
//                                            onBackPressed();
//                                            Intent totalAmountPaymentintent = new Intent(ProductDescription.this, RazorPayActivity.class);
//                                            totalAmountPaymentintent.putExtra("totalAmountPayment", totalfinalprice);
//                                            totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
//                                            totalAmountPaymentintent.putExtra("classFlag", "placeorder");
//                                            totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
//                                            startActivity(totalAmountPaymentintent);
//                                        }
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();


                    }
                });
                AlertDialog alert = alertDialogCash.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }else {
                    paymentMethod="Payment";
                    payOn();
                }



            }
        });
        AlertDialog alertAddress = alertDialog.create();
        alertAddress.setCanceledOnTouchOutside(false);
        alertAddress.show();
    }

public  void  CashAlert(){

    WebserviceController wss = new WebserviceController(ProductDescription.this);

    String str = gson.toJson(productRequestFinal);

    wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
        @Override
        public void notifySuccess(String response, int statusCode) {
            try {
                Log.e("getPlants response", response);

                final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);

                if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
                    builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "") + "<br/><br/> Delivery date is " + selectDateStr));

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
                }

                else {
                    Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void notifyError(VolleyError error) {
            Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            if (progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    });

}

public  void Alertpayment(){
      final String finalprice=totalPrice.getText().toString();
        final float totalfinalprice=Float.valueOf(finalprice.substring(1,finalprice.length()));
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
        builder.setMessage("Your Order Cost is "+finalprice+", click Confirm to place order")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        insertOrderAndProduct(false);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


private void buildAlertMessageNoGps() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    dialog.cancel();
                }
            });
    final AlertDialog alert = builder.create();
    alert.show();
}

public void payOn(){

    final String finalprice=totalPriceWithGst.getText().toString();
    final float totalfinalprice=Float.valueOf(finalprice.substring(1,finalprice.length()));
    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
    if (paymentMethod.equals("Request")) {
        builder.setMessage("Your Order Cost is "+finalprice+", click Confirm to place request");
    }else {
        builder.setMessage("Your Order Cost is "+finalprice+", click Confirm to place order");
    }
    builder.setCancelable(true)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (paymentMethod.equals("Request")) {
                        dialog.dismiss();
                        requestS.setPaymentStatus("NewRequest");
                        //paymentflowstatus
                        requestS.setOrderStatus("NewRequest");
                        requestS.setPaymentFlowStatus("gift");
                        productRequest.setRequest(requestS);
                        productRequestFinal=productRequest;
                        String str = gson.toJson(productRequest);
                        CashAlert();
                    } else {
                        requestS.setPaymentStatus("paid");
                        requestS.setOrderStatus("New");
                        //paymentflowstatus
                        productRequest.setRequest(requestS);
                        productRequestFinal=productRequest;
                        String str = gson.toJson(productRequest);
                        String useremail= sharedPreferences.getString("emailIdUser","");
                        Log.e("useremail",sharedPreferences.getString("emailIdUser",""));
                        final String strProductRequests = str;
                        onBackPressed();
                        Intent totalAmountPaymentintent = new Intent(ProductDescription.this, RazorPayActivity.class);
                        totalAmountPaymentintent.putExtra("totalAmountPayment", totalfinalprice);
                        totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
                        totalAmountPaymentintent.putExtra("classFlag", "placeorder");
                        totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
                        totalAmountPaymentintent.putExtra("productRequestObject",  productRequest);
                        startActivity(totalAmountPaymentintent);
                    }
                }
            });
    AlertDialog alert = builder.create();
    alert.show();
}
}
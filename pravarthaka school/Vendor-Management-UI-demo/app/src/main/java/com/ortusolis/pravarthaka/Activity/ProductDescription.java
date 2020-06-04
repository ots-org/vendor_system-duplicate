package com.ortusolis.pravarthaka.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
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
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ortusolis.pravarthaka.NetworkUtility.Constants;
import com.ortusolis.pravarthaka.NetworkUtility.IResult;
import com.ortusolis.pravarthaka.NetworkUtility.WebserviceController;
import com.ortusolis.pravarthaka.R;
import com.ortusolis.pravarthaka.location.LocationResultHelper;
import com.ortusolis.pravarthaka.pojo.AssignedResponse;
import com.ortusolis.pravarthaka.pojo.DistributorResponse;
import com.ortusolis.pravarthaka.pojo.GeneralResponse;
import com.ortusolis.pravarthaka.pojo.ProductDetails;
import com.ortusolis.pravarthaka.pojo.ProductRequest;
import com.ortusolis.pravarthaka.pojo.ProductRequestCart;
import com.ortusolis.pravarthaka.pojo.ProductsStock;
import com.ortusolis.pravarthaka.pojo.UserInfo;
import com.ortusolis.pravarthaka.service.RazorPayActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDescription extends AppCompatActivity {

    TextView minusQuantity, plusQuantity,minusQuantityReturn, plusQuantityReturn,textName,descriptionText,distributorText,customerText,dayText;
    TextView returnQuantity,returnQu,deliveryTime,startDateText,endDateText,scheduleText;
    Button placeOrder,addToCart,scheduleButton,salesVoucher;
    ImageButton editProduct;
    ImageView picture;
    LinearLayout orderPlaceLayout,customerLayout,normalLL,scheduleLL;
    View customeLinearLayoutView;
String paymentMethod="";
    String salesVaocherFalg="no";
    //
    float totalAmountPayment=0;
    String m_Text = "";
    ProductRequest productRequestFinal = new ProductRequest();
    List<ProductRequestCart> productRequests;
    java.util.HashMap<String,String> productlistMap=new HashMap<String,String>();
    ProgressDialog progressDialog;
    //
    Toolbar mToolbar;
    ActionBar action;

    float price = 0;
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
    DatePickerDialog startDate;
    DatePickerDialog startScheduleTime;
    DatePickerDialog endScheduleTime;
    boolean schedule = false;
    String scheduleString = "Daily";
    List<String> scheduleDays = null;
    //
    public AssignedResponse CashResponseOnly;
    //

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
        //
        float totalCost =0;
        totalAmountPayment=totalCost;
        productRequests = new ArrayList<>();
        //

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

        if (getIntent().hasExtra("customerId") && sharedPreferences.contains("userRoleId") && !(sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4"))){
//            getMappedRegister(getIntent().getStringExtra("customerId"));
        }

        setValues();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProductStock(productDetails.getProductId(),false);
                    insertOrderAndProducttoCart();
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProductStock(productDetails.getProductId(),true);
//                insertOrderAndProduct(false);
                Alertpayment();
            }
        });

        salesVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesVaocherFalg="yes";
//                showAlertDialog();
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
                        totalPrice.setText(getString(R.string.Rs) + (String.format("%.02f", total)));
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
                    totalPrice.setText(getString(R.string.Rs) + (String.format("%.02f", total)));
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
                    totalPrice.setText(getString(R.string.Rs) + (String.format("%.02f", total)));
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

        /*distributorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllRegister(true,false);
            }
        });
*/
        customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMappedRegister("");
//                getAllRegister(true,true);
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
//
                startDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//
                startDate.show();
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

//        Bitmap decodedByte = null;
//
//        if (productDetails.getProductImage()!=null) {
//            byte[] decodedString = Base64.decode(productDetails.getProductImage(), Base64.DEFAULT);
//            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        }
//
//        if (decodedByte != null) {
//            picture.setImageBitmap(decodedByte);
//        } else {
//            picture.setImageResource(R.drawable.no_image);
//        }
        //

        if (productDetails.getProductImage()!=null) {
            Picasso.get().load(productDetails.getProductImage()).into(picture);
            Log.i("tag", "This'll run 3000 milliseconds later");
        } else {
            picture.setImageResource(R.drawable.no_image);
        }
        distributorStr="1";
        //

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
         description.setText("Price "+getString(R.string.Rs)+Float.valueOf(productDetails.getProductPrice()));
         descriptionText.setText(""+productDetails.getProductDescription());

         price = Float.valueOf(productDetails.getProductPrice());
         totalPrice.setText(getString(R.string.Rs)+price);


         /*if (productDetails.getStock().equalsIgnoreCase("no")){
             orderPlaceLayout.setVisibility(View.GONE);
         }
         else {*/
             orderPlaceLayout.setVisibility(View.VISIBLE);
         //}

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
//        new LocationResultHelper().
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());

        if (distributorStr != null && customerStr != null && selectDateStr!=null) {
//            //code
//            progressDialog = new ProgressDialog(ProductDescription.this);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            //
//            WebserviceController wss = new WebserviceController(ProductDescription.this);

//            int quant = Integer.parseInt(returnQu.getText().toString());
//
//            ProductRequest productRequest = new ProductRequest();
//
//            ProductRequest.RequestS requestS =  new ProductRequest.RequestS();
//
//            requestS.setCustomerId(customerStr);
//            requestS.setCustomerName(customerStrName);
//            requestS.setOrderDate(currentDate);
//            requestS.setDelivaryDate(selectDateStr);
//            requestS.setDistributorId(distributorStr);
//            requestS.setOrderCost((price * quant)+"");
//            requestS.setUserLat(sharedPreferences.getString("latitude",""));
//            requestS.setUserLong(sharedPreferences.getString("longitude",""));
//            requestS.setPaymentStatus("paid");
//            requestS.setAssignedId(salesVoucher?sharedPreferences.getString("userid","") : null);
//            requestS.setOrderStatus(salesVoucher?"Assigned":"New");
//            requestS.setOrderNumber("");
//            requestS.setDeliverdDate("");
//
//            ProductRequest.RequestS.ProductOrder productOrder = new ProductRequest.RequestS.ProductOrder();
//            productOrder.setOts_delivered_qty("0");
//            productOrder.setOrderProductId(null);
//            productOrder.setOrderdId(null);
//            productOrder.setProductId(productDetails.getProductId());
//            productOrder.setOrderedQty(quant+"");
//            productOrder.setProductStatus("New");
//            productOrder.setProductCost(price+"");
//            ArrayList<ProductRequest.RequestS.ProductOrder> productOrders = new ArrayList<>();
//            productOrders.add(productOrder);
//
//            requestS.setProductList(productOrders);
//
//            productRequest.setRequest(requestS);
//
//            /*JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("customerId", sharedPreferences.getString("userid", ""));
//                jsonObject.put("orderDate", dateStr);
//                jsonObject.put("delivaryDate", JSONObject.NULL);
//                jsonObject.put("distributorId", distributorStr);
//                jsonObject.put("orderCost", price+"");
//                jsonObject.put("assignedId", JSONObject.NULL);
//                jsonObject.put("orderStatus", "NEW");
//                jsonObject.put("orderNumber", JSONObject.NULL);
//                jsonObject.put("deliverdDate", JSONObject.NULL);
//
//                JSONArray jsonArray = new JSONArray();
//
//                JSONObject jsonObject1 = new JSONObject();
//
//                jsonObject1.put("ots_delivered_qty", JSONObject.NULL);
//                jsonObject1.put("orderProductId", JSONObject.NULL);
//                jsonObject1.put("orderdId", JSONObject.NULL);
//                jsonObject1.put("productId", productDetails.getProductId());
//                jsonObject1.put("orderedQty", quant+"");
//                jsonObject1.put("productStatus", "Active");
//                jsonObject1.put("productCost", price+"");
//
//                jsonArray.put(jsonObject1);
//
//                jsonObject.put("productList", jsonObject1);
//
//                requestObject.put("requestData", jsonObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }*/
//            productRequestFinal=productRequest;
//            String str = gson.toJson(productRequest);
            showAlertDialog(salesVoucher);
//
//            wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
//                @Override
//                public void notifySuccess(String response, int statusCode) {
//                    progressDialog.dismiss();
//                    try {
//                        Log.e("getPlants response", response);
//
//                        final AssignedResponse distributorResponse = gson.fromJson(response, AssignedResponse.class);
//
//                        if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
//
//                            if (salesVoucher){
//
//                                Intent intent = new Intent(ProductDescription.this, AssignedOrderListActivity.class);
//                                if (!distributorResponse.getResponseData().getOrderList().isEmpty())
//                                    intent.putExtra("orderId",distributorResponse.getResponseData().getOrderList().get(0).getOrderId());
//                                startActivity(intent);
//                            }
//                            else {
//                                Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();
////
////                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
////                                builder.setMessage(Html.fromHtml((distributorResponse.getResponseDescription() != null ? distributorResponse.getResponseDescription() : "") + "<br/><br/> Delivery date is " + selectDateStr));
////
////                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////
//////                                        //
//////                                        final Dialog dialog = new Dialog(this);
//////                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//////                                        dialog.setContentView(R.layout.dialog_layout);
//////                                        List<String> stringList=new ArrayList<>();  // here is list
//////                                        for(int i=0;i<2;i++) {
//////                                            if (i==0){
//////                                                stringList.add("Number Mode");
//////                                            }else {
//////                                                stringList.add("Character Mode");
//////                                            }
//////
//////                                        }
//////                                        //
////
////
////                                    }
////                                });
////
////                                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
////                                    @Override
////                                    public void onDismiss(DialogInterface dialog) {
////                                        onBackPressed();
////                                    }
////                                });
////                                builder.show();
//                                CashResponseOnly=distributorResponse;
//                                onBackPressed();
////                                showAlertDialog();
//                            }
//
//                        }else
//                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void notifyError(VolleyError error) {
//                    progressDialog.dismiss();
//                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                    Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
//                }
//            });
        }
        else if (selectDateStr == null){
            Toast.makeText(this, "Please select Delivery Date", Toast.LENGTH_LONG).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Distributor", Toast.LENGTH_LONG).show();
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

        if (distributorStr != null && customerStr != null && selectDateStr!=null) {

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
                requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile",""));
                requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile",""));
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
                requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile",""));
                requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile",""));
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
//                    Intent intent = new Intent(ProductDescription.this, CardListActivity.class);
//                    startActivity(intent);
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
                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
                    Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
                }
            });*/
        }
        else if (selectDateStr == null){
            Toast.makeText(this, "Please select Delivery Date", Toast.LENGTH_LONG).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Distributor", Toast.LENGTH_LONG).show();
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
//                                    distributorText.setText(strDistName + " (" + strDist + ")");
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
//                                        distributorText.setText(strDistName + " (" + strDist + ")");
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

//                                distributorText.setText(strDistName + " (" + strDist + ")");
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
//                Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
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

                        //if (Integer.valueOf(responseData.getResponseData().getStockQuantity())>0 && (Integer.parseInt(returnQu.getText().toString()) <= Integer.valueOf(responseData.getResponseData().getStockQuantity()))){

                            if (yesT){
//                                insertOrderAndProduct(false);
                            }
                            else {
                                insertOrderAndProducttoCart();
                            }

                        /*}
                        else {
                            if((Integer.parseInt(returnQu.getText().toString()) > Integer.valueOf(responseData.getResponseData().getStockQuantity()))){
                                Toast.makeText(ProductDescription.this, "Please order a smaller quantity, available quantity is only "+responseData.getResponseData().getStockQuantity() , Toast.LENGTH_LONG).show();
                            }
                            else
                            Toast.makeText(ProductDescription.this, "Out of Stock", Toast.LENGTH_LONG).show();
                        }*/

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
//                Toast.makeText(ProductDescription.this , stringBuilder.toString(), Toast.LENGTH_LONG).show();
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

                        Toast.makeText(ProductDescription.this, distributorResponse.getResponseDescription()!=null?distributorResponse.getResponseDescription(): "", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                    Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (selectStartDateStr == null){
            Toast.makeText(this, "Please select Start Date", Toast.LENGTH_LONG).show();
        }
        else if (selectEndDateStr == null){
            Toast.makeText(this, "Please select End Date", Toast.LENGTH_LONG).show();
        }
        else if (customerStr == null){
            Toast.makeText(this, "Please select Customer", Toast.LENGTH_LONG).show();
        }
        else if (distributorStr == null){
            Toast.makeText(this, "Please select Distributor", Toast.LENGTH_LONG).show();
        }
    }

    void getMappedRegister(final String custId){
        //code
        progressDialog = new ProgressDialog(ProductDescription.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //
        WebserviceController wss = new WebserviceController(ProductDescription.this);

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
//            if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
//                jsonObject.put("mappedTo", sharedPreferences.getString("distId", ""));
//            }
//            else {
//                jsonObject.put("mappedTo", sharedPreferences.getString("userid", ""));
//            }
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

                            if (userInfo1.getUserRoleId().equalsIgnoreCase("4")) {
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
//                Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
            }
        });

    }
    //
    private void showAlertDialog(Boolean salesVoucher) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = format.format(Calendar.getInstance().getTime());
        int quant = Integer.parseInt(returnQu.getText().toString());

        final ProductRequest productRequest = new ProductRequest();

        final ProductRequest.RequestS requestS =  new ProductRequest.RequestS();

        requestS.setCustomerId(customerStr);
        requestS.setCustomerName(customerStrName);
        requestS.setOrderDate(currentDate);
        requestS.setDelivaryDate(selectDateStr);
        requestS.setDistributorId(distributorStr);
        requestS.setOrderCost((price * quant)+"");
        requestS.setAddress(sharedPreferences.getString("userAddress1",""));
        requestS.setUserLat(sharedPreferences.getString("userlatitudeProfile",""));
        requestS.setUserLong(sharedPreferences.getString("userlangitudeProfile",""));

        requestS.setAssignedId(salesVoucher?sharedPreferences.getString("userid","") : null);
        requestS.setOrderStatus(salesVoucher?"Assigned":"New");
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






        final String finalprice=totalPrice.getText().toString();
        final float totalfinalprice=Float.valueOf(finalprice.substring(1,finalprice.length()));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDescription.this);
        alertDialog.setTitle("Choose Payment Option");
        String[] items = {"Cash","Payment"};
        int checkedItem = 1;
         paymentMethod="";
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(ProductDescription.this, "Clicked on Cash", Toast.LENGTH_LONG).show();
                        paymentMethod="Cash";
                        break;
                    case 1:
                        Toast.makeText(ProductDescription.this, "Clicked on Payment", Toast.LENGTH_LONG).show();
                        paymentMethod="Payment";
                        break;

                }
            }
        });
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
                Toast.makeText(ProductDescription.this, paymentMethod, Toast.LENGTH_LONG).show();
                if (paymentMethod.equals("Cash")) {
                    dialog.dismiss();
                    requestS.setPaymentStatus("cash");
                    productRequest.setRequest(requestS);

                    productRequestFinal=productRequest;
                    String str = gson.toJson(productRequest);
                    CashAlert();
                } else {
                    requestS.setPaymentStatus("paid");
                    productRequest.setRequest(requestS);

                    productRequestFinal=productRequest;
                    String str = gson.toJson(productRequest);

                                    /*   Intent myIntent = new Intent(ProductDescription.this, RazorPayActivity.class);
                                       myIntent.putExtra("totalAmountPayment", Float.toString(totalAmountPayment));
                                       startActivity(myIntent);*/


//                    if (paymentMethod.equals("Payment")) {
                    String useremail= sharedPreferences.getString("emailIdUser","");
                    Log.e("useremail",sharedPreferences.getString("emailIdUser",""));
                    final String strProductRequests = str;
//                    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
//
//                     if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//                        buildAlertMessageNoGps();
//                    }else{

                        onBackPressed();
                        Intent totalAmountPaymentintent = new Intent(ProductDescription.this, RazorPayActivity.class);
                        //                               // pass amount
                        totalAmountPaymentintent.putExtra("totalAmountPayment", totalfinalprice);
                        totalAmountPaymentintent.putExtra("salesVaocherFalg", salesVaocherFalg);
                        totalAmountPaymentintent.putExtra("classFlag", "placeorder");
                        totalAmountPaymentintent.putExtra("productRequests", strProductRequests);
                        //                                 Log.d("hi",Parsef totalAmountPayment);
                        startActivity(totalAmountPaymentintent);
//                    }

                    }

//                    }
//                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //
    //
public  void  CashAlert(){ //

    WebserviceController wss = new WebserviceController(ProductDescription.this);

    String str = gson.toJson(productRequestFinal);

    wss.postLoginVolley(Constants.insertOrderAndProduct, str, new IResult() {
        @Override
        public void notifySuccess(String response, int statusCode) {
//                deleteFormCart();
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
//            Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
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
                        //do things
//                        placeOrder(prepareOrder(productRequests),false);
                        insertOrderAndProduct(false);
//                        showAlertDialog();
//                        Intent totalAmountPaymentintent = new Intent(ProductDescription.this, RazorPayActivity.class);
////                    // pass amount
//                        totalAmountPaymentintent.putExtra("totalAmountPayment",totalfinalprice );
//                        totalAmountPaymentintent.putExtra("salesVaocherFalg",salesVaocherFalg );
//                        totalAmountPaymentintent.putExtra("productRequests",productRequests.toString() );
////                  Log.d("hi",Parsef totalAmountPayment);
//                        startActivity(totalAmountPaymentintent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    void getCustomerDetails(){
//        WebserviceController wss = new WebserviceController(ProductDescription.this);
//
//        JSONObject requestObject = new JSONObject();
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("searchKey", "UserRoleId");
//            jsonObject.put("searchvalue", "2");
//            jsonObject.put("distributorId", "1");
//
//            requestObject.put("requestData",jsonObject);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        userNames.clear();
//        userIdList.clear();
//
//        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
//            @Override
//            public void notifySuccess(String response, int statusCode) {
//                try {
//                    Log.e("getPlants response", response);
//
//                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);
//
//                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
//
//
//
//                        if (showPop){
//
//                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductDescription.this);
//                            builderSingle.setTitle(Html.fromHtml("<font color='#000000'>Choose distributor</font>"));
//
//                            //First Step: convert ArrayList to an Object array.
//                            Object[] objNames = userNames.toArray();
//
//                            //Second Step: convert Object array to String array
//
//                            final String[] strOpts = Arrays.copyOf(objNames, objNames.length, String[].class);
//
//                            int checkedItem = 0;
//                            if (customer){
//                                strCust = userIdList.get(checkedItem);
//                                strCustName = userNames.get(checkedItem);
//                            }
//                            else {
//                                strDist = userIdList.get(checkedItem);
//                                strDistName = userNames.get(checkedItem);
//                            }
//                            builderSingle.setSingleChoiceItems(strOpts, checkedItem, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // user checked an item
//                                    if (customer){
//                                        strCust = userIdList.get(which);
//                                        strCustName = userNames.get(which);
//                                    }
//                                    else {
//                                        strDist = userIdList.get(which);
//                                        strDistName = userNames.get(which);
//                                    }
//                                }
//                            });
//
//                            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//
//                            builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (customer){
//                                        customerText.setText(strCustName + " (" + strCust + ")");
//                                        customerStr = strCust;
//                                        customerStrName = strCustName;
//                                    }
//                                    else {
////                                        distributorText.setText(strDistName + " (" + strDist + ")");
//                                        distributorStr = strDist;
//                                    }
//                                }
//                            });
//
//                            builderSingle.show();
//                        }
//                        else if (!showPop){
//                            if (!userIdList.isEmpty() && !userNames.isEmpty()) {
//
//                                for (int i = 0;i<userIdList.size();i++){
//                                    if (sharedPreferences.getString("distId","").equalsIgnoreCase(userIdList.get(i))){
//                                        strDist = userIdList.get(i);
//                                        strDistName = userNames.get(i);
//                                        break;
//                                    }
//                                }
//
////                                distributorText.setText(strDistName + " (" + strDist + ")");
//                                distributorStr = strDist;
//                            }
//                        }
//
//                    }
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void notifyError(VolleyError error) {
//                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
//                Toast.makeText(ProductDescription.this, WebserviceController.returnErrorMessage(error), Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
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

//    public Barcode.GeoPoint getLocationFromAddress(String strAddress){
//
//        Geocoder coder = new Geocoder(this);
//        List<Address> address;
//        Barcode.GeoPoint p1 = null;
//
//        try {
//            address = coder.getFromLocationName(strAddress,5);
//            if (address==null) {
//                return null;
//            }
//            Address location=address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new Barcode.GeoPoint((double) (location.getLatitude() * 1E6),
//                    (double) (location.getLongitude() * 1E6));
//
////            return p1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return p1;
//    }

}
//
package com.android.water_distribution.NetworkUtility;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Constants {

    static String baseUrl = "http://ec2-13-232-112-42.ap-south-1.compute.amazonaws.com:8081/ots/api/v18_1/";

    public static String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?q=bangalore&APPID=6e2fb28203a50854054df67ab72931e4#";

    public static String getLatLon = "http://testsgi.bridgeparents.com/";

    public static String LOGIN_API = baseUrl + "users/otsLoginAuthentication";

    public static String GET_PRODUCT_API = baseUrl + "product/getProductList";

    public static String Get_Product_Stock = baseUrl + "product/getProductStock";

    public static String AddUserDetails = baseUrl + "users/addUserRegistration";

    public static String getUserDetails = baseUrl + "users/getUserDetails";

    public static String addorUpdateProduct = baseUrl + "product/addOrUpdateProduct";

    public static String getNewRegistration = baseUrl + "users/getNewRegistration";

    public static String mappUser = baseUrl + "users/mappUser";

    public static String addUser = baseUrl + "users/addNewUser";

    public static String getCustomerOutstandingData = baseUrl + "users/getCustomerOutstandingData";

    public static String getUserDetailsByMapped = baseUrl + "users/getUserDetailsByMapped";

    public static String addProductStock = baseUrl + "product/addProductStock";

    public static String getProductDetailsForBill = baseUrl + "product/getProductDetailsForBill";

    public static String approveRegistration = baseUrl + "users/approveRegistration";

    public static String rejectUser = baseUrl + "users/rejectUser";

    public static String MapUserProduct = baseUrl + "users/MapUserProduct";

    public static String getProductStockList = baseUrl + "product/getProductStockList";

    public static String insertOrderAndProduct = baseUrl + "order/insertOrderAndProduct";

    public static String insertScheduler = baseUrl + "order/insertScheduler";

    public static String getOrderByStatusAndDistributor = baseUrl + "order/getOrderByStatusAndDistributor";

    public static String getCustomerOrderStatus = baseUrl + "order/getCustomerOrderStatus";

    public static String UpdateOrder = baseUrl + "order/UpdateOrder";

    public static String updateOrderAssgin = baseUrl + "order/updateOrderAssgin";

    public static String getAssginedOrder = baseUrl + "order/getAssginedOrder";

    public static String saleVoucher = baseUrl + "order/salesVocher";

    public static String closeOrder = baseUrl + "order/closeOrder";

    public static String getListOfOrderByDate = baseUrl + "order/getListOfOrderByDate";

    public static String orderReportByDate = baseUrl + "order/orderReportByDate";

    public static String getSchedulerByStatus = baseUrl + "order/getSchedulerByStatus";

    public static String getCustomerOutstandingAmt = baseUrl + "bill/getCustomerOutstandingAmt";

    public static String addOrUpdateCustomerOutstandingAmt = baseUrl + "bill/addOrUpdateCustomerOutstandingAmt";

    public static String addOrUpdateBill = baseUrl + "bill/addOrUpdateBill";

    public static String getBillReportByDate = baseUrl + "bill/getBillReportByDate";

    public static String updateEmpLatLong = baseUrl + "track/updateEmpLatLong";

    public static String getEmpLatLong = baseUrl + "track/getEmpLatLong";


    public static void SendSms(Context context, String SmsText){

        SharedPreferences sharedPreferences = context.getSharedPreferences("water_management",0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(sharedPreferences.getString("phone_number",""), null, SmsText, null,null);

        Toast.makeText(context, "Message Sent successfully!",
                Toast.LENGTH_LONG).show();

    }

}
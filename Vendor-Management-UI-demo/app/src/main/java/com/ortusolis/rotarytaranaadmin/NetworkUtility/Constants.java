package com.ortusolis.rotarytaranaadmin.NetworkUtility;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Constants {

//    static String baseUrl = "http://45.130.229.77:8094/ots/api/v18_1/";

    static String baseUrl = "https://api.etaarana.in:9443/ots/api/v18_1/";

    public static String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?q=bangalore&APPID=6e2fb28203a50854054df67ab72931e4#";

    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;

    public static String getLatLon = "http://testsgi.bridgeparents.com/";

    public static String LOGIN_API = baseUrl + "users/otsLoginAuthentication";

    public static String GET_PRODUCT_API = baseUrl + "product/getProductList";

    public static String Get_Product_Stock = baseUrl + "product/getProductStock";

    public static String AddUserDetails = baseUrl + "users/addUserRegistration";

    public static String forgotPassword = baseUrl + "users/forgotPassword";

    public static String changePassword = baseUrl + "users/changePassword";

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

    public static String updateProductStatus = baseUrl + "product/updateProductStatus";

    public static String insertOrderAndProduct = baseUrl + "order/insertOrderAndProduct";

    public static String insertScheduler = baseUrl + "order/insertScheduler";

    public static String getOrderByStatusAndDistributor = baseUrl + "order/getOrderByStatusAndDistributor";

    public static String getCustomerOrderStatus = baseUrl + "order/getCustomerOrderStatus";

    public static String UpdateOrder = baseUrl + "order/UpdateOrder";

    public static String updateOrderAssgin = baseUrl + "order/updateOrderAssgin";

    public static String updateOrderStatus = baseUrl + "order/updateOrderStatus";

    public static String getAssginedOrder = baseUrl + "order/getAssginedOrder";

    public static String saleVoucher = baseUrl + "order/salesVocher";

    public static String closeOrder = baseUrl + "order/closeOrder";

    public static String getListOfOrderByDate = baseUrl + "order/getListOfOrderByDate";

    public static String orderReportByDate = baseUrl + "order/orderReportByDate";

    public static String getSchedulerByStatus = baseUrl + "order/getSchedulerByStatus";

    public static String employeeTransferOrder = baseUrl + "order/employeeTransferOrder";

    public static String getCustomerOutstandingAmt = baseUrl + "bill/getCustomerOutstandingAmt";

    public static String addOrUpdateCustomerOutstandingAmt = baseUrl + "bill/addOrUpdateCustomerOutstandingAmt";

    public static String addOrUpdateBill = baseUrl + "bill/addOrUpdateBill";

    public static String getBillReportByDate = baseUrl + "bill/getBillReportByDate";

    public static String updateEmpLatLong = baseUrl + "track/updateEmpLatLong";

    public static String getEmpLatLong = baseUrl + "track/getEmpLatLong";
//
    public static String getupdatePassword = baseUrl + "users/updatePassword";

    public static String updateExcel = baseUrl + "product/productBulkUpload";

    public static String compressedImg = baseUrl + "product/addUpdateOrderAndProduct";

    public static String getProductCategory = baseUrl + "product/addUpdateOrderAndProduct";

    public static String addProductAndCategory = baseUrl + "product/addProductAndCategory";

    public static String getDonationListBystatus = baseUrl + "order/getDonationListBystatus";

    public static String addNewDonation = baseUrl + "order/addNewDonation";

    public static String getDonationReportByDate = baseUrl + "order/getDonationReportByDate";

    public static String getListOfOrderDetailsForRequest = baseUrl + "order/getListOfOrderDetailsForRequest";

    public static String getDonationForUpdateStatus = baseUrl + "order/getDonationForUpdateStatus";

    public static String updateDonation = baseUrl + "order/updateDonation";

    public static String getRazorPayOrder = baseUrl +"order/getRazorPayOrder";

//    public static String Transaction ="https://secure.ccavenue.com/transaction/initTrans";
//    public static String JSON ="https://secure.ccavenue.com/transaction/transaction.do";
//    public static String RSA ="https://secure.ccavenue.com/transaction/getRSAKey";

    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String JSON_URL = "https://test.ccavenue.com/transaction/transaction.do";
    public static final String TRANS_URL = "https://test.ccavenue.com/transaction/initTrans";



//

    public static void SendSms(Context context, String SmsText){

        SharedPreferences sharedPreferences = context.getSharedPreferences("water_management",0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(sharedPreferences.getString("phone_number",""), null, SmsText, null,null);

        Toast.makeText(context, "Message Sent successfully!",
                Toast.LENGTH_LONG).show();

    }

}

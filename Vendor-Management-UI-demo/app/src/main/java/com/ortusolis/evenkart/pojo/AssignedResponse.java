package com.ortusolis.evenkart.pojo;

import java.util.ArrayList;

public class AssignedResponse {

    String responseCode;
    String responseDescription;
    ResponseData responseData;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {

        ArrayList<AssignedOrderModel> orderList;

        public ArrayList<AssignedOrderModel> getOrderList() {
            return orderList;
        }

        public void setOrderList(ArrayList<AssignedOrderModel> orderList) {
            this.orderList = orderList;
        }
    }

}

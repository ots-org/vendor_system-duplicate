package com.ortusolis.rotarytarana.pojo;

import java.util.ArrayList;

public class OrderReportResponse {

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

        ArrayList<CompleteOrderDetails> completeOrderDetails;

        public ArrayList<CompleteOrderDetails> getCompleteOrderDetails() {
            return completeOrderDetails;
        }

        public void setCompleteOrderDetails(ArrayList<CompleteOrderDetails> completeOrderDetails) {
            this.completeOrderDetails = completeOrderDetails;
        }
    }

}

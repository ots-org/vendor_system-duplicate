package com.android.water_distribution.pojo;

import java.util.ArrayList;

public class BillResponse {

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

        ArrayList<BillRequest.RequestS> billNumber;

        public ArrayList<BillRequest.RequestS> getBillNumber() {
            return billNumber;
        }

        public void setBillNumber(ArrayList<BillRequest.RequestS> billNumber) {
            this.billNumber = billNumber;
        }
    }

}
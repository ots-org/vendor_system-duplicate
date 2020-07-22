package com.ortusolis.rotarytarana.pojo;

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
        //
        String pdf;

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        //


        public ArrayList<BillRequest.RequestS> getBillNumber() {

            return billNumber;

        }

        public void setBillNumber(ArrayList<BillRequest.RequestS> billNumber) {
            this.billNumber = billNumber;
        }
    }

}

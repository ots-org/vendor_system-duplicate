package com.ortusolis.subhaksha.pojo;

import java.util.ArrayList;

public class BillOutstandingResponse {

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

        public ArrayList<CustomerOutstanding> getCustomerOutstandingAmount() {
            return customerOutstandingAmount;
        }

        public void setCustomerOutstandingAmount(ArrayList<CustomerOutstanding> customerOutstandingAmount) {
            this.customerOutstandingAmount = customerOutstandingAmount;
        }

        ArrayList<CustomerOutstanding> customerOutstandingAmount;

    }

    public class CustomerOutstanding {

        String customerId;
        String customerOutstandingAmt;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerOutstandingAmt() {
            return customerOutstandingAmt;
        }

        public void setCustomerOutstandingAmt(String customerOutstandingAmt) {
            this.customerOutstandingAmt = customerOutstandingAmt;
        }
    }

}

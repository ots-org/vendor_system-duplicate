package com.ortusolis.evenkart.pojo;

import java.util.ArrayList;

public class DistributorResponse {

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

        ArrayList<UserInfo> userDetails;

        public ArrayList<UserInfo> getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(ArrayList<UserInfo> userDetails) {
            this.userDetails = userDetails;
        }
    }

}

package com.ortusolis.evenkart.pojo;

public class LoginResponse {

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

        UserInfo userDetails;

        public UserInfo getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserInfo userDetails) {
            this.userDetails = userDetails;
        }
    }

}

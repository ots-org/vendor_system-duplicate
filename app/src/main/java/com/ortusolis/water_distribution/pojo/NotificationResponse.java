package com.ortusolis.water_distribution.pojo;

import java.util.ArrayList;

public class NotificationResponse {

    String responseCode;
    String responseDescription;
    ArrayList<UserInfo> responseData;

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

    public ArrayList<UserInfo> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<UserInfo> responseData) {
        this.responseData = responseData;
    }
}

package com.ortusolis.water_distribution.pojo;

public class SchedulerResponse {

    String responseCode;
    String responseDescription;
    SchedulerModel responseData;

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

    public SchedulerModel getResponseData() {
        return responseData;
    }

    public void setResponseData(SchedulerModel responseData) {
        this.responseData = responseData;
    }
}

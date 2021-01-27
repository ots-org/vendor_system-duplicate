package com.ortusolis.rotarytaranaadmin.pojo;

public class BillReportResponse {

    String responseCode;
    String responseDescription;
    BillReportModel responseData;

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

    public BillReportModel getResponseData() {
        return responseData;
    }

    public void setResponseData(BillReportModel responseData) {
        this.responseData = responseData;
    }
}

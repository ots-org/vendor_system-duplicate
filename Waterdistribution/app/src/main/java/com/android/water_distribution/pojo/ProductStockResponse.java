package com.android.water_distribution.pojo;

import java.util.ArrayList;

public class ProductStockResponse {

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

        ArrayList<ProductDetails> productStockDetail;

        public ArrayList<ProductDetails> getProductStockDetail() {
            return productStockDetail;
        }

        public void setProductStockDetail(ArrayList<ProductDetails> productStockDetail) {
            this.productStockDetail = productStockDetail;
        }
    }

}

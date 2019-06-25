package com.android.water_distribution.pojo;

import java.util.ArrayList;

public class ProductsResponse {

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

        ArrayList<ProductDetails> productDetails;

        public ArrayList<ProductDetails> getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(ArrayList<ProductDetails> productDetails) {
            this.productDetails = productDetails;
        }
    }

}

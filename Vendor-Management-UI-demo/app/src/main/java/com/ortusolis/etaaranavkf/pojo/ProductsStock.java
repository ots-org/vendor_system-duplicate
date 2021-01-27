package com.ortusolis.etaaranavkf.pojo;

public class ProductsStock {

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

        String productId;
        String userId;
        String stockQuantity;
        String productStockStatus;
        String productStockId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStockQuantity() {
            return stockQuantity;
        }

        public void setStockQuantity(String stockQuantity) {
            this.stockQuantity = stockQuantity;
        }

        public String getProductStockStatus() {
            return productStockStatus;
        }

        public void setProductStockStatus(String productStockStatus) {
            this.productStockStatus = productStockStatus;
        }

        public String getProductStockId() {
            return productStockId;
        }

        public void setProductStockId(String productStockId) {
            this.productStockId = productStockId;
        }
    }

}

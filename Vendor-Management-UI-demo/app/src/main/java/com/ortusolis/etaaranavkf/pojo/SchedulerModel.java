package com.ortusolis.etaaranavkf.pojo;

import java.util.ArrayList;

public class SchedulerModel{

    ArrayList<SchedulerModelInternal> response;

    public ArrayList<SchedulerModelInternal> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<SchedulerModelInternal> response) {
        this.response = response;
    }

    public class SchedulerModelInternal {

        String day;
        String requestOrderId;
        String customerId;
        String productId;
        String requestedQty;
        String nxtScheduledDate;
        String reqStatus;
        String scheduledDate;
        UserInfo userDetails;
        ProductDetails productDetails;



        public String getRequestOrderId() {
            return requestOrderId;
        }

        public void setRequestOrderId(String requestOrderId) {
            this.requestOrderId = requestOrderId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getRequestedQty() {
            return requestedQty;
        }

        public void setRequestedQty(String requestedQty) {
            this.requestedQty = requestedQty;
        }

        public String getNxtScheduledDate() {
            return nxtScheduledDate;
        }

        public void setNxtScheduledDate(String nxtScheduledDate) {
            this.nxtScheduledDate = nxtScheduledDate;
        }
        public String getday() {
            return day;
        }

        public void setday(String day) {
            this.day = day;
        }



        public String getReqStatus() {
            return reqStatus;
        }

        public void setReqStatus(String reqStatus) {
            this.reqStatus = reqStatus;
        }

        public String getScheduledDate() {
            return scheduledDate;
        }

        public void setScheduledDate(String scheduledDate) {
            this.scheduledDate = scheduledDate;
        }

        public UserInfo getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserInfo userDetails) {
            this.userDetails = userDetails;
        }

        public ProductDetails getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(ProductDetails productDetails) {
            this.productDetails = productDetails;
        }

    }
}

package com.ortusolis.pravarthaka.pojo;

import java.util.ArrayList;
import java.util.List;

public class OutstandingResponse {

    String responseCode;
    String responseDescription;
    OrderRequest responseData;

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

    public OrderRequest getResponseData() {
        return responseData;
    }

    public void setResponseData(OrderRequest responseData) {
        this.responseData = responseData;
    }

    public class OrderRequest {

        List<RequestS> customerOutstandingList;
        //
        String pdf;

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        //

        public List<RequestS> getCustomerOutstandingList() {
            return customerOutstandingList;
        }

        public void setCustomerOutstandingList(List<RequestS> customerOutstandingList) {
            this.customerOutstandingList = customerOutstandingList;
        }
    }

    public static class RequestS{

    String customerId;
    String customerName;
    String outstandingAmount;
    ArrayList<ProductBalanceCan> balanceCan;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getOutstandingAmount() {
            return outstandingAmount;
        }

        public void setOutstandingAmount(String outstandingAmount) {
            this.outstandingAmount = outstandingAmount;
        }

        public ArrayList<ProductBalanceCan> getBalanceCan() {
            return balanceCan;
        }

        public void setBalanceCan(ArrayList<ProductBalanceCan> balanceCan) {
            this.balanceCan = balanceCan;
        }

        public static class ProductBalanceCan {

            String balanceCan;
            String productId;
            String productName;

            public String getBalanceCan() {
                return balanceCan;
            }

            public void setBalanceCan(String balanceCan) {
                this.balanceCan = balanceCan;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }
        }
    }

}

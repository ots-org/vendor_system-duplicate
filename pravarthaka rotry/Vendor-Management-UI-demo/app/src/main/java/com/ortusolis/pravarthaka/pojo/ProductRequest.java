package com.ortusolis.pravarthaka.pojo;

import java.util.ArrayList;

public class ProductRequest {

    RequestS request;

    public RequestS getRequest() {
        return request;
    }

    public void setRequest(RequestS request) {
        this.request = request;
    }

    public static class RequestS {

    String customerId;
    String customerName;
    String orderDate;
    String delivaryDate;
    String distributorId;
    String orderCost;
    String assignedId;
    String orderStatus;
    String orderNumber;
    String deliverdDate;
    ArrayList<ProductOrder> productList;

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

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getDelivaryDate() {
            return delivaryDate;
        }

        public void setDelivaryDate(String delivaryDate) {
            this.delivaryDate = delivaryDate;
        }

        public String getDistributorId() {
            return distributorId;
        }

        public void setDistributorId(String distributorId) {
            this.distributorId = distributorId;
        }

        public String getOrderCost() {
            return orderCost;
        }

        public void setOrderCost(String orderCost) {
            this.orderCost = orderCost;
        }

        public String getAssignedId() {
            return assignedId;
        }

        public void setAssignedId(String assignedId) {
            this.assignedId = assignedId;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getDeliverdDate() {
            return deliverdDate;
        }

        public void setDeliverdDate(String deliverdDate) {
            this.deliverdDate = deliverdDate;
        }

        public ArrayList<ProductOrder> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<ProductOrder> productList) {
            this.productList = productList;
        }

        public static class ProductOrder {

        String ots_delivered_qty;
        String orderProductId;
        String orderdId;
        String productId;
        String orderedQty;
        String productStatus;
        String productCost;

        public String getOts_delivered_qty() {
            return ots_delivered_qty;
        }

        public void setOts_delivered_qty(String ots_delivered_qty) {
            this.ots_delivered_qty = ots_delivered_qty;
        }

        public String getOrderProductId() {
            return orderProductId;
        }

        public void setOrderProductId(String orderProductId) {
            this.orderProductId = orderProductId;
        }

        public String getOrderdId() {
            return orderdId;
        }

        public void setOrderdId(String orderdId) {
            this.orderdId = orderdId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getOrderedQty() {
            return orderedQty;
        }

        public void setOrderedQty(String orderedQty) {
            this.orderedQty = orderedQty;
        }

        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

        public String getProductCost() {
            return productCost;
        }

        public void setProductCost(String productCost) {
            this.productCost = productCost;
        }
    }
}

}

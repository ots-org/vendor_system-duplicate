package com.ortusolis.etaaranavkf.pojo;

import java.util.List;

public class CompleteOrderDetails {

    String orderId;
    String distributorId;
    String customerId;
    String orderCost;
    String orderDate;
    String status;
    String orderDeliveryDate;
    String orderDeliverdDate;
    List<OrderResponse.RequestS.ProductOrder> orderProductDetails;
    UserInfo distributorDetails;
    UserInfo customerDetails;
    UserInfo employeeDetails;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public void setOrderDeliveryDate(String orderDeliveryDate) {
        this.orderDeliveryDate = orderDeliveryDate;
    }
    public String getOrderDeliverdDate() {
        return orderDeliverdDate;
    }

    public void setOrderDeliverdDate(String orderDeliverdDate) {
        this.orderDeliverdDate = orderDeliverdDate;
    }
    public List<OrderResponse.RequestS.ProductOrder> getOrderProductDetails() {
        return orderProductDetails;
    }

    public void setOrderProductDetails(List<OrderResponse.RequestS.ProductOrder> orderProductDetails) {
        this.orderProductDetails = orderProductDetails;
    }

    public UserInfo getDistributorDetails() {
        return distributorDetails;
    }

    public void setDistributorDetails(UserInfo distributorDetails) {
        this.distributorDetails = distributorDetails;
    }

    public UserInfo getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(UserInfo customerDetails) {
        this.customerDetails = customerDetails;
    }

    public UserInfo getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(UserInfo employeeDetails) {
        this.employeeDetails = employeeDetails;
    }
}

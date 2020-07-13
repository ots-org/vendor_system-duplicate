package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class OrderDetailsRequest {

	private String DistributorId;
	private String customerId;
	private String OrderNumber;
	private String AssignedId;
	private String OrderCost;
	private String OrderDate;
	private String DelivaryDate;
	private String DeliverdDate;
	private String OrderStatus;
	private String CustomerName;
	private String userLat;
	private String userLong;
	private String customerLat;
	private String address;
	private String paymentId;
	private String paymentStatus;
	private List<OrderedProductDetails> ProductList;
	
	public String getUserLat() {
		return userLat;
	}
	public void setUserLat(String userLat) {
		this.userLat = userLat;
	}
	public String getCustomerLat() {
		return customerLat;
	}
	public void setCustomerLat(String customerLat) {
		this.customerLat = customerLat;
	}
	public String getDistributorId() {
		return DistributorId;
	}
	public void setDistributorId(String distributorId) {
		DistributorId = distributorId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOrderNumber() {
		return OrderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}
	public String getAssignedId() {
		return AssignedId;
	}
	public void setAssignedId(String assignedId) {
		AssignedId = assignedId;
	}
	public String getOrderCost() {
		return OrderCost;
	}
	public void setOrderCost(String orderCost) {
		OrderCost = orderCost;
	}
	public String getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}
	public String getDelivaryDate() {
		return DelivaryDate;
	}
	public void setDelivaryDate(String delivaryDate) {
		DelivaryDate = delivaryDate;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public List<OrderedProductDetails> getProductList() {
		return ProductList;
	}
	public void setProductList(List<OrderedProductDetails> productList) {
		ProductList = productList;
	}
	public String getDeliverdDate() {
		return DeliverdDate;
	}
	public void setDeliverdDate(String deliverdDate) {
		DeliverdDate = deliverdDate;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getUserLong() {
		return userLong;
	}
	public void setUserLong(String userLong) {
		this.userLong = userLong;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	
}

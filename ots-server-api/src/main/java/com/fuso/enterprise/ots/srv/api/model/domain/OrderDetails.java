package com.fuso.enterprise.ots.srv.api.model.domain;

import javax.validation.constraints.Size;

public class OrderDetails {
	 @Size(max = 20)
	 private String orderId;
	 
	 @Size(max = 20)
	 private String distributorId;
	 
	 @Size(max = 20)
	 private String customerId;
	 
	 @Size(max = 20)
	 private String orderNumber;
	 
	 @Size(max = 20)
	 private String assignedId;
	 
	 @Size(max = 20)
	 private String orderCost;
	 
	 @Size(max = 20)
	 private String orderDate;
	 
	 @Size(max = 20)
	 private String orderDeliveryDate;
	 
	 @Size(max = 20)
	 private String orderDeliverdDate;
	 
	 @Size(max = 20)
	 private String status;
	 
	 @Size(max = 20)
	 private String billId;
	 
	 @Size(max = 20)
	 private String CreatedBy;
	 
	 @Size(max = 20)
	 private String OrderNumber;
	 
	 @Size(max = 20)
	 private String amountRecived;
	 
	 @Size(max = 20)
	 private String balanceCan;
	 
	 @Size(max = 20)
	 private String outstandingAmount;

	public String getAmountRecived() {
		return amountRecived;
	}

	public void setAmountRecived(String amountRecived) {
		this.amountRecived = amountRecived;
	}

	public String getBalanceCan() {
		return balanceCan;
	}

	public void setBalanceCan(String balanceCan) {
		this.balanceCan = balanceCan;
	}

	public String getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAssignedId() {
		return assignedId;
	}

	public void setAssignedId(String assignedId) {
		this.assignedId = assignedId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	 
	 
	 

	
}

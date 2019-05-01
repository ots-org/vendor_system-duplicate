package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class OrderDetailsAndProductDetails {

	private String OrderId;
	private String DistributorId;
	private String CustomerId;
	private String OrderNumber;
	private String AssignedId;
	private String OrderCost;
	private String OrderDate;
	private String DelivaryDate;
	private String DelivaredDate;
	private String OrderStatus;
	private String BillId;
	private String OrderProductId;
	private String OrderedQty;  
    private String DeliveredQty;
    private String OrderProductCost;
    private String OrderProductStatus;
    private String OutStandingAmount;
    private String amountRecived;
    private String orderOutStanding;
    private String balanceCan;
    private UserDetails DistributorDetails;
    private UserDetails EmployeeDetails;
    private UserDetails CustomerDetails;
    private List<OrderProductDetails> OrderdProducts;  
    private String emptyCanRecived;
    
	public String getEmptyCanRecived() {
		return emptyCanRecived;
	}
	public void setEmptyCanRecived(String emptyCanRecived) {
		this.emptyCanRecived = emptyCanRecived;
	}
	public UserDetails getDistributorDetails() {
		return DistributorDetails;
	}
	public void setDistributorDetails(UserDetails distributorDetails) {
		DistributorDetails = distributorDetails;
	}
	public UserDetails getEmployeeDetails() {
		return EmployeeDetails;
	}
	public void setEmployeeDetails(UserDetails employeeDetails) {
		EmployeeDetails = employeeDetails;
	}
	public UserDetails getCustomerDetails() {
		return CustomerDetails;
	}
	public void setCustomerDetails(UserDetails customerDetails) {
		CustomerDetails = customerDetails;
	}
	public String getOutStandingAmount() {
		return OutStandingAmount;
	}
	public void setOutStandingAmount(String outStandingAmount) {
		OutStandingAmount = outStandingAmount;
	}
	public List<OrderProductDetails> getOrderdProducts() {
		return OrderdProducts;
	}
	public void setOrderdProducts(List<OrderProductDetails> orderdProducts) {
		OrderdProducts = orderdProducts;
	}
	public String getOrderedQty() {
		return OrderedQty;
	}
	public void setOrderedQty(String orderedQty) {
		OrderedQty = orderedQty;
	}
	public String getDeliveredQty() {
		return DeliveredQty;
	}
	public void setDeliveredQty(String deliveredQty) {
		DeliveredQty = deliveredQty;
	}
	public String getOrderProductCost() {
		return OrderProductCost;
	}
	public void setOrderProductCost(String orderProductCost) {
		OrderProductCost = orderProductCost;
	}
	public String getOrderProductStatus() {
		return OrderProductStatus;
	}
	public void setOrderProductStatus(String orderProductStatus) {
		OrderProductStatus = orderProductStatus;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public String getDistributorId() {
		return DistributorId;
	}
	public void setDistributorId(String distributorId) {
		DistributorId = distributorId;
	}
	public String getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(String customerId) {
		CustomerId = customerId;
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
	public String getDelivaredDate() {
		return DelivaredDate;
	}
	public void setDelivaredDate(String delivaredDate) {
		DelivaredDate = delivaredDate;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getBillId() {
		return BillId;
	}
	public void setBillId(String billId) {
		BillId = billId;
	}
	public String getOrderProductId() {
		return OrderProductId;
	}
	public void setOrderProductId(String orderProductId) {
		OrderProductId = orderProductId;
	}
	public String getAmountRecived() {
		return amountRecived;
	}
	public void setAmountRecived(String amountRecived) {
		this.amountRecived = amountRecived;
	}
	public String getOrderOutStanding() {
		return orderOutStanding;
	}
	public void setOrderOutStanding(String orderOutStanding) {
		this.orderOutStanding = orderOutStanding;
	}
	public String getBalanceCan() {
		return balanceCan;
	}
	public void setBalanceCan(String balanceCan) {
		this.balanceCan = balanceCan;
	}
	
	
	
}

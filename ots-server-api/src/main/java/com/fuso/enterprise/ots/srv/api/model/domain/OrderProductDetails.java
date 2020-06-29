package com.fuso.enterprise.ots.srv.api.model.domain;

public class OrderProductDetails {
	private String otsOrderProductId;
	 
    private String otsOrderedQty;
  
    private String otsDeliveredQty;

    private String otsOrderProductCost;
  
    private String otsOrderProductStatus;

    private String otsOrderId;

    private String otsProductId;
    
    private String ProductName;
    
    private String BalanceCan;
    
    private String emptyCanRecived;
    
    private String status;
    
    private String Stock;

    private String type;
    
    private String productImage;
    
    private String requestedId;
    
	public String getEmptyCanRecived() {
		return emptyCanRecived;
	}

	public void setEmptyCanRecived(String emptyCanRecived) {
		this.emptyCanRecived = emptyCanRecived;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getOtsOrderProductId() {
		return otsOrderProductId;
	}

	public void setOtsOrderProductId(String otsOrderProductId) {
		this.otsOrderProductId = otsOrderProductId;
	}

	public String getOtsOrderedQty() {
		return otsOrderedQty;
	}

	public void setOtsOrderedQty(String otsOrderedQty) {
		this.otsOrderedQty = otsOrderedQty;
	}

	public String getOtsDeliveredQty() {
		return otsDeliveredQty;
	}

	public void setOtsDeliveredQty(String otsDeliveredQty) {
		this.otsDeliveredQty = otsDeliveredQty;
	}

	public String getOtsOrderProductCost() {
		return otsOrderProductCost;
	}

	public void setOtsOrderProductCost(String otsOrderProductCost) {
		this.otsOrderProductCost = otsOrderProductCost;
	}

	public String getOtsOrderProductStatus() {
		return otsOrderProductStatus;
	}

	public void setOtsOrderProductStatus(String otsOrderProductStatus) {
		this.otsOrderProductStatus = otsOrderProductStatus;
	}

	public String getOtsOrderId() {
		return otsOrderId;
	}

	public void setOtsOrderId(String otsOrderId) {
		this.otsOrderId = otsOrderId;
	}

	public String getOtsProductId() {
		return otsProductId;
	}

	public void setOtsProductId(String otsProductId) {
		this.otsProductId = otsProductId;
	}

	public String getBalanceCan() {
		return BalanceCan;
	}

	public void setBalanceCan(String balanceCan) {
		BalanceCan = balanceCan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStock() {
		return Stock;
	}

	public void setStock(String stock) {
		Stock = stock;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getRequestedId() {
		return requestedId;
	}

	public void setRequestedId(String requestedId) {
		this.requestedId = requestedId;
	}
}

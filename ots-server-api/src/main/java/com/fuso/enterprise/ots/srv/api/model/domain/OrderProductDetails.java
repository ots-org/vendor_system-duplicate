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
}

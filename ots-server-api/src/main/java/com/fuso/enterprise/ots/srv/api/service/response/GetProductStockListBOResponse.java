  package com.fuso.enterprise.ots.srv.api.service.response;

public class GetProductStockListBOResponse {
	
	long otsOpenBalance;
	
	long otsActualQuantity;
	
	long otsSoldProducts;
	
	long otsClosingBalance;

	public long getOtsOpenBalance() {
		return otsOpenBalance;
	}

	public void setOtsOpenBalance(long otsOpenBalance) {
		this.otsOpenBalance = otsOpenBalance;
	}

	public long getOtsActualQuantity() {
		return otsActualQuantity;
	}

	public void setOtsActualQuantity(long otsActualQuantity) {
		this.otsActualQuantity = otsActualQuantity;
	}

	public long getOtsSoldProducts() {
		return otsSoldProducts;
	}

	public void setOtsSoldProducts(long otsSoldProducts) {
		this.otsSoldProducts = otsSoldProducts;
	}

	public long getOtsClosingBalance() {
		return otsClosingBalance;
	}

	public void setOtsClosingBalance(long otsClosingBalance) {
		this.otsClosingBalance = otsClosingBalance;
	}


}

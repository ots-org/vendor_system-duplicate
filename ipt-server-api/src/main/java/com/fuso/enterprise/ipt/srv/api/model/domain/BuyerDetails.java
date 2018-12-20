package com.fuso.enterprise.ipt.srv.api.model.domain;

import javax.validation.constraints.Size;

public class BuyerDetails {
	
	@Size(max = 10)
    private String buyerCode;
    
    @Size(max = 50)
    private String buyerName;
    
    @Size(max = 20)
    private String buyerExtension;
    
    @Size(max = 30)
    private String buyerEmail;
    
    @Size(max = 10)
    private String buyerGroupCode;

	public String getBuyerCode() {
		return buyerCode;
	}

	public void setBuyerCode(String buyerCode) {
		this.buyerCode = buyerCode;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerExtension() {
		return buyerExtension;
	}

	public void setBuyerExtension(String buyerExtension) {
		this.buyerExtension = buyerExtension;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getBuyerGroupCode() {
		return buyerGroupCode;
	}

	public void setBuyerGroupCode(String buyerGroupCode) {
		this.buyerGroupCode = buyerGroupCode;
	}
    
}

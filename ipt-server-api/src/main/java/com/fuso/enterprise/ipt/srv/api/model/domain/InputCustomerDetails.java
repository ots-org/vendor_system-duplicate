package com.fuso.enterprise.ipt.srv.api.model.domain;

public class InputCustomerDetails {
	
private String  id;
    
    
    private String buyerCode;
    
    
    private String buyerName;
  
   
    private String  buyerEmail;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


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


	public String getBuyerEmail() {
		return buyerEmail;
	}


	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

}

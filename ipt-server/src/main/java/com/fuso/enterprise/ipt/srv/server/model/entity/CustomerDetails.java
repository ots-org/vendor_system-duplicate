package com.fuso.enterprise.ipt.srv.server.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "buyers")

public class CustomerDetails {
	
private static final long serialVersionUID = 7360672237739183568L;
	
    @Column(name = "id") 
    private String  id;
    
    @Id
    @Column(name = "buyer_code")
    private String buyerCode;
    
    @Column(name = "buyer_name")
    private String buyerName;
  
    @Column(name = "buyer_email")
    private String  buyerEmail;
    
    @Column(name = "buyer_group_code")
    private String buyerGroupCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBuyerGroupCode() {
		return buyerGroupCode;
	}

	public void setBuyerGroupCode(String buyerGroupCode) {
		this.buyerGroupCode = buyerGroupCode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    

}

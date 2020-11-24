package com.fuso.enterprise.ots.srv.api.model.domain;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductDetails {
	
	 @Size(max = 10)
	 private String productId;
	 
	 @Size(max = 20)
	 private String productName;
	 
	 @Size(max = 100)
	 private String productDescription;
	 
	 @Size(max = 10)
	 private String productStatus;
	 
	 @Size(max = 20)
	 private String productPrice;
	 
	 @Size(max = 1500)
	 private String productImage;
	 
	 @Size(max = 20)
	 private String Stock;
	 
	 private String addedStock;
	 
	 private String ProductType;
	 
	 private String productLevel;
	 
	 private String donationRequestId;
	 
	 private String gst;
	 
	 private String threshHold;
	 
	 private String productBasePrice;
	 
	 private String distributorId;
	 
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getStock() {
		return Stock;
	}

	public void setStock(String stock) {
		Stock = stock;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}

	public String getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(String productLevel) {
		this.productLevel = productLevel;
	}

	public String getAddedStock() {
		return addedStock;
	}

	public void setAddedStock(String addedStock) {
		this.addedStock = addedStock;
	}

	public String getDonationRequestId() {
		return donationRequestId;
	}

	public void setDonationRequestId(String donationRequestId) {
		this.donationRequestId = donationRequestId;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getThreshHold() {
		return threshHold;
	}

	public void setThreshHold(String threshHold) {
		this.threshHold = threshHold;
	}

	public String getProductBasePrice() {
		return productBasePrice;
	}

	public void setProductBasePrice(String productBasePrice) {
		this.productBasePrice = productBasePrice;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

}

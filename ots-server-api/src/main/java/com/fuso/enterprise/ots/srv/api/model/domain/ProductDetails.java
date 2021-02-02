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
	 
	 @Size(max = 1500)
	 private String multiProductImage1;
	 
	 @Size(max = 1500)
	 private String multiProductImage2;
	 
	 @Size(max = 1500)
	 private String multiProductImage3;
	 
	 @Size(max = 1500)
	 private String multiProductImage4;
	 
	 @Size(max = 1500)
	 private String multiProductImage5;
	 
	 @Size(max = 1500)
	 private String multiProductImage6;
	 
	 @Size(max = 1500)
	 private String multiProductImage7;
	 
	 @Size(max = 1500)
	 private String multiProductImage8;
	 
	 @Size(max = 1500)
	 private String multiProductImage9;
	 
	 @Size(max = 1500)
	 private String multiProductImage10;
	 
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

	public String getMultiProductImage1() {
		return multiProductImage1;
	}

	public void setMultiProductImage1(String multiProductImage1) {
		this.multiProductImage1 = multiProductImage1;
	}

	public String getMultiProductImage2() {
		return multiProductImage2;
	}

	public void setMultiProductImage2(String multiProductImage2) {
		this.multiProductImage2 = multiProductImage2;
	}

	public String getMultiProductImage3() {
		return multiProductImage3;
	}

	public void setMultiProductImage3(String multiProductImage3) {
		this.multiProductImage3 = multiProductImage3;
	}

	public String getMultiProductImage4() {
		return multiProductImage4;
	}

	public void setMultiProductImage4(String multiProductImage4) {
		this.multiProductImage4 = multiProductImage4;
	}

	public String getMultiProductImage5() {
		return multiProductImage5;
	}

	public void setMultiProductImage5(String multiProductImage5) {
		this.multiProductImage5 = multiProductImage5;
	}

	public String getMultiProductImage6() {
		return multiProductImage6;
	}

	public void setMultiProductImage6(String multiProductImage6) {
		this.multiProductImage6 = multiProductImage6;
	}

	public String getMultiProductImage7() {
		return multiProductImage7;
	}

	public void setMultiProductImage7(String multiProductImage7) {
		this.multiProductImage7 = multiProductImage7;
	}

	public String getMultiProductImage8() {
		return multiProductImage8;
	}

	public void setMultiProductImage8(String multiProductImage8) {
		this.multiProductImage8 = multiProductImage8;
	}

	public String getMultiProductImage9() {
		return multiProductImage9;
	}

	public void setMultiProductImage9(String multiProductImage9) {
		this.multiProductImage9 = multiProductImage9;
	}

	public String getMultiProductImage10() {
		return multiProductImage10;
	}

	public void setMultiProductImage10(String multiProductImage10) {
		this.multiProductImage10 = multiProductImage10;
	}
	
	

}

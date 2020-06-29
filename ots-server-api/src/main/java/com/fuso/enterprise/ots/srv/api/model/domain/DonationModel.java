package com.fuso.enterprise.ots.srv.api.model.domain;

public class DonationModel {

	private ProductDetails productDetails;

	private UserDetails userDetails;
	
	private String donationId;
	
	private String donarId;
	
	private String donationAmount;
	
	private String paymentId;
	
	private String donatedQty;
	
	private String donationDate;
	
	private String donationStatus;
	
	private String donationMethod;
	
	private String assignedId;
	
	public String getDonationStatus() {
		return donationStatus;
	}

	public void setDonationStatus(String donationStatus) {
		this.donationStatus = donationStatus;
	}

	public String getDonationMethod() {
		return donationMethod;
	}

	public void setDonationMethod(String donationMethod) {
		this.donationMethod = donationMethod;
	}

	public String getDonationId() {
		return donationId;
	}

	public void setDonationId(String donationId) {
		this.donationId = donationId;
	}

	public String getDonationAmount() {
		return donationAmount;
	}

	public void setDonationAmount(String donationAmount) {
		this.donationAmount = donationAmount;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getDonatedQty() {
		return donatedQty;
	}

	public void setDonatedQty(String donatedQty) {
		this.donatedQty = donatedQty;
	}

	public ProductDetails getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductDetails productDetails) {
		this.productDetails = productDetails;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public String getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(String donationDate) {
		this.donationDate = donationDate;
	}

	public String getAssignedId() {
		return assignedId;
	}

	public void setAssignedId(String assignedId) {
		this.assignedId = assignedId;
	}

	public String getDonarId() {
		return donarId;
	}

	public void setDonarId(String donarId) {
		this.donarId = donarId;
	}

	
}

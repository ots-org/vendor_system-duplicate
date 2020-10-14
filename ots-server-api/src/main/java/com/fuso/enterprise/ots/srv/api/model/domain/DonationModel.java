package com.fuso.enterprise.ots.srv.api.model.domain;

public class DonationModel  implements Comparable {

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
	
	private String paymentMethod;
	
	private String donationDescription;
	
	private String companyName;
	
	private String AtgAddress;
	
	
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

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		// return this.distance.compareTo(((UserDetails)arg0).getDistance());
		return this.donationId.compareTo(((DonationModel)arg0).getDonarId());
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDonationDescription() {
		return donationDescription;
	}

	public void setDonationDescription(String donationDescription) {
		this.donationDescription = donationDescription;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAtgAddress() {
		return AtgAddress;
	}

	public void setAtgAddress(String atgAddress) {
		AtgAddress = atgAddress;
	}
	
}

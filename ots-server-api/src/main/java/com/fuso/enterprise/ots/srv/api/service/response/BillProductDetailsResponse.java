package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetailsList;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public class BillProductDetailsResponse {

	private UserDetails customerDetails;
	
	private UserDetails distributorDetails;
	
	private List<ProductDetailsList> productDeatils;

	public List<ProductDetailsList> getProductDeatils() {
		return productDeatils;
	}

	public void setProductDeatils(List<ProductDetailsList> productDeatils) {
		this.productDeatils = productDeatils;
	}
	
	public UserDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(UserDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	public UserDetails getDistributorDetails() {
		return distributorDetails;
	}

	public void setDistributorDetails(UserDetails distributorDetails) {
		this.distributorDetails = distributorDetails;
	}
}

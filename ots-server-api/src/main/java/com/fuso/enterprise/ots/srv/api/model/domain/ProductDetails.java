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
	 
	 @Size(max = 100)
	 private String productTimestamp;
	 
	 @Size(max = 100)
	 private String productCreated;

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

	public String getProductTimestamp() {
		return productTimestamp;
	}

	public void setProductTimestamp(String productTimestamp) {
		this.productTimestamp = productTimestamp;
	}

	public String getProductCreated() {
		return productCreated;
	}

	public void setProductCreated(String productCreated) {
		this.productCreated = productCreated;
	}

}

package com.fuso.enterprise.ots.srv.api.service.response;

import java.math.BigDecimal;
import java.util.Date;

public class GetReviewAndRatingResponse {
	
	private String productId;
	
	private String orderId;
	
	private String customerName;

	private String productName;
	
	private Integer otsRatingReviewId;
   
	private String otsRatingReviewComment;

	private Integer otsRatingReviewRating;
	
	private String otsRatingReviewStatus;
	
	private String otsRatingReviewTitle;
	
	private String otsRatingReviewAddedDate;
	
	private String reviewImg;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public Integer getOtsRatingReviewId() {
	return otsRatingReviewId;
}

public void setOtsRatingReviewId(Integer otsRatingReviewId) {
	this.otsRatingReviewId = otsRatingReviewId;
}

public String getOtsRatingReviewComment() {
	return otsRatingReviewComment;
}

public void setOtsRatingReviewComment(String otsRatingReviewComment) {
	this.otsRatingReviewComment = otsRatingReviewComment;
}

public Integer getOtsRatingReviewRating() {
	return otsRatingReviewRating;
}

public void setOtsRatingReviewRating(Integer otsRatingReviewRating) {
	this.otsRatingReviewRating = otsRatingReviewRating;
}

public String getOtsRatingReviewStatus() {
	return otsRatingReviewStatus;
}

public void setOtsRatingReviewStatus(String otsRatingReviewStatus) {
	this.otsRatingReviewStatus = otsRatingReviewStatus;
}

public String getOtsRatingReviewTitle() {
	return otsRatingReviewTitle;
}

public void setOtsRatingReviewTitle(String otsRatingReviewTitle) {
	this.otsRatingReviewTitle = otsRatingReviewTitle;
}




	public String getOtsRatingReviewAddedDate() {
	return otsRatingReviewAddedDate;
}

public void setOtsRatingReviewAddedDate(String otsRatingReviewAddedDate) {
	this.otsRatingReviewAddedDate = otsRatingReviewAddedDate;
}

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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getReviewImg() {
		return reviewImg;
	}

	public void setReviewImg(String reviewImg) {
		this.reviewImg = reviewImg;
	}


	
}

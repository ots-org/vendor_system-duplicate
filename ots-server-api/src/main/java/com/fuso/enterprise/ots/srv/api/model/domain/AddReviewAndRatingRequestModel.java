package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.Date;

public class AddReviewAndRatingRequestModel {
	
	String searchKey;
	
	String searchvalue;
	
	private Integer productId;
	
	private Integer customerId;
	
	private Integer orderId;
	
	private Integer otsRatingReviewId;
	
	private String otsRatingReviewComment;

	private Integer otsRatingReviewRating;
	
	private String otsRatingReviewStatus;
	
	private String otsRatingReviewTitle;
	
	private Date otsRatingReviewAddedDate;
	
	private String reviewImage;
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public Date getOtsRatingReviewAddedDate() {
		return otsRatingReviewAddedDate;
	}

	public void setOtsRatingReviewAddedDate(Date otsRatingReviewAddedDate) {
		this.otsRatingReviewAddedDate = otsRatingReviewAddedDate;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getSearchvalue() {
		return searchvalue;
	}

	public void setSearchvalue(String searchvalue) {
		this.searchvalue = searchvalue;
	}

	public String getReviewImage() {
		return reviewImage;
	}

	public void setReviewImage(String reviewImage) {
		this.reviewImage = reviewImage;
	}

	
	
}

package com.fuso.enterprise.ots.srv.api.service.request;

import java.sql.Date;

public class UpdateOrderdProduct {

	private Integer orderProductId;
	
	private Integer sellerId;
	
	private String status;

	private Date deliverdDate;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId) {
		this.orderProductId = orderProductId;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public Date getDeliverdDate() {
		return deliverdDate;
	}

	public void setDeliverdDate(Date deliverdDate) {
		this.deliverdDate = deliverdDate;
	}

	
	
	
}

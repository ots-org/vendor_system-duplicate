package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.Date;
import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;

public class OrderProductBOResponse {
	
	private List<OrderDetailsAndProductDetails> OrderList;
	
	String pdf;

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public List<OrderDetailsAndProductDetails> getOrderList() {
		return OrderList;
	}

	public void setOrderList(List<OrderDetailsAndProductDetails> orderList) {
		OrderList = orderList;
	}

	
}

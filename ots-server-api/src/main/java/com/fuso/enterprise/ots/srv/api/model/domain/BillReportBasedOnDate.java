package com.fuso.enterprise.ots.srv.api.model.domain;

import java.sql.Date;

public class BillReportBasedOnDate {

	Date FromDate;
	Date ToDate;
	String customerId;
		
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getFromDate() {
		return FromDate;
	}
	
	public void setFromDate(Date fromDate) {
		FromDate = fromDate;
	}
	
	public Date getToDate() {
		return ToDate;
	}
	
	public void setToDate(Date toDate) {
		ToDate = toDate;
	}

}

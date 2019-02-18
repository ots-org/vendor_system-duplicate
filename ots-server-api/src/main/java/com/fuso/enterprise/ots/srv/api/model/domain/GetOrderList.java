package com.fuso.enterprise.ots.srv.api.model.domain;

import java.sql.Date;

public class GetOrderList {

	private String DistributorsId;
	private Date FromTime;
	private Date ToTime;

	
	public String getDistributorsId() {
		return DistributorsId;
	}
	public void setDistributorsId(String distributorsId) {
		DistributorsId = distributorsId;
	}
	public Date getFromTime() {
		return FromTime;
	}
	public void setFromTime(Date fromTime) {
		FromTime = fromTime;
	}
	public Date getToTime() {
		return ToTime;
	}
	public void setToTime(Date toTime) {
		ToTime = toTime;
	}

}

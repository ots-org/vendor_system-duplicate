package com.fuso.enterprise.ots.srv.api.model.domain;

import javax.validation.constraints.Size;

public class EmpLatLong {
	
	@Size(max = 20)
	private String latLongId;
	
	@Size(max = 20)
	private String latitude;
	
	@Size(max = 20)
	private String longitude;
	
	@Size(max = 20)
	private String userId;

	public String getLatLongId() {
		return latLongId;
	}

	public void setLatLongId(String latLongId) {
		this.latLongId = latLongId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}

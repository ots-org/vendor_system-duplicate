package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.EmpLatLong;

public class GetEmpLatLongBOResponse {
	
	List<EmpLatLong> empLatLong;

	public List<EmpLatLong> getEmpLatLong() {
		return empLatLong;
	}

	public void setEmpLatLong(List<EmpLatLong> empLatLong) {
		this.empLatLong = empLatLong;
	}
	
	

}

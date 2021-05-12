package com.fuso.enterprise.ots.srv.api.service.request;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;

public class AirTableRequest {

	private List<AirTableModel> airTabel;

	public List<AirTableModel> getAirTabel() {
		return airTabel;
	}

	public void setAirTabel(List<AirTableModel> airTabel) {
		this.airTabel = airTabel;
	}


}

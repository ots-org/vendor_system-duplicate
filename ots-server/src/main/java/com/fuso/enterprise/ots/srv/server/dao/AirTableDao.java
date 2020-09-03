package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;
import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;
import com.fuso.enterprise.ots.srv.api.service.request.AirTableRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;

public interface AirTableDao {
	String addAirTabelData(AirTableRequest airTableRequest);
	List<AirTableModel> airTabelCaluclation(GetProductStockListRequest airTableRequest); 
}

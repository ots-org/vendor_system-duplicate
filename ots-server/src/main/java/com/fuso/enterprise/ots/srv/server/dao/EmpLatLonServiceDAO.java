package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.EmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetEmpLatLongBOResponse;

public interface EmpLatLonServiceDAO {

	String updateEmpLatLong(EmpLatLongBORequest empLatLongBORequest);

	GetEmpLatLongBOResponse getEmpLatLong(GetEmpLatLongBORequest getEmpLatLongBORequest);

}

package com.fuso.enterprise.ots.srv.functional.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSEmpLatLonService;
import com.fuso.enterprise.ots.srv.api.service.request.EmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetEmpLatLongBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.EmpLatLonServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.StockDistObDAO;
@Service
@Transactional
public class OTSEmpLatLonServiceImpl implements OTSEmpLatLonService {
	private EmpLatLonServiceDAO empLatLonServiceDAO;
	@Inject
	public OTSEmpLatLonServiceImpl(EmpLatLonServiceDAO empLatLonServiceDAO) {
		this.empLatLonServiceDAO=empLatLonServiceDAO;
	}
	@Override
	public String updateEmpLatLong(EmpLatLongBORequest empLatLongBORequest) {
		String responseData;
		try {
			responseData = empLatLonServiceDAO.updateEmpLatLong(empLatLongBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return responseData;
	}
	@Override
	public GetEmpLatLongBOResponse getEmpLatLong(GetEmpLatLongBORequest getEmpLatLongBORequest) {
		GetEmpLatLongBOResponse getEmpLatLongBOResponse=new GetEmpLatLongBOResponse();
		try {
			getEmpLatLongBOResponse = empLatLonServiceDAO.getEmpLatLong(getEmpLatLongBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return getEmpLatLongBOResponse;
	}

}

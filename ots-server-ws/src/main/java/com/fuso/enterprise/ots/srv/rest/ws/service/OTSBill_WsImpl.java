package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSBillService;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

public class OTSBill_WsImpl implements OTSBill_Ws {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	@Inject
	private OTSBillService otsBillService;
	@Override
	public Response addOrUpdateBill(BillDetailsBORequest billDetailsBORequest) {
		Response response = null;
		BillDetailsBOResponse billDetailsBOResponse = new BillDetailsBOResponse();
		logger.info("Inside Event=1021,Class:OTSBill_WsImpl, Method:addOrUpdateBill, billDetailsBORequest:"
				+ billDetailsBORequest.getRequestData().getBillNumber());
		try {
			billDetailsBOResponse = otsBillService.addOrUpdateBill(billDetailsBORequest);
			if (billDetailsBOResponse != null) {
				logger.info("Inside Event=1021,Class:OTSBill_WsImpl,Method:addOrUpdateBill, " + "Successfull");
			}
			response = responseWrapper.buildResponse(billDetailsBOResponse,"Bill added/updated Successfully");
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_BILL_FAILURE);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_BILL_FAILURE);
		}
		return response;
	}

}

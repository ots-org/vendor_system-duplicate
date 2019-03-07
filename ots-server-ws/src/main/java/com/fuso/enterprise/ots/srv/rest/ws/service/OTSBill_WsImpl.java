package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSBillService;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
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
	
	@Override
	public Response updateCustomerOutstandingAmt(CustomerOutstandingBORequest customerOutstandingBORequest) {
		String responseData;
		Response response;
		logger.info("Inside Event=1022,Class:OTSBill_WsImpl, Method:updateCustomerOutstandingAmt, customerOutstandingBORequest:"
				+ customerOutstandingBORequest.getRequestData().getCustomerId());
		if(!(customerOutstandingBORequest.getRequestData().getCustomerId().isEmpty()) && customerOutstandingBORequest.getRequestData().getCustomerId()!=null) {
		try {
			  responseData =otsBillService.updateCustomerOutstandingAmt(customerOutstandingBORequest);
				if (responseData != null) {
					logger.info("Inside Event=1022,Class:OTSBill_WsImpl,Method:updateCustomerOutstandingAmt" + "Successfull");
				}
		response = buildResponse(200,responseData);
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.UPDATE_CUSTOMER_OUTSTANDINGAMT_FAILURE);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.UPDATE_CUSTOMER_OUTSTANDINGAMT_FAILURE);
		}
		}else{
			response = buildResponse(200,"Customer Id  can't be null");
		}
		return response;
	}
 
	@Override
	public Response getCustomerOutstandingAmt(GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest) {
		Response response = null;
		logger.info("Inside Event=1022,Class:OTSBill_WsImpl, Method:getCustomerOutstandingAmt, UserDataBORequest:"
				+  getCustomerOutstandingAmtBORequest.getRequestData().getCustomerId());
		GetCustomerOutstandingAmtBOResponse customerOutstandingAmtResponse = new GetCustomerOutstandingAmtBOResponse();
		if(!(getCustomerOutstandingAmtBORequest.getRequestData().getCustomerId().isEmpty()) && getCustomerOutstandingAmtBORequest.getRequestData().getCustomerId()!=null) {
			try {
				    customerOutstandingAmtResponse =otsBillService.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest);
					if (customerOutstandingAmtResponse != null) {
					logger.info("Inside Event=1022,Class:OTSBill_WsImpl,Method:getCustomerOutstandingAmt" + "Successfull");
						System.out.println(customerOutstandingAmtResponse.getCustomerOutstandingAmount().size());
					}
					
					if(customerOutstandingAmtResponse.getCustomerOutstandingAmount().size()==0) {
						response = buildResponse(200,"No Outstanding Balance found for given Customer Id");
					}
					else {
					response = responseWrapper.buildResponse(customerOutstandingAmtResponse,"Successfull");
					}
			} catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.GET_CUSTOMER_OUTSTANDINGAMT_FAILURE_WRONG_INPUT);
			} catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.GET_CUSTOMER_OUTSTANDINGAMT_FAILURE);
			}
		}else{
			response = buildResponse(200,"Customer Id  can't be null");
		}
			return response;
		}
	
	    public Response buildResponse(int code,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(code,description);
		return Response.ok(wrapper).build();
	    }
}

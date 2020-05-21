package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSEmpLatLonService;
import com.fuso.enterprise.ots.srv.api.service.request.EmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetEmpLatLongBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

public class OTSEmpLatLon_WsImpl implements OTSEmpLatLon_Ws {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	@Inject
	private OTSEmpLatLonService empLatLonService;
	@Override
	public Response updateEmpLatLong(EmpLatLongBORequest empLatLongBORequest) {
		String responseData;
		Response response = null;
		logger.info("Inside Event=1022,Class:OTSEmpLatLon_WsImpl, Method:addEmpLatLong, UserId:" + empLatLongBORequest.getRequestData().getUserId());
		if(!(empLatLongBORequest.getRequestData().getLatitude().equalsIgnoreCase("")) && 
				!(empLatLongBORequest.getRequestData().getLongitude().equalsIgnoreCase("")) || ((empLatLongBORequest.getRequestData().getLatitude()!=null) 
				&& (empLatLongBORequest.getRequestData().getLongitude()!=null))) {
			try{
				System.out.print("calling"+empLatLongBORequest.getRequestData().getLongitude()+empLatLongBORequest.getRequestData().getLatitude()+empLatLongBORequest.getRequestData().getUserId());
				responseData = empLatLonService.updateEmpLatLong(empLatLongBORequest);
				if (responseData != null) {
					logger.info("Inside Event=1022,Class:OTSEmpLatLon_WsImpl,Method:addEmpLatLong, " + "successful");
				}
				response = buildResponse(200,responseData);
			}catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.ADD_EMP_LATLONG_FAILURE);
			}catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.ADD_EMP_LATLONG_FAILURE);
			}
		}else{
			response = buildResponse(200,"Latitude or Longitude can't be null");
		}
		return response;
	}
	
	@Override
	public GetEmpLatLongBOResponse getEmpLatLong(GetEmpLatLongBORequest getEmpLatLongBORequest) {
		Response response = null;
		GetEmpLatLongBOResponse getEmpLatLongBOResponse=new GetEmpLatLongBOResponse();
		try {
			getEmpLatLongBOResponse = empLatLonService.getEmpLatLong(getEmpLatLongBORequest);
				if (getEmpLatLongBOResponse != null) {
					logger.info("Inside Event=1022,Class:OTSEmpLatLon_WsImpl,Method:addEmpLatLong, " + "successful");
				}
				response = responseWrapper.buildResponse(getEmpLatLongBOResponse);
			} catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.GET_EMP_LATLONG_FAILURE);
			} catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.GET_EMP_LATLONG_FAILURE);
			}
		return getEmpLatLongBOResponse;
	}

	public Response buildResponse(int code,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(code,description);
		return Response.ok(wrapper).build();
	}

}

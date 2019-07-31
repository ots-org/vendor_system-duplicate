package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

public class OTSProduct_WsImpl implements OTSProduct_Ws {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	@Inject
	private OTSProductService otsProductService;

	@Override
	public Response getProductList(ProductDetailsBORequest productDetailsBORequest) {
		if((!productDetailsBORequest.getRequestData().getSearchKey().isEmpty()) || (!productDetailsBORequest.getRequestData().getSearchvalue().isEmpty())) {
			Response response = null;
			logger.info("Inside Event=1012,Class:OTSProduct_WsImpl, Method:getProductList, Search Key:"
					+ productDetailsBORequest.getRequestData().getSearchKey()+"Search Val :"+productDetailsBORequest.getRequestData().getSearchvalue());
			ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
			try {
				System.out.print("1");
				productDetailsBOResponse = otsProductService.getProductList(productDetailsBORequest);
				if (productDetailsBOResponse != null) {
					logger.info("Inside Event=1012,Class:OTSProduct_WsImpl,Method:getProductList, " + "ProductList Size:"
							+ productDetailsBOResponse.getProductDetails().size());
				}
				if(productDetailsBOResponse.getProductDetails().size() == 0) {
					response = responseWrapper.buildResponse(productDetailsBOResponse,"Record is not present in DataBase");
				}else{
					response = responseWrapper.buildResponse(productDetailsBOResponse,"Successfull");
				}
			} catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.GET_PRODUCT_LIST_FAILURE);
		    } catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.GET_PRODUCT_LIST_FAILURE);
		    }
		      return response;
		}else{
			Response response = buildResponse(600,"SearchKey and SearchValue can't be Empty");
			return response;
		}
	}

	@Override
	public Response addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
		Response response = null;
		String responseData;
		logger.info("Inside Event=1011,Class:OTSProduct_WsImpl, Method:addOrUpdateProduct, addorUpdateProductBORequest:"
				+ addorUpdateProductBORequest.getRequestData().getProductName());
		try {
			  responseData = otsProductService.addOrUpdateProduct(addorUpdateProductBORequest);
				if (responseData != null) {
					logger.info("Inside Event=1011,Class:OTSProduct_WsImpl,Method:addOrUpdateProduct, " + "Successfull");
				}
				response = buildResponse(200,responseData);
			} catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_PRODUCT_FAILURE);
			} catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_PRODUCT_FAILURE);
			}
		return response;
	}
	
	private Response buildResponse(int code, String description) {
		ResponseWrapper wrapper = new ResponseWrapper(code,description);
		return Response.ok(wrapper).build();
	}

	@Override
	public Response addProductStock(AddProductStockBORequest addStockProductBORequest) {
		Response response = null;
		if(!addStockProductBORequest.getRequestData().getProductId().isEmpty() || !addStockProductBORequest.getRequestData().getProductStockQty().isEmpty() 
			||	!addStockProductBORequest.getRequestData().getProductStockStatus().isEmpty() || !addStockProductBORequest.getRequestData().getProductStockStatus().isEmpty()){
			logger.info("Inside Event=1014,Class:OTSProduct_WsImpl, Method:addProductStock, UserDataBORequest:"
				+ addStockProductBORequest);
			try {
				String StrResponse = otsProductService.addOrUpdateProductStock(addStockProductBORequest);
				if (StrResponse != null) {
					logger.info("Inside Event=1014,Class:OTSProduct_WsImpl,Method:addProductStock, " + "Response"
							+ StrResponse);
				}
				response = buildResponse(200,"SUCCESS");
			} catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.UpdationFailuer);
		    } catch (Throwable e) {
		    	throw new BusinessException(e, ErrorEnumeration.UpdationFailuer);
		    }
		}else{
			response = buildResponse(600,"Please check the data provided");
		}
		return response;
	}
	
	@Override
	public Response getProductStockList(GetProductStockListRequest getProductStockListRequest) {
		Response response = null;
		GetProductStockListBOResponse GetProductStockListBOResponse = new GetProductStockListBOResponse();
		if(!getProductStockListRequest.getRequestData().getUserId().isEmpty()) {
			try {
				logger.info("Inside Event=1015,Class:OTSProduct_WsImpl, Method:getProductStockList, getProductStockListRequest userId:"
						+ getProductStockListRequest.getRequestData().getUserId()+ "getProductStockListRequest date"+getProductStockListRequest.getRequestData().getTodaysDate());
				GetProductStockListBOResponse=otsProductService.getProductStockList(getProductStockListRequest);
				if(!GetProductStockListBOResponse.getProductStockDetail().isEmpty()) {
					response = responseWrapper.buildResponse(GetProductStockListBOResponse,"Successfull");
				}else {
				    response = responseWrapper.buildResponse("We do not have DATA for this Request,please check the input");
				}
			}catch(BusinessException e) {
				throw new BusinessException(e.getMessage(), e);
			}catch(Throwable e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}
		return response;
	}

	@Override
	public Response getProductStock(GetProductStockRequest getProductStockRequest) {
		Response response = null;
		GetProductBOStockResponse getProductBOStockResponse = new GetProductBOStockResponse();
			try {
				getProductBOStockResponse = otsProductService.getProductStockByUidAndPid(getProductStockRequest);
				if(!(getProductBOStockResponse.getProductStockId() == null)) {
					response = responseWrapper.buildResponse(getProductBOStockResponse,"Successfull");
				}else {
					response = responseWrapper.buildResponse(400,"stock npt found");
				}				
			}catch(BusinessException e) {
				throw new BusinessException(e.getMessage(), e);
			}catch(Throwable e) {
				throw new BusinessException(e.getMessage(), e);
			}
		return response;
	}

	@Override
	public Response getProductDetailsForBill(GetProductDetailsForBillRequst getProductDetailsForBillRequst) {
		try {
			Response response = null;
			response =  responseWrapper.buildResponse(otsProductService.getProductDetailsForBill(getProductDetailsForBillRequst),"Successfull");
			return response;
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	public Response UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel) {
		try {
			Response response = null;
			response =  responseWrapper.buildResponse(otsProductService.UpdateProductStatus(updateProductStatusRequestModel),"Successfull");
			return response;
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
}

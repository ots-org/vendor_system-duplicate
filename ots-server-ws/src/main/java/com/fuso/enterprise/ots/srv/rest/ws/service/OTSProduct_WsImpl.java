package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;

import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AirTableRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.NotifyProductForCustomerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductBulkUploadRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;
import org.springframework.beans.factory.annotation.Value;

public class OTSProduct_WsImpl implements OTSProduct_Ws {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    ResponseWrapper responseWrapper;
    @Inject
    private OTSProductService otsProductService;
    
    @Value("${product.percentage.price}")
	public String productPercentage;

    @Override
    public Response getProductList(ProductDetailsBORequest productDetailsBORequest) {
        if ((!productDetailsBORequest.getRequestData().getSearchKey().isEmpty()) ||
            (!productDetailsBORequest.getRequestData().getSearchvalue().isEmpty())) {
            Response response = null;
            logger.info("Inside Event=1012,Class:OTSProduct_WsImpl, Method:getProductList, Search Key:" +
                productDetailsBORequest.getRequestData().getSearchKey() + "Search Val :" +
                productDetailsBORequest.getRequestData().getSearchvalue());
            ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
            try {
                productDetailsBOResponse = otsProductService.getProductList(productDetailsBORequest);
                if(!( productDetailsBOResponse.getProductDetails().get(0).getProductStatus().equalsIgnoreCase("pending")||productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("category")||productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("subcategory"))) {
                	for(int i=0; i< productDetailsBOResponse.getProductDetails().size() ;i++) { 
                		Float finalPrice =	Float.parseFloat(productDetailsBOResponse.getProductDetails().get(i).getProductPrice() ) +( Float.parseFloat(productDetailsBOResponse.getProductDetails().get(i).getProductPrice()) * Float.parseFloat(productPercentage))/100;
                        if(productDetailsBORequest.getRequestData().getSearchKey().contains("range")||productDetailsBORequest.getRequestData().getSearchKey().contains("Range")) {
                        	productDetailsBOResponse.getProductDetails().get(i).setProductPrice(finalPrice.toString());
                            Float finalPrice1 =	Float.parseFloat(productDetailsBOResponse.getProductDetails().get(i).getProductBasePrice() ) +( Float.parseFloat(productDetailsBOResponse.getProductDetails().get(i).getProductBasePrice()) * Float.parseFloat(productPercentage))/100;
                            productDetailsBOResponse.getProductDetails().get(i).setProductBasePrice(finalPrice1.toString());
                        }else {
                        	productDetailsBOResponse.getProductDetails().get(i).setProductPrice(finalPrice.toString());
                            Float finalPrice1 =	Float.parseFloat(productDetailsBOResponse.getProductDetails().get(i).getProductBasePrice() ) +( Float.parseFloat(productDetailsBOResponse.getProductDetails().get(i).getProductBasePrice()) * Float.parseFloat(productPercentage))/100;
                            productDetailsBOResponse.getProductDetails().get(i).setProductBasePrice(finalPrice1.toString());
                        }
                		
                	}
                }
                
                if (productDetailsBOResponse != null) {
                    logger.info("Inside Event=1012,Class:OTSProduct_WsImpl,Method:getProductList, " +
                        "ProductList Size:" + productDetailsBOResponse.getProductDetails().size());
                }
                if (productDetailsBOResponse.getProductDetails().size() == 0) {
                    response = responseWrapper.buildResponse(productDetailsBOResponse,
                        "Record is not present in DataBase");
                } else {
                    response = responseWrapper.buildResponse(productDetailsBOResponse, "successful");
                }
            } catch (BusinessException e) {
            	System.out.println(e);
                throw new BusinessException(e, ErrorEnumeration.GET_PRODUCT_LIST_FAILURE);
            } catch (Throwable e) {
            	System.out.println(e);
                throw new BusinessException(e, ErrorEnumeration.GET_PRODUCT_LIST_FAILURE);
            }
            return response;
        } else {
            Response response = buildResponse(600, "SearchKey and SearchValue can't be Empty");
            return response;
        }
    }

    @Override
    public Response addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
        Response response = null;
        String responseData;
        logger.info("Inside Event=1011,Class:OTSProduct_WsImpl, Method:addOrUpdateProduct, addorUpdateProductBORequest:" +
            addorUpdateProductBORequest.getRequestData().getProductName());
        try {
            responseData = otsProductService.addOrUpdateProduct(addorUpdateProductBORequest);
            if (responseData != null) {
                logger.info("Inside Event=1011,Class:OTSProduct_WsImpl,Method:addOrUpdateProduct, " + "successful");
            }
            response = buildResponse(200, responseData);
        } catch (BusinessException e) {
            throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_PRODUCT_FAILURE);
        } catch (Throwable e) {
            throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_PRODUCT_FAILURE);
        }
        return response;
    }

    private Response buildResponse(int code, String description) {
        ResponseWrapper wrapper = new ResponseWrapper(code, description);
        return Response.ok(wrapper).build();
    }

    @Override
    public Response addProductStock(AddProductStockBORequest addStockProductBORequest) {
        Response response = null;
        if (!addStockProductBORequest.getRequestData().getProductId().isEmpty() ||
            !addStockProductBORequest.getRequestData().getProductStockQty().isEmpty() ||
            !addStockProductBORequest.getRequestData().getProductStockStatus().isEmpty() ||
            !addStockProductBORequest.getRequestData().getProductStockStatus().isEmpty()) {
            logger.info("Inside Event=1014,Class:OTSProduct_WsImpl, Method:addProductStock, UserDataBORequest:" +
                addStockProductBORequest);
            try {
                String StrResponse = otsProductService.addOrUpdateProductStock(addStockProductBORequest);
                if (StrResponse != null) {
                    logger.info("Inside Event=1014,Class:OTSProduct_WsImpl,Method:addProductStock, " + "Response" +
                        StrResponse);
                }
                response = buildResponse(200, "SUCCESS");
            } catch (BusinessException e) {
                throw new BusinessException(e, ErrorEnumeration.UpdationFailuer);
            } catch (Throwable e) {
                throw new BusinessException(e, ErrorEnumeration.UpdationFailuer);
            }
        } else {
            response = buildResponse(600, "Please check the data provided");
        }
        return response;
    }

    @Override
    public Response getProductStockList(GetProductStockListRequest getProductStockListRequest) {
        Response response = null;
        GetProductStockListBOResponse GetProductStockListBOResponse = new GetProductStockListBOResponse();
        if (!getProductStockListRequest.getRequestData().getUserId().isEmpty()) {
            try {
                logger.info(
                    "Inside Event=1015,Class:OTSProduct_WsImpl, Method:getProductStockList, getProductStockListRequest userId:" +
                    getProductStockListRequest.getRequestData().getUserId() +
                    "getProductStockListRequest date" +
                    getProductStockListRequest.getRequestData().getTodaysDate());
                GetProductStockListBOResponse = otsProductService.getProductStockList(getProductStockListRequest);
                if (!GetProductStockListBOResponse.getProductStockDetail().isEmpty()) {

                    response = responseWrapper.buildResponse(GetProductStockListBOResponse, "successful");
                } else {
                    response = responseWrapper
                        .buildResponse("We do not have DATA for this Request,please check the input");
                }
            } catch (BusinessException e) {
                throw new BusinessException(e.getMessage(), e);
            } catch (Throwable e) {
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
            if (!(getProductBOStockResponse.getProductStockId() == null)) {
                response = responseWrapper.buildResponse(getProductBOStockResponse, "Successful");
            } else {
                response = responseWrapper.buildResponse(400, "stock not found");
            }
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new BusinessException(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public Response getProductDetailsForBill(GetProductDetailsForBillRequst getProductDetailsForBillRequst) {
        try {
            Response response = null;
            response = responseWrapper.buildResponse(
                otsProductService.getProductDetailsForBill(getProductDetailsForBillRequst), "Successful");
            return response;
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public Response UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel) {
        try {
            Response response = null;
            response = responseWrapper.buildResponse(
                otsProductService.UpdateProductStatus(updateProductStatusRequestModel), "Successful");
            return response;
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public Response productBulkUpload(ProductBulkUploadRequest partsBase64Excel) {
        try {
            Response response = null;
            response = responseWrapper.buildResponse(otsProductService.productBulkUpload(partsBase64Excel), "successful");
            return response;
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage(), e);
        } catch (Throwable e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

	@Override
	public Response addProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest) {
		Response response = null;
		try {
	            response = responseWrapper.buildResponse(otsProductService.addProductAndCategory(addProductAndCategoryRequest), "successful");
	        } catch (BusinessException e) {
	            throw new BusinessException(e.getMessage(), e);
	        } catch (Throwable e) {
	            throw new BusinessException(e.getMessage(), e);
	        }// TODO Auto-generated method stub
		return response;
	}

	@Override
	public Response searchProduct(ProductDetailsBORequest ProductDetailsBORequest) {
		
		return null;
	}

	@Override
	public Response addAirTabelData(AirTableRequest airTableRequest) {
		Response response = null;
		try {
	            response = responseWrapper.buildResponse(otsProductService.addAirTabelData(airTableRequest), "successful");
	        } catch (BusinessException e) {
	            throw new BusinessException(e.getMessage(), e);
	        } catch (Throwable e) {
	            throw new BusinessException(e.getMessage(), e);
	        }// TODO Auto-generated method stub
		return response;
	}

	@Override
	public Response airTabelCaluclation(GetProductStockListRequest airTableRequest) {
		Response response = null;
		try {
			response = responseWrapper.buildResponse(otsProductService.airTabelCaluclation(airTableRequest), "successful");
		}catch (Throwable e) {
            throw new BusinessException(e.getMessage(), e);
        }
		
		return response;
	}
	 /***************shreekant rathod************/
	@Override
	public Response getAllProductDetails() {
		  try {
	            Response response = null;
	            response = responseWrapper.buildResponse(otsProductService.getAllProductDetails(), "Successful");
	            return response;
	        } catch (BusinessException e) {
	            throw new BusinessException(e.getMessage(), e);
	        } catch (Throwable e) {
	            throw new BusinessException(e.getMessage(), e);
	        }
	}

	@Override
	public Response getProductDetails(ProductDetailsBORequest productDetailsBORequest) {
		try {
			Response response = null;
	        response = responseWrapper.buildResponse(otsProductService.getProductDetails(productDetailsBORequest), "Successful");
	        return response;
	    } catch (BusinessException e) {
	        throw new BusinessException(e.getMessage(), e);
	    } catch (Throwable e) {
	        throw new BusinessException(e.getMessage(), e);
	    }
		
	}

	@Override
	public Response notifyProductForCustomer(NotifyProductForCustomerRequest notifyProductForCustomerRequest) {
		try {
			System.out.println("data");
			Response response = null;
			if(otsProductService.notifyProductForCustomer(notifyProductForCustomerRequest)==null) {
				response = responseWrapper.buildResponse("already existes", "Successful");
			}else {
				response = responseWrapper.buildResponse("Successful", "Successful");
			}
	        return response;
	    } catch (BusinessException e) {
	        throw new BusinessException(e.getMessage(), e);
	    } catch (Throwable e) {
	        throw new BusinessException(e.getMessage(), e);
	    }
	}

}
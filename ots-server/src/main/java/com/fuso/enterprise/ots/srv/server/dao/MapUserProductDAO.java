package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerProduct;

public interface MapUserProductDAO {

	String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest);

	String UpdateBySaleVocher(CustomerProductDataBORequest customerProductDataBORequest);

	CustomerProductDetails getCustomerProductDetailsByUserIdandProductId(String productId, String customerId);

}

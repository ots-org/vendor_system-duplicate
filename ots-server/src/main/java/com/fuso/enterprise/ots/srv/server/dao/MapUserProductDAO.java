package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;

public interface MapUserProductDAO {

	String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest);

	String UpdateBySaleVocher(CustomerProductDataBORequest customerProductDataBORequest);

}

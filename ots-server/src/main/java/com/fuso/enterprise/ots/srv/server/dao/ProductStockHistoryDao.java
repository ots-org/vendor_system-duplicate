package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;

public interface ProductStockHistoryDao  {

	void addProductStockHistory(AddProductStockBORequest addProductStockBORequest);
	
	long getOtsProductStockAddition(Integer otsProductId, GetProductStockListRequest getProductStockListRequest);
}

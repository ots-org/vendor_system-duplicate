package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStock;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb;

public interface StockDistObDAO {
	
	List<OtsStockDistOb> fetchOpeningBalance(List<OtsProductStock> productList,
			GetProductStockListRequest getProductStockListRequest);

	List<OtsStockDistOb> fetchOpeningBalance( GetProductStockListRequest getProductStockListRequest);

}

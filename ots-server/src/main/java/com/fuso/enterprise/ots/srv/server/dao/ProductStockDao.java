package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStock;

public interface ProductStockDao  {
	String addProductStock(AddProductStockBORequest addProductStockBORequest);
}
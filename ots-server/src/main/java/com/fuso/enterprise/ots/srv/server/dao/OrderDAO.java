package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;

public interface OrderDAO {

	List<OtsOrder> getOrderList(Integer otsProductId, GetProductStockListRequest getProductStockListRequest);
}

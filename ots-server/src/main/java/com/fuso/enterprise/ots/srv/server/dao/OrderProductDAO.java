package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderedProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;

public interface OrderProductDAO {

	long getListOfDeliverdQuantityOfDay(List<OtsOrder> orderList, Integer otsProductId);
	List<OrderProductDetails> getUserByStatuesAndDistributorId(OrderDetails orderDetails);
	String insertOrdrerProductByOrderId(Integer orderId,OrderedProductDetails orderedProductDetails);
	String addOrUpdateOrderProduct(OrderedProductDetails orderedProductDetails);
	
}

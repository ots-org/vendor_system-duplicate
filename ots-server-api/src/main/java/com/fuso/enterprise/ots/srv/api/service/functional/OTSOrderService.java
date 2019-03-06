package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateOrderDetailsModelRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;


public interface OTSOrderService {
	OrderDetailsBOResponse getOrderBydate(GetOrderBORequest getOrderBORequest);
	OrderProductBOResponse getOrderByStatusAndDistributor(GetOrderByStatusRequest getOrderByStatusRequest);
	String insertOrderAndProduct(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest);
	String addOrUpdateOrderProduct(AddOrUpdateOnlyOrderProductRequest AddOrUpdateOnlyOrderProductRequest); 
	String UpdateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest);
	String updateAssginedOrder(UpdateForAssgineBOrequest  updateForAssgineBOrequest);
}

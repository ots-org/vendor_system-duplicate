package com.fuso.enterprise.ots.srv.api.service.functional;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.SaleVocherBoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetListOfOrderByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;

public interface OTSOrderService {
	OrderDetailsBOResponse getOrderBydate(GetOrderBORequest getOrderBORequest);
	OrderProductBOResponse getOrderByStatusAndDistributor(GetOrderByStatusRequest getOrderByStatusRequest);
	String insertOrderAndProduct(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest);
	String addOrUpdateOrderProduct(AddOrUpdateOnlyOrderProductRequest AddOrUpdateOnlyOrderProductRequest); 
	String UpdateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest);
	String updateAssginedOrder(UpdateForAssgineBOrequest  updateForAssgineBOrequest);
	OrderProductBOResponse getAssginedOrder(GetAssginedOrderBORequest getAssginedOrderBORequest);
	String closeOrder(CloseOrderBORequest closeOrderBORequest);
	OrderProductBOResponse getCustomerOrderStatus(GetCustomerOrderByStatusBOrequest getCustomerOrderByStatusBOrequest);
	OrderProductBOResponse getOrderDetailsByDate(GetOrderBORequest getOrderBORequest);
	GetListOfOrderByDateBOResponse getListOfOrderByDate(GetListOfOrderByDateBORequest getListOfOrderByDateBORequest);
	String SalesVocher(SaleVocherBoRequest saleVocherBoRequest);
	OrderProductBOResponse orderReportByDate(GetOrderBORequest getOrderBORequest);
}

package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetBillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
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
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;

public interface OrderServiceDAO {

	void updateOrderwithBillID(OtsBill otsBill, List<ListOfOrderId> listOfOrderId);
	List<OrderDetails> getOrderBydate(GetOrderBORequest getOrderBORequest);
	List<OrderDetails> getOrderIdByDistributorId(GetOrderByStatusRequest getOrderByStatusRequest);
	Integer insertOrderAndGetOrderId(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest);
	String UpdateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest);
	String updateAssginedOrder(UpdateForAssgineBOrequest updateForAssgineBOrequest);
	List<OrderDetails> getAssginedOrder(GetAssginedOrderBORequest getAssginedOrderBORequest);
	OrderDetails closeOrder(CloseOrderBORequest closeOrderBORequest);
	List<OrderDetails> getListOrderForBill(GetBillDetails BillDetails);
	List<OrderDetails> getCustomerOrderStatus(GetCustomerOrderByStatusBOrequest getCustomerOrderByStatusBOrequest);
	List<CompleteOrderDetails> getListOfOrderByDate(GetListOfOrderByDateBORequest getListOfOrderByDateBORequest);
	OrderDetails SalesVocher(SaleVocherBoRequest saleVocherBoRequest);
}

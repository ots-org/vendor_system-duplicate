package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.RequestProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.GetUserDetailsForRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetUserDetailsForResponse;
import com.fuso.enterprise.ots.srv.server.dao.OrderRequestMappingDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrderrequestMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestProduct;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class OrderrequestMappingDaoImpl extends AbstractIptDao<OtsOrderrequestMapping, String> implements OrderRequestMappingDAO{

	private GetUserDetailsForResponse userDetailsForResponse;

	public OrderrequestMappingDaoImpl() {
		super(OtsOrderrequestMapping.class);
	}

	@Override
	public OrderDetailsAndProductDetails MapOrderAndRequest(
			OrderDetailsAndProductDetails orderDetailsAndProductDetails) {
		OtsOrderrequestMapping orderrequestMapping = new OtsOrderrequestMapping();
	
		OtsOrder orderId = new OtsOrder();
		orderId.setOtsOrderId(Integer.parseInt(orderDetailsAndProductDetails.getOrderId()));
		orderrequestMapping.setOtsOrderId(orderId);
		
		for(int i =0 ; i< orderDetailsAndProductDetails.getOrderdProducts().size();i++) {
			OtsRequestProduct requestProduct = new OtsRequestProduct();
			requestProduct.setOtsRequestProductId(Integer.parseInt(orderDetailsAndProductDetails.getOrderdProducts().get(i).getRequestedId()));
			orderrequestMapping.setOtsRequestProductId(requestProduct);
			super.getEntityManager().merge(orderrequestMapping);
		}
		
		return orderDetailsAndProductDetails;
	}

	@Override
	public GetUserDetailsForResponse getListOfOrderDetailsForRequest(
			GetUserDetailsForRequest getUserDetailsForRequest) {
		List<RequestProductDetails> requestUserList = new ArrayList<RequestProductDetails>();
		List<RequestProductDetails> oprequestUserList = new ArrayList<RequestProductDetails>();
		GetUserDetailsForResponse userDetailsForResponse = new GetUserDetailsForResponse();
		Map<String, Object> queryParameter = new HashMap<>();
		OtsRequestProduct requestProductId = new OtsRequestProduct();
		requestProductId.setOtsRequestProductId(Integer.parseInt(getUserDetailsForRequest.getRequestId().getRequestId()));
		queryParameter.put("OtsRequestProductId",requestProductId);
		List<OtsOrderrequestMapping> requestOrderMapingList = new ArrayList<OtsOrderrequestMapping>();
		requestOrderMapingList = super.getResultListByNamedQuery("OtsOrderrequestMapping.findByRequestProduct", queryParameter);
		
		requestUserList = requestOrderMapingList.stream().map(OtsOrderrequestMapping -> convertRequestOrderEntityTOModel(OtsOrderrequestMapping)).collect(Collectors.toList());
		for(int i=0 ; i<requestUserList.size();i++) {
			if(requestUserList.get(i)!=null) {
				oprequestUserList.add(requestUserList.get(i));
			}	
		}
		userDetailsForResponse.setRequestProductDetails(oprequestUserList);
		return userDetailsForResponse;
	}

	private RequestProductDetails convertRequestOrderEntityTOModel(OtsOrderrequestMapping orderrequestMapping) {
		RequestProductDetails requestProductDetails = new RequestProductDetails();
		if(orderrequestMapping.getOtsOrderId().getOtsOrderStatus().equalsIgnoreCase("newRequest")) {
			OrderDetailsAndProductDetails orderDetails = new OrderDetailsAndProductDetails();
			orderDetails.setOrderId(orderrequestMapping.getOtsOrderId().getOtsOrderId().toString());
			orderDetails.setAddress(orderrequestMapping.getOtsOrderId().getOtsOrderAddress());
			
			UserDetails userDetails = new UserDetails();
			userDetails.setUserId(orderrequestMapping.getOtsOrderId().getOtsCustomerId().getOtsUsersId().toString());
			userDetails.setFirstName(orderrequestMapping.getOtsOrderId().getOtsCustomerId().getOtsUsersFirstname());
			
			requestProductDetails.setOrderDetails(orderDetails);
			requestProductDetails.setUserDetails(userDetails);
		}else {
			requestProductDetails = null;
		}
		
		return requestProductDetails;
		
	}
	
}
package com.fuso.enterprise.ipt.srv.rest.ws.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.BuyersGroupsDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputAddBuyerstoGroup;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForCancelPartOrder;
import com.fuso.enterprise.ipt.srv.api.service.functional.IPTBuyersService;

@Validated
@Service
public class IPTBuyersV18_1WsImpl implements IPTBuyersV18_1Ws {

	@Inject
	private IPTBuyersService iptBuyersService;
	
	@Override
	public List<BuyerDetails> getBuyersList(String buyersGroup) {
		// TODO Auto-generated method stub
		return iptBuyersService.getBuyersList(buyersGroup);
	}

	@Override
	public List<BuyersGroupsDetails> getBuyerGroupsList() {
		// TODO Auto-generated method stub
		return iptBuyersService.getBuyerGroupsList();
	}
	
	@Override
	 public Response addBuyerstoGroup(InputAddBuyerstoGroup inputAddBuyerstoGroup) {
	 // TODO Auto-generated method stub
	
		iptBuyersService.addBuyerstoGroup(inputAddBuyerstoGroup);
	
		 return Response.ok().build();
	 }

	@Override
	public List<BuyerDetails> getAllBuyersList() {
		// TODO Auto-generated method stub
		return iptBuyersService.getAllBuyersList();
	}

	@Override
	public Response cancelPartOrder(InputForCancelPartOrder inputForCancelPartOrder) {
		// TODO Auto-generated method stub
		return null;
	}


}

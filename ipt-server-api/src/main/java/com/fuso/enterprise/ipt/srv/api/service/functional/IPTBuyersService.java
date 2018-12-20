package com.fuso.enterprise.ipt.srv.api.service.functional;

import java.util.List;
import java.util.Map;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.BuyersGroupsDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputAddBuyerstoGroup;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForCancelPartOrder;

public interface IPTBuyersService {
	
	List<BuyerDetails> getBuyersList(String buyersGroup);
	
	List<BuyersGroupsDetails> getBuyerGroupsList();

	boolean addBuyerstoGroup(InputAddBuyerstoGroup inputAddBuyerstoGroup);
	
	List<BuyerDetails> getAllBuyersList();

	//boolean cancelPartOrder(InputForCancelPartOrder inputForCancelPartOrder);


	
	

}

package com.fuso.enterprise.ipt.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyersGroupsDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputAddBuyerstoGroup;

public interface BuyersGroupDao {
	
	List<BuyersGroupsDetails> getBuyersGroupList();
	
	boolean addBuyerstoGroup(InputAddBuyerstoGroup inputAddBuyerstoGroup);

}

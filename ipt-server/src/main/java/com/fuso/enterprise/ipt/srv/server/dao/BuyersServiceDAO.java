package com.fuso.enterprise.ipt.srv.server.dao;

import java.util.List;
import java.util.Map;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;

public interface BuyersServiceDAO {
	
	List<BuyerDetails> getBuyersList(String buyersGroup);
	
	List<BuyerDetails> getAllBuyersList();
	
	
}

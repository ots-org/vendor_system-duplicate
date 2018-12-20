package com.fuso.enterprise.ipt.srv.functional.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.BuyersGroupsDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputAddBuyerstoGroup;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForCancelPartOrder;
import com.fuso.enterprise.ipt.srv.api.service.functional.IPTBuyersService;
import com.fuso.enterprise.ipt.srv.server.dao.BuyersGroupDao;
import com.fuso.enterprise.ipt.srv.server.dao.BuyersServiceDAO;

@Service
@Transactional
public class IPTBuyersServiceImpl implements IPTBuyersService {

	private BuyersServiceDAO buyersServiceDAO;
	private BuyersGroupDao buyersGroupDao;
	
	@Value("${ipt.mail.host}")
	public String mailHost;
	
	@Inject
	public IPTBuyersServiceImpl(BuyersServiceDAO buyersServiceDAO,
								BuyersGroupDao buyersGroupDao
								) {
		
		this.buyersServiceDAO=buyersServiceDAO;
		this.buyersGroupDao = buyersGroupDao;
	}
	
	@Override
	public List<BuyerDetails> getBuyersList(String buyersGroup) {
		// TODO Auto-generated method stub
		return buyersServiceDAO.getBuyersList(buyersGroup);
	}

	@Override
	public List<BuyersGroupsDetails> getBuyerGroupsList() {
		// TODO Auto-generated method stub
		return buyersGroupDao.getBuyersGroupList();
	}

	@Override
	public boolean addBuyerstoGroup(InputAddBuyerstoGroup inputAddBuyerstoGroup) {
		// TODO Auto-generated method stub
		return buyersGroupDao.addBuyerstoGroup(inputAddBuyerstoGroup);
	}

	@Override
	public List<BuyerDetails> getAllBuyersList() {
		// TODO Auto-generated method stub
		return buyersServiceDAO.getAllBuyersList();
	}


}

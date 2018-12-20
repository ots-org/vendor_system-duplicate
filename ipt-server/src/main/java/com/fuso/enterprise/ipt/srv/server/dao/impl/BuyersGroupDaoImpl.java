package com.fuso.enterprise.ipt.srv.server.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyersGroupsDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputAddBuyerstoGroup;
import com.fuso.enterprise.ipt.srv.server.dao.BuyersGroupDao;
import com.fuso.enterprise.ipt.srv.server.model.entity.BuyersGroup;
import com.fuso.enterprise.ipt.srv.server.util.AbstractIptDao;

@Repository
public class BuyersGroupDaoImpl extends AbstractIptDao<BuyersGroup, String> implements BuyersGroupDao {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public BuyersGroupDaoImpl() {
		super(BuyersGroup.class);
	}
	
	@Override
	public List<BuyersGroupsDetails> getBuyersGroupList() {
		// TODO Auto-generated method stub		
		List<BuyersGroup> buyersGroupList =  super.getResultListByNamedQuery("BuyersGroup.findAll", new HashMap<>());	
		
		return buyersGroupList.stream().map( buyersGroup -> convertFromEntityToDomain(buyersGroup)).collect(Collectors.toList());
	}
	
	private BuyersGroupsDetails convertFromEntityToDomain(BuyersGroup buyersGroup) {
		
		BuyersGroupsDetails buyersGroupsDetails = new BuyersGroupsDetails();
		
		buyersGroupsDetails.setDeptCode(buyersGroup.getDeptCode());
		buyersGroupsDetails.setGroupCode(buyersGroup.getGroupCode());
		buyersGroupsDetails.setGroupDesc(buyersGroup.getGroupDesc());
		buyersGroupsDetails.setGroupName(buyersGroup.getGroupName());
		buyersGroupsDetails.setSupplierAssistance(buyersGroup.getSupplierAssistance());
		
		return buyersGroupsDetails;
				
	}
	
	@Override
	public boolean addBuyerstoGroup(InputAddBuyerstoGroup inputAddBuyerstoGroup) {
		
		String useridlist = String.join(",", inputAddBuyerstoGroup.getUserIds());		
		
		System.out.println("----------------------------"+useridlist);		

		System.out.println("----------------------------"+inputAddBuyerstoGroup.getBuyerGroupName());
		
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("addBuyerstoGroup");
		
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("buyerGroupName", inputAddBuyerstoGroup.getBuyerGroupName());
		inParamMap.put("userIds", useridlist);
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		simpleJdbcCall.execute(in);		
		return true;
	}

}

package com.fuso.enterprise.ipt.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.server.dao.BuyersServiceDAO;
import com.fuso.enterprise.ipt.srv.server.model.entity.Buyers;
import com.fuso.enterprise.ipt.srv.server.util.AbstractIptDao;

@Repository
public class BuyersServiceDAOImpl extends AbstractIptDao<Buyers, String>  implements BuyersServiceDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public BuyersServiceDAOImpl() {
	        super(Buyers.class);
	 }

	@Override
	public List<BuyerDetails> getBuyersList(String buyersGroup) {
		Map<String, Object> queryParameter = new HashMap<>();
        queryParameter.put("buyer_group_code", buyersGroup);
        List<Buyers> buyers = super.getResultListByNamedQuery("getListOfBuyers", queryParameter);
        return buyers.stream().map(buyer -> convertBuyerDetailssFromEntityToDomain(buyer)).collect(Collectors.toList());
	}

	private BuyerDetails convertBuyerDetailssFromEntityToDomain(Buyers buyer) {
		BuyerDetails buyerDetail = new BuyerDetails();
		buyerDetail.setBuyerCode(buyer.getBuyerCode());
		buyerDetail.setBuyerEmail(buyer.getBuyerEmail());
		//buyerDetail.setBuyerExtension(buyer.getBuyerExtensio());
		buyerDetail.setBuyerGroupCode(buyer.getBuyerGroupCode());
		buyerDetail.setBuyerName(buyer.getBuyerName());
		return buyerDetail; 
	}

	@Override
	public List<BuyerDetails> getAllBuyersList() {
		// TODO Auto-generated method stub
		List<Buyers> buyers = super.getEntityManager().createQuery("from Buyers", Buyers.class).getResultList();
		return buyers.stream().map(buyer -> convertBuyerDetailssFromEntityToDomain(buyer)).collect(Collectors.toList());
	}

	
}

package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;
import com.fuso.enterprise.ots.srv.api.service.request.AirTableRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.AirTableDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsAirtable;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class AirTableDaoImpl extends AbstractIptDao<OtsAirtable, String> implements AirTableDao{

	private Logger logger = LoggerFactory.getLogger(getClass());
	LocalDateTime now = LocalDateTime.now();  
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public AirTableDaoImpl() {
		super(OtsAirtable.class);
	}

	@Override
	public String addAirTabelData(AirTableRequest airTableRequest) {
		try {
			for(AirTableModel airTableData : airTableRequest.getAirTabel()) 
			{
				System.out.println("Inserted data -" + airTableData.getProductName());
				OtsAirtable airtable = new OtsAirtable();
				airtable.setOtsairtableproductId(airTableData.getProductId());
				airtable.setOtsairtableproductStock(Integer.parseInt(airTableData.getProductStock()));
				airtable.setOtsairtableproductImage(airTableData.getProductImage());
				airtable.setOtsAirtableProductgst(airTableData.getProductGst());
				airtable.setOtsAirtableProductname(airTableData.getProductName());
				airtable.setOtsAirtableProductprice(airTableData.getProductPrice());
				airtable.setOtsAirtableProducername(airTableData.getProductProducerName());
				airtable.setOtsAirtableProductcategoryId(airTableData.getProductCategoryId());
				airtable.setOtsAirtableProductcategoryname(airTableData.getProductCategoryname());
				airtable.setOtsairtableproductsubcategoryId(airTableData.getProductSubCategoryId());
				airtable.setOtsAirtableProductsubcategoryName(airTableData.getProductSubCategoryName());
				airtable.setOtsAirtableTransactionId(airTableData.getProductId());
				airtable.setOtsAirtableAddedDate(airTableData.getAddedDate());
				airtable.setOtsairtableproductImage(airTableData.getProductImageList().get(0));
				save(airtable);
			}
		}catch(Exception e) {
			 throw new BusinessException(e, ErrorEnumeration.UpdationFailuer);
		}
		return "success";
	}

	
	@Override
	public List<AirTableModel> airTabelCaluclation(GetProductStockListRequest airTableRequest) {
		List<AirTableModel> previousairtableModelList = new ArrayList<AirTableModel>();
		List<AirTableModel> todayairtableModelList = new ArrayList<AirTableModel>();
		List<OtsAirtable> previousAirtableDataList = new ArrayList<OtsAirtable>();
		List<OtsAirtable> todayairtableDataList = new ArrayList<OtsAirtable>();
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsAirtableAddedDate",airTableRequest.getRequestData().getTodaysDate());
		try {
			todayairtableDataList = super.getResultListByNamedQuery("OtsAirtable.findByOtsAirtableAddedDate", queryParameter);
			todayairtableModelList = todayairtableDataList.stream().map(OtsAirtable -> convertAirTabelTOModel(OtsAirtable)).collect(Collectors.toList());
			//		requestUserList = requestOrderMapingList.stream().map(OtsOrderrequestMapping -> convertRequestOrderEntityTOModel(OtsOrderrequestMapping)).collect(Collectors.toList());
		
		}catch(Exception e) {
			System.out.print("ERROR IN GETTING CURRENT DATA");
			System.out.print(e);
		}
		
		try {
			String fmt = "yyyy-MM-dd";
			java.text.DateFormat df = new java.text.SimpleDateFormat(fmt);
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(df.parse(airTableRequest.getRequestData().getTodaysDate().toString()));
			cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
			
			System.out.println("privious day"+cal.getTime());
			queryParameter.put("otsAirtableAddedDate",cal.getTime());
			previousAirtableDataList = super.getResultListByNamedQuery("OtsAirtable.findByOtsAirtableAddedDate", queryParameter);
			previousairtableModelList = previousAirtableDataList.stream().map(OtsAirtable -> convertAirTabelTOModel(OtsAirtable)).collect(Collectors.toList());
			todayairtableModelList = processCurrentDayDataWithPrevious (todayairtableModelList,previousairtableModelList);
		}catch(Exception e) {
				System.out.print("ERROR IN GETTING PREVIOUS DAY DATA");
				System.out.print(e);
		}
		return todayairtableModelList;
	}
	
	
	public List<AirTableModel> processCurrentDayDataWithPrevious(List<AirTableModel> airtableList,List<AirTableModel> previousAirtableModelList) {
		for(int i=0; i< airtableList.size() ; i++) {
			int flag = 0;
			Integer caluclatedStock = 0;
			
			for(int j=0; j< previousAirtableModelList.size() ; j++) {
				if(airtableList.get(i).getTransactionId().equalsIgnoreCase(previousAirtableModelList.get(j).getTransactionId())) {
					flag = 1;
					airtableList.get(i).setPreviousDayFlag("yes");
					caluclatedStock = Integer.parseInt(airtableList.get(i).getProductStock()) - Integer.parseInt(previousAirtableModelList.get(j).getProductStock());
					airtableList.get(i).setAirTableProductCaluclatedStock(caluclatedStock.toString());
					break;
				}
			}
			if(flag!=1) {
				airtableList.get(i).setAirTableProductCaluclatedStock(airtableList.get(i).getProductStock());
			}
		}
		 return airtableList;
	}
	
	public AirTableModel convertAirTabelTOModel(OtsAirtable airTableData) {
		AirTableModel airTableModel = new AirTableModel();
		airTableModel.setAirtabelId(airTableData.getOtsAirtableId().toString());
		airTableModel.setProductStock(airTableData.getOtsairtableproductStock().toString());
		airTableModel.setProductImage(airTableData.getOtsairtableproductImage());
		airTableModel.setProductGst(airTableData.getOtsAirtableProductgst());
		airTableModel.setProductName(airTableData.getOtsAirtableProductname());
		airTableModel.setProductPrice(airTableData.getOtsAirtableProductprice());
		airTableModel.setProductProducerName(airTableData.getOtsAirtableProducername());
		airTableModel.setProductCategoryId(airTableData.getOtsAirtableProductcategoryId());
		airTableModel.setProductCategoryname(airTableData.getOtsAirtableProductcategoryname());
		airTableModel.setProductSubCategoryId(airTableData.getOtsairtableproductsubcategoryId());
		airTableModel.setProductSubCategoryName(airTableData.getOtsAirtableProductsubcategoryName());
		airTableModel.setAddedDate(airTableData.getOtsAirtableAddedDate());
		airTableModel.setTransactionId(airTableData.getOtsAirtableTransactionId());
		airTableModel.setProductId(airTableData.getOtsairtableproductId());
		airTableModel.setPreviousDayFlag("no");
		airTableModel.setCurrentDayStock(airTableData.getOtsairtableproductStock().toString());
		return airTableModel;
	}
}

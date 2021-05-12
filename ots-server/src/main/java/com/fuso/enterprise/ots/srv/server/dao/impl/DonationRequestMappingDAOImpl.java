package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationModel;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationReportByDateRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetUserDetailsForRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetDonationReportByDateResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetUserDetailsForResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.DonationRequestMappingDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsDonation;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsDonationRequestMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestProduct;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class DonationRequestMappingDAOImpl extends AbstractIptDao<OtsDonationRequestMapping, String> implements DonationRequestMappingDAO{

	public DonationRequestMappingDAOImpl() {
		super(OtsDonationRequestMapping.class);
	}

	@Override
	public String addNewDonation(DonationBoResponse donationBoResponse) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();  
	    System.out.println(formatter.format(date));  
		   
		try {
			OtsDonationRequestMapping donationRequestMapping = new OtsDonationRequestMapping();
			
			OtsDonation donationId = new OtsDonation();
			donationId.setOtsDonationId(Integer.parseInt(donationBoResponse.getDonationId()));
			donationRequestMapping.setOtsDonationId(donationId);
		
			donationRequestMapping.setOtsDonationDate(date);
			
			OtsRequestProduct requestProductId = new OtsRequestProduct();
			requestProductId.setOtsRequestProductId(Integer.parseInt(donationBoResponse.getPaymentRequestId()));
			donationRequestMapping.setOtsRequestProductId(requestProductId);
			
			super.getEntityManager().merge(donationRequestMapping);
			
			return "success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;	
	}
	
	@Override
	public GetDonationReportByDateResponse getDonationReportByDate(
			GetDonationReportByDateRequest donationReportByDateRequest) {
		GetDonationReportByDateResponse donationReportByDateResponse = new GetDonationReportByDateResponse();
		try {
			List<OtsDonationRequestMapping> donationRequestMapping = new ArrayList<OtsDonationRequestMapping>();
			List<DonationModel> donationModelList = new ArrayList<DonationModel>();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("startDate", donationReportByDateRequest.getRequest().getStartDate());
			queryParameter.put("endDate", donationReportByDateRequest.getRequest().getEndDate());
			donationRequestMapping = super.getResultListByNamedQuery("OtsDonation.getDonationReportByDate", queryParameter);
			
			donationModelList = donationRequestMapping.stream().map(OtsDonationRequestMapping -> convertEntityToModel(OtsDonationRequestMapping)).collect(Collectors.toList());

			donationReportByDateResponse.setDonationList(donationModelList);
			if(donationReportByDateRequest.getRequest().getStatus().equalsIgnoreCase("donorId")) {
				List<DonationModel> donationModelUserId = new ArrayList<DonationModel>();
				for(int i=0; i<donationModelList.size();i++) {
					if(donationModelList.get(i).getUserDetails().getUserId().equalsIgnoreCase(donationReportByDateRequest.getRequest().getUserId())) {
						donationModelUserId.add(donationModelList.get(i));
					}
				}	
				donationReportByDateResponse.setDonationList(donationModelUserId);
			}
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.No_Donation);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.No_Donation);
		}
		
		return donationReportByDateResponse;
	}
	
	public DonationModel convertEntityToModel(OtsDonationRequestMapping donationRequestMapping) {
		DonationModel donationModelResponse = new DonationModel();
		ProductDetails productDetails = new ProductDetails();
		
		donationModelResponse.setDonationAmount(donationRequestMapping.getOtsDonationId().getOtsDonationAmount());
		donationModelResponse.setDonatedQty(donationRequestMapping.getOtsDonationId().getOtsDonationDonatedqty());
		donationModelResponse.setDonationId(donationRequestMapping.getOtsDonationId().getOtsDonationId().toString());
		donationModelResponse.setDonationStatus(donationRequestMapping.getOtsDonationId().getOtsDonationStatus());
		donationModelResponse.setCompanyName(donationRequestMapping.getOtsDonationId().getOtsDonationCompanyName());
		donationModelResponse.setAtgAddress(donationRequestMapping.getOtsDonationId().getOtsDonationAtgAddress());
		donationModelResponse.setDonationMethod(donationRequestMapping.getOtsDonationId().getOtsDonationPaymentMethod());
		productDetails.setProductName(donationRequestMapping.getOtsRequestProductId().getOtsProductId().getOtsProductName());
		productDetails.setProductPrice(donationRequestMapping.getOtsRequestProductId().getOtsProductId().getOtsProductPrice().toString());
		
		UserDetails userDetails = new UserDetails();
		userDetails.setUserId(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersId().toString());
		userDetails.setFirstName(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersFirstname());
		userDetails.setLastName(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersLastname());
		userDetails.setAddress1(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersAddr1());
		userDetails.setAddress2(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersAddr2());
		userDetails.setContactNo(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersContactNo());
		donationModelResponse.setProductDetails(productDetails);
		donationModelResponse.setUserDetails(userDetails);
		return donationModelResponse;	
	}

	@Override
	public GetDonationReportByDateResponse getRequestByDonationId(
			GetDonationReportByDateResponse getDonationReportByDateResponse ) {
		OtsDonationRequestMapping donationRequestMapping = new OtsDonationRequestMapping();
		Map<String, Object> queryParameter = new HashMap<>();
		GetDonationReportByDateResponse newOpResp = new GetDonationReportByDateResponse();
		List<DonationModel> donationList = new ArrayList<DonationModel>(); 
		try {
			for(int i=0;i<getDonationReportByDateResponse.getDonationList().size();i++) {
			
				DonationModel donationModelResponse = new DonationModel();
				OtsDonation OtsDonation = new OtsDonation();
				OtsDonation.setOtsDonationId(Integer.parseInt(getDonationReportByDateResponse.getDonationList().get(i).getDonationId()));
				queryParameter.put("otsDonationId",OtsDonation );
				donationRequestMapping = super.getResultByNamedQuery("OtsDonation.getRequestByDonationId", queryParameter);
				
				ProductDetails productDetails = new ProductDetails();
				
				donationModelResponse.setDonationAmount(donationRequestMapping.getOtsDonationId().getOtsDonationAmount());
				donationModelResponse.setDonatedQty(donationRequestMapping.getOtsDonationId().getOtsDonationDonatedqty());
				donationModelResponse.setDonationId(donationRequestMapping.getOtsDonationId().getOtsDonationId().toString());
			
				productDetails.setProductId(donationRequestMapping.getOtsRequestProductId().getOtsProductId().getOtsProductId().toString());
				productDetails.setProductName(donationRequestMapping.getOtsRequestProductId().getOtsProductId().getOtsProductName());
				productDetails.setProductPrice(donationRequestMapping.getOtsRequestProductId().getOtsProductId().getOtsProductPrice().toString());
				
				UserDetails userDetails = new UserDetails();
				userDetails.setUserId(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersId().toString());
				userDetails.setFirstName(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersFirstname());
				userDetails.setLastName(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersLastname());
				userDetails.setAddress1(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersAddr1());
				userDetails.setAddress2(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersAddr2());
				userDetails.setContactNo(donationRequestMapping.getOtsDonationId().getOtsDonorsId().getOtsUsersContactNo());
				donationModelResponse.setProductDetails(productDetails);
				donationModelResponse.setUserDetails(userDetails);
				donationList.add(donationModelResponse);
				//getDonationReportByDateResponse.getDonationList().add(donationModelResponse);
				//newOpResp 
			}
			newOpResp.setDonationList(donationList);
			return newOpResp;
		}catch(Exception e) {
			System.out.print(e);
		}
		return getDonationReportByDateResponse;
		
		
	
	}

}

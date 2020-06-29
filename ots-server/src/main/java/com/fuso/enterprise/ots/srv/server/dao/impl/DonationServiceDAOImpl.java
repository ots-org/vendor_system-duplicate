package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationReportByDateRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateDonationRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetDonationReportByDateResponse;
import com.fuso.enterprise.ots.srv.server.dao.DonationServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsDonation;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsDonationRequestMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class DonationServiceDAOImpl extends AbstractIptDao<OtsDonation, String> implements DonationServiceDAO{

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	LocalDateTime now = LocalDateTime.now();  
	
	public DonationServiceDAOImpl() {
		super(OtsDonation.class);
	}

	@Override
	public String addNewDonation(AddDonationtoRequest addDonationtoRequest) {
		OtsDonation donation = new OtsDonation();
		String donationId = null;
		try {
			for(int i=0 ; i<addDonationtoRequest.getRequest().size();i++) {
				donation.setOtsDonationAmount(addDonationtoRequest.getRequest().get(i).getDontaionAmount());
				donation.setOtsDonationPaymentid(addDonationtoRequest.getRequest().get(i).getPaymentId());
				donation.setOtsDonationDonatedqty(addDonationtoRequest.getRequest().get(i).getDonatedQty());
				donation.setOtsDonationStatus(addDonationtoRequest.getRequest().get(i).getDonationStatus());
				donation.setOtsDonationPaymentMethod(addDonationtoRequest.getRequest().get(i).getDonationMethod());
				if(addDonationtoRequest.getRequest().get(0).getDonationStatus().equalsIgnoreCase("directDonation")) {
					donation.setOtsDonationDescription(addDonationtoRequest.getRequest().get(0).getDescription());
				}
				donation.setOtsDonationPanNumber(addDonationtoRequest.getRequest().get(0).getPanNumber());
				donation.setOtsDonationOtherNumber(addDonationtoRequest.getRequest().get(0).getOtherNumber());
				
				OtsUsers donorsId = new OtsUsers();
				donorsId.setOtsUsersId(Integer.parseInt(addDonationtoRequest.getRequest().get(i).getDonorId()));
				donation.setOtsDonorsId(donorsId);
				donation.setOtsDonationDate(Date.valueOf(now.toLocalDate()));
				
				save(donation);
				super.getEntityManager().flush();
				donationId = donation.getOtsDonationId().toString();
				System.out.println(donationId);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return donationId;
	}


	@Override
	public GetDonationReportByDateResponse getDonationForUpdateStatus(
			GetDonationByStatusRequest donationByStatusRequest) {
		GetDonationReportByDateResponse getDonationReportByDateResponse = new GetDonationReportByDateResponse();
		try {
			System.out.print("3");
			Map<String, Object> queryParameter = new HashMap<>();
			List<DonationModel> donationModelList =  new ArrayList<DonationModel>();
			List<OtsDonation> otsDonation = new ArrayList<OtsDonation>();
			queryParameter.put("otsDonationStatus", donationByStatusRequest.getRequest().getStatus());
			if(!donationByStatusRequest.getRequest().getStatus().equalsIgnoreCase("assigneeRequest")) {
				otsDonation = super.getResultListByNamedQuery("OtsDonation.findByOtsDonationStatus", queryParameter);
			}else {
				OtsUsers otsAssgineId = new OtsUsers();
				otsAssgineId.setOtsUsersId(Integer.parseInt(donationByStatusRequest.getRequest().getAssgineId()));
				queryParameter.put("otsAssgineId", otsAssgineId);
				otsDonation = super.getResultListByNamedQuery("OtsDonation.getRequestByDonationIdAndStatus", queryParameter);
			}
			
			//donationModelList = donationRequestMapping.stream().map(OtsDonationRequestMapping -> convertEntityToModel(OtsDonationRequestMapping)).collect(Collectors.toList());
			donationModelList = otsDonation.stream().map(OtsDonation -> convertEntityToModel(OtsDonation)).collect(Collectors.toList());
			getDonationReportByDateResponse.setDonationList(donationModelList);
			
		}catch(Exception e) {
			System.out.println(e);
		}
		return getDonationReportByDateResponse;
	}

	public DonationModel convertEntityToModel(OtsDonation otsDonation) {
		DonationModel donationModelResponse = new DonationModel();
		ProductDetails productDetails = new ProductDetails();
		donationModelResponse.setDonationId(otsDonation.getOtsDonationId().toString());
		donationModelResponse.setDonationAmount(otsDonation.getOtsDonationAmount());
		donationModelResponse.setDonatedQty(otsDonation.getOtsDonationDonatedqty());
		donationModelResponse.setDonationId(otsDonation.getOtsDonationId().toString());
		donationModelResponse.setDonationDate(otsDonation.getOtsDonationDate()==null?null:otsDonation.getOtsDonationDate().toString());
		UserDetails userDetails = new UserDetails();
		userDetails.setUserId(otsDonation.getOtsDonorsId().getOtsUsersId().toString());
		userDetails.setContactNo(otsDonation.getOtsDonorsId().getOtsUsersContactNo());
		userDetails.setFirstName(otsDonation.getOtsDonorsId().getOtsUsersFirstname());
		userDetails.setLastName(otsDonation.getOtsDonorsId().getOtsUsersLastname());
		userDetails.setAddress1(otsDonation.getOtsDonorsId().getOtsUsersAddr1());
		userDetails.setAddress2(otsDonation.getOtsDonorsId().getOtsUsersAddr2());
		
		donationModelResponse.setProductDetails(productDetails);
		donationModelResponse.setUserDetails(userDetails);
		
		return donationModelResponse;	
	}

	@Override
	public String updateDonation(UpdateDonationRequest updateDonationRequest) {
		try {
			OtsDonation otsDonation = new OtsDonation();
			Map<String, Object> queryParameter = new HashMap<>();
				queryParameter.put("otsDonationId", Integer.parseInt(updateDonationRequest.getRequest().getDonationId()));
				otsDonation = super.getResultByNamedQuery("OtsDonation.findByOtsDonationId", queryParameter);
				otsDonation.setOtsDonationStatus(updateDonationRequest.getRequest().getDonationStatus());
				
				OtsUsers OtsUsers = new OtsUsers();
				OtsUsers.setOtsUsersId(Integer.parseInt(updateDonationRequest.getRequest().getAssignedId()));
				otsDonation.setOtsAssgineId(OtsUsers);
			
			super.getEntityManager().merge(otsDonation);
			
		}catch(Exception e) {
			System.out.print(e);
		}
		return "Success";
	}
	
}

package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.fuso.enterprise.ots.srv.api.model.domain.BillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetBillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ListOfBillId;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.BillReportBasedOnDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetBillByOrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillReportByDateBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.BillServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class BillServiceDAOImpl extends AbstractIptDao<OtsBill, String> implements BillServiceDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	
	public BillServiceDAOImpl() {
		super(OtsBill.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest) {
		String BillNumber;
		List<BillDetails> billDetails = new ArrayList<BillDetails>();
		BillDetailsBOResponse billDetailsBOResponse=new BillDetailsBOResponse();
		Timestamp createDate = new Timestamp(System.currentTimeMillis());

		try{
			OtsBill otsBill = new OtsBill();
			otsBill.setOtsBillAmount(Long.parseLong(billDetailsBORequest.getRequestData().getBillAmount()));
			otsBill.setOtsBillAmountReceived(Long.parseLong(billDetailsBORequest.getRequestData().getBillAmountReceived()));
			otsBill.setOtsBillGenerated(billDetailsBORequest.getRequestData().getBillGenerated());
			otsBill.setOtsBillStatus("Active");
			otsBill.setOtsBillCreated(createDate);
			otsBill.setOtsbillIGST(Long.parseLong(billDetailsBORequest.getRequestData().getIGST()));
			otsBill.setOtsbillSGST(Long.parseLong(billDetailsBORequest.getRequestData().getCGST()));
			otsBill.setOtsBillOutstandingAmt(Long.parseLong(billDetailsBORequest.getRequestData().getOutstandingAmount()));
			otsBill.setOtsBillPdf(billDetailsBORequest.getRequestData().getBillPdf());
			OtsUsers otsUser = new OtsUsers();
			otsUser.setOtsUsersId(billDetailsBORequest.getRequestData().getCustomerId());
			otsBill.setOtsCustomerId(otsUser);
			try {
				if(billDetailsBORequest.getRequestData().getBillId()==0 || billDetailsBORequest.getRequestData().getBillId()==null) {
					super.getEntityManager().persist(otsBill);
					super.getEntityManager().flush();
					BillNumber = "OtsBill-"+otsBill.getOtsBillId();
					otsBill.setOtsBillNumber(BillNumber);
					super.getEntityManager().merge(otsBill);
				}else{
					otsBill.setOtsBillId(billDetailsBORequest.getRequestData().getBillId());
					BillNumber = "OtsBill-"+otsBill.getOtsBillId();
					otsBill.setOtsBillNumber(BillNumber);
					super.getEntityManager().merge(otsBill);
				}
				super.getEntityManager().flush();
			   billDetails.add(convertUserDetailsFromEntityToDomain(otsBill));
			   billDetailsBOResponse.setBillDetails(billDetails);
			}catch (NoResultException e) {
	        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
	        }
			logger.info("Inside Event=1021,Class:BillServiceDAOImpl,Method:addOrUpdateBill:"+billDetailsBOResponse);
		}catch (Exception e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return  billDetailsBOResponse;
	}

	/*this function is using same request and response for add bill api so I will create another fuction for converstion*/
	private BillDetails convertUserDetailsFromEntityToDomain(OtsBill otsBill) {
		BillDetails billDetails = new BillDetails();
		billDetails.setBillId(otsBill.getOtsBillId()==null?null:otsBill.getOtsBillId());
		billDetails.setBillAmount((otsBill.getOtsBillAmount())==null?null:otsBill.getOtsBillAmount().toString());
		billDetails.setBillAmountReceived(otsBill.getOtsBillAmountReceived()==null?null:otsBill.getOtsBillAmountReceived().toString());
		billDetails.setBillGenerated(otsBill.getOtsBillGenerated()==null?null:otsBill.getOtsBillGenerated());
		billDetails.setBillPdf(otsBill.getOtsBillPdf()==null?null:otsBill.getOtsBillPdf());
		return billDetails;
	}
	
	
	
	private GetBillDetails convertBillDetailsFromEntityToDomain(OtsBill otsBill) {
		GetBillDetails billDetails = new GetBillDetails();
		billDetails.setBillId(otsBill.getOtsBillId()==null?null:otsBill.getOtsBillId().toString());
		billDetails.setBillAmount((otsBill.getOtsBillAmount())==null?null:otsBill.getOtsBillAmount().toString());
		billDetails.setBillGenerated(otsBill.getOtsBillGenerated()==null?null:otsBill.getOtsBillGenerated());
		billDetails.setBillAmountRecived(otsBill.getOtsBillAmountReceived()==null?null:otsBill.getOtsBillAmountReceived().toString());
		billDetails.setBillStatus(otsBill.getOtsBillStatus());
		return billDetails;
	}

	@Override
	public BillDataBOResponse getBillDetailsByOrderId(GetBillByOrderIdBORequest getBillByOrderIdBORequest) {
	BillDataBOResponse billDataBOResponse = new BillDataBOResponse();
	try {
		GetBillDetails BillDetails = new GetBillDetails();
		OtsBill otsBill = new OtsBill();
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsBillNumber", getBillByOrderIdBORequest.getRequest().getBillNumber());
		otsBill = super.getResultByNamedQuery("OtsBill.findByOtsBillNumber", queryParameter);
		BillDetails = convertBillDetailsFromEntityToDomain(otsBill);
		billDataBOResponse.setGetBillDetails(BillDetails);
	}catch (Exception e) {
        logger.error("Exception while Getting data from DB  :"+e.getMessage());
    	e.printStackTrace();
        throw new BusinessException(e.getMessage(), e);}
		return billDataBOResponse;
	}

	@Override
	public BillReportByDateBOResponse getBillReportByDate(BillReportBasedOnDateBORequest billReportBasedOnDateBORequest) {
		BillReportByDateBOResponse billNumberResponse=new BillReportByDateBOResponse();
		List<OtsBill> otsBill = new ArrayList<OtsBill>();
		List<ListOfBillId> listOfBillId=new  ArrayList<ListOfBillId>();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("fromDate",billReportBasedOnDateBORequest.getRequestData().getFromDate());
			queryParameter.put("toDate", billReportBasedOnDateBORequest.getRequestData().getToDate());
			OtsUsers otsuser = new OtsUsers();
			otsuser.setOtsUsersId(Integer.parseInt(billReportBasedOnDateBORequest.getRequestData().getUserId()));
			queryParameter.put("otsCustomerId", otsuser);
			otsBill = super.getResultListByNamedQuery("OTSBill.findBillNumber", queryParameter);
			//listOfBillId = convertListOfBillIdFromEntityToDomain(otsBill);
			listOfBillId =  otsBill.stream().map(otsBillList -> convertListOfBillIdFromEntityToDomain(otsBillList)).collect(Collectors.toList());
			billNumberResponse.setBillNumber(listOfBillId);
		}catch (Exception e) {
	        logger.error("Exception while Getting data from DB  :"+e.getMessage());
	    	e.printStackTrace();
	        throw new BusinessException(e.getMessage(), e);}
			
		return billNumberResponse;
	}

	
	private ListOfBillId convertListOfBillIdFromEntityToDomain(OtsBill otsBillList) {
		ListOfBillId listOfBillId=new ListOfBillId();
		listOfBillId.setBillId(otsBillList.getOtsBillId().toString());
		listOfBillId.setBillNumber(otsBillList.getOtsBillNumber());
		listOfBillId.setBillAmount(otsBillList.getOtsBillAmount().toString());
		listOfBillId.setBillAmountReceived(otsBillList.getOtsBillAmountReceived().toString());
		listOfBillId.setBillGenerated(otsBillList.getOtsBillGenerated());
		listOfBillId.setBillStatus(otsBillList.getOtsBillStatus());
		listOfBillId.setCGST(otsBillList.getOtsbillSGST()+"");
		listOfBillId.setiGST(otsBillList.getOtsbillIGST()+"");
		listOfBillId.setOutstandingAmount(otsBillList.getOtsBillOutstandingAmt()+"");
		return listOfBillId;
	}

	
}

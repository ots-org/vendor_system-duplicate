package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.fuso.enterprise.ots.srv.api.model.domain.BillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetBillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetBillByOrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
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
		try{
			OtsBill otsBill = new OtsBill();
			otsBill.setOtsBillAmount(Long.parseLong(billDetailsBORequest.getRequestData().getBillAmount()));
			otsBill.setOtsBillAmountReceived(Long.parseLong(billDetailsBORequest.getRequestData().getBillAmountReceived()));
			otsBill.setOtsBillGenerated(billDetailsBORequest.getRequestData().getBillGenerated());
			otsBill.setOtsBillStatus("Active");
			try {
				if(billDetailsBORequest.getRequestData().getBillId()==0 || billDetailsBORequest.getRequestData().getBillId()==null) {
					super.getEntityManager().persist(otsBill);
					super.getEntityManager().flush();
					BillNumber = "OtsBill-"+otsBill.getOtsBillId();
					otsBill.setOtsBillNumber(BillNumber);
					super.getEntityManager().merge(otsBill);
				}else{
					otsBill.setOtsBillId(billDetailsBORequest.getRequestData().getBillId());
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

}

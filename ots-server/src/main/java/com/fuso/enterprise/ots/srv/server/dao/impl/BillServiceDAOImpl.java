package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.BillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
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
		List<BillDetails> billDetails = new ArrayList<BillDetails>();
		BillDetailsBOResponse billDetailsBOResponse=new BillDetailsBOResponse();
		try{
			OtsBill otsBill = new OtsBill();
			otsBill.setOtsBillNumber(billDetailsBORequest.getRequestData().getBillNumber());
			otsBill.setOtsBillAmount(Long.parseLong(billDetailsBORequest.getRequestData().getBillAmount()));
			otsBill.setOtsBillAmountReceived(Long.parseLong(billDetailsBORequest.getRequestData().getBillAmountReceived()));
			otsBill.setOtsBillGenerated(billDetailsBORequest.getRequestData().getBillGenerated());
			otsBill.setOtsBillStatus(billDetailsBORequest.getRequestData().getBillStatus());
			try {
				if(billDetailsBORequest.getRequestData().getBillId()==0 || billDetailsBORequest.getRequestData().getBillId()==null) {
					super.getEntityManager().persist(otsBill);
					super.getEntityManager().flush();
				}else{
					otsBill.setOtsBillId(billDetailsBORequest.getRequestData().getBillId());
					super.getEntityManager().merge(otsBill);
					super.getEntityManager().flush();
				}
			   billDetails.add(convertUserDetailsFromEntityToDomain(otsBill));
			   billDetailsBOResponse.setBillDetails(billDetails);
			}catch (NoResultException e) {
	        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
	        }
			logger.info("Inside Event=1021,Class:BillServiceDAOImpl,Method:addOrUpdateBill:"+billDetailsBOResponse);
		}catch (NoResultException e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return  billDetailsBOResponse;
	}

	private BillDetails convertUserDetailsFromEntityToDomain(OtsBill otsBill) {
		BillDetails billDetails = new BillDetails();
		billDetails.setBillId(otsBill.getOtsBillId()==null?null:otsBill.getOtsBillId());
		billDetails.setBillNumber((otsBill.getOtsBillNumber())==null?null:otsBill.getOtsBillNumber().toString());
		billDetails.setBillAmount((otsBill.getOtsBillAmount())==null?null:otsBill.getOtsBillAmount().toString());
		billDetails.setBillAmountReceived(otsBill.getOtsBillAmountReceived()==null?null:otsBill.getOtsBillAmountReceived().toString());
		billDetails.setBillGenerated(otsBill.getOtsBillGenerated()==null?null:otsBill.getOtsBillGenerated());
		billDetails.setBillStatus(otsBill.getOtsBillStatus()==null?null:otsBill.getOtsBillStatus());
		return billDetails;
	}
	
	
	

}

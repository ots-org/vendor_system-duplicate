package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstandingDetails;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerOutstanding;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class CustomerOutstandingAmtDAOImpl extends AbstractIptDao<OtsCustomerOutstanding, String> implements CustomerOutstandingAmtDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public CustomerOutstandingAmtDAOImpl() {
		super(OtsCustomerOutstanding.class);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String updateCustomerOutstandingAmt(CustomerOutstandingBORequest customerOutstandingBORequest) {
		String responseData;
		try{
			OtsCustomerOutstanding otsCustomerOutstanding=new OtsCustomerOutstanding();
			Map<String, Object> queryParameter = new HashMap<>();
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerOutstandingBORequest.getRequestData().getCustomerId()));
			try {
				queryParameter.put("customerId", otsUsers);
				otsCustomerOutstanding=super.getResultByNamedQuery("OtsCustomerOutstanding.findByOtscustomerId", queryParameter);
				otsCustomerOutstanding.setOtsCustomerOutstandingAmt(Long.parseLong(customerOutstandingBORequest.getRequestData().getCustomerOutstandingAmt()));
				super.getEntityManager().merge(otsCustomerOutstanding);
				responseData="Customer Outstanding Balance Updated Successfully";
				logger.info("Inside Event=1022,Class:CustomerOutstandingAmtDAOImpl,Method:updateCustomerOutstandingAmt");
			}catch (NoResultException e) {
				otsCustomerOutstanding.setOtsCustomerId(otsUsers);
				otsCustomerOutstanding.setOtsCustomerOutstandingAmt(Long.parseLong(customerOutstandingBORequest.getRequestData().getCustomerOutstandingAmt()));
				super.getEntityManager().merge(otsCustomerOutstanding);
				responseData="Customer Outstanding Balance Added Successfully";
	        }
		}catch (NoResultException e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return  responseData;
	}
	
	@Override
	public GetCustomerOutstandingAmtBOResponse getCustomerOutstandingAmt(GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest) {
		System.out.println("inside the func " );
		List<CustomerOutstandingDetails>  customerOutstandingDetails=new ArrayList<CustomerOutstandingDetails>();
		GetCustomerOutstandingAmtBOResponse customerOutstandingAmtResponse = new GetCustomerOutstandingAmtBOResponse();
		OtsUsers otsUsers = new OtsUsers();
		otsUsers.setOtsUsersId(Integer.parseInt(getCustomerOutstandingAmtBORequest.getRequestData().getCustomerId()));   
		try {
			System.out.println("inside the try block of func");
        	OtsCustomerOutstanding otsCustomerOutstanding=new OtsCustomerOutstanding();
			Map<String, Object> queryParameter = new HashMap<>();
			
			queryParameter.put("customerId", otsUsers);
			otsCustomerOutstanding=super.getResultByNamedQuery("OtsCustomerOutstanding.findByOtscustomerId", queryParameter);
			customerOutstandingDetails.add(convertCustomerOutstandingDetailsFromEntityToDomain(otsCustomerOutstanding));
			customerOutstandingAmtResponse.setCustomerOutstandingAmount(customerOutstandingDetails);
        }catch (NoResultException e) {
        	OtsCustomerOutstanding otsCustomerOutstanding = new OtsCustomerOutstanding();
        	otsCustomerOutstanding.setOtsCustomerId(otsUsers);
        	super.getEntityManager().persist(otsCustomerOutstanding);
        	customerOutstandingDetails.add(convertCustomerOutstandingDetailsFromEntityToDomain(otsCustomerOutstanding));
			customerOutstandingAmtResponse.setCustomerOutstandingAmount(customerOutstandingDetails);
        }
		return customerOutstandingAmtResponse;
	}
	private CustomerOutstandingDetails convertCustomerOutstandingDetailsFromEntityToDomain(OtsCustomerOutstanding otsCustomerOutstanding) {
		CustomerOutstandingDetails customerOutstandingDetails=new CustomerOutstandingDetails();
		customerOutstandingDetails.setCustomerId((otsCustomerOutstanding.getOtsCustomerId())==null?null:otsCustomerOutstanding.getOtsCustomerId().getOtsUsersId().toString());
		customerOutstandingDetails.setCustomerOutstandingAmt((otsCustomerOutstanding.getOtsCustomerOutstandingAmt())==null?"0":otsCustomerOutstanding.getOtsCustomerOutstandingAmt().toString());
		return customerOutstandingDetails;
	}
}

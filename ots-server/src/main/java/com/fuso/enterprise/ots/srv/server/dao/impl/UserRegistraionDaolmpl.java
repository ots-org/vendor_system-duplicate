package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserRegistrationDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class UserRegistraionDaolmpl  extends AbstractIptDao<OtsRegistration, String> implements UserRegistrationDao {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	//private UserTemp userTemp;
	
    public UserRegistraionDaolmpl() {
        super(OtsRegistration.class);
    }

	@Override
	public String  addUserRegistration(AddNewBORequest addNewBORequest) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		UserRegistrationResponce userRegistrationResponce = new UserRegistrationResponce();
		OtsRegistration otsRegistration = new OtsRegistration();
		OtsUsers otsUsers = new OtsUsers();
	      try {
	    	  otsRegistration.setOtsRegistrationFirstname(addNewBORequest.getRequestData().getFirstName());
	    	  otsRegistration.setOtsRegistrationLastname(addNewBORequest.getRequestData().getLastName());
	    	  otsRegistration.setOtsRegistrationContactNo(addNewBORequest.getRequestData().getPhonenumber());
	    	  otsRegistration.setOtsRegistrationAddr1(addNewBORequest.getRequestData().getAddress1());
	    	  otsRegistration.setOtsRegistrationAddr2(addNewBORequest.getRequestData().getAddress2());
	    	  otsRegistration.setOtsRegistrationPincode(addNewBORequest.getRequestData().getPincode());
	    	  OtsUserRole otsUserRole = new OtsUserRole();
	    	  otsUserRole.setOtsUserRoleId(Integer.parseInt(addNewBORequest.getRequestData().getUserRoleId()));
	    	  otsRegistration.setOtsUserRoleId(otsUserRole);
	    	  otsRegistration.setOtsRegistrationEmailid(addNewBORequest.getRequestData().getEmailId()); 
	    	  
	    	  otsUsers.setOtsUsersId(Integer.parseInt(addNewBORequest.getRequestData().getMappedTo()));
	    	  otsRegistration.setOtsUsersMappedTo(otsUsers);
	    	  
	    	  OtsProduct otsProduct = new OtsProduct();
	          otsProduct.setOtsProductId(Integer.parseInt(addNewBORequest.getRequestData().getProductId()));
	          otsRegistration.setOtsProductId(otsProduct);
	    	  otsRegistration.setOtsRegistrationStatus(addNewBORequest.getRequestData().getStatus());
	    	  otsRegistration.setOtsRegistrationPassword(addNewBORequest.getRequestData().getPassword());
	    	  super.getEntityManager().merge(otsRegistration);
	      }catch(Exception e){
	    	  e.printStackTrace(); 
	    	  throw new BusinessException(e.getMessage(), e);
	      }
        return addNewBORequest.getRequestData().getEmailId();
	}
	
	@Override
	public List<RegistorToUserDetails> getNewRegistrationDao(String mappedTo) {	
		List<RegistorToUserDetails> userDetails = new ArrayList<RegistorToUserDetails>();
    	try {
    		List<OtsRegistration> userList = null;		
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsUsersMappedTo", Integer.parseInt(mappedTo));
    			userList  = super.getResultListByNamedQuery("OtsUsers.findByRegistrationTable", queryParameter);
    		} catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }
            logger.info("Inside Event=1002,Class:UserRegistraionDaolmpl,Method:getNewRegistrationDao,"
					+ "UserList Size:" +userList.size());
            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;
	}
	
	@Override
	public UserDetails fetchUserDetailsfromRegistration(String registrationId) {
		UserDetails userDetails = new UserDetails();
		List<OtsRegistration> otsRegistrationList = new ArrayList<OtsRegistration>();
		try {
        	Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsRegistrationId", Integer.parseInt(registrationId));
			otsRegistrationList  = super.getResultListByNamedQuery("OtsRegistration.findByOtsRegistrationId", queryParameter);
		} catch (NoResultException e) {
        	logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		userDetails =  convertUserDetailsFromOtsRegistrationEntityToDomain(otsRegistrationList.get(0));
		return userDetails;
	}
	
	@Override
	public OtsRegistration fetOtsRegistrationBasedonRegisterID(Integer registrationId) {
		List<OtsRegistration> otsRegistrationList = new ArrayList<OtsRegistration>();
		try {
        	Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsRegistrationId", registrationId);
			otsRegistrationList  = super.getResultListByNamedQuery("OtsRegistration.findByOtsRegistrationId", queryParameter);
		} catch (NoResultException e) {
        	logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return otsRegistrationList.get(0);
	}
	
	@Override
	public ApproveRegistrationResponse approveRegistration(OtsRegistration otsRegistration) {
		ApproveRegistrationResponse approveRegistrationResponse = new ApproveRegistrationResponse();
		try {
			  otsRegistration.setOtsRegistrationStatus("Active");
	    	  super.getEntityManager().merge(otsRegistration);
	      }catch(Exception e){
	    	  e.printStackTrace(); 
	    	  throw new BusinessException(e.getMessage(), e);
	      }
		
		return approveRegistrationResponse;
	}
	
	private RegistorToUserDetails convertUserDetailsFromEntityToDomain(OtsRegistration otsRegistration) {
		RegistorToUserDetails registorToUserDetails = new RegistorToUserDetails();           
		registorToUserDetails.setFirstName(otsRegistration.getOtsRegistrationFirstname());
		registorToUserDetails.setLastName(otsRegistration.getOtsRegistrationLastname());
		registorToUserDetails.setAddress1(otsRegistration.getOtsRegistrationAddr1());
		registorToUserDetails.setAddress2(otsRegistration.getOtsRegistrationAddr2());
		registorToUserDetails.setPincode(otsRegistration.getOtsRegistrationPincode());
       	registorToUserDetails.setPhonenumber(otsRegistration.getOtsRegistrationContactNo());
		registorToUserDetails.setEmailId(otsRegistration.getOtsRegistrationEmailid());
       	registorToUserDetails.setUserRoleId(otsRegistration.getOtsUserRoleId().getOtsUserRoleId().toString());
       	registorToUserDetails.setProductId(otsRegistration.getOtsProductId().getOtsProductId().toString());
		registorToUserDetails.setStatus(otsRegistration.getOtsRegistrationStatus());
        registorToUserDetails.setMappedTo(otsRegistration.getOtsUsersMappedTo().getOtsUsersId().toString());
        registorToUserDetails.setPassword(otsRegistration.getOtsRegistrationPassword());            
        return registorToUserDetails;
    }
	
	private UserDetails convertUserDetailsFromOtsRegistrationEntityToDomain(OtsRegistration otsRegistration) {
		UserDetails userDetails = new UserDetails();
		userDetails.setFirstName(otsRegistration.getOtsRegistrationFirstname());
		userDetails.setLastName(otsRegistration.getOtsRegistrationLastname());
		userDetails.setAddress1(otsRegistration.getOtsRegistrationAddr1());
		userDetails.setAddress2(otsRegistration.getOtsRegistrationAddr2());
		userDetails.setPincode(otsRegistration.getOtsRegistrationPincode());
		//userDetails.setPhonenumber(otsRegistration.getOtsRegistrationContactNo());
		userDetails.setEmailId(otsRegistration.getOtsRegistrationEmailid());
		userDetails.setUserRoleId(otsRegistration.getOtsUserRoleId().getOtsUserRoleId().toString());
		userDetails.setProductId(otsRegistration.getOtsProductId().getOtsProductId().toString());
		//userDetails.setStatus(otsRegistration.getOtsRegistrationStatus());
		userDetails.setMappedTo(otsRegistration.getOtsUsersMappedTo().getOtsUsersId().toString());
		//userDetails.setPassword(otsRegistration.getOtsRegistrationPassword());            
        return userDetails;
    }

	

	

	
	
}




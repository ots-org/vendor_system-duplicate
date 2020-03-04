	package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class UserServiceUtilityDAOImpl  extends AbstractIptDao<OtsUsers, String> implements UserServiceUtilityDAO{

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;

	  public UserServiceUtilityDAOImpl() {
	        super(OtsUsers.class);
	    }

	@Override
	public List<UserDetails> getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {
		List<UserDetails> userDetails = new ArrayList<UserDetails>();
		String searchKey 	= requestBOUserBySearch.getRequestData().getSearchKey();
		String searchvalue  = requestBOUserBySearch.getRequestData().getSearchvalue();
        Map<String, Object> queryParameter = new HashMap<>();
        List<OtsUsers> userList = null;
		if(requestBOUserBySearch.getRequestData().getDistributorId()!=null) {
			System.out.print("Dis");
			try {
	            switch(searchKey)
		            {
			            case "UsersId":
			            					queryParameter.put("otsUsersId", Integer.parseInt(searchvalue));
			            					queryParameter.put("DistributorId", Integer.parseInt(requestBOUserBySearch.getRequestData().getDistributorId()));
			            					userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
			            				    break;
			            case "UsersFirstname":
											queryParameter.put("otsUsersFirstname","%"+searchvalue+"%");
											userList  = super.getResultListByNamedQuery("OtsUsers.findByPattrenMatchingotsUsersFirstname", queryParameter);
										    break;
			            case "UsersLastname":
											queryParameter.put("otsUsersLastname","%"+searchvalue+"%");
											userList  = super.getResultListByNamedQuery("OtsUsers.findByPattrenMatchingotsUsersLastname", queryParameter);
											break;
			            case "UsersEmailid":
											queryParameter.put("otsUsersEmailid","%"+searchvalue+"%");
											userList  = super.getResultListByNamedQuery("OtsUsers.findByPattrenMatchingotsUsersEmailid", queryParameter);
										    break;
			            case "UserRoleId":
											OtsUserRole otsUserRole = new OtsUserRole();
			            					otsUserRole.setOtsUserRoleId(Integer.parseInt(searchvalue));
			            					queryParameter.put("otsUserRoleId", otsUserRole);
											userList  = super.getResultListByNamedQuery("OtsUsers.findByUserOtsRoleId", queryParameter);
											break;
			           
			            default:
			            					return null;
		
		            }
	            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
						+ "UserList Size:" +userList.size());
	            //@formatter:off
	            userDetails =  userList.stream().map(OtsUsers -> convertUserDetailsFromEntityToDomain(OtsUsers)).collect(Collectors.toList());
	            for(int i=0;i<userDetails.size();i++) {
	            	if(requestBOUserBySearch.getRequestData().getUserLat()!=null&&requestBOUserBySearch.getRequestData()!=null) {
		            	userDetails.get(i).setDistance(distance(Double.valueOf(requestBOUserBySearch.getRequestData().getUserLat()),Double.valueOf(requestBOUserBySearch.getRequestData().getUserLong()), Double.valueOf(userDetails.get(i).getUserLat()), Double.valueOf(userDetails.get(i).getUserLong())));
					}
	            } 
	            return userDetails;
	            //@formatter:on
    	}catch (NoResultException e) {
        	logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		}else {
			 try {
		            switch(searchKey)
		            {
		            case "UsersId":
		            					queryParameter.put("otsUsersId", Integer.parseInt(searchvalue));
		            					userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
		            				    break;
		            case "UsersFirstname":
										queryParameter.put("otsUsersFirstname","%"+searchvalue+"%");
										userList  = super.getResultListByNamedQuery("OtsUsers.findByPattrenMatchingotsUsersFirstname", queryParameter);
									    break;
		            case "UsersLastname":
										queryParameter.put("otsUsersLastname","%"+searchvalue+"%");
										userList  = super.getResultListByNamedQuery("OtsUsers.findByPattrenMatchingotsUsersLastname", queryParameter);
										break;
		            case "UsersEmailid":
										queryParameter.put("otsUsersEmailid","%"+searchvalue+"%");
										userList  = super.getResultListByNamedQuery("OtsUsers.findByPattrenMatchingotsUsersEmailid", queryParameter);
									    break;
		            case "UserRoleId":
										OtsUserRole otsUserRole = new OtsUserRole();
		            					otsUserRole.setOtsUserRoleId(Integer.parseInt(searchvalue));
		            					queryParameter.put("otsUserRoleId", otsUserRole);
										userList  = super.getResultListByNamedQuery("OtsUsers.findByUserOtsRoleId", queryParameter);
										break;
		           
		            default:
		            					return null;

		            }
		            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
							+ "UserList Size:" +userList.size());
		            //@formatter:off
		            userDetails =  userList.stream().map(OtsUsers -> convertUserDetailsFromEntityToDomain(OtsUsers)).collect(Collectors.toList());
		            for(int i=0;i<userDetails.size();i++) {
		            	if(requestBOUserBySearch.getRequestData().getUserLat()!=null&&requestBOUserBySearch.getRequestData()!=null) {
			            	userDetails.get(i).setDistance(distance(Double.valueOf(requestBOUserBySearch.getRequestData().getUserLat()),Double.valueOf(requestBOUserBySearch.getRequestData().getUserLong()), Double.valueOf(userDetails.get(i).getUserLat()), Double.valueOf(userDetails.get(i).getUserLong())));
			            
		            	}
		            }   
		            Collections.sort(userDetails,Collections.reverseOrder());
		            return userDetails;
		            //@formatter:on
	    	}catch (NoResultException e) {
	        	logger.error("Exception while fetching data from DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
	        }
		}
        
        
        
        
       
	}

	private UserDetails convertUserDetailsFromEntityToDomain(OtsUsers users) {
	   	UserDetails userDetails = new UserDetails();
	   	userDetails.setUserId(users.getOtsUsersId()==null?null:Integer.toString(users.getOtsUsersId()));
	   	userDetails.setFirstName(users.getOtsUsersFirstname()==null?null:users.getOtsUsersFirstname());
	   	userDetails.setLastName(users.getOtsUsersLastname()==null?null:users.getOtsUsersLastname());
	   	userDetails.setAddress1(users.getOtsUsersAddr1()==null?null:users.getOtsUsersAddr1());
	   	userDetails.setAddress2(users.getOtsUsersAddr2()==null?null:users.getOtsUsersAddr2());
	   	userDetails.setPincode(users.getOtsUsersPincode()==null?null:users.getOtsUsersPincode());
	   	userDetails.setEmailId(users.getOtsUsersEmailid()==null?null:users.getOtsUsersEmailid());
	   	userDetails.setProfilePic(users.getOtsUsersProfilePic()==null?null:users.getOtsUsersProfilePic());
	   	userDetails.setUsrStatus(users.getOtsUsersStatus()==null?null:users.getOtsUsersStatus());
	   	userDetails.setUsrStatus(users.getOtsUsersStatus()==null?null:users.getOtsUsersStatus());
	    userDetails.setUsrPassword(users.getOtsUsersPassword()==null?null:users.getOtsUsersPassword());
	   	userDetails.setContactNo(users.getOtsUsersContactNo()==null?null:users.getOtsUsersContactNo());
	   	userDetails.setUserLat(users.getOtsUsersLat()==null?null:users.getOtsUsersLat());
	   	userDetails.setUserLong(users.getOtsUsersLong()==null?null:users.getOtsUsersLong());
	   	userDetails.setUserRoleId(users.getOtsUserRoleId().getOtsUserRoleId()==null?null:users.getOtsUserRoleId().getOtsUserRoleId().toString());
	   	userDetails.setMappedTo(users.getOtsUserMapping().getOtsMappedTo().toString());
	   	
	   	List<OtsCustomerProduct> customerProductDetails = new ArrayList(users.getOtsCustomerProductCollection());
	   	
	   	for(int i=0 ; i<customerProductDetails.size() ; i++) {
	   		CustomerProductDetails tempcustomerProductDetails = new CustomerProductDetails();
	   		tempcustomerProductDetails.setProductname(customerProductDetails.get(i).getOtsProductId().getOtsProductName()==null?null:customerProductDetails.get(i).getOtsProductId().getOtsProductName());
	   		tempcustomerProductDetails.setProductPrice(customerProductDetails.get(i).getOtsCustomerProductPrice()==null?null:customerProductDetails.get(i).getOtsCustomerProductPrice().toString());
	   		tempcustomerProductDetails.setCustomerProductId(customerProductDetails.get(i).getOtsCustomerProductId()==null?null:customerProductDetails.get(i).getOtsCustomerProductId().toString());
	   		
	   		userDetails.setProductPrice(customerProductDetails.get(i).getOtsCustomerProductPrice());
	   		userDetails.setProductId(customerProductDetails.get(i).getOtsProductId().getOtsProductId().toString());
	   		
	   		userDetails.getCustomerProductDetails().add(i,tempcustomerProductDetails);
	   	}
	   	
	   	return userDetails;
   }

	 @Override
		public List<UserDetails> getUserDetailsByMapped(String MappedTo) {
	    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
	    	try {
	            List<OtsUsers> userList = null;
	            try {
	            	Map<String, Object> queryParameter = new HashMap<>();
	    			queryParameter.put("MappedTo", Integer.parseInt(MappedTo));
	    			userList  = super.getResultListByNamedQuery("OtsUsers.findByUserOtsbyMappedTo", queryParameter);
	            } catch (NoResultException e) {
	            	logger.error("Exception while fetching data from DB :"+e.getMessage());
	        		e.printStackTrace();
	            	throw new BusinessException(e.getMessage(), e);
	            }
	            logger.info("Inside Event=1008,Class:UserServiceUtilityDAOImpl,Method:getUserDetailsByMapped, "
						+ "UserList Size:" +userList.size());
	            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
	    	}catch(Exception e) {
	    		logger.error("Exception while fetching data from DB :"+e.getMessage());
	    		e.printStackTrace();
	    		throw new BusinessException(e.getMessage(), e);
	    	}
	    	return userDetails;
			
		}
	 
	 
	 private static double distance(double lat1, double lon1, double lat2, double lon2) {
			if ((lat1 == lat2) && (lon1 == lon2)) {
				return 0;
			}
			else {
				double theta = lon1 - lon2;
				double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
				dist = Math.acos(dist);
				dist = Math.toDegrees(dist);
				dist = dist * 60 * 1.1515;
				dist = dist * 1.609344;
				return (dist);
			}
		}

}

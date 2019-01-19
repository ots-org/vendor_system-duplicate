
package com.fuso.enterprise.ots.srv.server.dao.impl;

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

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class UserServiceDAOImpl extends AbstractIptDao<OtsUsers, String> implements UserServiceDAO {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    public UserServiceDAOImpl() {
        super(OtsUsers.class);
    }
    
    @Override
	public List<UserDetails> getUserIdUsers(String userId) {
    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
    	try {
            List<OtsUsers> userList = null;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsUsersId", Integer.parseInt(userId));
    			userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }
            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
					+ "UserList Size:" +userList.size());
            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;
		
	}

    private UserDetails convertUserDetailsFromEntityToDomain(OtsUsers otsUsers) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstname(otsUsers.getOtsUsersFirstname());
        userDetails.setLastname(otsUsers.getOtsUsersLastname());
        userDetails.setAddress(otsUsers.getOtsUsersAddr1()+","+otsUsers.getOtsUsersAddr2());
        userDetails.setEmailId(otsUsers.getOtsUsersEmailid());
        userDetails.setUsrStatus(otsUsers.getOtsUsersStatus());
        return userDetails;
    }

   

	
	
}

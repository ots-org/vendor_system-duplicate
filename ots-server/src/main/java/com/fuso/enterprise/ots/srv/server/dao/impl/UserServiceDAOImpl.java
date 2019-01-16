
package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.Users;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class UserServiceDAOImpl extends AbstractIptDao<Users, String> implements UserServiceDAO {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    public UserServiceDAOImpl() {
        super(Users.class);
    }
    
    @Override
	public List<UserDetails> getUserIdUsers(String userId) {
    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
    	try {
	    	if(userId!=null) {
	    		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
	            CriteriaQuery<Users> criteriaQuery = criteriaBuilder.createQuery(Users.class);
	            Root<Users> root = criteriaQuery.from(Users.class);
	            List<Predicate> predicates = new ArrayList<Predicate>();
	            predicates.add(criteriaBuilder.equal(root.get("userid"), userId));
	          
	            if (!predicates.isEmpty()) {
	                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
	            }
	            List<Users> userList = null;
	            try {
	            	 userList = super.getResultListByCriteria(criteriaQuery);
	            } catch (NoResultException e) {
	            	logger.error("Exception while fetching data from DB :"+e.getMessage());
	        		e.printStackTrace();
	            	throw new BusinessException(e.getMessage(), e);
	            }
	            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
						+ "UserList Size:" +userList.size());
	            //@formatter:off
	            userDetails =  userList.stream().map(users -> convertUserDetailsFromEntityToDomain(users)).collect(Collectors.toList());
	            return userDetails;
	            //@formatter:on
	    	}else {
	    		return null;
	    	}
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
		
	}

    private UserDetails convertUserDetailsFromEntityToDomain(Users user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstname(user.getFirstname());
        userDetails.setLastname(user.getLastname());
        return userDetails;
    }

   

	
	
}

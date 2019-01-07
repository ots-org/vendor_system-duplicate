
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
	
	private Logger logger = LoggerFactory.getLogger(UserServiceDAOImpl.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    public UserServiceDAOImpl() {
        super(Users.class);
    }
    
    @Override
	public List<UserDetails> getUserIdUsers(String userId) {
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
            	throw new BusinessException(e.getMessage(), e);
            }
            //@formatter:off
            return userList.stream().map(users -> convertUserDetailsFromEntityToDomain(users)).collect(Collectors.toList());
            //@formatter:on
    	}else {
    		return null;
    	}
		
	}

    private UserDetails convertUserDetailsFromEntityToDomain(Users user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstname(user.getFirstname());
        userDetails.setLastname(user.getLastname());
        return userDetails;
    }

   

	
	
}

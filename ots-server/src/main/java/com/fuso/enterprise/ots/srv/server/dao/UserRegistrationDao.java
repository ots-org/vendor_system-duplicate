package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;


public interface UserRegistrationDao {

	public String addUserRegistration(AddNewBORequest addNewBORequest); 
	public List<RegistorToUserDetails> getNewRegistrationDao(String mappedTo);
	
}

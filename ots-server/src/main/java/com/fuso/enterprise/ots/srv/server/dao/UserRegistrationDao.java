package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;


public interface UserRegistrationDao {

	public UserRegistrationResponce addUserRegistration(AddNewBORequest addNewBORequest); 
	public List<RegistorToUserDetails> getNewRegistrationDao(String mappedTo);
	public UserDetails fetchUserDetailsfromRegistration(String registrationId);
	public OtsRegistration fetOtsRegistrationBasedonRegisterID(Integer otsRegistrationId);
	public ApproveRegistrationResponse approveRegistration(OtsRegistration otsRegistration);
	public Integer CheckForExists(AddUserDataBORequest addUserDataBORequest);
}

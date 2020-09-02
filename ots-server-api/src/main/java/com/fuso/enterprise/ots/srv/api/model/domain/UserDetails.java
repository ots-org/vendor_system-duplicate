
package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDetails implements Comparable{

	 @Size(max = 10)
	 private String userId;

	 @Size(max = 20)
	 private String firstName;

	 @Size(max = 20)
	 private String lastName;

	 @Size(max = 40)
	 private String emailId;

	 @Size(max = 10)
	 private String usrStatus;
     
	 @Size(max=20)
	 private String usrPassword;
	 
	 @Size(max = 15)
	 private String contactNo;

	 @Size(max = 100)
	 private String address1;
	    
	 @Size(max = 100)
	 private String address2;
	    
	 @Size(max = 100)
	 private String pincode;
	 
	 @Size(max = 100)
	 private String profilePic;
	    
	 @Size(max = 100)
	 private String usersCreated;

	 @Size(max = 100)
	 private String registrationId;
	    
	 @Size(max = 100)
	 private String UserRoleId;
 
	@Size(max = 100)
	 private String mappedTo;
	 
	 @Size(max = 100)
	 private String productPrice;
	 
	 @Size(max = 100)
	 private String productId;	
	 
	 @Size(max = 100)
	 private String DeviceId;
	 
	 @Size(max = 100)
	 private String DistributorId;
	 
	 private String userLat;
	 
	 private String userLong;
	 
	 private Double distance;
	 
	 private String userAdminFlag;

	public String getUserLat() {
		return userLat;
	}

	public void setUserLat(String userLat) {
		this.userLat = userLat;
	}

	public String getUserLong() {
		return userLong;
	}

	public void setUserLong(String userLong) {
		this.userLong = userLong;
	}

	public String getDistributorId() {
		return DistributorId;
	}

	public void setDistributorId(String distributorId) {
		DistributorId = distributorId;
	}

	public String getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}

	private List<CustomerProductDetails> CustomerProductDetails = new ArrayList<>();

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getMappedTo() {
		return mappedTo;
	 }

	 public void setMappedTo(String mappedTo) {
		this.mappedTo = mappedTo;
	 }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUsrStatus() {
		return usrStatus;
	}

	public void setUsrStatus(String usrStatus) {
		this.usrStatus = usrStatus;
	}
	
	public String getUsrPassword() {
		return usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getUsersCreated() {
		return usersCreated;
	}

	public void setUsersCreated(String usersCreated) {
		this.usersCreated = usersCreated;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}	
	public String getUserRoleId() {
		return UserRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		UserRoleId = userRoleId;
	}

	public List<CustomerProductDetails> getCustomerProductDetails() {
		return CustomerProductDetails;
	}

	public void setCustomerProductDetails(List<CustomerProductDetails> customerProductDetails) {
		CustomerProductDetails = customerProductDetails;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return this.distance.compareTo(((UserDetails)arg0).getDistance());
	}

	public void setAdsAuthentication(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public String getUserAdminFlag() {
		return userAdminFlag;
	}

	public void setUserAdminFlag(String userAdminFlag) {
		this.userAdminFlag = userAdminFlag;
	}
  
	
}

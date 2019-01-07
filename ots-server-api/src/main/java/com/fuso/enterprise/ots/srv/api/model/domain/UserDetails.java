
package com.fuso.enterprise.ots.srv.api.model.domain;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDetails {

	@Size(max = 10)
	private String mode;

    @Size(max = 10)
    private String userid;

    @Size(max = 20)
    private String firstname;

    @Size(max = 20)
    private String lastname;

    @Size(max = 40)
    private String emailId;

    @Size(max = 10)
    private String usrStatus;

    @Size(max = 10)
    private String designation;

    @Size(max = 15)
    private String contactNo;

    @Size(max = 100)
    private String Address;

    @Size(max = 5)
    private String groupId;

    @Size(max = 30)
    private String departmentId;

    private String registeredDate;

    @Size(max = 5)
    private String approvedBy;

    private String roleId;

    private boolean adsAuthentication;

    public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	private String approvedDate;

    private Integer notifyShikeishyo;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getNotifyShikeishyo() {
        return notifyShikeishyo;
    }

    public void setNotifyShikeishyo(Integer notifyShikeishyo) {
        this.notifyShikeishyo = notifyShikeishyo;
    }

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean getAdsAuthentication() {
		return adsAuthentication;
	}

	public void setAdsAuthentication(boolean adsAuthentication) {
		this.adsAuthentication = adsAuthentication;
	}

}

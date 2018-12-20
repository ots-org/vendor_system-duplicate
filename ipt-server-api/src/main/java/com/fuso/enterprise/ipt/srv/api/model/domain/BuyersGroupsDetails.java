package com.fuso.enterprise.ipt.srv.api.model.domain;

import javax.validation.constraints.Size;

public class BuyersGroupsDetails {
	
	@Size(max = 50)
    private String groupCode;
    
    @Size(max = 50)
    private String groupName;
    
    @Size(max = 50)
    private String groupDesc;
    
    @Size(max = 50)
    private String supplierAssistance;
    
    @Size(max = 10)
    private String deptCode;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getSupplierAssistance() {
		return supplierAssistance;
	}

	public void setSupplierAssistance(String supplierAssistance) {
		this.supplierAssistance = supplierAssistance;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

    
}

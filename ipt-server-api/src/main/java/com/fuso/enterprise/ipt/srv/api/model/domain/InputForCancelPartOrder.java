package com.fuso.enterprise.ipt.srv.api.model.domain;

import java.util.List;

public class InputForCancelPartOrder {

	private String reasonForCancel;
	
	private String statusText;
	
	private String updatingUser;
	
	//private List<InputForPartStatus> partList;

	public String getReasonForCancel() {
		return reasonForCancel;
	}

	public void setReasonForCancel(String reasonForCancel) {
		this.reasonForCancel = reasonForCancel;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

/*	public List<InputForPartStatus> getPartList() {
		return partList;
	}

	public void setPartList(List<InputForPartStatus> partList) {
		this.partList = partList;
	}*/

	public String getUpdatingUser() {
		return updatingUser;
	}

	public void setUpdatingUser(String updatingUser) {
		this.updatingUser = updatingUser;
	}
}

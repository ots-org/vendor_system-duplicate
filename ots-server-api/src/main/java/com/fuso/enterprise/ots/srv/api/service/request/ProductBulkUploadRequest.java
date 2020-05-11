package com.fuso.enterprise.ots.srv.api.service.request;

public class ProductBulkUploadRequest {

	private String base64ExcelString;

	public String getBase64ExcelString() {
		return base64ExcelString;
	}

	public void setBase64ExcelString(String base64ExcelString) {
		this.base64ExcelString = base64ExcelString;
	}
}

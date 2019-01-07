package com.fuso.enterprise.ots.srv.server.util;

import java.io.Serializable;

public class ResponseWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String message="";
	private Object applicationSpecific;
	
	public ResponseWrapper() {
	}

	/**
	 * @param body
	 */
	public ResponseWrapper(Object applicationSpecific) 
	{
		this.applicationSpecific = applicationSpecific;
	}
	public ResponseWrapper(int code, Object applicationSpecific) 
	{
		this.code = code;
		this.applicationSpecific = applicationSpecific;
	}
	
	/**
	 * @param ret
	 * @param error
	 * @param body
	 */
	public ResponseWrapper(int code, String message, Object applicationSpecific) {
		this.code = code;
		this.message = message;
		this.applicationSpecific = applicationSpecific;
	}

	/**
	 * @param ret
	 * @param error
	 */
	public ResponseWrapper(int code, String error) {
		this.code = code;
		this.message = message;
	}


	

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Object getApplicationSpecific() {
		return applicationSpecific;
	}

	

}

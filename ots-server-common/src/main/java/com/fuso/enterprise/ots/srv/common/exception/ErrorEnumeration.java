
package com.fuso.enterprise.ots.srv.common.exception;

public enum ErrorEnumeration {

    INPUT_PARAMETER_INCORRECT(1001, "err.key.input.parameter.incorrect"),
    SYSTEM_ERROR(1002, "err.key.system.error"),
    EXCEPTION_IN_OPTIMISTIC_LOCK(1003,"err.key.optimistic.lock"),
    USERID_UNAVAILABLE(1004,"err.user.unavailable"),
    USER_INSERT(1005,"err.user.inserting"),
    USERID_AVAILED(1006,"err.user.availed"),
	USER_UPDATE_FAILURE(1006,"user.update.failed"),
	SUPPLIER_ALREADY_AVAILABLE(1006,"err.user.availed"),
	USER_MAPPING_PRODUCT_FAILURE(100,"err.user.mapping.product.failure"),
	ADD_USER_FAILURE(1004,"err.user.add.failure"), 
	USER_MAPPINGTO_FAILURE(1005,"err.user.mapp.failure"),
	GET_PRODUCT_LIST_FAILURE(1012,"err.get.productlist.failure"), 
	ADD_UPDATE_PRODUCT_FAILURE(1011,"err.add.update.product.failure"),
	APPROVE_REGISTRATION_FAILURE(1003,"err.approve.register.failure"),
	Mapped_to_value_is_empty(1006,"err.in.MappedTo"),
	UpdationFailuer(1009,"err.in.productstock"),
	User_Not_exists(1014,"err.in.usernotexists"),
	USR_REGISTER_failure(1001,"err.user.register.failure"),
	ADD_UPDATE_BILL_FAILURE(1021,"err.add.update.bill.failure"),
	FAILURE_ORDER_GET(1004,"err.ordrer.get"),
	ERROR_IN_ORDER_INSERTION(1006,"err.in.ordertable"),
	ERROR_IN_ORDER_PRODUCT_INSERTION(1006,"err.in.orderproducttable"),
	ADD_EMP_LATLONG_FAILURE(1022,"err.in.emplatlongtable"), 
	GET_EMP_LATLONG_FAILURE(1022,"err.in.getemplatlong"),
	ERROR_IN_GETPRODUCTSTOCK(1006,"err.in.getproductstock");
	
    ErrorEnumeration(int errorCode, String messageKey) {
        this.errorCode = errorCode;
        this.messageKey = messageKey;
    }

    private final int errorCode;

    private final String messageKey;

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
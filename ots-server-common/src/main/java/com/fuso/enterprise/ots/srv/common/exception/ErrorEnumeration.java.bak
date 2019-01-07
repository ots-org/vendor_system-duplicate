
package com.fuso.enterprise.ipt.srv.common.exception;

public enum ErrorEnumeration {

    INPUT_PARAMETER_INCORRECT(1001, "err.key.input.parameter.incorrect"),
    SYSTEM_ERROR(1002, "err.key.system.error"),
    EXCEPTION_IN_OPTIMISTIC_LOCK(1003,"err.key.optimistic.lock"),
    USERID_UNAVAILABLE(1004,"err.user.unavailable"),
    USER_INSERT(1005,"err.user.inserting"),
    USERID_AVAILED(1006,"err.user.availed");
	
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
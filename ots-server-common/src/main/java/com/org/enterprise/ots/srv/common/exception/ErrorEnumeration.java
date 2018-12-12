
package com.org.enterprise.ots.srv.common.exception;

public enum ErrorEnumeration {

    INPUT_PARAMETER_INCORRECT(1001, "err.key.input.parameter.incorrect"),
    SYSTEM_ERROR(1002, "err.key.system.error"),
    EXCEPTION_IN_OPTIMISTIC_LOCK(1003,"err.key.optimistic.lock");

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
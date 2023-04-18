package com.github.framework.core.exception;


/**
 *
 *  验证异常信息类，所有跟业务验证有关的异常信息都应该抛出此异常
 *
 */
public class ViolationException extends BaseException {
    public ViolationException(Throwable cause) {
        super(cause);
    }

    public ViolationException(String message) {
        super(message);
    }

    public ViolationException(final String message,final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public ViolationException(String code, String message) {
        super(code, message);
    }

    public ViolationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String getDefaultCode() {
        return ErrorCode.VIOLATION.value();
    }
}

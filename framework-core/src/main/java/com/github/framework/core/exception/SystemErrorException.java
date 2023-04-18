package com.github.framework.core.exception;

/**
 *
 * 系统错误异常，用于封装jdk层抛出的异常以及未知异常
 *
 */
public class SystemErrorException extends BaseException {

    public SystemErrorException(Throwable cause) {
        super(cause);
    }

    public SystemErrorException(String message) {
        super(message);
    }

    public SystemErrorException(final String message,final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public SystemErrorException(String code, String message) {
        super(code, message);
    }

    public SystemErrorException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String getDefaultCode() {
        return ErrorCode.SYSTEM.value();
    }
}

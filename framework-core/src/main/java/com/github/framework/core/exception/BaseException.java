package com.github.framework.core.exception;

import com.github.framework.core.ErrorMessage;
import com.github.framework.core.lang.CustomStringUtils;

/**
 * 异常处理父类，所有的异常处理基于该类派生
 */
public class BaseException extends RuntimeException implements ErrorMessage {
    private static final long serialVersionUID = -1535405572500029548L;

    /**
     * 错误代码
     */
    protected String code;

    public BaseException(final Throwable cause) {
        super(cause);
        this.code = getDefaultCode();
    }

    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public BaseException(final String message) {
        super(message);
        this.code = getDefaultCode();
    }

    public BaseException(final String code, final String message) {
        this(message);
        this.code = CustomStringUtils.defaultString(code, getDefaultCode());
    }

    public BaseException(final String code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = CustomStringUtils.defaultString(code, getDefaultCode());
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getErrorDetail() {
        return Ex.getStackTraceAsString(this);
    }

    public String getDefaultCode() {
        return ErrorMessage.DEFAULT_ERROR_CODE;
    }

}

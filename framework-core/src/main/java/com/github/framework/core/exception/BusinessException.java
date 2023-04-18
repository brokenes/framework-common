package com.github.framework.core.exception;

/**
 *
 * @ClassName: BusinessException
 * @Description: 业务异常信息类，所有业务异常都应该抛出此类
 * @author sam
 * @date 2019年3月15日 下午12:02:16
 *
 */
public class BusinessException extends BaseException {

    public BusinessException(final Throwable cause) {
        super(cause);
    }

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final String code, final String message) {
        super(code, message);
    }

    public BusinessException(final String code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

}

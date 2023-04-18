/**
 * @Package com.welllink.framework.common.exception
 * @Description:
 * @author sam
 * @date 2020/1/10 23:48
 */
package com.github.framework.core.exception;

/**
 * TimeoutException
 *
 */
public class TimeoutException extends BaseException {
    public TimeoutException(Throwable cause) {
        super(cause);
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(final String message, final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public TimeoutException(String code, String message) {
        super(code, message);
    }

    public TimeoutException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String getDefaultCode() {
        return ErrorCode.TIMEOUT.value();
    }
}
package com.github.framework.core.exception;

/**
 *
 * 权限异常，当访问某个资料没有权限时应该抛出此异常
 *
 */
public class PermissionException extends BaseException {
    public PermissionException(Throwable cause) {
        super(cause);
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(final String message,final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public PermissionException(String code, String message) {
        super(code, message);
    }

    public PermissionException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String getDefaultCode() {
        return ErrorCode.PERMISSION.value();
    }
}

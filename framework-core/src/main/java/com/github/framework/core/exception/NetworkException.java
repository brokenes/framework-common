package com.github.framework.core.exception;

/**
 *
 * 网络异常，当调用ftp,http,tcp出现未预料问题应该抛出此异常
 *
 */
public class NetworkException extends BaseException {

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(final String message,final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public NetworkException(String code, String message) {
        super(code, message);
    }

    public NetworkException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String getDefaultCode() {
        return ErrorCode.NETWORK.value();
    }
}

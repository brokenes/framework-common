package com.github.framework.core.exception;

/**
 * 加密、编码异常信息类，对数据进行加密、解密、转码等出现问题应抛出此异常
 */
public class CryptoException extends BaseException {

    public CryptoException(Throwable cause) {
        super(cause);
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(final String message,final Throwable cause) {
        super(message, cause);
        this.code = getDefaultCode();
    }

    public CryptoException(String code, String message) {
        super(code, message);
    }

    public CryptoException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    @Override
    public String getDefaultCode() {
        return ErrorCode.CRYPTO.value();
    }
}
